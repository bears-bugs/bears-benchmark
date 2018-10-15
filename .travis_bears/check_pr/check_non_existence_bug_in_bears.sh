#!/usr/bin/env bash

set -e

BRANCH_NAME="$TRAVIS_PULL_REQUEST_BRANCH"

RED='\033[0;31m'
GREEN='\033[0;32m'

git checkout -qf master

if grep -q "$BRANCH_NAME" ./releases/branches_per_version.json; then
    echo -e "$RED$BRANCH_NAME [FAILURE] (the bug already exists in Bears)"
    exit 1
fi

echo -e "$GREEN$BRANCH_NAME [OK]"
