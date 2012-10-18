#import "_Tips.js"
#import "../core/testcaseBase.js"

/*
This file provides methods to navigate to specify view.
This will go from nothing to the specify view, so kill the app first.

	+ tapButton : 	tap a button with a certain caption
	+ tapProgress: 	tap progress button
	+ tapPlanner: 	tap the planner button
	+ tapHistory: 	tap history button
	+ tapSettings: 	tap the Settings button
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
	this.isVisible = isVisible;
	
	function isVisible() {
		return app.tabBar().isVisible();
	}
	
	function tapButton(name) {
		log("Button name: " + name);
		app.tabBar().buttons()[name].tap();
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
}
tabBar = new TabBar();
