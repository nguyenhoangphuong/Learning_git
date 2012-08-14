/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

/**
 * 
 * @author qa
 */
public class SampleTest extends AutomationTest {

  @Test(groups = {"ios", "Perry", "Perry_1"})
  public void sampleTest() throws InterruptedException, StopConditionException {
      System.out.println("LOG [SampleTest.sampleTest]:  ---------- 1");
	  AppHelper.install(AppHelper.getCurrentUdid(), AppHelper.getAppPath());
	  ShortcutsTyper.delayTime(3000);
	  System.out.println("LOG [SampleTest.sampleTest]:  ---------- 2");
	  AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
				 "script/test.js");
//	  Logger logger = Util.setupLogger(SampleTest.class);
//	  logger.info("This is a test message");
  }
  
  
}
