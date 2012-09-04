#import "../core/testcaseBase.js"


function PlanChooser()
{
	// Private fields
//	var app = target.frontMostApp();
	var mainWindow = app.mainWindow();
	
	
	var activeBtn = mainWindow.buttons()["active plan"];
	var normalBtn = mainWindow.buttons()["normal plan"];
	var easyBtn = mainWindow.buttons()["easy plan"];
	var otherBtn = mainWindow.buttons()["other"];
	
	// Methods
	this.selectEasy = selectEasy;
	this.selectNormal = selectNormal;
	this.selectActive = selectActive;
	this.selectOther = selectOther;
	
	
	// Method definition
	
	function selectEasy() {
		easyBtn.tap();
	}
	
	function selectNormal() {
		normalBtn.tap();
	}
	
	function selectActive() {
		activeBtn.tap();
	}
	
	function selectOther() {
		otherBtn.tap();
		target.delay();
	}
	
	UIATarget.onAlert = function onAlert(alert){   
	   var name = alert.name();
	   UIALogger.logMessage("Alert "+name+" encountered");
	   app.alert().defaultButton().tap();
	   return true;
	}

}