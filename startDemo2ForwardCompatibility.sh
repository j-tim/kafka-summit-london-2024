docker-compose -f docker-compose.yml up -d
curl -k -X PUT -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"compatibility": "FORWARD"}' http://localhost:8081/config/customers-topic-forward-value
./registerAvroSchema.sh "customers-topic-forward" "spring-kafka-consumer-first/src/main/resources/avro/customer-demo2-forward.avsc"
./registerAvroSchema.sh "customers-topic-forward" "spring-kafka-producer-one/src/main/resources/avro/customer-demo2-forward.avsc"
./registerAvroSchema.sh "customers-topic-forward" "spring-kafka-producer-two/src/main/resources/avro/customer-demo2-forward.avsc"
curl -k -X PUT -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"compatibility": "FORWARD"}' http://localhost:8081/config/forward-output-topic-value
./registerAvroSchema.sh "forward-output-topic" "spring-kafka-producer-two/src/main/resources/avro/customer-demo2-forward.avsc"
./registerAvroSchema.sh "customers-topic-forward" "spring-kafka-producer-three/src/main/resources/avro/customer-demo2-forward.avsc"
docker-compose -f docker-compose-applications-demo1-backward.yml -f docker-compose-applications-demo2-forward.yml down -v && docker-compose -f docker-compose-applications-demo2-forward.yml up -d