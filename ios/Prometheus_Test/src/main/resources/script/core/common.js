/*
--------------------------------------------------------------------------------
FUNCTION LIST
This section is the place where you list functions. Please list them as comments
--------------------------------------------------------------------------------

*** TEST HELPERS ***
- logTree()		:	log element tree of current screen
- wait(s)		:	wait for s seconds (default = 1)
- start(text)	:	UIALogger.logStart(default = Test started)
- log(text)		:	UIALogger.logDebug(default = Debug log)
- pass(text)	:	UIALogger.logPass(default = Pass)
- fail(text)	:	UIALogger.logFail(default = Fail)
- hr()			:	this is the ULTIMATE function! it print -----------------------------------
					to the log debug. SO AWESOME!

- assertEqual(actual, expected, logs)	:	simplify if(a == b) pass() else fail()
- assertTrue(expression, logs)		:	simplify if(a == true) pass() else fail()
- assertFalse(expression, logs)		:	simplify if(a == false) pass() else fail()

*** TA HELPERS ***
- staticTextExist(text)			:	check if the text is valid and currently visible on screen
- buttonExist(id)				:	check if button with id is valid and currently visible on screen
- elementExist(id)				:	check if element with id is valid and currently visible on screen
- isElementLabelValid(id, label):	check if the element with id is valid and currently visible on screen
									and the text displaying is equal to label
									(*Note: currently this is only work SOMETIMES due to XCode and Instrucment stability)

- wheelPick(picker, index, value)	:	auto the picker progress (shouldn't use directly from test script)
- dateWheelPick(picker, year, monthString, day, yearIndex = 2, monthIndex = 0, dayIndex = 1)
	+ picker: the picker;
	+ year: year value (number);
	+ monthString: month value (string, case insensitive);
	+ day: day value (number);
	+ yearIndex: index of year wheel, default = 2;
	+ monthIndex: index of month wheel, default = 0;
	+ dayIndex: index of day wheel, default = 1;
- getWheelRange(picker, wheelindex)	:	get the min item and max item of the wheel
- isKeyboardVisible()			:	check if the system keyboard is currently shown on screen
- swipeVertically(startX, startY, endY, duration)		:	swipe along Y-axis (duration default = 0.6)
- swipeHorizontally(startX, startY, endX, duration)		:	swipe along X-axis (duration default = 0.6)
- tap(x, y)						:	tap on the screen at coord(x, y)
- lockApp(type, time)			:	cause interruption by pressing Home or Lock button
	+ lockApp("Home", 2)
	+ lockApp("Lock", 2)

- listAllStaticTexts(parent)	:	list all the static texts content (name) of current parent
- listAllButtons(parent)		:	list all the buttons content (name) of current parent
--------------------------------------------------------------------------------
IMPLEMENTATION
This section is the place where you implement functions declared above
--------------------------------------------------------------------------------
*/

var target = UIATarget.localTarget();
var app = target.frontMostApp();


// ============== Test helpers
function logTree() 
{
	target.logElementTree();
	wait(1);
}

function wait(s)
{
	if(typeof s == "undefined")
		s = 1;
	target.delay(s);
}

function start(text)
{
	if(typeof text == "undefined")
		text = "Test started";
	UIALogger.logStart("§ --------------------------- " + text + " ------------------------------- §");
}

function log(text)
{
	if (typeof text == "undefined")
		text = "Debug log";
    UIALogger.logDebug("•  " + text);
}

function print(text)
{
    UIALogger.logDebug(text);
}

function hr()
{
	UIALogger.logDebug("-------------------------------------------------------");
}

function pass(text)
{
	if (typeof text == "undefined")
		text = "Pass";
	UIALogger.logPass("TEST PASSED: " + text);
}

function fail(text)
{
	if(typeof text == "undefined")
		text = "Fail";
	UIALogger.logFail("TEST FAILED: " + text);
}

function assertEqual(actual, expected, logs)
{
	if(typeof logs == "undefined")
		logs = "Assert equal";
		
	if(actual == expected)
		log(">>> " + logs + " pass: " + "[" + actual.toString() + "] == [" + expected.toString() + "]");
	else
		fail("<<< " + logs + " fail: " + "[" + actual.toString() + "] >< [" + expected.toString() + "]");
}

function assertTrue(expression, logs)
{
	if(typeof logs == "undefined")
		logs = "Assert true";
		
	if(expression == true)
		log(">>> " + logs + ": pass");
	else
		fail(">>> " + logs + ": fail");
}

function assertFalse(expression, logs)
{
	if(typeof logs == "undefined")
		logs = "Assert false";
		
	if(expression == false)
		log(">>> " + logs + ": pass");
	else
		fail(">>> " + logs + ": fail");
}

// =============== TA helpers
function staticTextExist(text, win)
{
	if(typeof win == "undefined")
		win = app.mainWindow();
	
	return win.staticTexts()[text].isValid() && win.staticTexts()[text].isVisible();
}

