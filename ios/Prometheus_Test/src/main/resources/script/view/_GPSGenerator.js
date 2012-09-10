#import "SignUp.js"
#import "UserInfo.js"
#import "PlanChooser.js"
#import "GoalProgress.js"
#import "GoalStart.js"
#import "GoalPlan.js"
#import "About.js"
#import "../core/testcaseBase.js"



function GPSGenerator()
{
	// =========================== Methods =====================
	this.generateInMiles=generateInMiles
	
	// ====================== Method definition ================

	function generateInMiles(numberOfTips) {
		 var target = UIATarget.localTarget();  
		 // speed is in meters/sec  
		 
		 
		 //106.661865, 10.763231
		 var points = [  
		                 {location:{latitude:106.6618,longitude:14.2}, options:{speed:8, altitude:200, horizontalAccuracy:10, verticalAccuracy:15}},  
		                 {location:{latitude:48.8899,longitude:14.9}, options:{speed:11, altitude:200, horizontalAccuracy:10, verticalAccuracy:15}},  
		                 {location:{latitude:48.8899,longitude:14.6}, options:{speed:12, altitude:200, horizontalAccuracy:10, verticalAccuracy:15}},  
		                 {location:{latitude:48.8899,longitude:14.7}, options:{speed:13, altitude:200, horizontalAccuracy:10, verticalAccuracy:15}},  
		                 {location:{latitude:49.2,longitude:14.10}, options:{speed:15, altitude:200, horizontalAccuracy:10, verticalAccuracy:15}},  
		                 {location:{latitude:49.4,longitude:14.8}, options:{speed:15, altitude:200, horizontalAccuracy:10, verticalAccuracy:15}},  
		                 {location:{latitude:48.8899,longitude:14.9}, options:{speed:9, altitude:200, horizontalAccuracy:10, verticalAccuracy:15}},  
		                 {location:{latitude:48.8899,longitude:15.1}, options:{speed:8, altitude:200, horizontalAccuracy:10, verticalAccuracy:15}},  
		                 {location:{latitude:48.8899,longitude:16.1}, options:{speed:3, altitude:200, horizontalAccuracy:10, verticalAccuracy:15}},  
		                 ];  
		 
		 var latitude = 106.6618;
		 var longitude = 10.763231;
		 var location = "{location:{latitude:106.6618,longitude:10.7632}, options:{speed:8, altitude:200, horizontalAccuracy:10, verticalAccuracy:15}}";
		 for (var i = 0; i < points.length; i++)  
		 {  
//		      target.setLocationWithOptions(points[i].location,points[i].options);
		      target.setLocationWithOptions("{location:{latitude:106.6618,longitude:10.7632}", "options:{speed:8, altitude:200, horizontalAccuracy:10, verticalAccuracy:15}}");
//		      target.captureScreenWithName(i+"_.png");  
		      target.delay(1.0);  
		 } 
	}
	
	
	
}

tips = new Tips();
