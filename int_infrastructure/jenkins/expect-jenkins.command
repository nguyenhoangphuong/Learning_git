#!/usr/bin/expect

set timeout 10

spawn sudo su Jenkins -c "source ~/.bashrc; JENKINS_HOME=/Users/Shared/Jenkins/Home bash /Library/Application\\ Support/Jenkins/jenkins-runner.sh"

expect "Password:"

send "misfit\n"

set timeout -1

expect "wait for forever .."
