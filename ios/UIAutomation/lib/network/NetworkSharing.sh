#!/bin/bash
# Input argument
# $1 STATUS: ON/OFF/TOGGLE

STATUS=$1

InternetSharingIndex=10
WifiIndex=3


function toggleNetworkSharing {
/usr/bin/osascript << EOF
property bOff : true

-- passing  MyApplVar to MyShellVar
set InternetSharingIndex to do shell script "echo '$InternetSharingIndex'"
set WifiIndex to do shell script "echo '$WifiIndex'"


tell application "System Preferences"
    activate
end tell

tell application "System Events"
    tell process "System Preferences"
        click menu item "Sharing" of menu "View" of menu bar 1
        delay 1
        tell window "Sharing"
            click checkbox 1 of row InternetSharingIndex of table 1 of scroll area 1 of group 1
            delay 1
            click pop up button 1 of group 1
            click menu item "Ethernet" of menu 1 of pop up button 1 of group 1
            select row WifiIndex of table 1 of scroll area 2 of group 1
            -- check the checkbox value
            -- if (value of checkbox 1 of row WifiIndex of table 1 of scroll area 2 of group 1 = 0) then
                click checkbox 1 of row WifiIndex of table 1 of scroll area 2 of group 1
                click checkbox 1 of row InternetSharingIndex of table 1 of scroll area 1 of group 1
                delay 1
            -- end if
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

-- passing  MyApplVar to MyShellVar
--set InternetSharingIndex to do shell script "echo '$InternetSharingIndex'"
--set WifiIndex to do shell script "echo '$WifiIndex'"

property InternetSharingIndex : 10
property WifiIndex : 3

tell application "System Preferences"
    activate
end tell

tell application "System Events"
    tell process "System Preferences"
        click menu item "Sharing" of menu "View" of menu bar 1
        delay 1
        tell window "Sharing"
            select row InternetSharingIndex of table 1 of scroll area 1 of group 1
            delay 1
            if (value of static text 1 of group 1 is "internet Sharing: Off") then
                click checkbox 1 of row InternetSharingIndex of table 1 of scroll area 1 of group 1
                delay 1
                click pop up button 1 of group 1
                click menu item "Ethernet" of menu 1 of pop up button 1 of group 1
                delay 1
                
                select row WifiIndex of table 1 of scroll area 2 of group 1
                if (value of checkbox 1 of row WifiIndex of table 1 of scroll area 2 of group 1 = 0) then
                    click checkbox 1 of row WifiIndex of table 1 of scroll area 2 of group 1
                end if
                delay 1
                
                click checkbox 1 of row InternetSharingIndex of table 1 of scroll area 1 of group 1
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

-- passing  MyApplVar to MyShellVar
--set InternetSharingIndex to do shell script "echo '$InternetSharingIndex'"
--set WifiIndex to do shell script "echo '$WifiIndex'"

property InternetSharingIndex : 10
property WifiIndex : 3

tell application "System Preferences"
    activate
end tell

tell application "System Events"
    tell process "System Preferences"
        click menu item "Sharing" of menu "View" of menu bar 1
        delay 1
        tell window "Sharing"
            select row InternetSharingIndex of table 1 of scroll area 1 of group 1
            delay 1
            if (value of static text 1 of group 1 is "internet Sharing: On") then
                click checkbox 1 of row InternetSharingIndex of table 1 of scroll area 1 of group 1
                delay 1
                click pop up button 1 of group 1
                click menu item "Ethernet" of menu 1 of pop up button 1 of group 1
                delay 1
                
                select row WifiIndex of table 1 of scroll area 2 of group 1
                if (value of checkbox 1 of row WifiIndex of table 1 of scroll area 2 of group 1 = 1) then
                    click checkbox 1 of row WifiIndex of table 1 of scroll area 2 of group 1
                end if
                delay 1
                
                --click checkbox 1 of row InternetSharingIndex of table 1 of scroll area 1 of group 1
                --delay 1
                --if (exists sheet 1) then
                --    click button "Start" of sheet 1
                ---end if
            end if
        end tell
    end tell
end tell

ignoring application responses
    tell application "System Preferences" to quit
end ignoring
EOF
}

if [ $STATUS == "ON" ] ; then     
    turnOnNetworkSharing
fi
if [ $STATUS == "OFF" ] ; then      
    turnOffNetworkSharing
fi
if [ $STATUS == "TOGGLE" ] ; then      
    toggleNetworkSharing
fi
