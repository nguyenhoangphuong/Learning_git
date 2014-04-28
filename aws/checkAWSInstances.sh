#!/bin/bash
# Input argument
# 
# iOS clean cache v2.7.3


PROJECT="DEV-PROMETHEUS-IOS"
BUILD_CONFIG="buildDebug"
APP_NAME="Prometheus.app"
APP_BUNDLE="com.misfitwearables.Prometheus"
APP_BUNDLE_RELEASE="com.misfitwearables.Prometheus.release.apn"
APP_BUNDLE_STAGING="com.misfitwearables.Prometheus.staging.apn"
APP_BUNDLE_DEBUG="com.misfitwearables.Prometheus"
HOST_NAME="jenkins.misfitwearables.com"
# there is a delay when doing DNS resolution with .local on mac so use IP instead
HOST_NAME_LOCAL="192.168.1.7:8080" 



ZIP_FILE_NAME="archive"


function setHost() {
	isLocal=`curl -s --connect-timeout 1 --head http://$HOST_NAME_LOCAL | head -n 1`
	if [ -z "$isLocal" ]
		then
		echo "Using internet URL"
	else
		HOST_NAME=$HOST_NAME_LOCAL
	fi
}

function cleanDebug() {
	./fruitstrap_v2.0 uninstall --bundle $APP_BUNDLE
}

function cleanStaging() {
	./fruitstrap_v2.0 uninstall --bundle $APP_BUNDLE_STAGING
}

function cleanRelease() {
	./fruitstrap_v2.0 uninstall --bundle $APP_BUNDLE_RELEASE
}

function clean() {
	echo "Cleaning caches..."

	echo $APP_BUNDLE
	./fruitstrap_v2.0 uninstall --bundle $APP_BUNDLE
}

function download() {
	rm -rf $ZIP_FILE_NAME*
	rm -rf build*
	setHost
	echo "Downloading the app..."
                  
	host="http://qa:misfitqc@$HOST_NAME/job/$PROJECT/lastSuccessfulBuild/artifact/$BUILD_CONFIG/*zip*/$BUILD_CONFIG.zip"
	#host="http://qa:misfitqc@jenkins-ci.misfitwearables.com/job/DEV-PROMETHEUS/4050/artifact/$BUILD_CONFIG/*zip*/$BUILD_CONFIG.zip"
	#host="http://qa:misfitqc@jenkins-ci.misfitwearables.com/job/DEV-PROMETHEUS/3564/artifact/$BUILD_CONFIG/*zip*/$BUILD_CONFIG.zip"

echo "HOST = $host"	
	curl $host > $APP_NAME.zip
	#curl 'http://qa:misfitqc@jenkins-ci.misfitwearables.com/view/Development/job/DEV-PROMETHEUS/lastSuccessfulBuild/artifact/$BUILD_CONFIG/*zip*/$BUILD_CONFIG.zip' > $ZIP_FILE_NAME.zip
	unzip -qq -o $APP_NAME.zip
	rm -rf $BUILD_CONFIG/$APP_NAME
	mv $BUILD_CONFIG/*.ipa $BUILD_CONFIG/tmp.zip
	unzip -qq -o $BUILD_CONFIG/tmp.zip -d $BUILD_CONFIG/
}	

function install() {
	echo $APP_NAME
	./fruitstrap_v2.0 install --bundle $BUILD_CONFIG/Payload/$APP_NAME
}

function help() {
	echo "Arguments: [all|install|install_only|clean|help] [release|debug]"
	echo "	all			:download new build, clean caches and install"
	echo "	all_no_clean			:download new build and install, no cleaning"
	echo "	install*		:clean and install, this is default"
	echo "	install_only		:only install"
	echo "	clean			:only clean caches"
	echo "	download			:download the client"
	echo "	help 			:show help"
	echo ""
	echo "	release 		:release build"
	echo "	debug* 			:debug/automation build"
}





if [ "$2" = "release" ]
	then
	APP_NAME="Prometheus.app"
	APP_BUNDLE=$APP_BUNDLE_RELEASE
	BUILD_CONFIG="buildRelease"

elif [ "$2" = "debug" ] 
then
	APP_NAME="Prometheus.app"
	APP_BUNDLE=$APP_BUNDLE_DEBUG
	BUILD_CONFIG="buildDebug"

elif [ "$2" = "staging" ] 
then
	APP_NAME="Prometheus.app"
	APP_BUNDLE=$APP_BUNDLE_STAGING
	BUILD_CONFIG="buildStaging"

fi

if [ "$1" = "help" ]
	then
  help
elif [ "$1" = "all" ]
then
  download
  clean
  sleep 4
  install

elif [ "$1" = "all_no_clean" ]
then
  download
  sleep 4
  install
elif [ "$1" = "install" ] || [ "$1" == "" ]
then
  clean
  install
elif [ "$1" = "install_only" ]
then
  install
elif [ "$1" = "clean" ]
then
  clean
elif [ "$1" = "download" ]
then
  download
elif [ "$1" = "debug" ]
then
  setHost

else
  echo ""
fi




