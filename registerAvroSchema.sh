#!/bin/bash

# Variables, adjust them as needed
topic_name="$1"
echo $topic_name
avro_schema_type="value"
avro_schema_file="$2"
schema_registry_url="http://localhost:8081"

# Validate the Avro file (Json)
jq empty $avro_schema_file || { echo "JSON is invalid"; exit 1; }

# prepare the schema for upload
escaped_schema="$(jq -c < $avro_schema_file | sed 's/"/\\"/g')"
upload_schema="{\"schema\":\"${escaped_schema}\"}"

echo "Schema to register with the Confluent Schema Registry:"
echo $upload_schema

# Register the schema
curl -s -k -H "Content-Type: application/vnd.schemaregistry.v1+json" -X POST -d "$upload_schema" \
    "${schema_registry_url}/subjects/${topic_name}-${avro_schema_type}/versions" | jq