#!/usr/bin/env bash

set -e

BRANCH_NAME="$TRAVIS_PULL_REQUEST_BRANCH"

RED='\033[0;31m'
GREEN='\033[0;32m'

SCRIPT_DIR="$(dirname "${BASH_SOURCE[0]}")"
JSON_SCHEMA="$SCRIPT_DIR/bears-schema.json"
if [ ! -f $JSON_SCHEMA ]; then
    echo "> The json schema ($JSON_SCHEMA) cannot be found."
    exit 2
else
    echo "> JSON schema path: $JSON_SCHEMA"
fi

if [ ! -e "bears.json" ]; then
    echo -e "$RED$BRANCH_NAME [FAILURE] (bears.json does not exist)"
    exit 1
fi

if ajv test -s $JSON_SCHEMA -d bears.json --valid; then
    echo -e "$GREEN$BRANCH_NAME [OK]"
else
    echo -e "$RED$BRANCH_NAME [FAILURE] (bears.json is invalid)"
    exit 1
fi
