/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.misfit.ta.android;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.misfit.ta.utils.ScreenShooter;

public class AutomationTest extends com.misfit.ta.aut.AutomationTest {

  private static final Logger logger = Util.setupLogger(AutomationTest.class);

  private boolean flightModeOn = false;

  private static boolean deviceIDAdded = false;

  public void setOfflineChanged(boolean changed) {
    flightModeOn = changed;
  }

  @AfterMethod(alwaysRun = true)
  public void cleanUpTest(Method method, ITestResult tr) {
    if (flightModeOn) {
      // offline has been on, turn it off
      Gui.toggleFlightMode(null);
      flightModeOn = false;
    }
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

  @BeforeMethod(alwaysRun = true)
  public void setUpTest(Method method) {

    logger.info("*****************************************************");
    logger.info("***** Start of test case: " + method.getName());
    logger.info("*****************************************************");

    Gui.init();
    // Gui.pressHome();
    ScreenShooter.resetShotQueue();
    Gui.cleanCache();
    Gui.start("com.misfitwearables.prometheus/.MainActivity");
    
    System.out.print(Gui.getScreenWidth());
    System.out.print(Gui.getScreenHeight());
  }



}
