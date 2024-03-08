# Compatibility testing

Make sure to stop demo 2:

```bash
docker-compose -f docker-compose.yml -f docker-compose-applications-demo2-forward.yml down -v
```

Start the infrastructure only:

```bash
docker-compose up -d
```

Show a breaking change (adding a required field):

* [V1 schema](avro-schemas/src/main/resources/avro/backward/customer-demo3-backward-v1.avsc)
* [V2 schema (incompatible change)](avro-schemas/src/test/resources/avro/backward/incompatible-change/customer-demo3-backward-v2.avsc)
* [V2 schema (compatible change)](avro-schemas/src/test/resources/avro/backward/compatible-change/customer-demo3-backward-v2.avsc)

Go to `TODO`: [pom.xml](/avro-schemas/pom.xml) to enable the breaking change and explain the plugin configuration

### To build without a running Schema Registry  

```bash
cd avro-schemas
./mvnw clean package
```

## Run the build with a running Schema Registry:

```bash
cd avro-schemas
./mvnw clean package -Pschema-compatibility-check-real-schema-registry
```

Show registered schema: http://localhost/console/default/schema-registry

Fix it and make it an optional field (default value)

* [V1 schema](avro-schemas/src/main/resources/avro/backward/customer-demo3-backward-v1.avsc)
* [V2 schema (incompatible change)](avro-schemas/src/test/resources/avro/backward/incompatible-change/customer-demo3-backward-v2.avsc)
* [V2 schema (compatible change)](avro-schemas/src/test/resources/avro/backward/compatible-change/customer-demo3-backward-v2.avsc)

```json
{
  "name": "newField",
  "type": "string",
  "default": "Hello Kafka Summit"
}
```

Additionally, we can now register the new Schema (V2)

```bash
cd avro-schemas
./mvnw io.confluent:kafka-schema-registry-maven-plugin:register@register-updated-schema -Pschema-compatibility-check-real-schema-registry
```

Shutdown:

```bash
docker-compose down -v
```