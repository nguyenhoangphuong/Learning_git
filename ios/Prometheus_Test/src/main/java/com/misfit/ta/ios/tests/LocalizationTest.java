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
	
	// this test will be run daily to make sure
	// our script is up to date
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "LocalizeDebug" })
    public void Localization()
    {
   		testLocalizationForLanguage("ru");
    }
	

	// these tests are excluded on jenkins by default
	// separate into multiple tests instead of using an array
	// so that we can run it from multi machines
	// and in case of failure, we need to run failed tests
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Localize", "LocalizeFR", "ProductionOnly" })
    public void LocalizationFR()
    {
   		testLocalizationForLanguage("fr");
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Localize", "LocalizeTH", "ProductionOnly" })
    public void LocalizationTH()
    {
   		testLocalizationForLanguage("th");
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Localize", "LocalizeKO", "ProductionOnly" })
    public void LocalizationKO()
    {
   		testLocalizationForLanguage("ko");
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Localize", "LocalizeZH_HANS", "ProductionOnly" })
    public void LocalizationZH_HANS()
    {
   		testLocalizationForLanguage("zh-hans");
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Localize", "LocalizeZH_HANT", "ProductionOnly" })
    public void LocalizationZH_HANT()
    {
   		testLocalizationForLanguage("zh-hant");
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Localize", "LocalizeJA", "ProductionOnly" })
    public void LocalizationJA()
    {
   		testLocalizationForLanguage("ja");
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Localize", "LocalizeRU", "ProductionOnly" })
    public void LocalizationRU()
    {
   		testLocalizationForLanguage("ru");
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Localize", "LocalizeMS", "ProductionOnly" })
    public void LocalizationMS()
    {
   		testLocalizationForLanguage("ms");
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Localize", "LocalizeAR", "ProductionOnly" })
    public void LocalizationAR()
    {
   		testLocalizationForLanguage("ar");
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Localize", "LocalizeES", "ProductionOnly" })
    public void LocalizationES()
    {
   		testLocalizationForLanguage("es");
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Localize", "LocalizeIT", "ProductionOnly" })
    public void LocalizationIT()
    {
   		testLocalizationForLanguage("it");
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Localize", "LocalizeTR", "ProductionOnly" })
    public void LocalizationTR()
    {
   		testLocalizationForLanguage("tr");
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Localize", "LocalizePT_PT", "ProductionOnly" })
    public void LocalizationPT_PT()
    {
   		testLocalizationForLanguage("pt-PT");
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Localize", "LocalizeDE", "ProductionOnly" })
    public void LocalizationDE()
    {
   		testLocalizationForLanguage("de");
    }
	
	
	// helpers
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
    	    	String result = Gui.launchInstrument(AppHelper.getCurrentUdid(),
    					AppHelper.getAppPath(), "script/localize/localize_test.js", outputDir);
    	    	
    	    	Gui.failOnError(result);
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
