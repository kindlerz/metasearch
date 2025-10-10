#!/bin/bash
set -ev

git config --global user.email $EMAIL
git config --global user.name $USERNAME
git symbolic-ref HEAD refs/heads/$(git branch --show-current)
git symbolic-ref HEAD

IMAGE_NAME=kasramp/metasearch &&
docker login -u "$DOCKER_USERNAME" --password-stdin <<< "$DOCKER_PASSWORD" &&
docker buildx create --use &&
docker buildx build --platform linux/amd64,linux/arm64 -t "$IMAGE_NAME":latest -t "$IMAGE_NAME:$TAGGED_VERSION" . --push
git add version.txt &&
git commit -m "Update version file to $TAGGED_VERSION" &&
git push origin HEAD:master &&
echo "Successfully build and pushed Docker $TAGGED_VERSION to Docker Hub"
