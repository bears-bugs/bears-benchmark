[![Build Status](https://travis-ci.org/DmitriiSerikov/money-transfer-service.svg?branch=master)](https://travis-ci.org/DmitriiSerikov/money-transfer-service)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.github.example%3Amoney-transfer-service&metric=alert_status)](https://sonarcloud.io/dashboard/index/com.github.example%3Amoney-transfer-service)
[![codecov](https://codecov.io/gh/DmitriiSerikov/money-transfer-service/branch/master/graph/badge.svg)](https://codecov.io/gh/DmitriiSerikov/money-transfer-service)

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/57052f61858db233b359#?env%5BHeroku%5D=W3siZW5hYmxlZCI6dHJ1ZSwia2V5IjoidXJsIiwidmFsdWUiOiJodHRwczovL21vbmV5LXRyYW5zZmVyLXNlcnZpY2UuaGVyb2t1YXBwLmNvbSIsInR5cGUiOiJ0ZXh0In0seyJlbmFibGVkIjp0cnVlLCJrZXkiOiJhcGlfdmVyc2lvbiIsInZhbHVlIjoiMS4wIiwidHlwZSI6InRleHQifV0=)

# Money transfer service powered by Micronaut

## How to start

Once the application is pulled from git it can be built and run by maven wrapper using command:

    mvnw clean install

This will fetch dependencies and run all tests

To run the app use following command:

    mvnw exec:exec

The application will start on the default port 8080

