import sys
import os
import subprocess
import json

BRANCH_NAME = sys.argv[1]

branches_per_version_file_path = os.path.join("releases", "branches_per_version.json")

# check out master
cmd = "git checkout -qf master;"
subprocess.call(cmd, shell=True)

# read the json file containing all branches per version
versions = None
if os.path.exists(branches_per_version_file_path):
    with open(branches_per_version_file_path,'r') as f:
        try:
            versions = json.load(f)
        except Exception as e:
            print("got %s on json.load()" % e)

if versions is not None:
    NUMBER_OF_BRANCHES=0
    for version in versions:
        NUMBER_OF_BRANCHES=NUMBER_OF_BRANCHES+len(versions[version])

    # check out new created branch
    cmd = "git checkout -qf %s;" % BRANCH_NAME
    subprocess.call(cmd, shell=True)

    # read bears.json
    with open('bears.json', 'r') as f:
        data = json.load(f)

    # add into bears.json the property { "bugId": "Bears_X" }
    data['bugId'] = "Bears_" + str(NUMBER_OF_BRANCHES + 1)

    # add into bears.json the property { "version": "latest" }
    data['version'] = "latest"

    # write bears.json
    with open('bears.json', 'w') as f:
        f.write(json.dumps(data, indent=2))

    # update branch
    cmd = "git add bears.json; git commit --amend --no-edit; git push -f github;"
    subprocess.call(cmd, shell=True)
