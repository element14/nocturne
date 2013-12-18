#!/bin/bash
#
#set -x

echo "-------------------"
echo "creating user guide"

CURR_DIR=`pwd`
echo "in directory ["$CURR_DIR"]"


echo "creating ebooks"
#publican build --formats epub,html,html-single,html-desktop,pdf,man --langs en-UK --config publican.cfg
publican build --formats epub,pdf --langs en-UK --config publican.cfg

cp ./tmp/en-UK/pdf/*.pdf ./
cp ./tmp/en-UK/*.epub ./

echo "removing temporary files"
rm -rf ./tmp

echo "Finished creating user guide"
echo "----------------------------"
