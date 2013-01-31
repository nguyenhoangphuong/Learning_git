import json
import os
import sys
import getopt
from pprint import pprint
from datetime import datetime

defaultFile = "config.json"
configFile = defaultFile
options = sys.argv
if len(options) >= 1:
	configFile = options[1]

print "Options: ", options
print "Getting the logs, config file= " + configFile 

with open(configFile) as data_file:    
	data = json.load(data_file)

date = str(datetime.now()).replace(" ", "_").replace(":", "-")[:19]
dest = data["destination"] + "/" + date + "_logs"
sources = data["sources"]

for source in sources:
	for log in source["logs"]:
		cmd1 = "mkdir -p %s/%s" % (dest, log["type"])
		print source["key"]
		
		cmd2 = "scp "
		if (source["key"] != ""):
			cmd2 = cmd2 + " -i %s " %(source["key"])

		cmd2 = cmd2 + "-r %s@%s:%s %s" % (source["user"], source["host"], log["path"], dest + "/" + log["type"])
		print cmd2
		os.system(cmd1)
		os.system(cmd2)