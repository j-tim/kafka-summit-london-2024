# Demo 1 - Backward Compatibility

## Build

### Mvn project + docker images

```bash
./buildDockerImages.sh
```

## Demo applications purpose and description

| Applications                 | Topic(s)                                                                                                | Description                                                                                                                  |
|------------------------------|---------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| spring-kafka-producer-one    | `customers-topic-backward`| Contains v1 the initial avro schema                                                                                          |
| spring-kafka-consumer-first  | `customers-topic-backward`| Contains v2 of the schema, example of removing a **required** field (**occupation**) and using an alias for **name** |
| spring-kafka-consumer-second | `customers-topic-backward`| Contains v3 of the schema, example of adding an **optional** field (**annualIncome**)                                        |
| spring-kafka-consumer-third  | `customers-topic-backward`| Contains v4 of the schema, example of a backwawrd breaking change - adding a required field (**age**)                        |


## Start infrastructure

```bash
docker-compose -f docker-compose.yml up -d
```

## Start applications

Make sure to build the Docker images for the applications first

```bash
./startDemo1BackwardCompatibility.sh
```

Above command will run all needed Docker containers(infrastructure + applications)

## Start local producer with Maven

```bash
cd spring-kafka-producer-one
mvn spring-boot:run -Dspring-boot.run.fork=false -Dspring-boot.run.profiles=backward
```

## Shutdown 

* Stop demo applications

```bash
docker-compose -f docker-compose-applications-demo1-backward.yml down -v
```

* All Components

```bash
docker-compose -f docker-compose.yml -f docker-compose-applications-demo1-backward.yml down -v
```