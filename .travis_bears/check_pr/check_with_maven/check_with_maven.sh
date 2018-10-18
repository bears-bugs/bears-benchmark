#!/usr/bin/env bash

if [ "$#" -ne 2 ]; then
    echo "Usage: ./check_with_maven.sh <branch name> <1 for buggy commit, 0 for patched commit>"
    exit 2
fi

BRANCH_NAME=$1
IS_BUGGY_COMMIT=$2

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

MAVEN_TEST_ARGS="-Denforcer.skip=true -Dcheckstyle.skip=true -Dcobertura.skip=true -DskipITs=true -Drat.skip=true -Dlicense.skip=true -Dfindbugs.skip=true -Dgpg.skip=true -Dskip.npm=true -Dskip.gulp=true -Dskip.bower=true"

cd pr

BUGGY_COMMIT_ID=""

CASE=$(cat bears.json | sed 's/.*"type": "\(.*\)".*/\1/;t;d')
echo "> Branch from case $CASE"
if [ "$CASE" == "failing_passing" ]; then
    BUGGY_COMMIT_ID=$(git log --format=format:%H --grep="Bug commit")
else
    BUGGY_COMMIT_ID=$(git log --format=format:%H --grep="Changes in the tests")
fi

PATCHED_COMMIT_ID=$(git log --format=format:%H --grep="Human patch")

BUGGY_BUILD_ID=$(jq -r '.["builds"]["buggyBuild"]["id"]' bears.json)
POM_PATH=$(jq -r '.["reproductionBuggyBuild"]["projectRootPomPath"]' bears.json)
POM_PATH=$(echo "$POM_PATH" | sed -e "s/.*$BUGGY_BUILD_ID\///g")
POM_PATH=$(echo "$POM_PATH" | sed -e 's/pom.xml//g')

if [ ! -z "$POM_PATH" ]; then
    cd "$POM_PATH"
fi

if [ "$IS_BUGGY_COMMIT" -eq 1 ]; then

    echo "> Checking out the buggy commit: $BUGGY_COMMIT_ID"
    git log --format=%B -n 1 $BUGGY_COMMIT_ID

    git checkout -q $BUGGY_COMMIT_ID

    mvn install -V -DskipTests=true -B  $MAVEN_TEST_ARGS

    mvn -B test $MAVEN_TEST_ARGS

    status=$?
    if [ "$status" -eq 0 ]; then
        echo -e "$RED$BRANCH_NAME [FAILURE] (bug reproduction - status = 0)"
        exit 1
    fi

else

    echo "> Checking out the patched commit: $PATCHED_COMMIT_ID"
    git log --format=%B -n 1 $PATCHED_COMMIT_ID

    git checkout -q $PATCHED_COMMIT_ID

    mvn install -V -DskipTests=true -B  $MAVEN_TEST_ARGS

    mvn -B test $MAVEN_TEST_ARGS

    status=$?
    if [ "$status" -ne 0 ]; then
        echo -e "$RED$BRANCH_NAME [FAILURE] (patch reproduction - status = $status)"
        exit 1
    fi
fi

echo -e "$GREEN$BRANCH_NAME [OK]"
