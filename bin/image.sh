#!/usr/bin/env bash

if [ -z "$1" ]; then
  TAG=latest
else
  TAG="$1"
fi

DIR=$(dirname "$0")
IMAGE=vitorenesduarte/vcd-java-client:${TAG}
DOCKERFILE=${DIR}/../Dockerfiles/vcd-java-client

# release vcd-java-client
cd ${DIR}/.. && make rel && cd -

# build image
docker build \
  --no-cache \
  -t "${IMAGE}" -f "${DOCKERFILE}" .

# push image
docker push "${IMAGE}"

