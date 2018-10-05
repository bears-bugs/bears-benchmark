# VCD-java-client

[![Build Status](https://img.shields.io/travis/vitorenesduarte/VCD-java-client/master.svg)](
https://travis-ci.org/vitorenesduarte/VCD-java-client)

### Getting started

Assuming you have a VCD cluster running locally:

```bash
$ make run
```

or

```bash
$ docker run --net=host --env CLIENTS=3 \
                        --env OPS=10000 \
                        --env CONFLICTS=100 \
                        --env ZK=127.0.0.1:2181 \
                        -ti vitorenesduarte/vcd-java-client
```
