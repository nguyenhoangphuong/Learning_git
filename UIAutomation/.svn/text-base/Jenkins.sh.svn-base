# setting up parameters
#BUILD_NUMBER=11
#JOB_NAME=AppleOne
PROJECTNAME=Celeste
INSTRUMENT_TEMPLATE="/Users/jenkins/qa/Projects/${PROJECTNAME}/Instruments/Templates/Celeste.tracetemplate"
#APPLICATION="/Users/Shared/Jenkins/Home/jobs/${JOB_NAME}/builds/${BUILD_NUMBER}/archive/${PROJECTNAME}/build/Release-iphonesimulator/${PROJECTNAME}.app"
APPLICATION="/Users/Shared/Jenkins/Home/jobs/QA_Celeste_iOS_RealDevice/workspace/Celeste/build/Debug-iphoneos/WLSA.app"
TC_DIR="/Users/jenkins/qa/Projects/${PROJECTNAME}/TCs"
TC_CONFIG="/Users/jenkins/qa/Projects/${PROJECTNAME}/config.sh"
TC_REPORT="/Users/jenkins/qa/Projects/${PROJECTNAME}/TestResult/"
EMAIL="trong@misfitwearables.com"
fruitstrap="/Users/jenkins/qa/fruitstrap-demo/bin/fruitstrap"
#DEVICE="bf5550be399a34bc69b15a7aa97e79f0b1992e6d"
echo "INSTRUMENT_TEMPLATE $INSTRUMENT_TEMPLATE"
echo "APPLICATION $APPLICATION"

#set DEVICE_UDID_LIST ('bf5550be399a34bc69b15a7aa97e79f0b1992e6d' 'fd0c1314f043c0744f274be07950a49fdef5d6b4' '61f1935464e8c10c0b252cc94031350248db960b')

DEVICE_UDID_LIST=( 'bf5550be399a34bc69b15a7aa97e79f0b1992e6d' 'fd0c1314f043c0744f274be07950a49fdef5d6b4' )

TOTAL_DEVICE=${#DEVICE_UDID_LIST[@]}
for (( i=0;i<$TOTAL_DEVICE;i++)); do

    DEVICE_UDID=${DEVICE_UDID_LIST[${i}]}

    #Install app to real device
    #/Users/jenkins/qa/fruitstrap-demo/bin/fruitstrap   /Users/Shared/Jenkins/Home/jobs/QA_Celeste_iOS_RealDevice/workspace/Celeste/build/Debug-iphoneos/WLSA.app

    cd /Users/jenkins/qa/Projects

    #./UIAbatchParam.sh $INSTRUMENT_TEMPLATE $APPLICATION $TC_DIR $TC_CONFIG $TC_REPORT $EMAIL $fruitstrap $DEVICE_UDID

    # execute the batch
    screen -S qa${i} -X stuff "./UIAbatchParam.sh $INSTRUMENT_TEMPLATE $APPLICATION $TC_DIR $TC_CONFIG $TC_REPORT $EMAIL $fruitstrap $DEVICE_UDID"

    # send enter
    screen -S qa -X  eval "stuff \015"
done