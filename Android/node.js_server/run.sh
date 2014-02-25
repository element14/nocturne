#!/bin/bash
#

CURR_DIR=`pwd`
echo "in directory ["$CURR_DIR"]"

LOGNAME=$(logname)
echo "current login name is ["$LOGNAME"]"

nodejs index.js

