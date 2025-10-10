#!/bin/bash
IMAGE_VERSION="${1:-latest}"
DOCKERHUB_USERNAME="${2:-$DOCKER_USERNAME}"
DOCKERHUB_PASSWORD="${3:-$DOCKER_PASSWORD}"
HOST_IP="$(ip addr show docker_gwbridge | grep -oP '(?<=inet\s)\d+(\.\d+){3}')"
export SPRING_DATASOURCE_USERNAME=metasearch_user
export SPRING_DATASOURCE_PASSWORD=secret
export SPRING_DATASOURCE_URL="jdbc:mysql://$HOST_IP:3306/metasearch?connectionTimeZone=UTC&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC"
export STANDARD_EBOOKS_TOKEN=TEST_TOKEN
export IMAGE_TAG="$IMAGE_VERSION"
docker login -u "$DOCKERHUB_USERNAME" --password-stdin <<< "$DOCKERHUB_PASSWORD" && \
docker stack deploy --with-registry-auth -c docker-compose-swarm.yml metasearch_stack