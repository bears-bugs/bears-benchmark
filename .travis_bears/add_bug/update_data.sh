#!/usr/bin/env bash

set -e

# the branch pr-add-bug is checked out

python ./.travis_bears/add_bug/update_webpage.py "$NEW_BRANCH_NAME" "$TRAVIS_REPO_SLUG"
python ./.travis_bears/add_bug/update_releases_folder.py "$NEW_BRANCH_NAME"
