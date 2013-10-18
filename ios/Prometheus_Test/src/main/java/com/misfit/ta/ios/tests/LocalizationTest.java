package com.misfit.ta.ios.tests;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.Settings;
import com.misfit.ta.aut.AutomationTest;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.InstrumentHelper;
import com.misfit.ta.utils.ShortcutsTyper;

public class LocalizationTest extends AutomationTest 
{
	private static Logger logger = Util.setupLogger(LocalizationTest.class);
	
	private String[] languages = {
			"fr", "th", "ko", "zh-Hans", "zh-Hant", "ja", "du",
			"ru", "ms", "ar", "es", "it", "tr", "pt"
	};

    @Test
    public void Localization()
    {
    	for(int i = 0; i < languages.length; i++)
    		testLocalizationForLanguage(languages[i]);
    }
    
    public void testLocalizationForLanguage(String language) {
    	
		logger.info("Language to test: " + language);
		
		// create output folder
		final String outputDir = "localize_result/" + language;
		File folder = new File(outputDir);
		if(!folder.isDirectory()) {
			folder.mkdirs();
		}
		else {
			FileUtils.deleteQuietly(folder);
			folder.mkdirs();    			
		}   		
		
    	// launch instrument
    	InstrumentHelper instrument = new InstrumentHelper();
    	instrument.kill();
    	Gui.uninstall(Gui.getCurrentUdid());
    	Gui.install();
    	Thread thread = new Thread()
    	{
    	    public void run() {
    	    	Gui.launchInstrument(AppHelper.getCurrentUdid(),
    					AppHelper.getAppPath(), "script/localize/localize_test.js", outputDir);
    	    }
    	};
    	thread.start();
        
        // start locale thread
    	ShortcutsTyper.delayTime(10000);
    	Gui.init(Settings.getParameter("DeviceIP"), 30);
    	//Gui.startLocaleThread();
    	Gui.setLanguageWithLanguageCode(language);
    	
    	while(thread.getState() != Thread.State.TERMINATED) {
    		ShortcutsTyper.delayTime(1000);
    	}
    	
    	ShortcutsTyper.delayTime(2000);
    	
    }
}
