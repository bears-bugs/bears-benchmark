lettuce - Advanced Java Redis client
===============================

[![Build Status](https://travis-ci.org/lettuce-io/lettuce-core.svg)](https://travis-ci.org/lettuce-io/lettuce-core) [![codecov](https://codecov.io/gh/lettuce-io/lettuce-core/branch/master/graph/badge.svg)](https://codecov.io/gh/lettuce-io/lettuce-core)
 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.lettuce/lettuce-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.lettuce/lettuce-core)

Lettuce is a scalable thread-safe Redis client for synchronous,
asynchronous and reactive usage. Multiple threads may share one connection if they avoid blocking and transactional
operations such as `BLPOP` and  `MULTI`/`EXEC`.
Lettuce is built with [netty](https://github.com/netty/netty).
Supports advanced Redis features such as Sentinel, Cluster, Pipelining, Auto-Reconnect and Redis data models.

This version of Lettuce has been tested against the latest Redis source-build.

* [synchronous](https://github.com/lettuce-io/lettuce-core/wiki/Basic-usage), [asynchronous](https://github.com/lettuce-io/lettuce-core/wiki/Asynchronous-API-%284.0%29) and [reactive](https://github.com/lettuce-io/lettuce-core/wiki/Reactive-API-%284.0%29) usage
* [Redis Sentinel](https://github.com/lettuce-io/lettuce-core/wiki/Redis-Sentinel)
* [Redis Cluster](https://github.com/lettuce-io/lettuce-core/wiki/Redis-Cluster)
* [SSL](https://github.com/lettuce-io/lettuce-core/wiki/SSL-Connections) and [Unix Domain Socket](https://github.com/lettuce-io/lettuce-core/wiki/Unix-Domain-Sockets) connections
* [Streaming API](https://github.com/lettuce-io/lettuce-core/wiki/Streaming-API)
* [CDI](https://github.com/lettuce-io/lettuce-core/wiki/CDI-Support) and [Spring](https://github.com/lettuce-io/lettuce-core/wiki/Spring-Support) integration
* [Codecs](https://github.com/lettuce-io/lettuce-core/wiki/Codecs) (for UTF8/bit/JSON etc. representation of your data)
* multiple [Command Interfaces](https://github.com/lettuce-io/lettuce-core/wiki/Command-Interfaces-%284.0%29)
* Compatible with Java 8 and 9 (implicit automatic module w/o descriptors)

See the [reference documentation](https://lettuce.io/docs/) and [Wiki](https://github.com/lettuce-io/lettuce-core/wiki) for more details.


Communication
---------------

* Google Group: [lettuce-redis-client-users](https://groups.google.com/d/forum/lettuce-redis-client-users) or lettuce-redis-client-users@googlegroups.com
* [![Join the chat at https://gitter.im/lettuce-io/Lobby](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/lettuce-io/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
* Twitter: [@LettuceDriver](https://twitter.com/LettuceDriver)
* [GitHub Issues](https://github.com/lettuce-io/lettuce-core/issues)


Documentation
---------------

* [Reference documentation](https://lettuce.io/docs/)
* [Wiki](https://github.com/lettuce-io/lettuce-core/wiki)
* [Javadoc](http://redis.paluch.biz/docs/api/releases/latest/)


Binaries/Download
----------------

Binaries and dependency information for Maven, Ivy, Gradle and others can be found at http://search.maven.org.

Releases of lettuce are available in the Maven Central repository. Take also a look at the [Releases](https://github.com/lettuce-io/lettuce-core/releases).

Example for Maven:

```xml
<dependency>
  <groupId>io.lettuce</groupId>
  <artifactId>lettuce-core</artifactId>
  <version>x.y.z</version>
</dependency>
```

Shaded JAR-File (packaged dependencies  and relocated to the `io.lettuce.core` package to prevent version conflicts)

```xml
<dependency>
  <groupId>io.lettuce</groupId>
  <artifactId>lettuce-core</artifactId>
  <version>x.y.z</version>
  <classifier>shaded</classifier>
  <exclusions>
    <exclusion>
      <groupId>io.projectreactor</groupId>
      <artifactId>reactor-core</artifactId>
    </exclusion>
    <exclusion>
      <groupId>org.latencyutils</groupId>
      <artifactId>LatencyUtils</artifactId>
    </exclusion>
    <exclusion>
      <groupId>io.netty</groupId>
      <artifactId>netty-common</artifactId>
    </exclusion>
    <exclusion>
      <groupId>io.netty</groupId>
      <artifactId>netty-transport</artifactId>
    </exclusion>
    <exclusion>
      <groupId>io.netty</groupId>
      <artifactId>netty-handler</artifactId>
    </exclusion>
    <exclusion>
      <groupId>io.netty</groupId>
      <artifactId>netty-codec</artifactId>
    </exclusion>
    <exclusion>
      <groupId>io.netty</groupId>
      <artifactId>netty-transport-native-epoll</artifactId>
    </exclusion>
    <exclusion>
      <groupId>io.netty</groupId>
      <artifactId>netty-transport-native-kqueue</artifactId>
    </exclusion>
    <exclusion>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-pool2</artifactId>
    </exclusion>
  </exclusions>
</dependency>
```    

or snapshots at https://oss.sonatype.org/content/repositories/snapshots/

Basic Usage
-----------

```java
RedisClient client = RedisClient.create("redis://localhost");
StatefulRedisConnection<String, String> connection = client.connect();
RedisStringCommands sync = connection.sync();
String value = sync.get("key");
```

Each Redis command is implemented by one or more methods with names identical
to the lowercase Redis command name. Complex commands with multiple modifiers
that change the result type include the CamelCased modifier as part of the
command name, e.g. zrangebyscore and zrangebyscoreWithScores.

See [Basic usage](https://github.com/lettuce-io/lettuce-core/wiki/Basic-usage) for further details.

Asynchronous API
------------------------

```java
StatefulRedisConnection<String, String> connection = client.connect();
RedisStringAsyncCommands<String, String> async = connection.async();
RedisFuture<String> set = async.set("key", "value")
RedisFuture<String> get = async.get("key")

async.awaitAll(set, get) == true

set.get() == "OK"
get.get() == "value"
```

See [Asynchronous API](https://github.com/lettuce-io/lettuce-core/wiki/Asynchronous-API-%284.0%29) for further details.

Reactive API
------------------------

```java
StatefulRedisConnection<String, String> connection = client.connect();
RedisStringReactiveCommands<String, String> reactive = connection.reactive();
Observable<String> set = reactive.set("key", "value")
Observable<String> get = reactive.get("key")

set.subscribe();

get.toBlocking().single() == "value"
```

See [Reactive API](https://github.com/lettuce-io/lettuce-core/wiki/Reactive-API-%284.0%29) for further details.

Pub/Sub
-------

```java
RedisPubSubCommands<String, String> connection = client.connectPubSub().sync();
connection.addListener(new RedisPubSubListener<String, String>() { ... })
connection.subscribe("channel")
```

Building
-----------

Lettuce is built with Apache Maven. The tests require multiple running Redis instances for different test cases which
are configured using a ```Makefile```. All tests run against Redis branch 3.0

To build:

```
$ git clone https://github.com/lettuce-io/lettuce-core.git
$ cd lettuce/
$ make prepare ssl-keys
$ make test
```

* Initial environment setup (clone and build `redis`): ```make prepare```
* Setup SSL Keys: ```make ssl-keys```
* Run the build: ```make test```
* Start Redis (manually): ```make start```
* Stop Redis (manually): ```make stop```

Bugs and Feedback
-----------

For bugs, questions and discussions please use the [GitHub Issues](https://github.com/lettuce-io/lettuce-core/issues).

License
-------

* [Apache License 2.0] (http://www.apache.org/licenses/LICENSE-2.0)
* Fork of https://github.com/wg/lettuce

Contributing
-------

Github is for social coding: if you want to write code, I encourage contributions through pull requests from forks of this repository. 
Create Github tickets for bugs and new features and comment on the ones that you are interested in and take a look into [CONTRIBUTING.md](https://github.com/lettuce-io/lettuce-core/blob/master/.github/CONTRIBUTING.md)
