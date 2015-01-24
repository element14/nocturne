#!/bin/sh
#
# ---------------------------------------------------------------------
# Android Studio startup script.
# ---------------------------------------------------------------------
#
export GRADLE_HOME=~/gradle-2.2.1
export JAVA_HOME=/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt
export JAVA_HOME=/usr/lib/jvm/jdk-7-oracle-armhf

export PATH=$JAVA_HOME/bin:$GRADLE_HOME/bin:$PATH

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
cd $GitDir/TestServer
echo "changed to ["`pwd`"] directory"
ant



