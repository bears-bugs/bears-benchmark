#!/usr/bin/env bash

set -e

# the branch pr-add-bug is checked out

PARENTS_OF_THE_MERGED_COMMIT=$(git log --pretty=%P -n 1)
LAST_COMMIT_BEFORE_MERGING=${PARENTS_OF_THE_MERGED_COMMIT%' '*}
git reset --hard "$LAST_COMMIT_BEFORE_MERGING"
git push -f github pr-add-bug
