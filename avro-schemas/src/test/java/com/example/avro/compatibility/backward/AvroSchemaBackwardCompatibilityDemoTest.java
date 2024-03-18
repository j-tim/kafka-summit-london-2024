package com.example.avro.compatibility.backward;

import com.example.avro.util.AvroUtils;
import io.confluent.kafka.schemaregistry.CompatibilityChecker;
import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class AvroSchemaBackwardCompatibilityDemoTest {

    private final CompatibilityChecker backwardChecker = CompatibilityChecker.BACKWARD_CHECKER;

    @Test
    void addingAFieldWithoutADefaultValueIsNotABackwardCompatibleChange() {
        // Given the current schema
        AvroSchema currentSchema = AvroUtils.loadSchema("avro/backward/customer-demo3-backward-v1.avsc");

        // When I would like to add a new field (I made a mistake because: Adding a field without a default value is not a backward compatible change!)
        AvroSchema updatedSchema = AvroUtils.loadSchema("avro/backward/incompatible-change/customer-demo3-backward-v2.avsc");

        log.info("Schema V1: {}", AvroUtils.toRawSchemaString(currentSchema));
        log.info("Schema V2: {}", AvroUtils.toRawSchemaString(updatedSchema));

        List<String> compatible = backwardChecker.isCompatible(updatedSchema, Collections.singletonList(currentSchema));
        assertThat(compatible)
                .isEmpty();
//                .isNotEmpty()
//                .contains("{errorType:'READER_FIELD_MISSING_DEFAULT_VALUE', description:'The field 'newField' at path '/fields/2' in the new schema has no default value and is missing in the old schema', additionalInfo:'newField'}");
    }
}

