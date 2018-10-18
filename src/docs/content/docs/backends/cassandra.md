+++
[menu.docs]
name = "Cassandra"
parent = "backends"
weight = 4
+++

# Cassandra Backend

To use Apache Cassandra as the persistent storage for Reaper, the `storageType` setting must be set to **cassandra** in the Reaper configuration YAML file. In addition, the connection details for the Apache Cassandra cluster being used to store Reaper data must be specified in the configuration YAML file. An example of how to configure Cassandra as persistent storage for Reaper can be found in the *[cassandra-reaper-cassandra.yaml](https://github.com/thelastpickle/cassandra-reaper/blob/master/src/packaging/resource/cassandra-reaper-cassandra.yaml)*.

```yaml
storageType: cassandra
cassandra:
  clusterName: "test"
  contactPoints: ["127.0.0.1"]
  keyspace: reaper_db
  queryOptions:
    consistencyLevel: LOCAL_QUORUM
    serialConsistencyLevel: SERIAL
```

If you're using authentication or SSL:

```yaml
cassandra:
  storageType: cassandra
  clusterName: "test"
  contactPoints: ["127.0.0.1"]
  keyspace: reaper_db
  authProvider:
    type: plainText
    username: cassandra
    password: cassandra
  ssl:
    type: jdk
```

The Apache Cassandra backend is the only deployment that allows multiple Reaper instances to operate concurrently. This provides high availability and allows to repair multi DC clusters.

To run Reaper using the Cassandra backend, create a reaper_db keyspace with an appropriate placement strategy. This is installation specific, and names of the data centers in the cluster that will host the Reaper data must be specified. For example:

```none
CREATE KEYSPACE reaper_db WITH replication = {'class': 'NetworkTopologyStrategy', '<data_center>': 3};
```

Where:

* `<data_center>` is the name of the Cassandra data center that will contain the keyspace replicas.

When operating Reaper in a production environment, it is recommended that:

* An RF (Replication Factor) of 3 be used in each data center for the `reaper_db` keyspace. This is to ensure that all Reaper state data is still available should a node in the cluster be unavailable.
* The `NetworkTopologyStrategy` should be used for the replication strategy of the keyspace. This is because `LOCAL_*` requests will fail if the `SimpleNetworkingStrategy` is used in an environment where there is more than one data center defined.

Schema initialization and migration will be done automatically upon startup.