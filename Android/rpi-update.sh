#!/bin/sh
#
# ---------------------------------------------------------------------
# Android Studio startup script.
# ---------------------------------------------------------------------
#
ScriptName=`basename $0`
TestServerUrl=https://github.com/element14/nocturne.git
GitDir=~/nocturne

echo
echo "# executing script ["$ScriptName"]"
echo "# path to me --------------->  ${0}     "
echo "# parent path -------------->  ${0%/*}  "
echo "# my name ------------------>  ${0##*/} "

if [ -d "$GitDir" ]; then
	echo
	echo "updating exising repo"
	echo
	cd $GitDir
	git pull
else
	echo
	echo "checking out new repo"
	echo
	git clone $TestServerUrl ~/nocturne
fi

echo
echo "Building TestServer classes"
echo
cd $GitDir/Android
echo "changed to ["`pwd`"] directory"
cp -F $GitDir/Android/rpi-update.sh ${0}
chmod +x ${0}
./gradlew :TestServer:compileJava

echo
echo "finished updating TestServer"
echo "to execute use the command [gradlew :TestServer:run]"
echo
echo "Executing TestServer"
echo
./gradlew :TestServer:run


