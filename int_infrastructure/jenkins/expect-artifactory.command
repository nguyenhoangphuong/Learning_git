#!/usr/bin/expect

set timeout 10

spawn sudo su jenkins -c "source ~/.bashrc; sudo /Users/Jenkins/qa/artifactory-2.6.3/bin/artifactory.sh"

expect "Password:"

send "misfit\n"

set timeout -1

expect "wait for forever .."

