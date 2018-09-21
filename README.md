[![Build Status][ci-img]][ci] [![Coverage Status][cov-img]][cov] [![Released Version][maven-img]][maven]

# opentracing-p6spy
OpenTracing instrumentation for p6spy

## Installation

pom.xml
```xml
<dependency>
    <groupId>io.opentracing.contrib</groupId>
    <artifactId>opentracing-p6spy</artifactId>
    <version>0.2.0</version>
</dependency>
```

build.gradle
```groovy
compile 'io.opentracing.contrib:opentracing-p6spy:0.2.0'
```

## Usage
If you don't already have one, create a `spy.properties` file on your resource folder (ie `src/main/resources`)

Add the tracing module in your list
```properties
modulelist=io.opentracing.contrib.p6spy.TracingP6SpyFactory
tracingPeerService=token_database
traceWithActiveSpanOnly=true
```
* `tracingPeerService` is used to set the `peer.service` value as defined [here](https://github.com/opentracing/specification/blob/master/semantic_conventions.md).
* `traceWithActiveSpanOnly` in case you only want to trace calls when there is an active span;

`spy.properties` is set globally to all instrumented connections. This can be limitating especially in environment accessing many databases.
To overcome this, you can optionally set the `tracingPeerService` and `traceWithActiveSpanOnly` in the jdbc url : 
```
jdbc:p6spy:mysql://localhost/tk_db?tracingPeerService=token_database;traceWithActiveSpanOnly=true
```
This will override `spy.properties`.

Beware that some JDBC drivers do not support adding unknown properties.

Tips when using it in JavaEE application servers. If you happen to deploy many applications within the same application server, add the `jmxPrefix` to avoid jmx name conflict :
```properties
modulelist=io.opentracing.contrib.p6spy.TracingP6SpyFactory
tracingPeerService=token_database
traceWithActiveSpanOnly=true
jmxPrefix=authentication_service
``` 

You can find more info on p6spy [here](https://github.com/p6spy/p6spy)

## Tracing tags
The following tags are added to traces :
 
| Span tag name | Notes |
|:--------------|:-------------------|
| `span.kind` | `client` |
| `component` | `java-p6spy` |
| `peer.service` | if exists, the peer service name set in `spy.properties` or within the jdbc url using `tracingPeerService` |
| `error` | `true` is any error occurred. `false` otherwise |
| `db.type` | if exists, the authoritative part of the jdbc url (ex : `mysql` in `jdbc:mysql://localhost`) |
| `db.statement` | the SQL query |
| `db.instance` | if exists, the connection's catalog (can be a database name or a schema) |
| `db.user` | if exists, the user name |

[ci-img]: https://travis-ci.org/opentracing-contrib/java-p6spy.svg?branch=master
[ci]: https://travis-ci.org/opentracing-contrib/java-p6spy
[cov-img]: https://coveralls.io/repos/github/opentracing-contrib/java-p6spy/badge.svg?branch=master
[cov]: https://coveralls.io/github/opentracing-contrib/java-p6spy?branch=master
[maven-img]: https://img.shields.io/maven-central/v/io.opentracing.contrib/opentracing-p6spy.svg
[maven]: http://search.maven.org/#search%7Cga%7C1%7Copentracing-p6spy