#!/bin/bash
#

#set -x

CURR_DIR=`pwd`
echo "in directory ["$CURR_DIR"]"

adb uninstall com.projectnocturne
adb uninstall com.bime.nocturne

