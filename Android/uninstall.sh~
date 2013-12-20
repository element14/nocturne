#!/bin/bash
#

#set -x

CURR_DIR=`pwd`
echo "in directory ["$CURR_DIR"]"

function convertFunction () {
    MARKDOWN_DOC=$1

    cd "$CURR_DIR/$MARKDOWN_DOC"
    echo "working in directory ["`pwd`"]"
    rm -rf tmp
    publican build --formats epub,html,pdf --langs en-US --config publican.cfg

    cd $CURR_DIR
}


convertFunction UserGuide


