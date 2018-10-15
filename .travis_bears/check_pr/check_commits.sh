#!/usr/bin/env bash

set -e

BRANCH_NAME="$TRAVIS_PULL_REQUEST_BRANCH"

RED='\033[0;31m'
GREEN='\033[0;32m'

function checkCommit {
    if [ -z "$1" ]; then
        echo -e "$RED$BRANCH_NAME [FAILURE] (some commit is missing)"
        exit 1
    else
        echo "> The commit is OK."
    fi
}

function checkParent {
    if [ "$1" != "$2" ]; then
        echo -e "$RED$BRANCH_NAME [FAILURE] (the commits are not in the right sequence)"
        exit 1
    else
        echo "> The parent commit is OK."
    fi
}

SCRIPT_DIR="$(dirname "${BASH_SOURCE[0]}")"

CASE=$(cat bears.json | sed 's/.*"type": "\(.*\)".*/\1/;t;d')
echo "> Branch from $CASE case."

if [ "$CASE" == "failing_passing" ]; then
    echo "> 3 commits must exist."
else
    echo "> 4 commits must exist."
fi

BUGGY_COMMIT_ID=$(git log --format=format:%H --grep="$BUGGY_COMMIT_MESSAGE_PATTERN")
TEST_COMMIT_ID=$(git log --format=format:%H --grep="$TEST_COMMIT_MESSAGE_PATTERN")
PATCHED_COMMIT_ID=$(git log --format=format:%H --grep="$PATCHED_COMMIT_MESSAGE_PATTERN")
END_COMMIT_ID=$(git log --format=format:%H --grep="$END_COMMIT_MESSAGE_PATTERN")

echo "> Checking commits..."

echo "Buggy commit: $BUGGY_COMMIT_ID"
checkCommit "$BUGGY_COMMIT_ID"
if [ "$CASE" == "passing_passing" ]; then
    echo "Changes in the tests commit: $TEST_COMMIT_ID"
    checkCommit "$TEST_COMMIT_ID"
fi
echo "Patched commit: $PATCHED_COMMIT_ID"
checkCommit "$PATCHED_COMMIT_ID"
echo "End of the process commit: $END_COMMIT_ID"
checkCommit "$END_COMMIT_ID"

echo "> Checking parent commits..."

PARENT_END_COMMIT=$(git log --pretty=%P -n 1 "$END_COMMIT_ID")
PARENT_PATCHED_COMMIT=$(git log --pretty=%P -n 1 "$PATCHED_COMMIT_ID")

echo "End of the process commit's parent: $PARENT_END_COMMIT"
checkParent "$PARENT_END_COMMIT" "$PATCHED_COMMIT_ID"

if [ "$CASE" == "failing_passing" ]; then
    echo "Patched commit's parent: $PARENT_PATCHED_COMMIT"
    checkParent "$PARENT_PATCHED_COMMIT" "$BUGGY_COMMIT_ID"
else
    PARENT_TEST_COMMIT=$(git log --pretty=%P -n 1 "$TEST_COMMIT_ID")

    echo "Patched commit's parent: $PARENT_PATCHED_COMMIT"
    checkParent "$PARENT_PATCHED_COMMIT" "$TEST_COMMIT_ID"
    echo "Changes in the tests commit's parent: $PARENT_TEST_COMMIT"
    checkParent "$PARENT_TEST_COMMIT" "$BUGGY_COMMIT_ID"
fi

echo -e "$GREEN$BRANCH_NAME [OK]"
