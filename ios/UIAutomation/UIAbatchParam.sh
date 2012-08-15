#!/bin/bash

# Input argument
# $1 INSTRUMENT_TEMPLATE
# $2 APPLICATION
# $3 TC_DIR
# $4 TC_CONFIG
# $5 TC_REPORT
# $6 EMAIL
# $7 JOB_NAME
# $8 fruitstrapbin
# $9 DEVICE UDID
# $10 DEVICE_INFO
# $11 BUNDLE_NAME (use to uninstall app ex: com.misfitwearables.perry)

# Global variables
#INSTRUMENT_TEMPLATE="~/Library/Application\ Support/Instruments/Templates/AppleOne.tracetemplate"
#APPLICATION="/Users/trongvu/Library/Developer/Xcode/DerivedData/AppleOne-fhnuehkhlcoaevbuptkhlucfglyv/Build/Products/Debug-iphonesimulator/AppleOne.app"
#APPLICATION="/Users/trongvu/Downloads/AppleOne.app"
#TC_DIR="/Users/trongvu/Working/TCs"
#TC_LIST=( 'login.js' 'login-fail.js')
#TC_REPORT="/Users/trongvu/Working/Testing/031412"

INSTRUMENT_TEMPLATE=$1
APPLICATION=$2
TC_DIR=$3
TC_CONFIG=$4
TC_REPORT=$5
EMAIL=$6
JOB_NAME=$7
fruitstrapbin=$8
DEVICE_UDID=$9
DEVICE_INFO=${10}
BUNDLE_NAME=${11}

# Get list of device ID
argumentlist=$@

# ${!i} get subtitive of variables

# load TC_LIST variable in TC_CONFIG
source $TC_CONFIG

TIMESTAMP=`date +%s`
TC_SUITE=${TC_REPORT}/Test-suite-${TIMESTAMP}
SUITE_REPORT="$TC_SUITE/report.txt"

echo "INSTRUMENT_TEMPLATE $INSTRUMENT_TEMPLATE"
echo "APPLICATION $APPLICATION"
echo "TC_DIR $TC_DIR"
echo "TC_CONFIG $TC_CONFIG"
echo "TC_REPORT $TC_REPORT"
echo "DEVICE $DEVICE"

# Create test suite folder
mkdir -p "$TC_SUITE"

#build Application
function buildApp {
    echo "Building application"
    xcodebuild -project /Users/trongvu/Documents/Working/AppleOne/AppleOne.xcodeproj -scheme AppleOne -configuration Debug -sdk iphonesimulator5.0 TARGETED_DEVICE_FAMILY=1 clean build
}

#Install app to real device
function installApptoDevice {
    echo "Install application $DEVICE_UDID"
    if [[ "$fruitstrapbin" == *v2.0* ]]; then
        $fruitstrapbin install --id $DEVICE_UDID --bundle $APPLICATION
    else 
        # support compatible with old version fruitstrap 1.0
        $fruitstrapbin $APPLICATION $DEVICE_UDID
    fi
}

function uninstallApponDevice {
    echo "Uninstall application $DEVICE_UDID"
    if [[ "$fruitstrapbin" == *v2.0* ]]; then
        $fruitstrapbin uninstall --id $DEVICE_UDID --bundle $BUNDLE_NAME
        echo "ninstall application $DEVICE_UDID Done"
    fi
}

