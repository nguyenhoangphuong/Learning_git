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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.misfit.ta.Settings;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.Logging;
import com.misfit.ta.utils.ScreenShooter;

public class AutomationTest extends com.misfit.ta.aut.AutomationTest {

	private static final Logger logger = Util.setupLogger(AutomationTest.class);
	private static final String target = Settings.getValue("target");

	@BeforeMethod(alwaysRun = true)
	public void setUpTest(Method method) {
		logger.info("*****************************************************");
		logger.info("***** Start of test case: " + method.getName());
		logger.info("*****************************************************");

		logger.debug("Start the TRS");

		// if (target.contains("Sim")) {
		// // Kill any running simulator client
		// Processes.killSimulator();
		//
		// // Clean screenshot queue
		// ScreenShooter.resetShotQueue();
		// logger.debug("Cleaning test output screenshots");
		// ScreenShooter.resetShotQueue();
		// }

	}

	@AfterMethod(alwaysRun = true)
	public void cleanUpTest(Method method, ITestResult tr) {
		// try {

		// String[] extensions = {"log", "1", "2", "3", "4", "5"};
		// Collection<File> logs = FileUtils.listFiles(new File("logs"),
		// extensions, false);
		// logger.debug("Found " + logs.size() + " log file(s)");
		// for (Iterator<File> iterator = logs.iterator(); iterator.hasNext();)
		// {
		// File file = iterator.next();
		// logger.debug("Log file: " + file.getAbsolutePath());
		// }
		// } catch (Exception e) {
		// Logging.logStackTraceToError(e);
		// } finally {
		// }

		String result;
		switch (tr.getStatus()) {
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
		logger.info("*****************************************************");
		logger.info("***** End of test case: " + method.getName());
		logger.info("***** Result: " + result);
		logger.info("*****************************************************");

	}

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() {
		// extracts scripts folder
		try {
			Files.extractJar("script", true);
			Files.extractJar("tools", true);
			} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
