#import "../libs/core/common.js"

var alert = {};

// helpers
UIATarget.onAlert = function(_alert) {
	
	if(alert.choice !== undefined && alert.choice !== null) {
		
		if(alert.choice < 0)
			_alert.cancelButton().tap();
		else
			_alert.buttons()[alert.choice].tap();
	}
	else
		_alert.defaultButton().tap();
	
	return true;
}

// ------------------ sign in view
wait(20);
target.frontMostApp().mainWindow().buttons()[2].tap();
wait(2);
target.frontMostApp().mainWindow().buttons()[4].tap();
wait(2);

// invalid email alert
target.frontMostApp().mainWindow().buttons()[1].tap();
wait(2);

// invalid password alert
target.frontMostApp().mainWindow().textFields()[0].setValue("qa001@a.a");
target.frontMostApp().mainWindow().buttons()[1].tap();
wait(2);

// invalid credential alert
alert.choice = -1;
target.frontMostApp().mainWindow().secureTextFields()[0].setValue("asdfas");
target.frontMostApp().mainWindow().buttons()[1].tap();
wait(5);
alert.choice = null;

// ------------------ forgot password view
target.frontMostApp().mainWindow().buttons()[2].tap();
wait(2);

// invalid password alert
target.frontMostApp().mainWindow().textFields()[0].setValue("");
target.frontMostApp().mainWindow().buttons()[1].tap();
wait(2);

// non-existed email
target.frontMostApp().mainWindow().textFields()[0].setValue("valid1325@a.a");
target.frontMostApp().mainWindow().buttons()[1].tap();
wait(5);

// reset email sent
target.frontMostApp().mainWindow().textFields()[0].setValue("qa001@a.a");
target.frontMostApp().mainWindow().buttons()[1].tap();
wait(5);

// ------------------ sign up account view
target.frontMostApp().mainWindow().buttons()[0].tap();
wait(2);
target.frontMostApp().mainWindow().buttons()[2].tap();
wait(2);

// invalid email
target.frontMostApp().mainWindow().textFields()[0].setValue("invalid");
target.frontMostApp().mainWindow().buttons()[1].tap();
wait(2);

// invalid password
target.frontMostApp().mainWindow().textFields()[0].setValue("valid@email.com");
target.frontMostApp().mainWindow().buttons()[1].tap();
wait(2);

// duplicated email
target.frontMostApp().mainWindow().secureTextFields()[0].setValue("33wfdas");
target.frontMostApp().mainWindow().buttons()[1].tap();
wait(5);

// ------------------ sign up profile view
target.frontMostApp().mainWindow().textFields()[0].setValue(generateSignupAccount());
target.frontMostApp().mainWindow().secureTextFields()[0].setValue("qqqqqq");
target.frontMostApp().mainWindow().buttons()[1].tap();
wait(5); 
 
// sign out so soon alert
alert.choice = -1;
target.frontMostApp().mainWindow().buttons()[2].tap();
wait(2);
alert.choice = null;

// forget something to tell alert
target.frontMostApp().mainWindow().buttons()[1].tap();

// ------------------ sign up goal view
 
target.frontMostApp().mainWindow().buttons()[3].tap();
target.frontMostApp().mainWindow().staticTexts()[1].tap();
target.frontMostApp().windows()[1].toolbar().buttons()[0].tap();
target.frontMostApp().mainWindow().staticTexts()[3].tap();
target.frontMostApp().windows()[1].toolbar().buttons()[0].tap();
target.frontMostApp().mainWindow().staticTexts()[5].tap();
target.frontMostApp().windows()[1].toolbar().buttons()[0].tap();
target.frontMostApp().mainWindow().buttons()[1].tap();
wait(2);

// points tutorial
target.frontMostApp().mainWindow().buttons()[2].tap();
wait(2);
target.frontMostApp().mainWindow().buttons()[3].tap();
wait(2);

// activity levels
target.frontMostApp().mainWindow().images()["active.png"].tap();
wait();
target.frontMostApp().mainWindow().images()["highlyactive.png"].tap();
wait();

// ------------------ sign up link shine view
target.frontMostApp().mainWindow().buttons()[1].tap();
target.dragFromToForDuration({x: 30, y: 400}, {x: 260, y: 400}, 0.5);
wait(5);

// ------------------ app tutorial
swipeHorizontally(300, 20, 350, 0.5);
wait(2);
swipeHorizontally(300, 20, 350, 0.5);
wait(2);
swipeHorizontally(300, 20, 350, 0.5);
wait(2);
target.frontMostApp().mainWindow().scrollViews()[0].buttons()[0].tap();
wait(2);
























































