import sys
import argparse
import pymongo
from pymongo import MongoClient
import time
import random
import string


args = None
time_write = None
time_read = None
time_update = None
time_delete = None
db = None
common_data = "Alex"


def prepair_arguments():
	"parse command line arguments"

	parser = argparse.ArgumentParser()

	parser.add_argument("-s", "--server", help="mongodb server ip/address")
	parser.add_argument("-p", "--port", help="mongodb port number", type=int)
	parser.add_argument("-n", "--rec_num", help="how many records do ya want?",
		type=int)
	parser.add_argument("-d", "--duration", help="NOT IMPLEMENTED YET. How long\
		(in seconds) ya wanna loop? (will disable -l)", type=int)
	parser.add_argument("-r", "--random", help="random data or not?",
		action="store_true")

	args = parser.parse_args()

	return args


def enough_arguments():
	"check to see if arguments are enough to start testing"

	if not args:
		return False

	return args.server != None and\
		args.port != None and\
		args.rec_num != None


def prepair_db():
	"prepair objects for databse manipulation"
	
	global args

	if not args:
		args = prepair_arguments()

	if not enough_arguments():
		sys.exit("Error: not enough arguments to start. Please check: " +\
			"server, port, rec_num\nPlease use -h or --help for help")

	connection = MongoClient(args.server, args.port)
	db = connection.measurer

	return db


def create_record(id = 1, random_data = False, name = common_data):
	"create a record"

	if random_data is True:
		random_text = ''.join(random.choice(string.letters) for i in range(10))
		foo = {"id": id, "name": name, "random": random_text}
	else:
		foo = {"id": id, "name": name, "random": "identical"}

	return foo


def write_records(number_of_records, random = False, common_data = common_data):
	"write \"number_of_records records\", random data if \"random\" is True"

	global db

	if not db:
		db = prepair_db()

	print "Writing data ..."
	t1 = time.time()

	for id in range(0, number_of_records):
		foo = create_record(id, random, common_data) # this common_data is local
		db.data.save(foo)

	t2 = time.time()
	global time_write
	time_write = t2 - t1
	print "Wrote " + str(number_of_records) + " records\n"

	return


def read_records(common_data = common_data):
	"read all records having the same name field with value of \"common_data\""

	global db

	if not db:
		db = prepair_db()

	print "Reading data ..."
	query = {"name": common_data}
	t1 = time.time()
	cursor = db.data.find(query)
	t2 = time.time()
	global time_read
	time_read = t2 - t1
	print "Read " + str(cursor.count()) + " records\n"

	return cursor.count()


def update_records(new_data, old_data = common_data):
	"update all records having the same name field with value of \"old_data\""

	global db

	if not db:
		db = prepair_db()

	print "Updating data ..."
	where_query = {"name": old_data}
	set_query = {"name": new_data}
	t1 = time.time()
	db.data.update(where_query, {"$set": set_query}, multi=True)
	t2 = time.time()
	global time_update
	time_update = t2 - t1
	print "Updated\n"

	return


def delete_records(common_data = common_data):
	"delete all records having the same name field with\
	value of \"common_data\""

	global db

	if not db:
		db = prepair_db()

	print "Deleting data ..."
	query = {"name": common_data}
	t1 = time.time()
	db.data.remove(query)
	t2 = time.time()
	global time_delete
	time_delete = t2 - t1
	print "Deleted\n"

	# clean up
	db.data.drop()
	db.command({"dropDatabase" : 1})

	return


def write_times_to_file(filename = "result.log"):
	"write time of operations to file"

	f = open(filename, "a")
	f.write("number of records:" + "\t" + str(args.rec_num) + "\n")
	f.write("creating (second): " + "\t" + str(time_write) + "\n")
	f.write("reading (second): " + "\t" + str(time_read) + "\n")
	f.write("updating (second): " + "\t" + str(time_update) + "\n")
	f.write("deleting (second): " + "\t" + str(time_delete) + "\n")
	f.write("------------------------------\n")
	f.close()

	return


def main():
	global args
	args = prepair_arguments()

	global db
	db = prepair_db()

	write_records(args.rec_num, args.random)
	read_records()
	update_records("John")
	delete_records("John")

	write_times_to_file()

	return


main()
