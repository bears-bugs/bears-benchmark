# The Bears-Benchmark

The Bears-Benchmark, or just Bears, is a benchmark of bugs for automatic program repair studies in Java.
The bugs are collected from open-source projects hosted on GitHub through a process that scans pairs of builds from Travis Continuous Integration and reproduces bugs (by test failure) and their patches (passing test suite).

If you use Bears, please cite our paper:

```bibtex
@inproceedings{Madeiral2019,
  author = {Fernanda Madeiral and Simon Urli and Marcelo Maia and Martin Monperrus},
  title = {{Bears: An Extensible Java Bug Benchmark for Automatic Program Repair Studies}},
  booktitle = {Proceedings of the 26th IEEE International Conference on Software Analysis, Evolution and Reengineering (SANER '19)},
  year = {2019},
  url = {https://arxiv.org/abs/1901.06024}
}
```

## Bug Repository Design

Each bug is stored in a branch.
The name of each branch follows the pattern `<GitHub project slug>-<buggy Travis build id>-<patched Travis build id>`.
The files and data on a bug are organized in commits in the branch of the bug as follows:

- Commit #1 contains the version of the program with the bug
- Commit #2 contains the changes in the tests (the one to execute to get a failure)
- Commit #3 contains the version of the program with the human-written patch
- Commit #4 contains the metadata file `bears.json`, which is a gathering of information collected during the bug reproduction process. It contains information about the bug (e.g. test failure names), the patch (e.g. patch size), and the bug reproduction process (e.g. duration).

## Prerequisites

Ensure you use Java 8.

```
$export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/ 
$ mvn -version
Apache Maven 3.6.3
Maven home: /usr/share/maven
Java version: 1.8.0_382, vendor: Private Build, runtime: /usr/lib/jvm/java-8-openjdk-amd64/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "5.15.0-79-generic", arch: "amd64", family: "unix"
```


## How to use

In the folder [scripts](scripts/) you can find scripts to check out bugs, to compile them, and to run tests on them.


You can check out a single bug by given a bug ID (see [bug IDs](https://bears-bugs.github.io/bears-benchmark)) as argument with

```bash
python scripts/checkout_bug.py --bugId <bug ID>
```

For example,

```bash
python scripts/checkout_bug.py --bugId Bears-1
python scripts/run_tests_bug.py --bugId Bears-1 # this should fail because the checkout_bug.py has checked out the third, buggy commit
```


In the same way as you can check out one or all bugs you can compile the checked out buggy program versions and run tests on them using the scripts `compile_all.py`, `compile_bug.py`, `run_tests_all.py`, `run_tests_bug.py`.

## Data

The original benchmark contained 251 bugs (see file `scripts/data/bug_id_and_branch_2019.json`). However, many bugs have rotten with dependencies having disappeared (in particular snaphots) and version problems.

The benchmark as of Sep 2023 contains 116 bugs (`jq length scripts/data/bug_id_and_branch.json`).

## Contact

Feel free to create issues in this repository with questions.