function buttonExist(id, win) 
{
	if(typeof win == "undefined")
		win = app.mainWindow();
	
	return win.buttons()[id].isValid() && win.buttons()[id].isVisible();
}

function elementExist(id, win)
{
	if(typeof win == "undefined")
		win = app.mainWindow();
	
	return win.elements()[id].isValid() && win.elements()[id].isVisible();
}

function isElementLabelValid(id, label, win)
{
	if(typeof win == "undefined")
		win = app.mainWindow();
	
	ele = win.elements()[id];
	if(ele.isValid())
	{
		return ele.name() == label || ele.value() == label;
	}
	
	return false;
}

function wheelPick(picker, wheelIndex, value)
{
	wait();
	
	// wheel and items
	wheel = picker.wheels()[wheelIndex];
	items = wheel.values();

	// get current value
	current = wheel.value();
	if (current == null) {
		log("Current is null");
		wait(5);
		current = wheel.value();
	}
	current = current.substring(0, current.lastIndexOf("."));
	
	// find the index of current and expect value
	start = items.indexOf(current);	
	end = items.indexOf(value);
	steps = Math.abs(end - start);
	

	// invalid value
	if(end < 0)
		fail("Wheel has no such value [" + value + "]!");
	
	// scroll up
	if(start > end)
	{
		while(steps > 0)
		{
			wheel.tapWithOptions({tapOffset:{x:0.5, y:0.25}});
			target.delay(0.1);
			steps--;
		}
	}
	// scroll down
	else
	{
		while(steps > 0)
		{
			wheel.tapWithOptions({tapOffset:{x:0.5, y:0.75}});
			target.delay(0.1);
			steps--;
		}
	}  
}

function dateWheelPick(picker, year, monthString, day, yearIndex, monthIndex, dayIndex)
{
	// set default values for wheels of year, month and day
	if (yearIndex == null)
		yearIndex = 2;
	
	if (monthIndex == null)
		monthIndex = 0;
	
	if (dayIndex == null)
		dayIndex = 1;

	if (year < 1900 || 2100 < year ||
		day < 1 || 31 < day ||
		yearIndex < 0 || 2 < yearIndex ||
		monthIndex < 0 || 2 < monthIndex ||
		dayIndex < 0 || 2 < dayIndex)
		return null;
		
	var month = monthString.toLowerCase();
	var yearW = picker.wheels()[yearIndex];
	var monthW = picker.wheels()[monthIndex];
	var dayW = picker.wheels()[dayIndex];
	
	yearW.selectValue(year);
	
	while (month != monthW.value().toLowerCase()) {
		monthW.tapWithOptions({tapOffset:{x:0.5, y:0.25}});
	}
	
	while (day != dayW.value()) {
		dayW.tapWithOptions({tapOffset:{x:0.5, y:0.25}});
	}
}

function getWheelRange(picker, wheelIndex)
{
	wait(0.5);
	if(picker.isValid())
	{
		wheel = picker.wheels()[wheelIndex];
		items = wheel.values();

		// get current value
		info = {};
		info.min = items[0];
		info.max = items[items.length - 1];
			
		return info;
	}
	
	log("No picker visible");
	return null;
}

function isKeyboardVisible()
{
	return app.keyboard().isVisible();
}

function swipeVertically(startX, startY, endY, duration)
{
	if(typeof duration == "undefined")
		duration = 0.6;
		
	target.dragFromToForDuration({x:startX, y:startY}, {x:startX, y:endY}, duration);
}

function swipeHorizontally(startX, startY, endX, duration)
{
	if(typeof duration == "undefined")
		duration = 0.6;	

    target.dragFromToForDuration({x:startX, y:startY}, {x:endX, y:startY}, duration);
}

function tap(x, y)
{
	target.tap({x:x, y:y});
}

function lockApp(type, atime)
 {
    if (type == "Home") 
    {
        target.deactivateAppForDuration(atime);
        target.delay(2);
    } 
    else if (type == "Lock") 
    {
        target.lockForDuration(atime);
        target.delay(2);
    }
}

function generateSignupAccount()
{
	today = new Date();
	ms = today.getTime();
  
	return "test" + ms.toString() + "@test.com";
}

function generateRandomDigitString()
{
	today = new Date();
	ms = today.getTime();
	return ms.toString();
}

function listAllStaticTexts(p)
{
	texts = p.staticTexts();
	n = texts.length;
	
	for(i = 0; i < n; i++)
		print("StaticText " + i + ": " + texts[i].name() + " - " + texts[i].value());
}

function listAllButtons(p)
{
	btns = p.buttons();
	n = btns.length;
	
	for(i = 0; i < n; i++)
		print("Button " + i + ": " + btns[i].name() + " - " + btns[i].value());
}

function listControls(p)
{
	listAllStaticTexts(p);
	listAllButtons(p);
}