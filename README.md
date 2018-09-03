# Hand Hygiene Monitoring System (H2MS)



## Build


```
./mvnw install
```

## Run

And then launch the server:

```
java -jar target/h2ms-0.0.1-SNAPSHOT.jar
```

And visit http://localhost:8080/ in your browser.

To see an example End Point visit http://localhost:8080/managementDashboard/eventexample in your browser.

You can also build and run a docker container, see below.

## Docker

See [Spring Boot Docker](https://spring.io/guides/gs/spring-boot-docker/).  Basically:

```
./mvnw install dockerfile:build
```

And then:

```
docker run -d -p 8080:8080 cscie599/h2ms
```

Alternatively (to not keep the container running after ctrl-c):
```
docker run --rm -it -p 8080:8080 cscie599/h2ms
```

## Docker Compose

In the project root:

```
docker-compose up
```

## In Docker for Windows

1. Start Kitematic
1. Go into Docker CLI
1. If not done already, set user permissions  
``Set-ExecutionPolicy -Scope CurrentUser Unrestricted``
1. Then
``./buildrun.ps1``

## Email Setup


H2MS requires mailserver.properties for email to function.  E.g., for Gmail:

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=username
spring.mail.password=password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

You can use mailserver.properties.template as template to create mailserver.propertiesin src/main/resources folder.S

### Troubleshooting
If you are having issues with Docker not starting up, try uninstalling and installing the older version.
 [Docker Windows Older Version Installer](https://download.docker.com/win/stable/14687/Docker%20for%20Windows%20Installer.exe)  
Relevant Docker Issue: [Upgrade then Unable to create File System Image · Issue #1514 · docker/for-win](https://github.com/docker/for-win/issues/1514)

## Credits

- Security Implementation based on Giau Ngo's tutorial: https://hellokoding.com/registration-and-login-example-with-spring-security-spring-boot-spring-data-jpa-hsql-jsp/
- OAuth2 implementation based on https://gigsterous.github.io/engineering/2017/03/01/spring-boot-4.html
- OAuth2 tests based on http://www.baeldung.com/oauth-api-testing-with-spring-mvc
- Reset password based on https://www.codebyamir.com/blog/forgot-password-feature-with-java-and-spring-boot

