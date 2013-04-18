/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.misfit.ta.ios;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.misfit.ta.Settings;
import com.misfit.ta.gui.Gui;

public class AutomationTest extends com.misfit.ta.aut.AutomationTest
{

	private static final String[] results = {"PASSED", "FAILED", "UNKNOW"};
	private static final Logger logger = Util.setupLogger(AutomationTest.class);
	protected boolean debug = false;
	
	
	@BeforeMethod(alwaysRun = true)
	public void setUpTest(Method method) 
	{
		logger.info("==================================================================================================================");
		logger.info("|  Start of test case: " + method.getName());
		logger.info("==================================================================================================================");
		
		if(!debug) start();
	}
	
	@AfterMethod(alwaysRun = true)
	public void cleanUpTest(Method method, ITestResult tr)
	{
		logger.info("==================================================================================================================");
		logger.info("|  End of test case: " + method.getName());
		logger.info("|  Result: " + results[tr.getStatus()]);
		logger.info("==================================================================================================================\n\n");
	
		if(!debug) stop();
	}
	
	public void start()
	{
		Gui.cleanCache();
		Gui.start(Settings.getParameter("DeviceIP"));
	}
	
	public void stop()
	{
		Gui.shutdown();
	}

}
