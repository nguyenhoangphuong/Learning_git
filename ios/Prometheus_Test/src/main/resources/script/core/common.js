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

*** TA HELPERS ***
- staticTextExist(text)			:	check if the text is valid and currently visible on screen
- buttonExist(id)				:	check if button with id is valid and currently visible on screen
- elementExist(id)				:	check if element with id is valid and currently visible on screen
- isElementLabelValid(id, label):	check if the element with id is valid and currently visible on screen
									and the text displaying is equal to label
									(*Note: currently this is only work SOMETIMES due to XCode and Instrucment stability)

- wheelPick(wheel, index, value):	auto the picker progress (shouldn't use directly from test script)
- isKeyboardVisible()			:	check if the system keyboard is currently shown on screen
- swipeVertically(startX, startY, endY, duration)		:	swipe along Y-axis (duration default = 0.6)
- swipeHorizontally(startX, startY, endX, duration)		:	swipe along X-axis (duration default = 0.6)
- tap(x, y)						:	tap on the screen at coord(x, y)

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
	UIALogger.logStart(text);
}

function log(text)
{
	if (typeof text == "undefined")
		text = "Debug log";
    UIALogger.logDebug(text);
}

function pass(text)
{
	if (typeof text == "undefined")
		text = "Pass";
	UIALogger.logPass(text);
}

function fail(text)
{
	if(typeof text == "undefined")
		text = "Fail";
	UIALogger.logFail(text);
}

// =============== TA helpers
function staticTextExist(text)
{
	win = app.mainWindow();
	return win.staticTexts()[text].isValid() && win.staticTexts()[text].isVisible();
}

function buttonExist(id) 
{
	win = app.mainWindow();    
	return win.buttons()[id].isValid() && win.buttons()[id].isVisible();
}

function elementExist(id)
{
	win = app.mainWindow();
	return win.elements()[id].isValid() && win.elements()[id].isVisible();
}

function isElementLabelValid(id, label)
{
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
	wait(2);
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
	log("Current= " + current);
	current = current.substring(0, current.lastIndexOf("."));
	
	// find the index of current and expect value
	start = items.indexOf(current);	
	end = items.indexOf(value);
	steps = Math.abs(end - start);
	
	log("start: " + items[start] + " : " + start.toString());
	log("end: " + items[end] + " : " + end.toString());
	log("current: " + current + " - value: " + value);

	// invalid value
	if(end < 0)
		fail("Wheel has no such value [" + value + "]!");
	
	// scroll up
	if(start > end)
	{
		while(steps > 0)
		{
			wheel.tapWithOptions({tapOffset:{x:0.5, y:0.25}});
			target.delay(0.5);
			steps--;
		}
	}
	// scroll down
	else
	{
		while(steps > 0)
		{
			wheel.tapWithOptions({tapOffset:{x:0.5, y:0.75}});
			target.delay(0.5);
			steps--;
		}
	}  
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