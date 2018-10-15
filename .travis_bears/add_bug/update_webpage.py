import sys
import os
import subprocess
import json

BRANCH_NAME = sys.argv[1]
REPO_SLUG = sys.argv[2]

all_bears_bugs_json_file = os.path.join("docs", "data", "bears-bugs.json")

cmd = "git checkout %s;" % BRANCH_NAME
subprocess.call(cmd, shell=True)

cmd = "git rev-parse HEAD~2;"
buggy_commit = subprocess.check_output(cmd, shell=True).replace("\n", "")

cmd = "git rev-parse HEAD~1;"
fixed_commit = subprocess.check_output(cmd, shell=True).replace("\n", "")

cmd = "git diff %s %s -- '*.java';" % (buggy_commit, fixed_commit)
human_patch = subprocess.check_output(cmd, shell=True)

with open('bears.json') as original_json_file:
    bug = json.load(original_json_file)
    bug['repository']['name'] = bug['repository']['name'].replace("/","-")
    bug['branchUrl'] = "https://github.com/" + REPO_SLUG + "/tree/" + BRANCH_NAME
    bug['diff'] = human_patch

cmd = "git checkout -qf master;"
subprocess.call(cmd, shell=True)

bugs = None
if os.path.exists(all_bears_bugs_json_file):
    with open(all_bears_bugs_json_file,'r') as f:
        try:
            bugs = json.load(f)
        except Exception as e:
            print("got %s on json.load()" % e)

if bugs is not None:
    bugs.append(bug)

    with open(all_bears_bugs_json_file, 'w') as f:
        f.write(json.dumps(bugs, indent=2))

    cmd = "git add -A; git commit -m '(Automatic commit) Add %s'; git push github;" % BRANCH_NAME
    subprocess.call(cmd, shell=True)

cmd = "git checkout pr-add-bug;"
subprocess.call(cmd, shell=True)
