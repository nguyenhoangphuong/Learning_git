/*
	NavigationBar class represents the bar at the top of the screen

	+ isVisible			: check if navigation bar is visible;
	+ xScreenIsVisible	: check if the screen having the name of x is visible;
	+ progressIsVisible	: check if Progress screen is visible;
	+ plannerIsVisible	: check if Planner screen is visible;
	+ historyIsVisible	: check if History screen is visible;
	+ settingsIsVisible	: check if Settings screen is visible;
*/

function NavigationBar()
{
	var progressName = "Progress";
	var plannerName = "Planner";
	var historyName = "History";
	var settingsName = "Settings";
	
	// =========================== Methods =====================
	this.isVisible = isVisible;
	this.xScreenIsVisible = xScreenIsVisible;
	this.progressIsVisible = progressIsVisible;
	this.plannerIsVisible = plannerIsVisible;
	this.historyIsVisible = historyIsVisible;
	this.settingsIsVisible = settingsIsVisible;
	
	function isVisible() {
		return navigationBar.isVisible();
	}
	
	function xScreenIsVisible(screenName) {
		return isVisible() &&
			UIATarget.localTarget().frontMostApp().navigationBar().name() == screenName;
	}
	
	function progressIsVisible() {
		return xScreenIsVisible(progressName);
	}
	
	function plannerIsVisible() {
		return xScreenIsVisible(plannerName);
	}
	
	function historyIsVisible() {
		return xScreenIsVisible(historyName);
	}
	
	function settingsIsVisible() {
		return xScreenIsVisible(settingsName);
	}
}

navigationBar = new NavigationBar();