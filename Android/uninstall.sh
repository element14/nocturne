#!/bin/bash
#
#set -x

CURR_DIR=`pwd`
echo
echo -e '\E[35m'"\033[1m ** in directory ["$CURR_DIR"] ** \033[0m"

echo -e '\E[35m'"\033[1m ** adb uninstall com.projectnocturne ** \033[0m"
adb uninstall com.projectnocturne

echo -e '\E[35m'"\033[1m ** adb uninstall com.bime.nocturne ** \033[0m"
adb uninstall com.bime.nocturne

echo
echo

