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
- Commit #2 contains the changes in the tests
- Commit #3 contains the version of the program with the human-written patch
- Commit #4 contains the metadata file `bears.json`, which is a gathering of information collected during the bug reproduction process. It contains information about the bug (e.g. test failure names), the patch (e.g. patch size), and the bug reproduction process (e.g. duration).

## How to use

In the folder [scripts](scripts/) you can find scripts to check out and compile bugs.

You can check out all bugs at once with

```bash
python scripts/checkout_all.py --workspace <path to folder to store Bears bugs>
```

or you can check out a single bug by given a bug ID (see [bug IDs](https://bears-bugs.github.io/bears-benchmark)) as argument with

```bash
python scripts/checkout_bug.py --bugId <bug ID> --workspace <path to folder to store Bears bugs>
```

To compile checked out buggy program versions you can use the scripts `compile_all.py` and `compile_bug.py` in the same way as the scripts `checkout_all.py` and `checkout_bug.py`.

## How to contribute

Any contribution is very welcome! The types of contribution are, but not limited to:

- The proposal of bugs to be added in Bears (specific instructions comming soon)
- To flag possibly incorrect branches added in the Bears
- To participate in the manual validation of bugs/branches, i.e. review open pull requests

## Contact

Feel free to create issues in this repository with questions or to drop an email to fer.madeiral@gmail.com
