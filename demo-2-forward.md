# Demo 2 - Forward Compatibility

## Build

### Mvn project + docker images

```bash
./buildDockerImages.sh
```

## Demo applications purpose and description

| Applications                | Topic(s)                                                                                                | Description                                                                                                       |
|-----------------------------|---------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| spring-kafka-consumer-one   | `customers-topic-forward`| Contains v1 of the schema                                                                                         |
| spring-kafka-producer-one   | `customers-topic-forward`| Contains v2 of the schema, example of removing an optional field (**annualIncome**)                               |
| spring-kafka-producer-two   | `customers-topic-forward`| Contains v3 of the schema, example adding a required field (**dateOfBirth**)                                      |
| spring-kafka-producer-three | `customers-topic-forward`| Contains v4 of the schema, example of a forward incompatible change - removing a required field (**phoneNumber**) |


## Start infrastructure

```bash
docker compose -f docker-compose.yml up -d
```


## Start applications

Make sure to build the Docker images for the applications first

```bash
./startDemo2ForwardCompatibility.sh
```

Above command will run all needed docker containers(infrastructure + applications)

## Start local producer with maven

```bash
cd spring-kafka-consumer-first
```

```bash
mvn spring-boot:run -Dspring-boot.run.fork=false -Dspring-boot.run.profiles=forward
```

## Shutdown 

* Stop demo applications

```bash
docker compose -f docker-compose-applications-demo2-forward.yml down -v
```

* All Components
```bash
docker compose -f docker-compose.yml -f docker-compose-applications-demo2-forward.yml down -v
```