#import "PlanPicker.js"

planBuilderData = 
{
	prepackage:
	{
		theStartersPlan: [{name: "Treadmill", value: "3.00 miles"}, {name: "Plank", value: "120 secs"}, {name: "Sit-up", value: "35 reps"}],
		
		theMoversPlan: [{name: "Running", value: "5.00 miles"}, {name: "Push-up", value: "90 reps"}, {name: "Swimming", value: "1500.00 ft"}],
		
		theShakersPlan: [{name: "Swimming", value: "1800.00 ft"}, {name: "Push-up", value: "245 reps"}, {name: "Treadmill", value: "8.00 miles"}],
	}
}
var pp = new PlanPicker();
pinfodefault =
{
	type: pp.easy,
	name: pp.easyStarter
}

pinfonormal =
{
	type: pp.normal,
	name: pp.normalMover
}

pinfoactive =
	{
		type: pp.active,
		name: pp.activeShaker
	}


