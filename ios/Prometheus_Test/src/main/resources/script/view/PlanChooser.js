#import "../core/testcaseBase.js"


function PlanChooser()
{
	// Private fields
	var mainWindow = app.mainWindow();
	var app = target.frontMostApp();
	
	var activeBtn = mainWindow.buttons()["active plan"];
	var normalBtn = mainWindow.buttons()["normal plan"];
	var easyBtn = mainWindow.buttons()["easy plan"];
	
	// Methods
	this.selectEasy = selectEasy;
	this.selectNormal = selectNormal;
	this.selectActive = selectActive;
	this.selectOther = selectOther;
	
	
	// Method definition
	
	function selectEasy() {
		mainWindow.buttons()["easy plan"].tap();
	}
	
	function selectNormal() {
		mainWindow.buttons()["normal plan"].tap();
	}
	
	function selectActive() {
		mainWindow.buttons()["active plan"].tap();
	}
	
	function selectOther() {
		mainWindow.buttons()["other"].tap();
		target.delay();
	}
	
	UIATarget.onAlert = function onAlert(alert){   
	   var name = alert.name();
	   UIALogger.logMessage("Alert "+name+" encountered");
	   app.alert().defaultButton().tap();
	   return true;
	}

}