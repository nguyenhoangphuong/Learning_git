#!/bin/bash

# Input argument
# $1 STATUS: ON/OFF
# $2 PASSWORD
# $3 NETWORKTYPE: 3G Cable DSL Edge Wifi
# $4 STATE:  Average  Good Lossy
    # PROFILE
    # "3G, Average Case" "3G, Good Connectivity" "3G, Lossy Network" 
    # "Cable Modem" "DSL" 
    # "Edge, Average Case" "Edge, Good Connectivity" "Edge, Lossy Network" 
    # "Wifi, Average Case" "Wifi, Good Connectivity" "Wifi, Lossy Network" 

STATUS=$1
MYPASSWORD=$2
NETWORKTYPE=$3
STATE=$4

# Build PROFILE
PROFILE="DSL"
if [ $NETWORKTYPE == "3G" ] ; then
    if [ $STATE == "Average" ]; then
        PROFILE="$NETWORKTYPE, $STATE Case"
    fi
    if [ $STATE == "Good" ]; then
        PROFILE="$NETWORKTYPE, $STATE Connectivity"
    fi
    if [ $STATE == "Lossy" ]; then
        PROFILE="$NETWORKTYPE, $STATE Network"
    fi
fi
if [ $NETWORKTYPE == "Edge" ] ; then
    if [ $STATE == "Average" ]; then
        PROFILE="$NETWORKTYPE, $STATE Case"
    fi
    if [ $STATE == "Good" ]; then
        PROFILE="$NETWORKTYPE, $STATE Connectivity"
    fi
    if [ $STATE == "Lossy" ]; then
        PROFILE="$NETWORKTYPE, $STATE Network"
    fi
fi
if [ $NETWORKTYPE == "Wifi" ] ; then
    if [ $STATE == "Average" ]; then
        PROFILE="$NETWORKTYPE, $STATE Case"
    fi
    if [ $STATE == "Good" ]; then
        PROFILE="$NETWORKTYPE, $STATE Connectivity"
    fi
    if [ $STATE == "Lossy" ]; then
        PROFILE="$NETWORKTYPE, $STATE Network"
    fi
fi
if [ $NETWORKTYPE == "Cable" ] ; then     
    PROFILE="Cable Modem"
fi
if [ $NETWORKTYPE == "DSL" ] ; then      
    PROFILE="DSL"
fi


#Apple script
function NetworkThrottling {
/usr/bin/osascript << EOF

-- passing  MyApplVar to MyShellVar
set STATUS to do shell script "echo '$STATUS'"
set PROFILE to do shell script "echo '$PROFILE'"
set MYPASSWORD to do shell script "echo '$MYPASSWORD'"

tell application "System Preferences"
    activate
    reveal anchor "passwordPref" of pane id "com.apple.preferences.users"
end tell

tell application "System Events"
    tell process "System Preferences"
        click menu item "Network Link Conditioner" of menu "View" of menu bar 1
        delay 1
        tell window "Network Link Conditioner"
            if (exists checkbox "Click the lock to make changes.") then
                click checkbox "Click the lock to make changes."
                delay 1
            end if
        end tell
    end tell
    
    -- input password to unlock
    if (exists window 1 of process "SecurityAgent") then
        tell window 1 of process "SecurityAgent"
            --tell process "SecurityAgent"
            --keystroke myusername
            --keystroke tab
            --keystroke return
            --delay 0.5
            keystroke MYPASSWORD
            --delay 0.5
            click button "OK" of group 2
        end tell
    end if
    
    -- Enable Network
    tell process "System Preferences"
        tell window "Network Link Conditioner"
            -- ON
            click button STATUS
            
            -- Select profile
            click pop up button 1 of group 1
            click menu item PROFILE of menu 1 of pop up button 1 of group 1
            
            delay 1
        end tell
    end tell
end tell

ignoring application responses
    -- tell application "System Preferences" to quit
end ignoring
EOF
}

NetworkThrottling