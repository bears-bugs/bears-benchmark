#!/bin/bash -x
#
# Copyright (C) 2014-2016 LinkedIn Corp. (pinot-core@linkedin.com)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Ignore changes not related to pinot code
echo 'Changed files:'
git diff --name-only $TRAVIS_COMMIT_RANGE
if [ $? -ne 0 ]; then
  echo 'Commit range is invalid.'
  exit 1
fi

# ThirdEye related changes
git diff --name-only $TRAVIS_COMMIT_RANGE | egrep '^(thirdeye)'
noThirdEyeChange=$?
if [ $noThirdEyeChange -eq 0 ]; then
  echo 'ThirdEye changes.'
  if [ "$RUN_INTEGRATION_TESTS" == 'false' ]; then
    echo 'Skip ThirdEye build when integration tests off'
    exit 0
  fi
fi

mvn clean install -B -DskipTests=true -Dmaven.javadoc.skip=true -Dassembly.skipAssembly=true
if [ $? -ne 0 ]; then
  exit 1
fi

# Build ThirdEye for ThirdEye related changes
if [ $noThirdEyeChange -eq 0 ]; then
  cd thirdeye
  mvn clean install -DskipTests
  exit $?
fi
