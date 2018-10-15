import sys
import os
import subprocess
import json

BRANCH_NAME = sys.argv[1]

branches_per_version_file_path = os.path.join("releases", "branches_per_version.json")

# check out master
cmd = "git checkout -qf master;"
subprocess.call(cmd, shell=True)

# update the json file containing all branches per version
versions = None
if os.path.exists(branches_per_version_file_path):
    with open(branches_per_version_file_path,'r') as f:
        try:
            versions = json.load(f)
        except Exception as e:
            print("got %s on json.load()" % e)

if versions is not None:
    versions["latest"].append(BRANCH_NAME)

    with open(branches_per_version_file_path,'w') as f:
        f.write(json.dumps(versions, indent=2))

    cmd = "git add -A; git commit --amend --no-edit; git push -f github;"
    subprocess.call(cmd, shell=True)

cmd = "git checkout pr-add-bug;"
subprocess.call(cmd, shell=True)
