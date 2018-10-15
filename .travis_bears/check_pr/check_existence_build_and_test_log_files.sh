#!/usr/bin/env bash

set -e

BRANCH_NAME="$TRAVIS_PULL_REQUEST_BRANCH"

RED='\033[0;31m'
GREEN='\033[0;32m'

BUILD_LOG_FILE_NAME="repairnator.maven.buildproject.log"
TEST_LOG_FILE_NAME="repairnator.maven.testproject.log"

echo "> Checking $BUILD_LOG_FILE_NAME..."

SEARCH_BUILD_LOG_IN_BUGGY_COMMIT=$(git log -n 1 --grep="$BUGGY_COMMIT_MESSAGE_PATTERN" --format=format:%H -- "$BUILD_LOG_FILE_NAME")
if [ -z "$SEARCH_BUILD_LOG_IN_BUGGY_COMMIT" ]; then
    echo -e "$RED$BRANCH_NAME [FAILURE] (buggy commit does not change the file $BUILD_LOG_FILE_NAME)"
    exit 1
else
    echo "> The file $BUILD_LOG_FILE_NAME was changed in the buggy commit."
fi
SEARCH_BUILD_LOG_IN_PATCHED_COMMIT=$(git log -n 1 --grep="$PATCHED_COMMIT_MESSAGE_PATTERN" --format=format:%H -- "$BUILD_LOG_FILE_NAME")
if [ -z "$SEARCH_BUILD_LOG_IN_PATCHED_COMMIT" ]; then
    echo -e "$RED$BRANCH_NAME [FAILURE] (patched commit does not change the file $BUILD_LOG_FILE_NAME)"
    exit 1
else
    echo "> The file $BUILD_LOG_FILE_NAME was changed in the patched commit."
fi

echo "> Checking $TEST_LOG_FILE_NAME..."

SEARCH_TEST_LOG_IN_BUGGY_COMMIT=$(git log -n 1 --grep="$BUGGY_COMMIT_MESSAGE_PATTERN" --format=format:%H -- "$TEST_LOG_FILE_NAME")
if [ -z "$SEARCH_TEST_LOG_IN_BUGGY_COMMIT" ]; then
    echo -e "$RED$BRANCH_NAME [FAILURE] (buggy commit does not change the file $TEST_LOG_FILE_NAME)"
    exit 1
else
    echo "> The file $TEST_LOG_FILE_NAME was changed in the buggy commit."
fi
SEARCH_TEST_LOG_IN_PATCHED_COMMIT=$(git log -n 1 --grep="$PATCHED_COMMIT_MESSAGE_PATTERN" --format=format:%H -- "$TEST_LOG_FILE_NAME")
if [ -z "$SEARCH_TEST_LOG_IN_PATCHED_COMMIT" ]; then
    echo -e "$RED$BRANCH_NAME [FAILURE] (patched commit does not change the file $TEST_LOG_FILE_NAME)"
    exit 1
else
    echo "> The file $TEST_LOG_FILE_NAME was changed in the patched commit."
fi

echo -e "$GREEN$BRANCH_NAME [OK]"
