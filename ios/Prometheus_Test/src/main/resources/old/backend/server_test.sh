#!/bin/bash
# 
# server_test

POST_FILE="info.txt"
CONTENT_TYPE="application/x-www-form-urlencoded"
RESULT_FILE="abresult"
BASE_URL="https://staging-api.misfitwearables.com/shine/v2/"
TEMP_FILE="temp"

LOCAL="https://192.168.1.102:3000/shine/v2/"
STAGING="https://staging-api.misfitwearables.com/shine/v2/"
PRODUCTION="https://api.misfitwearables.com/shine/v2/"

SERVER="local"

# default action
TYPE="help"
NMIN=1000
NMAX=10000
NSTEP=1000
CMIN=50
CMAX=200
CSTEP=50


function create_result_file() {
	echo "concurency,requests,time (sec),non-2xx status,requests per sec,time per request (ms),time per request (ms) (across all concurrent requests),200 OK number" > $RESULT_FILE"_"$test_type".csv"
}

function get_values() {
	# get total time
	t=`cat $TEMP_FILE | grep "Time taken for tests:" | cut -d ' ' -f 7`
	
	# get failed requests
	f=`cat $TEMP_FILE | grep "Non-2xx responses:"`

	# get number of requests per second
	nps=`cat $TEMP_FILE | grep "Requests per second:" | cut -d ' ' -f 7`

	# get time per request
	tpr=`cat $TEMP_FILE | grep "Time per request:" | grep "(mean)" | cut -d ' ' -f 10`

	# get time per request (across all concurrent requests)
	tpra=`cat $TEMP_FILE | grep "Time per request:" | grep "(mean," | cut -d ' ' -f 10`

	# write status code to file
	cat $TEMP_FILE | grep "HTTP/1.1 200" >> status_code.txt
	counter=`wc -l "status_code.txt" | awk '{print $1'}`
	rm -f status_code.txt
}

function write_values() {
	echo $c,$n,$t,$f,$nps,$tpr,$tpra,$counter >> $RESULT_FILE"_"$test_type".csv"
}

function sign_up() {
	ab -n $n -c $c -v 4 -p $POST_FILE -T $CONTENT_TYPE -d $BASE_URL/signup
}

function sign_in() {
	ab -n $n -c $c -v 4 -p $POST_FILE -T $CONTENT_TYPE -d $BASE_URL/login
}

function sync() {
	ab -n $n -c $c -v 4 -p $POST_FILE -T $CONTENT_TYPE -d $BASE_URL/sync
}

function general_test() {	
	create_result_file $test_type

	# stress test
	for (( c=$CMIN; c<=CMAX; c+=CSTEP ))
	do
		echo "=================================================="
		echo "Concurency level:" $c

		for (( n=$NMIN; n<=NMAX; n+=NSTEP ))
		do
			echo "Number of requests:" $n

			if [ "$test_type" == "sign_up" ]
			then
				echo "test sign up"
				sign_up $c $n > $TEMP_FILE
			elif [ "$test_type" == "sign_in" ]
			then
				echo "test sign in"
				sign_in $c $n > $TEMP_FILE
			elif [ "$test_type" == "sync" ]
			then
				echo "test sync"
				sync $c $n > $TEMP_FILE
			fi

			get_values
			write_values $c $n $t $f $nps $tpr $tpra
		done

		sleep 5
	done

	# remove temporary file
	#rm -f $TEMP_FILE

	# change the result filename
	mv $RESULT_FILE"_"$test_type".csv" $SERVER"_"$test_type".csv"
}

function test_all() {
	echo "function has not been implemented"
}

function test_sign_up() {
	test_type="sign_up"
	general_test $test_type
}

function test_sign_in() {
	test_type="sign_in"
	general_test $test_type
}

function test_sync() {
	test_type="sync"
	general_test $test_type
}

function help() {
	echo "---------------------------------------------------------------------------"
	echo "EXAMPLE: "
	echo "./server_test.sh -t sign_in -u local -n 500 -N 500 -s 10 -c 20 -C 200 -S 20"
	echo "---------------------------------------------------------------------------"
	echo "ARGUMENTS: "
	echo " -t: api method to be run (sign_in / sign_up / sync)"
	echo " -u: server on which the test will be run (local / staging / production)"
	echo " -n: min number of requests"
	echo " -N: max number of requests"
	echo " -s: increament number of requests"
	echo " -c: min number of concurrent requests"
	echo " -C: max number of concurrent requests"
	echo " -S: increament number of concurrent requests"
	echo "---------------------------------------------------------------------------"
}

# run test
while getopts t:u:n:N:s:c:C:S: option
do
        case "${option}"
        in
                t) TYPE="${OPTARG}";;
				u) SERVER="${OPTARG}";;
                n) NMIN=${OPTARG};;
                N) NMAX=${OPTARG};;
                s) NSTEP=$OPTARG;;
				c) CMIN=${OPTARG};;
                C) CMAX=${OPTARG};;
                S) CSTEP=${OPTARG};;
        esac
done

if [ "$SERVER" == "local" ]
then
	BASE_URL="$LOCAL"
elif [ "$SERVER" == "staging" ]
then
	BASE_URL="$STAGING"
elif [ "$SERVER" == "production" ]
then
	BASE_URL="$PRODUCTION"
else
	BASE_URL="$SERVER"
fi

echo "$TYPE, $NMIN, $NMAX, $NSTEP, $CMIN, $CMAX, $CSTEP, $BASE_URL"

# run test
if [ "$TYPE" == "help" ]
then
	help
elif [ "$TYPE" == "all" ]
then
	test_all
elif [ "$TYPE" == "sign_up" ]
then
	test_sign_up
elif [ "$TYPE" == "sign_in" ]
then
	test_sign_in
elif [ "$TYPE" == "sync" ]
then
	test_sync
else
	help
fi