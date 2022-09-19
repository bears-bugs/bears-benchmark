import os
import sys
import subprocess
import json
import argparse

parser = argparse.ArgumentParser(description='Script to run tests related to one bug checked out from Bears')
parser.add_argument('--bugId', help='The ID of the bug', required=True, metavar='')
parser.add_argument('--workspace', help='The path to the folder where the bug is checked out', required=True, metavar='')
args = parser.parse_args()

BUG_ID = args.bugId
WORKSPACE = args.workspace

BUG_FOLDER_PATH = os.path.join(WORKSPACE, BUG_ID)
if not os.path.isdir(BUG_FOLDER_PATH):
    print("The bug %s has not been checked out." % BUG_ID)
    sys.exit()

print("Running the tests related to the bug %s..." % BUG_ID)

BUG_BEARS_JSON = None
if os.path.exists(os.path.join(BUG_FOLDER_PATH, 'bears.json')):
    with open(os.path.join(BUG_FOLDER_PATH, 'bears.json'), 'r') as f:
        try:
            BUG_BEARS_JSON = json.load(f)
        except Exception as e:
            print("got %s on json.load()" % e)
            sys.exit()

if BUG_BEARS_JSON is not None:
    POM_PATH = BUG_BEARS_JSON['reproductionBuggyBuild']['projectRootPomPath']
    BUGGY_BUILD_ID = BUG_BEARS_JSON['builds']['buggyBuild']['id']
    POM_PATH = POM_PATH.partition(str(BUGGY_BUILD_ID))[2]
    POM_PATH = POM_PATH.replace("/pom.xml", "")
    POM_PATH = POM_PATH.replace("/", "", 1)
    if POM_PATH:
        BUG_FOLDER_PATH = os.path.join(BUG_FOLDER_PATH, POM_PATH)

MAVEN_ARGS = "-V -B -Denforcer.skip=true -Dcheckstyle.skip=true -Dcobertura.skip=true -DskipITs=true -Drat.skip=true -Dlicense.skip=true -Dfindbugs.skip=true -Dgpg.skip=true -Dskip.npm=true -Dskip.gulp=true -Dskip.bower=true"

cmd = "cd %s; mvn test %s;" % (BUG_FOLDER_PATH, MAVEN_ARGS)
subprocess.call(cmd, shell=True)

print("The tests related to the bug %s were executed." % BUG_ID)
