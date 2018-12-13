# Release Notes

## 2.6.4

Enhancements / Features

- Support access Redis with password, [#2146](https://github.com/apache/incubator-dubbo/pull/2146)
- Support char array for GenericService, [#2137](https://github.com/apache/incubator-dubbo/pull/2137)
- Direct return when the server goes down abnormally, [#2451](https://github.com/apache/incubator-dubbo/pull/2451)
- Add log for trouble-shooting when qos start failed, [#2455](https://github.com/apache/incubator-dubbo/pull/2455)
- PojoUtil support subclasses of java.util.Date, [#2502](https://github.com/apache/incubator-dubbo/pull/2502)
- Add ip and application name for MonitorService, [#2166](https://github.com/apache/incubator-dubbo/pull/2166)
- New ASCII logo, [#2402](https://github.com/apache/incubator-dubbo/pull/2402)

Bugfixes

- Change consumer retries default value from 0 to 2, [#2303](https://github.com/apache/incubator-dubbo/pull/2303)
- Fix the problem that attachment is lost when retry, [#2024](https://github.com/apache/incubator-dubbo/pull/2024)
- Fix NPE when telnet get a null parameter, [#2453](https://github.com/apache/incubator-dubbo/pull/2453)

UT stability

- Improve the stability by changing different port, setting timeout to 3000ms, [#2501](https://github.com/apache/incubator-dubbo/pull/2501)

Issues and Pull Requests, check [milestone-2.6.4](https://github.com/apache/incubator-dubbo/milestone/19).

## 2.6.3

Enhancements / Features

- Support implicit delivery of attachments from provider to consumer, #889
- Support inject Spring bean to SPI by bean type, #1837
- Add generic invoke and attachments support for http&hessian protocol, #1827
- Get the real methodname to support consistenthash for generic invoke, #1872
- Remove validation key from provider url on Consumer side, config depedently, #1386
- Introducing the Bootstrap module as a unified entry for Dubbo startup and resource destruction, #1820
- Open TCP_NODELAY on Netty 3, #1746
- Support specify proxy type on provider side, #1873
- Support dbindex in redis, #1831
- Upgrade tomcat to 8.5.31, #1781

Bugfixes

- ExecutionDispatcher meet with user docs, #1089
- Remove side effects of Dubbo custom loggers on Netty logger, #1717
- Fix isShutdown() judge of Dubbo biz threadpool always return true, #1426
- Selection of invoker node under the critical condition of only two nodes, #1759
- Listener cann't be removed during unsubscribe when use ZK as registry, #1792
- URL parsing problem when user filed contains '@',  #1808
- Check null in CacheFilter to avoid NPE, #1828
- Fix potential deadlock in DubboProtocol, #1836
- Restore the bug that attachment has not been updated in the RpcContext when the Dubbo built-in retry mechanism is triggered, #1453
- Some other small bugfixes

Performance Tuning

- ChannelState branch prediction optimization. #1643
- Optimize AtomicPositiveInteger, less memory and compute cost, #348
- Introduce embedded Threadlocal to replace the JDK implementation, #1745

Hessian-lite

- Hessian deserialization optimization, #1705
- Support Locale type, #1761

Compatibilities  
This release is compatible with other versions since 2.5.3, and you can upgrade smoothly.
- The RPC protocol version has been upgraded from 2.0.1 to 2.0.2, to support attachments delivery in #889, 
generally, the protocol version is used internally, so it should have no side effects on users.

Issues and Pull Requests, check [milestone-2.6.3](https://github.com/apache/incubator-dubbo/milestone/17).


## 2.6.2

1. Hessian-lite serialization: revert locale serialization for compatibility, #1413
2. Asset transfer to ASF, including pom, license, DISCLAIMER and so on, #1491
3. Introduce of new dispatcher policy: EagerThreadpool, #1568
4. Separate monitor data with group and version, #1407
5. Spring Boot Enhancenment, #1611
6. Graceful shutdown enhancement
   - Remove exporter destroy logic in AnnotationBean.
   - Waiting for registry notification on consumer side by checking channel state.
7. Simplify consumer/provider side check in RpcContext, #1444.

Issues and Pull Requests, check [milestone-2.6.2](https://github.com/apache/incubator-dubbo/milestone/15).