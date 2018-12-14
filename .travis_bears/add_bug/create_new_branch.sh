#!/usr/bin/env bash

set -e

# the branch pr-add-bug is checked out

CASE=$(cat bears.json | sed 's/.*"type": "\(.*\)".*/\1/;t;d')

BUGGY_COMMIT_ID=$(git log --format=format:%H --grep="$BUGGY_COMMIT_MESSAGE_PATTERN")
TEST_COMMIT_ID=$(git log --format=format:%H --grep="$TEST_COMMIT_MESSAGE_PATTERN")
PATCHED_COMMIT_ID=$(git log --format=format:%H --grep="$PATCHED_COMMIT_MESSAGE_PATTERN")
END_COMMIT_ID=$(git log --format=format:%H --grep="$END_COMMIT_MESSAGE_PATTERN")

# creates new orphan branch for the commits and push it

git checkout --orphan "$NEW_BRANCH_NAME"
git reset .
git clean -fd
git cherry-pick "$BUGGY_COMMIT_ID"
if [ "$CASE" == "passing_passing" ]; then
    git cherry-pick "$TEST_COMMIT_ID"
fi
git cherry-pick "$PATCHED_COMMIT_ID"
git cherry-pick "$END_COMMIT_ID"
git push github "$NEW_BRANCH_NAME":"$NEW_BRANCH_NAME"

# update bears.json in such new branch

git checkout pr-add-bug
python ./.travis_bears/add_bug/update_bears_json_file.py "$NEW_BRANCH_NAME"
