source "config.sh"



TIMESTAMP=`date +%s`
FOLDER_RESULT="Result-$TIMESTAMP"
FILE_TEMP="$FOLDER_RESULT/temp.txt"
FILE_SUMMARY_SIGNUP="$FOLDER_RESULT/summarysignup.csv"
FILE_SUMMARY_SIGNIN="$FOLDER_RESULT/summarysignin.csv"


# Create result folder
mkdir -p "$FOLDER_RESULT"



function runsignup
{
    echo "============run sign up stress test==================="
    # Variables
    TOTAL_C=${#PARAMS_C[@]}
    TOTAL_N=${#PARAMS_N[@]}
    echo $TOTAL_C
    echo $TOTAL_N

    echo "Concurency,Total Requests,Total time (sec),failed requests,requests per sec,time per request (ms),time per request (ms) (across all concurrent requests)" > "$FILE_SUMMARY_SIGNUP"
    # Run with each concurrent
    for (( i=0;i<$TOTAL_C;i++)); do
        echo "Concurent=${PARAMS_C[$i]}"
        #Run with each n
        for (( j=0;j<$TOTAL_N;j++)); do
            echo "Requests = ${PARAMS_N[$j]}"
            # Run command --> file temp
            #Check variable
            PARAM_C=${PARAMS_C[${i}]}
            PARAM_N=${PARAMS_N[${j}]}

            #Execute script
            eval ab -n "$PARAM_N" -c "$PARAM_C" -p "$FILE_TEXT_SIGNUP" -T 'application/x-www-form-urlencoded' -d "$LINK_SIGNUP" > "$FILE_TEMP" 

            #sleep 10s
            echo "sleep 10s"
            sleep 10 

            # Grepfile : concurency,requests,time (sec),failed requests,requests per sec,time per request (ms),time per request (ms) (across all concurrent requests)
            # Grep -> Variable
            TIME_TAKEN=`eval cat "$FILE_TEMP" | grep "Time taken for tests:" | cut -d ' ' -f 7 `
            FAIL_REQUESTS=`eval cat "$FILE_TEMP" | grep "Failed requests:" | cut -d ' ' -f 10 `
            REQS_PER_SEC=`eval cat "$FILE_TEMP" | grep "Requests per second:" | cut -d ' ' -f 7 `
            TIME_PER_REQ=`eval cat "$FILE_TEMP" | grep "Time per request:" | grep "(mean)" | cut -d ' ' -f 10 `
            TIME_PER_REQ_CONCURRENT=`eval cat "$FILE_TEMP" | grep "Time per request:" | grep "(mean, across all concurrent requests)" | cut -d ' ' -f 10`

            #Write variables to file
            echo "${PARAMS_C[$i]},${PARAMS_N[$j]},$TIME_TAKEN,$FAIL_REQUESTS,$REQS_PER_SEC,$TIME_PER_REQ,$TIME_PER_REQ_CONCURRENT" >> "$FILE_SUMMARY_SIGNUP"

            # clear file temp
            rm -rf "$FILE_TEMP"
        done
    done 
}

function runlogin
{
    echo "============run log in stress test==================="
    # Variables
    TOTAL_C=${#PARAMS_C[@]}
    TOTAL_N=${#PARAMS_N[@]}
    echo $TOTAL_C
    echo $TOTAL_N

    echo "Concurency,Total Requests,Total time (sec),failed requests,requests per sec,time per request (ms),time per request (ms) (across all concurrent requests)" > "$FILE_SUMMARY_SIGNIN"
    # Run with each concurrent
    for (( i=0;i<$TOTAL_C;i++)); do
        echo "Concurent=${PARAMS_C[$i]}"
        #Run with each n
        for (( j=0;j<$TOTAL_N;j++)); do
            echo "Requests = ${PARAMS_N[$j]}"
            # Run command --> file temp
            #Check variable
            PARAM_C=${PARAMS_C[${i}]}
            PARAM_N=${PARAMS_N[${j}]}

            #Execute script
            eval ab -n "$PARAM_N" -c "$PARAM_C" -p "$FILE_TEXT_LOGIN" -T 'application/x-www-form-urlencoded' -d "$LINK_LOGIN" > "$FILE_TEMP" 

            #sleep 10s
            echo "sleep 10s"
            sleep 10 

            # Grepfile : concurency,requests,time (sec),failed requests,requests per sec,time per request (ms),time per request (ms) (across all concurrent requests)
            # Grep -> Variable
            TIME_TAKEN=`eval cat "$FILE_TEMP" | grep "Time taken for tests:" | cut -d ' ' -f 7 `
            FAIL_REQUESTS=`eval cat "$FILE_TEMP" | grep "Failed requests:" | cut -d ' ' -f 10 `
            REQS_PER_SEC=`eval cat "$FILE_TEMP" | grep "Requests per second:" | cut -d ' ' -f 7 `
            TIME_PER_REQ=`eval cat "$FILE_TEMP" | grep "Time per request:" | grep "(mean)" | cut -d ' ' -f 10 `
            TIME_PER_REQ_CONCURRENT=`eval cat "$FILE_TEMP" | grep "Time per request:" | grep "(mean, across all concurrent requests)" | cut -d ' ' -f 10`

            #Write variables to file
            echo "${PARAMS_C[$i]},${PARAMS_N[$j]},$TIME_TAKEN,$FAIL_REQUESTS,$REQS_PER_SEC,$TIME_PER_REQ,$TIME_PER_REQ_CONCURRENT" >> "$FILE_SUMMARY_SIGNIN"

            # clear file temp
            rm -rf "$FILE_TEMP"
        done
    done 
}
runsignup
runlogin
