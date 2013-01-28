

function fillRegisterForm(name, email, password, sex, birthYear, birthDate, birthMonth, unit, w1, w2, h1, h2)
{
	log("Input name");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Name"].textFields()[0].tap();
	target.frontMostApp().keyboard().typeString(name);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();

	log("Input Email");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Email"].textFields()[0].tap();
	target.frontMostApp().keyboard().typeString(email);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();

	log("Input Password");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Password"].tap();
	target.frontMostApp().keyboard().typeString(password);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();

	log("Input Birthday");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[4].tap();
	var picker = target.frontMostApp().windows()[1].pickers()[0];
	dateWheelPick(picker, year, month, day);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();

	log("Input Gender");
	if(sex == "female" || sex == "FEMALE")
		target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Sex"].buttons()["FEMALE"].tap();
	else 
		target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Sex"].buttons()["MALE"].tap();

	log("Input Unit");
	if(unit == "us" || unit == "US")
		target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Units"].buttons()["US"].tap();
	else
		target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Units"].buttons()["METRIC"].tap();

	log("Input Height");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[6].tap();
	picker = target.frontMostApp().windows()[1].pickers()[0];
	wheelPick(picker, 0, h1);
	wheelPick(picker, 1, h2);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();

	log("Input Weight");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[7].tap();
	picker = target.frontMostApp().windows()[1].pickers()[0];
	wheelPick(picker, 0, w1);
	wheelPick(picker, 1, w2);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
}

function fillStep1Form(name, sex, birthYear, birthDate, birthMonth, unit, w1, w2, h1, h2)
{
	log("Input name");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Name"].textFields()[0].tap();
	target.frontMostApp().keyboard().typeString(name);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();

	log("Input Birthday");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[2].tap();
	var picker = target.frontMostApp().windows()[1].pickers()[0];
	dateWheelPick(picker, year, month, day);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();

	log("Input Gender");
	if(sex == "female" || sex == "FEMALE")
		target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Sex"].buttons()["FEMALE"].tap();
	else 
		target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Sex"].buttons()["MALE"].tap();

	log("Input Unit");
	if(unit == "us" || unit == "US")
		target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Units"].buttons()["US"].tap();
	else
		target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Units"].buttons()["METRIC"].tap();

	log("Input Height");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[4].tap();
	picker = target.frontMostApp().windows()[1].pickers()[0];
	wheelPick(picker, 0, h1);
	wheelPick(picker, 1, h2);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();

	log("Input Weight");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[5].tap();
	picker = target.frontMostApp().windows()[1].pickers()[0];
	wheelPick(picker, 0, w1);
	wheelPick(picker, 1, w2);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
}

