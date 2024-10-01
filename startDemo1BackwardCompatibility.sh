docker compose -f docker-compose.yml up -d
curl -k -X PUT -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"compatibility": "BACKWARD"}' http://localhost:8081/config/customers-topic-backward-value
./registerAvroSchema.sh "customers-topic-backward" "spring-kafka-producer-one/src/main/resources/avro/customer-demo1-backward.avsc"
./registerAvroSchema.sh "customers-topic-backward" "spring-kafka-consumer-first/src/main/resources/avro/customer-demo1-backward.avsc"
./registerAvroSchema.sh "customers-topic-backward" "spring-kafka-consumer-second/src/main/resources/avro/customer-demo1-backward.avsc"
./registerAvroSchema.sh "customers-topic-backward" "spring-kafka-consumer-third/src/main/resources/avro/customer-demo1-backward.avsc"

docker compose -f docker-compose-applications-demo1-backward.yml -f docker-compose-applications-demo2-forward.yml down -v && docker compose -f docker-compose-applications-demo1-backward.yml up -d


