Feature: Using Reaper to launch repairs and schedule them

  ## TODO: clean-up and split the scenarios to be more like Given -> When -> Then [-> But]
  Background:
    Given cluster seed host "127.0.0.1@test" points to cluster with name "test"
    And ccm cluster "test" has keyspace "booya" with tables "booya1, booya2"

  @full
  Scenario: Registering a cluster
    Given that we are going to use "127.0.0.1@test" as cluster seed host
    And reaper has no cluster in storage
    When an add-cluster request is made to reaper
    Then reaper has the last added cluster in storage
    When the last added cluster is deleted
    Then reaper has no longer the last added cluster in storage
    
  @full
  Scenario: Create a cluster and a scheduled repair run and delete them
    Given that we are going to use "127.0.0.1@test" as cluster seed host
    And reaper has no cluster in storage
    When an add-cluster request is made to reaper
    Then reaper has the last added cluster in storage
    And reaper has 0 scheduled repairs for the last added cluster
    When a new daily "full" repair schedule is added for the last added cluster and keyspace "booya"
    Then reaper has 1 scheduled repairs for the last added cluster
    And deleting the last added cluster fails
    When the last added schedule is deleted for the last added cluster
    And the last added cluster is deleted
    Then reaper has no longer the last added cluster in storage

  @full
  Scenario: Create a cluster and a scheduled repair run with repair run history and delete them
    Given that we are going to use "127.0.0.1@test" as cluster seed host
    And reaper has no cluster in storage
    When an add-cluster request is made to reaper
    Then reaper has the last added cluster in storage
    And reaper has 0 scheduled repairs for the last added cluster
    When a new daily repair schedule is added for the last added cluster and keyspace "booya" with next repair immediately
    Then reaper has 1 scheduled repairs for the last added cluster
    And deleting the last added cluster fails
    When we wait for a scheduled repair run has started for cluster "test"
    And we wait for at least 1 segments to be repaired
    Then reaper has 1 started or done repairs for the last added cluster
    When the last added repair is stopped
    Then reseting one segment sets its state to not started
    And the last added repair run is deleted
    And deleting the last added cluster fails
    When all added schedules are deleted for the last added cluster
    And the last added cluster is deleted
    Then reaper has no longer the last added cluster in storage

  @full
  Scenario: Create a cluster and a repair run and delete them
    Given that we are going to use "127.0.0.1@test" as cluster seed host
    And reaper has no cluster in storage
    When an add-cluster request is made to reaper
    Then reaper has the last added cluster in storage
    And reaper has 0 repairs for the last added cluster
    When a new repair is added for the last added cluster and keyspace "booya" with the table "booya2" blacklisted
    And the last added repair has table "booya2" in the blacklist
    And deleting the last added cluster fails
    And the last added repair is activated
    And we wait for at least 1 segments to be repaired
    Then reaper has 1 started or done repairs for the last added cluster
    When the last added repair is stopped
    And the last added repair run is deleted
    And the last added cluster is deleted
    Then reaper has no longer the last added cluster in storage
    
  @incremental
  Scenario: Create a cluster and an incremental repair run and delete them
    Given that we are going to use "127.0.0.1@test" as cluster seed host
    And reaper has no cluster in storage
    When an add-cluster request is made to reaper
    Then reaper has the last added cluster in storage
    And reaper has 0 repairs for the last added cluster
    When a new incremental repair is added for the last added cluster and keyspace "booya"
    And deleting the last added cluster fails
    And the last added repair is activated
    And we wait for at least 1 segments to be repaired
    Then reaper has 1 started or done repairs for the last added cluster
    When the last added repair is stopped
    And the last added repair run is deleted
    And the last added cluster is deleted
  
  @incremental  
  Scenario: Create a cluster, create a cluster wide snapshot and delete it
    Given that we are going to use "127.0.0.1" as cluster seed host
    And reaper has no cluster in storage
    When an add-cluster request is made to reaper
    Then reaper has the last added cluster in storage
    When a request is made to clear the existing snapshot cluster wide
    And a cluster wide snapshot request is made to Reaper
    Then there is 1 snapshot returned when listing snapshots
    When a request is made to clear the existing snapshot cluster wide
    Then there is 0 snapshot returned when listing snapshots
    When the last added cluster is deleted
    Then reaper has no longer the last added cluster in storage
  
  @incremental  
  Scenario: Create a cluster, create a snapshot on a single host and delete it
    Given that we are going to use "127.0.0.1" as cluster seed host
    And reaper has no cluster in storage
    When an add-cluster request is made to reaper
    Then reaper has the last added cluster in storage
    When a request is made to clear the seed host existing snapshots
    And a snapshot request for the seed host is made to Reaper
    Then there is 1 snapshot returned when listing snapshots
    When a request is made to clear the seed host existing snapshots
    Then there is 0 snapshot returned when listing snapshots
    When the last added cluster is deleted
    Then reaper has no longer the last added cluster in storage
 