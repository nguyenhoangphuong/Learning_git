package com.misfit.ta.ios.tests;

import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.Settings;
import com.misfit.ta.aut.AutomationTest;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.InstrumentHelper;
import com.misfit.ta.utils.ShortcutsTyper;

public class LocalizationTest extends AutomationTest 
{
	private String[] languages = {
			"fr", "th", "ko", "zh-Hans", "zh-Hant", "ja", "du",
			"ru", "ms", "ar", "es", "it", "tr", "pt"
	};

    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "Localization", "ProductionOnly" })
    public void Localization()
    {
    	for(int i = 0; i < languages.length; i++) {
    		
    		System.out.println("Language to test: " + languages[i]);
    		
	    	// launch instrument
	    	InstrumentHelper instrument = new InstrumentHelper();
	    	instrument.kill();
	    	Gui.uninstall(Gui.getCurrentUdid());
	    	Gui.install();
	    	Thread thread = new Thread()
	    	{
	    	    public void run() {
	    	    	
	    	    	AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
	    					AppHelper.getAppPath(), "script/localize/localize.js");
	    	    }
	    	};
	    	thread.start();
	        
	        // start locale thread
	    	ShortcutsTyper.delayTime(10000);
	    	Gui.init(Settings.getParameter("DeviceIP"), 30);
	    	Gui.startLocaleThread();
	    	Gui.setLanguageWithLanguageCode(languages[i]);
	    	
	    	while(thread.getState() != Thread.State.TERMINATED) {
	    		ShortcutsTyper.delayTime(1000);
	    	}
	    	ShortcutsTyper.delayTime(2000);
    	}
    }
}