# Run test suite
function runTestSuite {
    echo "Start run test suite"

    # get number of elements in the array
    TOTALTC=${#TC_LIST[@]}
    PASSED=0
    FAILED=0
    TOTALTCEXE=0

    DetailReport="$TC_SUITE/.detailreport.txt"
    echo "Detail Report" > $DetailReport

    # echo each element in array 
    # for loop
    for (( i=0;i<$TOTALTC;i++)); do

        testcase=${TC_LIST[${i}]}

        # check install the app
        if [ $testcase == "installApptoDevice" ] ; then
            uninstallApponDevice
            installApptoDevice
            continue
        fi

        TOTALTCEXE=`expr $TOTALTCEXE + 1`
        
        # Create test case folder
        TC_LOC="${TC_SUITE}/TC-$testcase"
        mkdir -p "$TC_LOC" 

        # remove all instrument trace log
        rm -rf "instrumentscli*"
    
        # execute TC script using instrument cli
        if [ $DEVICE_UDID == "SIM" ] ; then
            eval instruments -t $INSTRUMENT_TEMPLATE $APPLICATION -e UIASCRIPT "$TC_DIR/$testcase" -e UIARESULTSPATH $TC_LOC
        else     
            eval instruments -w $DEVICE_UDID -t $INSTRUMENT_TEMPLATE $APPLICATION -e UIASCRIPT "$TC_DIR/$testcase" -e UIARESULTSPATH $TC_LOC
        fi
        
        # move instruments trace log to TC folder
        mv "instrumentscli0.trace" "$TC_LOC"
        
        # get result from plist file using plistBuddy
        TCresult=`/usr/libexec/PlistBuddy -c "Print 'All Samples:LogType'" ${TC_LOC}/Run\ 1/Automation\ Results.plist | /usr/bin/awk -F '=' '/LogType = /{print $2}'`
        
        
        #TC Report
        echo "  + Test case $testcase: $TCresult" >> $DetailReport
        echo "          - Test data: $TC_LOC" >> $DetailReport
        
        if [ $TCresult == "Pass" ] ; then
            PASSED=`expr $PASSED + 1`
        else     
            FAILED=`expr $FAILED + 1`
        fi
    done 

    # Create the summary report
    SummaryReport="$TC_SUITE/.summaryreport.txt"
    echo "Summary Report" > $SummaryReport
    echo "  + TOTAL : $TOTALTCEXE" >> $SummaryReport
    echo "  + PASS  : $PASSED" >> $SummaryReport
    echo "  + FAIL  : $FAILED" >> $SummaryReport
    echo "--------------------------------------------" >> $SummaryReport
    
    cat $SummaryReport > $SUITE_REPORT
    cat $DetailReport >> $SUITE_REPORT

}

# Script to send a email
function sendEmail {
    echo "sending report email"

	# The subject
	SUBJECT="[UIAutomation TEST REPORT] - JOB=$JOB_NAME - DEVICE=$DEVICE_INFO - UDID=$DEVICE_UDID"

	# Who to send the email to
	#EMAIL="trong@misfitwearables.com"

	# A file to hold the body of the message which
	#is later redirected into mail
	EMAILMESSAGE="/tmp/testemail.txt"

	#Echo the body of the message into the file. The first line is
	#added with > subsequent lines needs to be added with >>
    #echo "The server is down and needs restarting" > $EMAILMESSAGE

	#Send an email using /bin/mail
	mail -s "$SUBJECT" "$EMAIL" < $SUITE_REPORT
}

function toggleNetworkSharing {
/usr/bin/osascript << EOF
property bOff : true

tell application "System Preferences"
    activate
end tell

tell application "System Events"
    tell process "System Preferences"
        click menu item "Sharing" of menu "View" of menu bar 1
        delay 2
        tell window "Sharing"
            click checkbox 1 of row 11 of table 1 of scroll area 1 of group 1
            delay 1
            click pop up button 1 of group 1
            click menu item "Ethernet" of menu 1 of pop up button 1 of group 1
            click checkbox 1 of row 1 of table 1 of scroll area 2 of group 1
            click checkbox 1 of row 11 of table 1 of scroll area 1 of group 1
            delay 1
            if (exists sheet 1) then
                click button "Start" of sheet 1
            end if
        end tell
    end tell
end tell

ignoring application responses
    tell application "System Preferences" to quit
end ignoring
EOF
}

function turnOnNetworkSharing {
/usr/bin/osascript << EOF
property bOff : true

tell application "System Preferences"
    activate
end tell

tell application "System Events"
    tell process "System Preferences"
        click menu item "Sharing" of menu "View" of menu bar 1
        delay 2
        tell window "Sharing"
            if (value of static text 1 of group 1 is "internet Sharing: Off") then
                click checkbox 1 of row 11 of table 1 of scroll area 1 of group 1
                delay 1
                click pop up button 1 of group 1
                click menu item "Ethernet" of menu 1 of pop up button 1 of group 1
                click checkbox 1 of row 1 of table 1 of scroll area 2 of group 1
                click checkbox 1 of row 11 of table 1 of scroll area 1 of group 1
                delay 1
                if (exists sheet 1) then
                    click button "Start" of sheet 1
                end if
            end if
        end tell
    end tell
end tell

ignoring application responses
    tell application "System Preferences" to quit
end ignoring
EOF
}

function turnOffNetworkSharing {
/usr/bin/osascript << EOF
property bOff : true

tell application "System Preferences"
    activate
end tell

tell application "System Events"
    tell process "System Preferences"
        click menu item "Sharing" of menu "View" of menu bar 1
        delay 2
        tell window "Sharing"
            if (value of static text 1 of group 1 is "internet Sharing: On") then
                click checkbox 1 of row 11 of table 1 of scroll area 1 of group 1
                delay 1
                click pop up button 1 of group 1
                click menu item "Ethernet" of menu 1 of pop up button 1 of group 1
                click checkbox 1 of row 1 of table 1 of scroll area 2 of group 1
                click checkbox 1 of row 11 of table 1 of scroll area 1 of group 1
                delay 1
                if (exists sheet 1) then
                    click button "Start" of sheet 1
                end if
            end if
        end tell
    end tell
end tell

ignoring application responses
    tell application "System Preferences" to quit
end ignoring
EOF
}

#checkout
#buildApp
installApptoDevice
runTestSuite
sendEmail
