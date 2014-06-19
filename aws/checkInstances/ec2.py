import boto.ec2

ec2conn = boto.ec2.connect_to_region("us-east-1", aws_access_key_id='AKIAJP3YYUEL7NN46C5Q', aws_secret_access_key='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')
		



# print ec2conn.get_all_instances()

reservations = ec2conn.get_all_instances()
instances = [i for r in reservations for i in r.instances]
for i in instances:
	print(i.__dict__)



