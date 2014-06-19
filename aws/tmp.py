
NAME = "1"

def print1():
    # name = NAME
    # NAME = "2"
    global NAME

    NAME = 2;
    # print name
    print NAME

def print2():
    print NAME

print1()

print2()
