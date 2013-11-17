#!/bin/sh
#

# Absolute path to this script. /home/user/bin/foo.sh
SCRIPT=$(readlink -f "$0")

# Absolute path this script is in. /home/user/bin
SCRIPTPATH=`dirname "$SCRIPT"`

JARFILE="$SCRIPTPATH/violetumleditor-2.0.0.jar"

echo "Script=[$SCRIPT]"
echo "ScriptPath=[$SCRIPTPATH]"
echo "JARFILE=[$JARFILE]"

#java -jar com.horstmann.violet-0.21.1.jar
java -jar "$JARFILE"


