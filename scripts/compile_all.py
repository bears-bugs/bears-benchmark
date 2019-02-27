import os
import argparse

from config import *

parser = argparse.ArgumentParser(description='Script to compile bugs checked out from Bears')
parser.add_argument('--workspace', help='The path to the folder where the bugs are checked out', required=True, metavar='')
args = parser.parse_args()

WORKSPACE = args.workspace

BUG_FOLDERS = [d for d in os.listdir(WORKSPACE) if os.path.isdir(os.path.join(WORKSPACE, d))]

for BUG_FOLDER in BUG_FOLDERS:
    if BUG_FOLDER.startswith('Bears'):
        os.system("python %s/scripts/compile_bug.py --bugId %s --workspace %s" % (BEARS_PATH, BUG_FOLDER, WORKSPACE))
