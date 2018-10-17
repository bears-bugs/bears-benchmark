#!/bin/bash

set -ex
mvn clean
pdd --file=/dev/null
mvn install -Pqulice