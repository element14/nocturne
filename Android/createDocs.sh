#!/bin/bash
#
#set -x

echo "-------------------"
echo "creating user guide"

CURR_DIR=`pwd`
echo "in directory ["$CURR_DIR"]"

function convertFunction () {
    MARKDOWN_DOC=$1

    cd "$CURR_DIR/$MARKDOWN_DOC"
    echo "working in directory ["`pwd`"]"
    rm -rf tmp
    publican build --formats epub,html,pdf --langs en-UK --config publican.cfg
    
	cp ./tmp/en-UK/pdf/*.pdf "$CURR_DIR/"
	cp ./tmp/en-UK/*.epub "$CURR_DIR/"
    

    cd "$CURR_DIR"
}


convertFunction UserGuide

echo "Finished creating user guide"
echo "----------------------------"


