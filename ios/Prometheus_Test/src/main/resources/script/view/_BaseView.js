function _BaseView
{
	// Target
	this.Target = target;
	this.App = target.frontMostApp();
	
	// Common actions
	this.Wait = Wait;
	this.TypeText = TypeText;
	this.Flick = Flick;
	this.FlickLeft = FlickLeft;
	this.FlickRight = FlickRight;
	this.SwipeLeft = FlickLeft;
	this.SwipeRight = FlickRight;

	// Common action definition
	function Wait(sec)
	{
		if (sec === undefined)
			Target.delay(1);
		else
			Target.delay(sec);
	}
	
	function TypeText(var textfield, String text)
	{
		textfield.tap();
		Wait(0.5);
		App.keyboard().typeString(text);
	}
	
	function Flick(xf, yf, xt, yt) 
	{
		Wait();
	    Target.dragFromToForDuration({x:xf, y:yf}, {x:xt, y:yt}, 0.6);
		Wait();
	}

	function FlickLeft()
	{
		Wait();
		if (deviceType == "IPOD")
		{
	    	Target.dragFromToForDuration({x:240, y:416}, {x:16, y:402}, 0.6);
    	} 
    	else if (deviceType == "IPAD") 
    	{
        	Target.dragFromToForDuration({x:440, y:416}, {x:16, y:402}, 0.6);
    	}
		Wait();
	}
	
	function FlickRight() 
	{
		Wait();
		if (deviceType == "IPOD") 
		{
        	Target.dragFromToForDuration({x:16, y:402}, {x:240, y:416}, 0.6);
    	} 
    	else if (deviceType == "IPAD") 
    	{
        	// IPAD
        	Target.dragFromToForDuration({x:16, y:402}, {x:440, y:416}, 0.6);
    	}
		Wait();
	}
}