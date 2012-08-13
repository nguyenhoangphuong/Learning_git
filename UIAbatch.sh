#!/bin/bash

# Global variables
INSTRUMENT_TEMPLATE="~/Library/Application\ Support/Instruments/Templates/AppleOne.tracetemplate"
#APPLICATION="/Users/trongvu/Library/Developer/Xcode/DerivedData/AppleOne-fhnuehkhlcoaevbuptkhlucfglyv/Build/Products/Debug-iphonesimulator/AppleOne.app"
APPLICATION="/Users/trongvu/Downloads/AppleOne.app"
TC_DIR="/Users/trongvu/Working/TCs"
TC_LIST=( 'login.js' 'login-fail.js')
TC_REPORT="/Users/trongvu/Working/Testing/031412"
TIMESTAMP=`date +%s`
TC_SUITE=${TC_REPORT}/Test-suite-${TIMESTAMP}
SUITE_REPORT="$TC_SUITE/report.txt"

# Create test suite folder
mkdir -p "$TC_SUITE"

# Run test suite
function runTestSuite {
    echo "Start run test suite"

    # get number of elements in the array
    TOTALTC=${#TC_LIST[@]}
    PASSED=0
    FAILED=0
    
    DetailReport="$TC_SUITE/.detailreport.txt"
    echo "Detail Report" > $DetailReport

    # echo each element in array 
    # for loop
    for (( i=0;i<$TOTALTC;i++)); do

        testcase=${TC_LIST[${i}]}

        # Create test case folder
        TC_LOC="${TC_SUITE}/TC-$testcase"
        mkdir -p "$TC_LOC" 

        # remove all instrument trace log
        rm -rf "instrumentscli*"
    
        # execute TC script using instrument cli
        eval instruments -t $INSTRUMENT_TEMPLATE $APPLICATION -e UIASCRIPT "$TC_DIR/$testcase" -e UIARESULTSPATH $TC_LOC
        
        # move instruments trace log to TC folder
        mv "instrumentscli0.trace" "$TC_LOC"
        
        # get result from plist file using plistBuddy
        TCresult=`/usr/libexec/PlistBuddy -c "Print 'All Samples:LogType'" ${TC_LOC}/Run\ 1/Automation\ Results.plist | /usr/bin/awk -F '=' '/LogType = /{print $2}'`
        
        
        #TC Report
        echo "  + Test case $testcase: $TCresult" >> $DetailReport
        echo "          - Test data: $TC_LOC" >> $DetailReport
        
        if [ $TCresult == "Pass" ] ; then
            let PASSED=PASSED+1
        else     
            let FAILED=FAILED+1
        fi
    done 

    # Create the summary report
    SummaryReport="$TC_SUITE/.summaryreport.txt"
    echo "Summary Report" > $SummaryReport
    echo "  + TOTAL : $TOTALTC" >> $SummaryReport
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
	SUBJECT="UIAutomation test report"

	# Who to send the email to
	EMAIL="trong@misfitwearables.com"

	# A file to hold the body of the message which
	#is later redirected into mail
	EMAILMESSAGE="/tmp/testemail.txt"

	#Echo the body of the message into the file. The first line is
	#added with > subsequent lines needs to be added with >>
    #echo "The server is down and needs restarting" > $EMAILMESSAGE

	#Send an email using /bin/mail
	mail -s "$SUBJECT" "$EMAIL" < $SUITE_REPORT
}

#build Application
function buildApp {
    echo "Building application"
    xcodebuild -project /Users/trongvu/Documents/Working/AppleOne/AppleOne.xcodeproj -scheme AppleOne -configuration Debug -sdk iphonesimulator5.0 TARGETED_DEVICE_FAMILY=1 clean build
}

#checkout
#buildApp
runTestSuite
sendEmail
