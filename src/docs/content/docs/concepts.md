+++
[menu.docs]
name = "Core Concepts"
weight = 5
+++

# Core Concepts

## Segments

Reaper splits repair runs in segments. A segment is a subrange of tokens that fits entirely in one of the cluster token ranges. The minimum number of segments for a repair run is the number of token ranges in the cluster. With a 3 nodes cluster using 256 vnodes per node, a repair run will have at least 768 segments. If necessary, each repair can define a higher number of segments than the number of token ranges.

## Back-pressure

Reaper will associate each segment with its replicas, and run repairs sequentially on only one of the replicas. If a repair is already running on one of the replicas or if there are too many pending compactions (20 by default), Reaper will postpone the segment for future processing and try to repair the next segment.

## Concurrency and Multithreading

Being a multi threaded service, Reaper will compute how many concurrent repair sessions can run on the cluster and adjust its thread pool accordingly. To that end, it will check the number of nodes in the cluster and the RF (Replication Factor) of the repaired keyspace. On a three node cluster with RF=3, only one segment can be repaired at a time. On a six node cluster with RF=3, two segments can be repaired at the same time.

The maximum number of concurrent repairs is 15 by default and can be modified in the YAML configuration (_cassandra-reaper.yaml_) file.

## Timeout
By default, each segment must complete within 30 minutes. Reaper subscribes to the repair service notifications of Cassandra to monitor completion, and if a segment takes more than 30 minutes it gets cancelled and postponed. This means that if a repair job is subject to frequent segment cancellation, it is necessary to either split it up into more segments or raise the timeout over its default value.

## Pause and Resume
Pausing a repair will force the termination of all running segments. Once the job is resumed, cancelled segments will be fully processed once again from the beginning.

## Intensity
Intensity controls the eagerness by which Reaper triggers repair segments. The Reaper will use the duration of the previous repair segment to compute how much time to wait before triggering the next one. The idea behind this is that long segments mean a lot of data mismatch, and thus a lot of streaming and compaction. Intensity allows reaper to adequately back off and give the cluster time to handle the load caused by the repair.

## Scheduling Interval
Reaper polls for new segments to process at a fixed interval. By default the interval is set to 30 seconds and this value can be modified in the Reaper configuration YAML file.