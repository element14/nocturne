#!/bin/bash
#

CURR_DIR=`pwd`
echo "in directory ["$CURR_DIR"]"

LOGNAME=$(logname)
echo "current login name is ["$LOGNAME"]"

APT_CMD='sudo apt-get --yes'

$APT_CMD install npm nodejs-legacy

npm install sqlite3 #--save
npm install formidable
npm install mqtt
npm install commander


