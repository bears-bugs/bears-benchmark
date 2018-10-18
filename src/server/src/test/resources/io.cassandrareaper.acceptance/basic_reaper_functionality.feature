Feature: Using Reaper to launch repairs

  ## TODO: clean-up and split the scenarios to be more like Given -> When -> Then [-> But]

  Background:
    Given cluster seed host "127.0.0.1" points to cluster with name "test_cluster"
    And cluster "test_cluster" has keyspace "test_keyspace" with tables "test_table1, test_table2"
    And cluster "test_cluster" has keyspace "test_keyspace2" with tables "test_table1, test_table2"
    And cluster "test_cluster" has keyspace "test_keyspace3" with tables "test_table1, test_table2"
    And cluster "test_cluster" has keyspace "system" with tables "system_table1, system_table2"
    And cluster seed host "127.0.0.2" points to cluster with name "other_cluster"
    And cluster "other_cluster" has keyspace "other_keyspace" with tables "test_table3, test_table4, test_table5"
    And cluster "other_cluster" has keyspace "system" with tables "system_table1, system_table2"
    And a reaper service is running

  Scenario: Registering a cluster
    Given that we are going to use "127.0.0.1" as cluster seed host
    And reaper has no cluster with name "test_cluster" in storage
    When an add-cluster request is made to reaper
    Then reaper has a cluster called "test_cluster" in storage

  Scenario: Registering a scheduled repair
    Given reaper has a cluster called "test_cluster" in storage
    And reaper has 0 scheduled repairs for cluster called "test_cluster"
    When a new daily "full" repair schedule is added for "test_cluster" and keyspace "test_keyspace"
    Then reaper has a cluster called "test_cluster" in storage
    And reaper has 1 scheduled repairs for cluster called "test_cluster"

  Scenario: Deleting a scheduled repair
    Given reaper has a cluster called "test_cluster" in storage
    And reaper has 1 scheduled repairs for cluster called "test_cluster"
    When a new daily "full" repair schedule is added for "test_cluster" and keyspace "test_keyspace2"
    And a second daily repair schedule is added for "test_cluster" and keyspace "system"
    Then reaper has a cluster called "test_cluster" in storage
    And reaper has 3 scheduled repairs for cluster called "test_cluster"
    When the last added schedule is deleted for cluster called "test_cluster"
    Then reaper has 2 scheduled repairs for cluster called "test_cluster"
    And deleting cluster called "test_cluster" fails
    
  Scenario: Adding a scheduled repair that already exists
    Given reaper has a cluster called "test_cluster" in storage
    And reaper has 2 scheduled repairs for cluster called "test_cluster"
    When a new daily "full" repair schedule is added that already exists for "test_cluster" and keyspace "test_keyspace2"
    Then reaper has 2 scheduled repairs for cluster called "test_cluster"
    And deleting cluster called "test_cluster" fails
    
  Scenario: Adding a scheduled full repair and a scheduled incremental repair for the same keyspace
    Given reaper has a cluster called "test_cluster" in storage
    And reaper has 2 scheduled repairs for cluster called "test_cluster"
    When a new daily "incremental" repair schedule is added for "test_cluster" and keyspace "test_keyspace3"
    And a new daily "full" repair schedule is added for "test_cluster" and keyspace "test_keyspace3"
    Then reaper has 4 scheduled repairs for cluster called "test_cluster"
    And deleting cluster called "test_cluster" fails

  Scenario: Create a cluster and a repair run and delete them
    Given reaper has no cluster with name "other_cluster" in storage
    And that we are going to use "127.0.0.2" as cluster seed host
    When an add-cluster request is made to reaper
    Then reaper has a cluster called "other_cluster" in storage
    And reaper has 0 scheduled repairs for cluster called "other_cluster"
    When a new daily "full" repair schedule is added for "other_cluster" and keyspace "other_keyspace"
    Then reaper has 1 scheduled repairs for cluster called "other_cluster"
    And deleting cluster called "other_cluster" fails
    When the last added schedule is deleted for cluster called "other_cluster"
    And a new repair is added for "other_cluster" and keyspace "system"
    Then reaper has 1 started or done repairs for cluster called "other_cluster"
    And deleting cluster called "other_cluster" fails
    When the last added repair run is deleted
    And cluster called "other_cluster" is deleted
    Then reaper has no cluster called "other_cluster" in storage
  
  Scenario: Create a cluster and one incremental repair run and one full repair run on the same keyspace
    Given reaper has no cluster with name "other_cluster" in storage
    And that we are going to use "127.0.0.2" as cluster seed host
    When an add-cluster request is made to reaper
    Then reaper has a cluster called "other_cluster" in storage
    And reaper has 0 scheduled repairs for cluster called "other_cluster"
    When a new incremental repair is added for "other_cluster" and keyspace "system"
    And a new repair is added for "other_cluster" and keyspace "system"
    Then reaper has 2 repairs for cluster called "other_cluster"
    
  Scenario: Create a cluster, create a cluster wide snapshot and delete it
    Given reaper has no cluster with name "other_cluster" in storage
    And that we are going to use "127.0.0.2" as cluster seed host
    When an add-cluster request is made to reaper
    Then reaper has a cluster called "other_cluster" in storage
    When a cluster wide snapshot request is made to Reaper
    Then there is 1 snapshot returned when listing snapshots
    When a request is made to clear the existing snapshot cluster wide
    Then there is 0 snapshot returned when listing snapshots
