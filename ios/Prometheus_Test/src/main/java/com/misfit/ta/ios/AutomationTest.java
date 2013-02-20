/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.misfit.ta.ios;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import EDU.oswego.cs.dl.util.concurrent.CountDown;

import com.misfit.ios.AppHelper;
import com.misfit.ta.Settings;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.Logging;
import com.misfit.ta.utils.ScreenShooter;

public class AutomationTest extends com.misfit.ta.aut.AutomationTest
{

	private static final Logger logger = Util.setupLogger(AutomationTest.class);
	private static final String target = Settings.getValue("target");
	
	private static final String detailReport = "test-output/DetailReport.txt";
	private static int passCount =0;
	private static int failCount = 0;
	private static int unknownCount = 0;

	
	private static StringBuffer details= new StringBuffer();
	
	@BeforeMethod(alwaysRun = true)
	public void setUpTest(Method method) 
	{
		logger.info("==================================================================================================================");
		logger.info("|  Start of test case: " + method.getName());
		logger.info("==================================================================================================================");

		logger.debug("Start the TRS");

		Files.delete("instrumentscli0.trace");
		try {
            Files.getFile(detailReport);
        } catch (FileNotFoundException e) {
            new File(detailReport);
        }
	}

	@AfterMethod(alwaysRun = true)
	public void cleanUpTest(Method method, ITestResult tr)
	{
		String result;
		switch (tr.getStatus()) 
		{
		case 1:
			result = "PASSED";
			break;
		case 2:
			result = "FAILED";
			break;
		default:
			result = "UNKNOWN";
			break;
		}
		
		logger.info("==================================================================================================================");
		logger.info("|  End of test case: " + method.getName());
		logger.info("|  Result: " + result);
		logger.info("==================================================================================================================");
		logger.info("\n\n");
		
		// report IOS test results
		
//		ProcessBuilder pb = new ProcessBuilder();
//		pb.command("/bin/sh", "-c","/usr/libexec/PlistBuddy -c \"Print 'All Samples:LogType'\" instrumentscli0.trace/Run 1/Automation Results.plist | /usr/bin/awk -F '=' '/LogType = /{print $2}'");
//		String results = AppHelper.runProcess(pb);
//		
////		details.append("Testcase:  " + method.getName() + "\tStatus: " + results + "\n");
//		if (results.equalsIgnoreCase("Passed")) {
//		    passCount++;
//		} else {
//		    failCount++;
//		}
		
	}

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() throws FileNotFoundException
	{
		// extracts scripts folder
		try {
			Files.extractJar("script", true);
			Files.extractJar("tools", true);
			} catch (Exception e) {
			e.printStackTrace();
		}
		Files.delete(detailReport);
	}	
	
	@AfterSuite(alwaysRun = true)
    public void afterSuite() throws FileNotFoundException 
    {
//	    Files.write(detailReport, "\n===================================" +
//	    		"\n" + "Summary report:\n");
//	    Files.write(detailReport, "TOTAL: " + (passCount + failCount + unknownCount));
//	    Files.write(detailReport, "PASSED: " + passCount);
//	    Files.write(detailReport, "FAILED: " + failCount);
//	    Files.write(detailReport, "UNKNOWN: " + unknownCount);
//	    Files.write(detailReport, "===================================\n");
//	    Files.write(detailReport, details.toString());
//	    passCount=0;
//	    failCount =0;
//	    unknownCount=0;
//	    details = new StringBuffer();
	}

}
