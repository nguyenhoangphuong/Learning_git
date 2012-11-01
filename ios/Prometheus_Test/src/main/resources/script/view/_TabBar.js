#import "_Tips.js"

/*
This file provides methods to navigate to specify view.
This will go from nothing to the specify view, so kill the app first.

	+ tapButton: 	tap a button with a certain caption
	+ tapProgress: 	tap progress button
	+ tapPlanner: 	tap the planner button
	+ tapHistory: 	tap history button
	+ tapSettings: 	tap the Settings button
	+ tapTracker: 	tap the start tracker button
	+ isVisible: 	is the tabbar visible
*/

function TabBar()
{
	// =========================== Methods =====================
	this.tapButton = tapButton;
	this.tapProgress = tapProgress;
	this.tapPlanner = tapPlanner;
	this.tapHistory = tapHistory;
	this.tapSettings = tapSettings;
	this.tapTracker = tapTracker;
	this.isVisible = isVisible;
	
	function isVisible() {
		return app.tabBar().isVisible();
	}
	
	function tapButton(name) {
		log("Tap button name: " + name);
		app.tabBar().buttons()[name].tap();
		wait(0.5);
	}
	
	function tapProgress() {
		tapButton("Progress");
	}
	
	function tapPlanner() {
		tapButton("Planner");
	}
	
	function tapHistory() {
		tapButton("History");
	}
	
	function tapSettings() {
		tapButton("Settings");
	}
	
	function tapTracker() {
		log("Tap tracker");
		app.mainWindow().buttons()["tracker button"].tap();
		wait(0.5);
	}
}

tabBar = new TabBar();
