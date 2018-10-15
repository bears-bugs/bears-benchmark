#!/usr/bin/env bash

set -e

IS_BUGGY_COMMIT=$1

docker pull "$DOCKER_TAG";
docker run --name "$TRAVIS_PULL_REQUEST_BRANCH" --mount type=bind,source="$(pwd)",target=/var/app/pr --env BRANCH_NAME="$TRAVIS_PULL_REQUEST_BRANCH" --env IS_BUGGY_COMMIT="$IS_BUGGY_COMMIT" "$DOCKER_TAG";
exit $(docker inspect "$TRAVIS_PULL_REQUEST_BRANCH" --format='{{.State.ExitCode}}');