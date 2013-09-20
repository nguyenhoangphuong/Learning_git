package com.misfit.ta.ios.tests;

import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.gui.InstrumentHelper;
import com.misfit.ta.utils.ShortcutsTyper;

public class BackgroundSyncDebug 
{

    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "QuietSync" })
    public void QuietSyncContinously()
    {
    	InstrumentHelper instrument = new InstrumentHelper();
    	
    	for(int i = 0; i < 200; i++) {
    		
	    	instrument.kill();
	        AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
	        		AppHelper.getAppPath(), 
	        		"script/quietsync.js");
	        
	        ShortcutsTyper.delayTime(1000 * 360);
    	}
    }
}
