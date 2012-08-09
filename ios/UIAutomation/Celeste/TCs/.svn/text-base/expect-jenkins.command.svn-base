#!/usr/bin/expect

set timeout 10

spawn sudo su jenkins -c "JENKINS_HOME=/Users/Shared/Jenkins/Home /Library/Application\\ Support/Jenkins/jenkins-runner.sh"

expect "Password:"

send "misfit\n"

set timeout 600

expect "up running"
