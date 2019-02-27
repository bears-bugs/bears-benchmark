import os
import sys
import json
import argparse

from config import *

parser = argparse.ArgumentParser(description='Script to check out all bugs from Bears')
parser.add_argument('--workspace', help='The path to a folder to store the checked out bugs', required=True, metavar='')
args = parser.parse_args()

WORKSPACE = args.workspace

bugs = None
if os.path.exists(os.path.join(BEARS_PATH, BEARS_BUGS)):
    with open(os.path.join(BEARS_PATH, BEARS_BUGS), 'r') as f:
        try:
            bugs = json.load(f)
        except Exception as e:
            print("got %s on json.load()" % e)
            sys.exit()

if bugs is not None:
    for bug in bugs:
        os.system("python %s/scripts/checkout_bug.py --bugId %s --workspace %s" % (BEARS_PATH, bug['bugId'], WORKSPACE))
        