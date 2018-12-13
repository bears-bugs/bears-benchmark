# Apache Dubbo (incubating) Project

[![Build Status](https://travis-ci.org/apache/incubator-dubbo.svg?branch=master)](https://travis-ci.org/apache/incubator-dubbo)
[![codecov](https://codecov.io/gh/apache/incubator-dubbo/branch/master/graph/badge.svg)](https://codecov.io/gh/apache/incubator-dubbo)
![maven](https://img.shields.io/maven-central/v/com.alibaba/dubbo.svg)
![license](https://img.shields.io/github/license/alibaba/dubbo.svg)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/apache/incubator-dubbo.svg)](http://isitmaintained.com/project/apache/incubator-dubbo "Average time to resolve an issue")
[![Percentage of issues still open](http://isitmaintained.com/badge/open/apache/incubator-dubbo.svg)](http://isitmaintained.com/project/apache/incubator-dubbo "Percentage of issues still open")
[![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=Apache%20Dubbo%20(incubating)%20is%20a%20high-performance%2C%20java%20based%2C%20open%20source%20RPC%20framework.&url=http://dubbo.incubator.apache.org/&via=ApacheDubbo&hashtags=rpc,java,dubbo,micro-service)
[![Gitter](https://badges.gitter.im/alibaba/dubbo.svg)](https://gitter.im/alibaba/dubbo?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

Apache Dubbo (incubating) is a high-performance, Java based open source RPC framework. Please visit [official site](http://dubbo.incubator.apache.org) for quick start and documentations, as well as [Wiki](https://github.com/apache/incubator-dubbo/wiki) for news, FAQ, and release notes.

We are now collecting dubbo user info in order to help us to improve Dubbo better, pls. kindly help us by providing yours on [issue#1012: Wanted: who's using dubbo](https://github.com/apache/incubator-dubbo/issues/1012), thanks :)

## Architecture

![Architecture](http://dubbo.apache.org/img/architecture.png)

## Features

* Transparent interface based RPC
* Intelligent load balancing
* Automatic service registration and discovery
* High extensibility
* Runtime traffic routing
* Visualized service governance

## Getting started

The following code snippet comes from [Dubbo Samples](https://github.com/dubbo/dubbo-samples/tree/master/dubbo-samples-api). You may clone the sample project and step into `dubbo-samples-api` sub directory before read on.

```bash
# git clone https://github.com/dubbo/dubbo-samples.git
# cd dubbo-samples/dubbo-samples-api
```

There's a [README](https://github.com/dubbo/dubbo-samples/blob/master/dubbo-samples-api/README.md) file under `dubbo-samples-api` directory. Read it and try this sample out by following the instructions.

### Maven dependency

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>dubbo</artifactId>
    <version>2.6.4</version>
</dependency>
```

### Define service interfaces

```java
package org.apache.dubbo.demo.api;

public interface GreetingService {
    String sayHello(String name);
}
```

*See [api/GreetingService.java](https://github.com/dubbo/dubbo-samples/blob/master/dubbo-samples-api/src/main/java/com/alibaba/dubbo/samples/api/GreetingsService.java) on GitHub.*

### Implement service interface for the provider

```java
package org.apache.dubbo.demo.provider;
 
import org.apache.dubbo.demo.GreetingService;
 
public class GreetingServiceImpl implements GreetingService {
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
```

*See [provider/GreetingServiceImpl.java](https://github.com/dubbo/dubbo-samples/blob/master/dubbo-samples-api/src/main/java/com/alibaba/dubbo/samples/server/GreetingsServiceImpl.java) on GitHub.*

### Start service provider

```java
package org.apache.dubbo.demo.provider;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import org.apache.dubbo.demo.GreetingService;

import java.io.IOException;
 
public class Application {

    public static void main(String[] args) throws IOException {
        ServiceConfig<GreetingService> serviceConfig = new ServiceConfig<GreetingService>();
        serviceConfig.setApplication(new ApplicationConfig("first-dubbo-provider"));
        serviceConfig.setRegistry(new RegistryConfig("multicast://224.5.6.7:1234"));
        serviceConfig.setInterface(GreetingService.class);
        serviceConfig.setRef(new GreetingServiceImpl());
        serviceConfig.export();
        System.in.read();
    }
}
```

*See [provider/Application.java](https://github.com/dubbo/dubbo-samples/blob/master/dubbo-samples-api/src/main/java/com/alibaba/dubbo/samples/provider/Application.java) on GitHub.*

### Build and run the provider

```bash
# mvn clean package
# mvn -Djava.net.preferIPv4Stack=true -Dexec.mainClass=org.apache.dubbo.demo.provider.Application exec:java
```

### Call remote service in consumer

```java
package org.apache.dubbo.demo.consumer;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import org.apache.dubbo.demo.GreetingService;

public class Application {
    public static void main(String[] args) {
        ReferenceConfig<GreetingService> referenceConfig = new ReferenceConfig<GreetingService>();
        referenceConfig.setApplication(new ApplicationConfig("first-dubbo-consumer"));
        referenceConfig.setRegistry(new RegistryConfig("multicast://224.5.6.7:1234"));
        referenceConfig.setInterface(GreetingService.class);
        GreetingService greetingService = referenceConfig.get();
        System.out.println(greetingService.sayHello("world"));
    }
}
```

### Build and run the consumer

```bash
# mvn clean package
# mvn -Djava.net.preferIPv4Stack=true -Dexec.mainClass=org.apache.dubbo.demo.consumer.Application exec:java
```

The consumer will print out `Hello world` on the screen.

*See [consumer/Application.java](https://github.com/dubbo/dubbo-samples/blob/master/dubbo-samples-api/src/main/java/com/alibaba/dubbo/samples/consumer/Application.java) on GitHub.*

### Next steps

* [Your first Dubbo application](http://dubbo.apache.org/en-us/blog/dubbo-101.html) - A 101 tutorial to reveal more details, with the same code above.
* [Dubbo user manual](http://dubbo.apache.org/en-us/docs/user/preface/background.html) - How to use Dubbo and all its features.
* [Dubbo developer guide](http://dubbo.apache.org/en-us/docs/dev/build.html) - How to invovle in Dubbo development.
* [Dubbo admin manual](http://dubbo.apache.org/en-us/docs/admin/install/provider-demo.html) - How to admin and manage Dubbo services.

## Contact

* Mailing list: 
  * dev list: for dev/user discussion. [subscribe](mailto:dev-subscribe@dubbo.incubator.apache.org), [unsubscribe](mailto:dev-unsubscribe@dubbo.incubator.apache.org), [archive](https://lists.apache.org/list.html?dev@dubbo.apache.org),  [guide](https://github.com/apache/incubator-dubbo/wiki/Mailing-list-subscription-guide)
  
* Bugs: [Issues](https://github.com/apache/incubator-dubbo/issues/new?template=dubbo-issue-report-template.md)
* Gitter: [Gitter channel](https://gitter.im/alibaba/dubbo) 
* Twitter: [@ApacheDubbo](https://twitter.com/ApacheDubbo)

## Contributing

See [CONTRIBUTING](https://github.com/apache/incubator-dubbo/blob/master/CONTRIBUTING.md) for details on submitting patches and the contribution workflow.

### How can I contribute?

* Take a look at issues with tag called [`Good first issue`](https://github.com/apache/incubator-dubbo/issues?q=is%3Aopen+is%3Aissue+label%3A%22good+first+issue%22) or [`Help wanted`](https://github.com/apache/incubator-dubbo/issues?q=is%3Aopen+is%3Aissue+label%3A%22help+wanted%22).
* Join the discussion on mailing list, subscription [guide](https://github.com/apache/incubator-dubbo/wiki/Mailing-list-subscription-guide).
* Answer questions on [issues](https://github.com/apache/incubator-dubbo/issues).
* Fix bugs reported on [issues](https://github.com/apache/incubator-dubbo/issues), and send us pull request.
* Review the existing [pull request](https://github.com/apache/incubator-dubbo/pulls).
* Improve the [website](https://github.com/apache/incubator-dubbo-website), typically we need
  * blog post
  * translation on documentation
  * use cases about how Dubbo is being used in enterprise system.
* Improve the [dubbo-ops/dubbo-monitor](https://github.com/apache/incubator-dubbo-ops).
* Contribute to the projects listed in [ecosystem](https://github.com/dubbo).
* Any form of contribution that is not mentioned above.
* If you would like to contribute, please send an email to dev@dubbo.incubator.apache.org to let us know!

## Reporting bugs

Please follow the [template](https://github.com/apache/incubator-dubbo/issues/new?template=dubbo-issue-report-template.md) for reporting any issues.

## Reporting a security vulnerability

Please report security vulnerability to [us](security@dubbo.incubator.apache.org) privately.

## Dubbo eco system

* [Dubbo Ecosystem Entry](https://github.com/dubbo) - A GitHub group `dubbo` to gather all Dubbo relevant projects not appropriate in [apache](https://github.com/apache) group yet
* [Dubbo Website](https://github.com/apache/incubator-dubbo-website) - Apache Dubbo (incubating) official website
* [Dubbo Samples](https://github.com/dubbo/dubbo-samples) - samples for Apache Dubbo (incubating)
* [Dubbo Spring Boot](https://github.com/apache/incubator-dubbo-spring-boot-project) - Spring Boot Project for Dubbo
* [Dubbo OPS](https://github.com/apache/incubator-dubbo-ops) - The reference implementation for Dubbo admin

#### Language

* [Node.js](https://github.com/dubbo/dubbo2.js)
* [Python](https://github.com/dubbo/dubbo-client-py)
* [PHP](https://github.com/dubbo/dubbo-php-framework)

## License

Apache Dubbo is under the Apache 2.0 license. See the [LICENSE](https://github.com/apache/incubator-dubbo/blob/master/LICENSE) file for details.
