#!/usr/bin/env bash

set -e

BRANCH_NAME="$TRAVIS_PULL_REQUEST_BRANCH"

RED='\033[0;31m'
GREEN='\033[0;32m'

PATCHED_COMMIT_STATS=$(git show --stat -n 1 --grep="$PATCHED_COMMIT_MESSAGE_PATTERN" --format=format:%f -- '*.java')
if [ -z "$PATCHED_COMMIT_STATS" ]; then
    echo -e "$RED$BRANCH_NAME [FAILURE] (the patched commit does not change java files)"
    exit 1
else
    echo "> The patched commit changed java files."
fi

CASE=$(cat bears.json | sed 's/.*"type": "\(.*\)".*/\1/;t;d')
if [ "$CASE" == "passing_passing" ]; then
    TEST_COMMIT_STATS=$(git show --stat -n 1 --grep="$TEST_COMMIT_MESSAGE_PATTERN" --format=format:%f -- '*.java')
    if [ -z "$TEST_COMMIT_STATS" ]; then
        echo -e "$RED$BRANCH_NAME [FAILURE] (the commit with changed test files does not change java files)"
        exit 1
    else
        echo "> The commit with changed test files changed java files."
    fi
fi

echo -e "$GREEN$BRANCH_NAME [OK]"
