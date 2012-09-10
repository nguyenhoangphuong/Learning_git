/*
This file implement a Prometheus class which stand for the current app
The global var "prometheus" is declared

This is the current function:
- isActive()		:	check if the app is running or is in background

Will be implemented (if I can)
- bringToFront()	:	bring the app from background to running mode
*/

#import "../core/common.js"

function Prometheus()
{
	// Private fields
	this.AppID = "com.misfitwearables.Prometheus"; 
	
	// Methods
	this.isActive = isActive;
	
	// Method definitions
	function isActive()
	{
		current = target.frontMostApp().bundleID();
		log("current application:  222" + current);
		return current == this.AppID;
	}
}

prometheus = new Prometheus();