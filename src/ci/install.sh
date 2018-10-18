#!/bin/bash

echo "Starting Install step..."

set -xe

case "${TEST_TYPE}" in
    "")
        echo "ERROR: Environment variable TEST_TYPE is unspecified."
        exit 1
        ;;
    "ccm")
        cp src/ci/jmxremote.password /home/travis/.local/jmxremote.password
        chmod 400 /home/travis/.local/jmxremote.password
        cat /usr/lib/jvm/java-8-oracle/jre/lib/management/jmxremote.access
        sudo chmod 777 /usr/lib/jvm/java-8-oracle/jre/lib/management/jmxremote.access
        echo "cassandra     readwrite" >> /usr/lib/jvm/java-8-oracle/jre/lib/management/jmxremote.access
        cat /usr/lib/jvm/java-8-oracle/jre/lib/management/jmxremote.access
        ccm create test -v $CASSANDRA_VERSION > /dev/null
        ccm populate --vnodes -n 2 > /dev/null
        sed -i 's/etc\/cassandra\/jmxremote.password/home\/travis\/.local\/jmxremote.password/' /home/travis/.ccm/test/node1/conf/cassandra-env.sh
        sed -i 's/etc\/cassandra\/jmxremote.password/home\/travis\/.local\/jmxremote.password/' /home/travis/.ccm/test/node2/conf/cassandra-env.sh
        sed -i 's/#MAX_HEAP_SIZE="4G"/MAX_HEAP_SIZE="192m"/' /home/travis/.ccm/test/node1/conf/cassandra-env.sh
        sed -i 's/#MAX_HEAP_SIZE="4G"/MAX_HEAP_SIZE="192m"/' /home/travis/.ccm/test/node2/conf/cassandra-env.sh
        sed -i 's/_timeout_in_ms:.*/_timeout_in_ms: 60000/' /home/travis/.ccm/test/node1/conf/cassandra.yaml
        sed -i 's/_timeout_in_ms:.*/_timeout_in_ms: 60000/' /home/travis/.ccm/test/node2/conf/cassandra.yaml
        sed -i 's/start_rpc: true/start_rpc: false/' /home/travis/.ccm/test/node1/conf/cassandra.yaml
        sed -i 's/start_rpc: true/start_rpc: false/' /home/travis/.ccm/test/node2/conf/cassandra.yaml
        sed -i 's/cross_node_timeout: false/cross_node_timeout: true/' /home/travis/.ccm/test/node1/conf/cassandra.yaml
        sed -i 's/cross_node_timeout: false/cross_node_timeout: true/' /home/travis/.ccm/test/node2/conf/cassandra.yaml
        sed -i 's/concurrent_reads: 32/concurrent_reads: 2/' /home/travis/.ccm/test/node1/conf/cassandra.yaml
        sed -i 's/concurrent_reads: 32/concurrent_reads: 2/' /home/travis/.ccm/test/node2/conf/cassandra.yaml
        sed -i 's/concurrent_writes: 32/concurrent_writes: 2/' /home/travis/.ccm/test/node1/conf/cassandra.yaml
        sed -i 's/concurrent_writes: 32/concurrent_writes: 2/' /home/travis/.ccm/test/node2/conf/cassandra.yaml
        sed -i 's/concurrent_counter_writes: 32/concurrent_counter_writes: 2/' /home/travis/.ccm/test/node1/conf/cassandra.yaml
        sed -i 's/concurrent_counter_writes: 32/concurrent_counter_writes: 2/' /home/travis/.ccm/test/node2/conf/cassandra.yaml
        sed -i 's/num_tokens: 256/num_tokens: 32/' /home/travis/.ccm/test/node1/conf/cassandra.yaml
        sed -i 's/num_tokens: 256/num_tokens: 32/' /home/travis/.ccm/test/node2/conf/cassandra.yaml
        ;;
    *)
        echo "Skipping, no actions for TEST_TYPE=${TEST_TYPE}."
esac