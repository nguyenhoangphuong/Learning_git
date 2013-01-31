import json
import os
from pprint import pprint
from datetime import datetime

with open('config.json') as data_file:    
	data = json.load(data_file)

date = str(datetime.now()).replace(" ", "_").replace(":", "-")[:19]
dest = data["destination"] + "/" + date + "_logs"
sources = data["sources"]

for source in sources:
	for log in source["logs"]:
		cmd1 = "mkdir -p %s/%s" % (dest, log["type"])
		cmd2 = "scp -r %s:%s %s" % (source["host"], log["path"], dest + "/" + log["type"])
		os.system(cmd1)
		os.system(cmd2)