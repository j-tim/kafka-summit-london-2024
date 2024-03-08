package com.example.avro.util;

import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.SchemaParseException;

@Slf4j
public class AvroUtils {

    /**
     * Convert a schema string into a schema object and a canonical schema string.
     * Default fields will be validated!
     *
     * @return A schema object and a canonical representation of the schema string. Return null if
     * there is any parsing error.
     */
    public static AvroSchema parseSchema(String schemaString) {
        try {
            // Will enable the validation of default values!
            Schema.Parser parser = new Schema.Parser();
            parser.setValidateDefaults(true);
            Schema schema = parser.parse(schemaString);
            return from(schema);
        } catch (SchemaParseException e) {
            log.info("Failed to parse schema: {}", e.getMessage(), e);
            return null;
        }
    }

    public static AvroSchema from(Schema schema) {
        return new AvroSchema(schema);
    }

    public static AvroSchema loadSchema(String schemaClasspathLocation) {
        String schema = ClassPathResourceUtils.readClassPathResourceAsString(schemaClasspathLocation);
        return parseSchema(schema);
    }

    public static String toRawSchemaString(AvroSchema schema) {
        return schema.rawSchema().toString(true);
    }

    public static String toRawSchemaString(Schema schema) {
        return toRawSchemaString(from(schema));
    }
}
