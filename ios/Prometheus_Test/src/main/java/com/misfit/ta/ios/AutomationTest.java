/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.misfit.ta.ios;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.misfit.ios.AppHelper;
import com.misfit.ta.Settings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ShortcutsTyper;

public class AutomationTest extends com.misfit.ta.aut.AutomationTest {

    private static final String[] results = { "PASSED", "FAILED", "UNKNOW" };
    private static final Logger logger = Util.setupLogger(AutomationTest.class);
    protected static boolean debug = false;
    private Thread instrument = new Thread() 
	{
		public void run() 
		{
			AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
    				AppHelper.getAppPath(), "script/automation/alertsupport.js");
		}
	};

    static
    {
        String isDebug = Settings.getParameter("MVPDebug");
        if (isDebug != null && isDebug.equalsIgnoreCase("true")) 
        {
            debug = true;
        }
        else 
        {
            debug = false;
        }
    }

    @Override
    @BeforeMethod(alwaysRun = true)
    public void setUpTest(Method method) 
    {
        logger.info("==================================================================================================================");
        logger.info("|  Start of test case: " + method.getName());
        logger.info("==================================================================================================================");

        Files.delete("instrumentscli0.trace");

        if (!debug)
        {
    		AppHelper.cleanCache();
    		ShortcutsTyper.delayTime(1000);
        }
        
        instrument.start();
		ShortcutsTyper.delayTime(20000);
    	Gui.init(Settings.getParameter("DeviceIP"));
    }

    @Override
    @AfterMethod(alwaysRun = true)
    public void cleanUpTest(Method method, ITestResult tr)
    {
    	Gui.shutdown();
    	    	
        logger.info("==================================================================================================================");
        logger.info("|  End of test case: " + method.getName());
        logger.info("|  Result: " + results[tr.getStatus() - 1]);
        logger.info("==================================================================================================================\n\n");
    }

}
