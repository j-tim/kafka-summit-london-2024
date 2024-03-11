package com.example.avro.serialization;

import com.example.avro.customer.CustomerBackwardDemo;
import com.example.avro.util.AvroUtils;
import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class KafkaAvroSerializerDeserializerDemoTest {
    private final CustomerBackwardDemo.Builder builder = CustomerBackwardDemo.newBuilder();
    private MockSchemaRegistryClient mockSchemaRegistryClient;

    @BeforeEach
    void setUp() {
        mockSchemaRegistryClient = new MockSchemaRegistryClient();
    }

    /**
     * auto.register.schemas=false
     * avro.remove.java.properties=false (default)
     * <p>
     * Schema used during Serialization derived from: {@link CustomerBackwardDemo#SCHEMA$}
     * <p>
     * 1. The User class is generated with the Avro Maven Plugin (Will add "avro.java.string":"String" to the {@link CustomerBackwardDemo#SCHEMA$})
     * 2. The producer (serializer) is not auto registering the schema (auto.register.schemas=false)
     * 3. When producing the schema is derived from the CustomerBackwardDemo Object
     * 4. There will be a mismatch between the schemas causing a SerializationException
     */
    @Test
    void shouldThrowSerializationException() throws Exception {
        // We build up the CustomerBackwardDemo object
        CustomerBackwardDemo customer = builder.setName("Tim")
                .setOccupation("Software Developer")
                .build();

        log.info("Avro Schema from Java: {}", AvroUtils.toRawSchemaString(customer.getSchema()));

        // We mock the interaction with a real Schema Registry
        MockSchemaRegistryClient mockSchemaRegistryClient = new MockSchemaRegistryClient();

        // We register the 1st version of the schema is registered without "avro.java.string" : "String"
        mockSchemaRegistryClient.register("customers-value", AvroUtils.loadSchema("avro/backward/customer-demo3-backward-v1.avsc"));

        // We configure the KafkaAvroSerializer
        Map<String, String> properties = new HashMap<>();
        properties.put("auto.register.schemas", "false");
        properties.put("schema.registry.url", "mock://");

        // Since auto.register.schemas=false the Schema used for serialization is derived from {@link User#SCHEMA$}
        // This schema contains the "avro.java.string":"String" for each string field
        KafkaAvroSerializer kafkaAvroSerializer = new KafkaAvroSerializer(mockSchemaRegistryClient, properties);

        // Causing a
        assertThatThrownBy(() -> {
            kafkaAvroSerializer.serialize("customers", customer);
        }).isInstanceOf(SerializationException.class)
                .hasMessageContaining("Error retrieving Avro schema");
    }

    /**
     * auto.register.schemas=false
     * avro.remove.java.properties=true
     * <p>
     * Schema used during Serialization derived from: {@link CustomerBackwardDemo#SCHEMA$}
     * <p>
     * 1. The User class is generated with the Avro Maven Plugin (Will add "avro.java.string":"String" to the {@link CustomerBackwardDemo#SCHEMA$})
     * 2. The producer (serializer) is not auto registering the schema (auto.register.schemas=false)
     * 3. When producing the schema is derived from the CustomerBackwardDemo Object
     * 4. The "avro.java.string":"String" will be removed during the comparison of the schemas (avro.remove.java.properties=true)
     * 5. The Serialization will succeed
     */
    @Test
    void workAround1() throws Exception {
        // We build up the User object
        CustomerBackwardDemo customer = builder.setName("Tim")
                .setOccupation("Software Developer")
                .build();

        log.info("Avro Schema from Java: {}", AvroUtils.toRawSchemaString(customer.getSchema()));

        // We register the 1st version of the schema is registered without "avro.java.string" : "String"
        mockSchemaRegistryClient.register("customers-value", AvroUtils.loadSchema("avro/backward/customer-demo3-backward-v1.avsc"));

        // We configure the KafkaAvroSerializer
        Map<String, String> properties = new HashMap<>();
        properties.put("auto.register.schemas", "false");
        properties.put("schema.registry.url", "mock://");

        // This is the important property for work around 1!
        properties.put("avro.remove.java.properties", "true");

        // Since auto.register.schemas=false the Schema used for serialization is derived from {@link User#SCHEMA$}
        // This schema contains the "avro.java.string":"String" for each string field
        KafkaAvroSerializer kafkaAvroSerializer = new KafkaAvroSerializer(mockSchemaRegistryClient, properties);

        // Serialization is successful because of avro.remove.java.properties=true
        byte[] serialized = kafkaAvroSerializer.serialize("customers", customer);

        assertThat(serialized).isNotEmpty();
    }

    /**
     * auto.register.schemas=false
     * use.latest.version=true
     * avro.remove.java.properties=false (default)
     * <p>
     * Schema used during Serialization derived from: Schema registry
     * <p>
     * 1. The User class is generated with the Avro Maven Plugin (Will add "avro.java.string":"String" to the {@link CustomerBackwardDemo#SCHEMA$})
     * 2. The producer (serializer) is not auto registering the schema (auto.register.schemas=false)
     * 4. The "avro.java.string":"String" will not be removed during the comparison of the schemas (avro.remove.java.properties=false)
     * 3. The schema is derived from schema registry and not from the User object since we specify use.latest.version=true
     * 5. The Serialization will succeed
     */
    @Test
    void workAround2() throws Exception {
        // We build up the User object
        CustomerBackwardDemo customer = builder.setName("Tim")
                .setOccupation("Software Developer")
                .build();

        log.info("Avro Schema from Java: {}", AvroUtils.toRawSchemaString(customer.getSchema()));

        // We register the 1st version of the schema is registered without "avro.java.string" : "String"
        mockSchemaRegistryClient.register("customers-value", AvroUtils.loadSchema("avro/backward/customer-demo3-backward-v1.avsc"));

        // We configure the KafkaAvroSerializer
        Map<String, String> properties = new HashMap<>();
        properties.put("auto.register.schemas", "false");
        properties.put("schema.registry.url", "mock://");

        // This is the important property for work around 2!
        properties.put("use.latest.version", "true");

        // Althoug auto.register.schemas=false but use.latest.version=true the Schema used for serialization
        // is derived Schema registry this schema doesn't contains the "avro.java.string":"String" for each string field
        KafkaAvroSerializer kafkaAvroSerializer = new KafkaAvroSerializer(mockSchemaRegistryClient, properties);

        // Serialization is successful because of avro.remove.java.properties=true
        byte[] serialized = kafkaAvroSerializer.serialize("customers", customer);

        assertThat(serialized).isNotEmpty();
    }

    /**
     * auto.register.schemas=false
     * use.latest.version=false (default)
     * avro.remove.java.properties=false (default)
     * <p>
     * Schema used during Serialization derived from: Schema registry
     * <p>
     * 1. The User class is generated with the Avro Maven Plugin (Will add "avro.java.string":"String" to the {@link CustomerBackwardDemo#SCHEMA$})
     * 2. The producer (serializer) is not auto registering the schema (auto.register.schemas=false)
     * 4. The "avro.java.string":"String" will not be removed during the comparison of the schemas (avro.remove.java.properties=false)
     * 3. The schema is derived from schema registry and not from the User object since we specify use.schema.id=1
     * 5. The Serialization will succeed
     */
    @Test
    void workAround3() throws Exception {
        // We build up the User object
        CustomerBackwardDemo customer = builder.setName("Tim")
                .setOccupation("Software Developer")
                .build();

        log.info("Avro Schema from Java: {}", AvroUtils.toRawSchemaString(customer.getSchema()));

        // We register the 1st version of the schema is registered without "avro.java.string" : "String"
        mockSchemaRegistryClient.register("customers-value", AvroUtils.loadSchema("avro/backward/customer-demo3-backward-v1.avsc"));

        // We configure the KafkaAvroSerializer
        Map<String, String> properties = new HashMap<>();
        properties.put("auto.register.schemas", "false");
        properties.put("schema.registry.url", "mock://");

        // This is the important property for work around 3!
        properties.put("use.schema.id", "1");

        // Since auto.register.schemas=false the Schema used for serialization is derived from {@link User#SCHEMA$}
        // This schema contains the "avro.java.string":"String" for each string field
        KafkaAvroSerializer kafkaAvroSerializer = new KafkaAvroSerializer(mockSchemaRegistryClient, properties);

        // Serialization is successful because of avro.remove.java.properties=true
        byte[] serialized = kafkaAvroSerializer.serialize("customers", customer);

        assertThat(serialized).isNotEmpty();

        // Verify the no new schemas is registered
        List<ParsedSchema> registeredSchemas = mockSchemaRegistryClient.getSchemas("customers", false, false);
        assertThat(registeredSchemas).hasSize(1);
    }

    /**
     * In this example the Schema will be registered automatically:  (auto.register.schemas=true)
     */
    @Test
    void happyFlowShouldSerializeAndDeserialize() throws Exception {
        CustomerBackwardDemo customer = builder.setName("Tim")
                .setOccupation("Software Developer")
                .build();

        log.info("Avro Schema from Java: {}", AvroUtils.toRawSchemaString(customer.getSchema()));

        // The schema is registered without "avro.java.string" : "String"
        mockSchemaRegistryClient.register("customers-value", AvroUtils.loadSchema("avro/backward/customer-demo3-backward-v1.avsc"));

        Map<String, String> properties = new HashMap<>();
        properties.put("auto.register.schemas", "true");
        properties.put("schema.registry.url", "mock://");

        KafkaAvroSerializer kafkaAvroSerializer = new KafkaAvroSerializer(mockSchemaRegistryClient, properties);
        byte[] serialized = kafkaAvroSerializer.serialize("customers", customer);

        // Deserializer specific property
        properties.put("specific.avro.reader", "true");
        KafkaAvroDeserializer kafkaAvroDeserializer = new KafkaAvroDeserializer(mockSchemaRegistryClient, properties);
        CustomerBackwardDemo deserialized = (CustomerBackwardDemo) kafkaAvroDeserializer.deserialize("customers", serialized);

        assertThat(deserialized).isNotNull();

        // Verify the new schemas is registered
        List<ParsedSchema> registeredSchemas = mockSchemaRegistryClient.getSchemas("customers", false, false);
        assertThat(registeredSchemas).hasSize(2);

        assertThat(serialized).isNotNull();
    }
}
