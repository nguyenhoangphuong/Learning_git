#import "../MVPLibs.js"

/*
List of functions:
================================================================================
- isVisible()
- isSkipButtonVisible()
- isDoneButtonVisible()
================================================================================
- tapButton()
- next()
- previous()
================================================================================
*/

function WhatsNew()
{
	// Private fields
	var window;
	var mainView;
	var scrollView;
	
	var button;
	
	// Initalize
	assignControls();
	
	// Public methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	
	this.isSkipButtonVisible = isSkipButtonVisible;
	this.isDoneButtonVisible = isDoneButtonVisible;
	
	this.tapButton = tapButton;
	this.next = next;
	this.previous = previous;
	
	// Method definition
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window;
		scrollView = mainView.scrollViews()[0];
		
		button = mainView.buttons()[0];
	}
	
	function isVisible()
	{
		exist = button.isVisible() && (button.name() == "Skip" || button.name() == "Done");
		
		log("What's New visible: " + exist);
		return exist;
	}
	
	
	function isSkipButtonVisible()
	{	
		exist = button.isVisible() && button.name() == "Skip";
		log("[Skip] visible: " + exist);
		return exist;
	}
	
	function isDoneButtonVisible()
	{
		exist = button.isVisible() && button.name() == "Done";
		log("[Done] visible: " + exist);
		return exist;
	}
	
	
	function tapButton()
	{
		wait(0.5);
		button.tap();
		
		log("Tap [" + button.name() + "]");
	}
	
	function next()
	{
		wait(0.5);
		scrollView.scrollRight();
		log("Scroll to next news");
	}
	
	function previous()
	{
		wait(0.5);
		scrollView.scrollLeft();
		log("Scroll to previous news");
	}
}
