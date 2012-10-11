//=================== Test data generator ===================== //
function toDayString(dateObj, today)
{
	if(typeof today == "undefined") today = false;
	
	d = dateObj.toString().split(" ");

	if(!today)
		s = d[1] + " " + d[2];
	else
		s = "Today " + d[1] + " " + d[2];
	
	/*
	t = parseInt(d[2], 10);
	if(t == 1 || t == 21 || t == 31) s = s + "st";
	else if(t == 2 || t == 22) s = s + "nd";
	else if(t == 3 || t == 23) s = s + "rd";
	else s = s + "th";
	*/
	return s;
}

function e51(goal)
{
    var today = new Date();
    var nextweek = new Date();
    nextweek.setDate(nextweek.getDate() + 6);
    
	var s1 = today.toString().split(" ");
	var s2 = nextweek.toString().split(" ");
	
	var e = {};
	e.duration = s1[1] + " " + s1[2] + " - " + s2[1] + " " + s2[2];
	e.goal = goal;
	
	return e;
}

function e52()
{
	d0 = new Date();
	d0 = toDayString(d0, true);
	d1 = new Date();
    d1.setDate(d1.getDate() + 1);
	d1 = toDayString(d1);
	d2 = new Date();
    d2.setDate(d2.getDate() + 2);
	d2 = toDayString(d2);    
	d3 = new Date();
    d3.setDate(d3.getDate() + 3);
	d3 = toDayString(d3);
	d4 = new Date();
    d4.setDate(d4.getDate() + 4);
	d4 = toDayString(d4);
	d5 = new Date();
    d5.setDate(d5.getDate() + 5);
	d5 = toDayString(d5);
	d6 = new Date();
    d6.setDate(d6.getDate() + 6);
    d6 = toDayString(d6);
    	
    var e = {};
    e.total = 7;
    e.passed = 1;
    e.days = [d0, d1, d2, d3, d4, d5, d6];
    
    return e;
}

function e53(index)
{
	// 0: sunday, 1: monday....
	milesVal = [1.24, 1.47, 1.49, 1.53, 1.45, 1.47, 1.35];
	
	// index = 0: today
	d = new Date();
    d.setDate(d.getDate() + index);
	s = toDayString(d, index === 0);
	
    t = new Date(d[1] + " " + d[2] + ", " + d[3]);
    
	var e = {};
	e.date = s;
	e.run = 0;
	e.total = milesVal[t.getDay()];
	
	return e;
}

//======================== Test data ======================== //
setupData = 
{
	activity: "Running",
	plan: 10
};

norm = 
{
	maxMPD : 3.25,
	e51 :	e51(setupData.plan),
	e52	:	e52(),
	e53	:
	{
		e531 :	{i: 0, e: e53(0)},
		e532 :	{i: 1, e: e53(1)},
		e533 :	{i: 4, e: e53(4)},
	},
};