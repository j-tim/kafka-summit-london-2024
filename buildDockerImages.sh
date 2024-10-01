./mvnw clean install
docker compose -f docker-compose-applications-demo1-backward.yml -f docker-compose-applications-demo2-forward.yml build --progress=plain --no-cache