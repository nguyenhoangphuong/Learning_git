import boto.ec2

global conn
MAX_INSTANCES = 70

def connect():

	global conn 
	conn = boto.ec2.connect_to_region("us-east-1", aws_access_key_id='AKIAJP3YYUEL7NN46C5Q', aws_secret_access_key='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')
	connected = 1


def getNumberOfRunningInstances():

	reservations = conn.get_all_reservations()
	return len(reservations)


# start from here

connect()

number = getNumberOfRunningInstances()
print 'Current number of instances: %d' % number

if number >= MAX_INSTANCES:
	print 'Fk, No good'
	exit(1)
else:
	print 'Still okay'

print 'Dummy'