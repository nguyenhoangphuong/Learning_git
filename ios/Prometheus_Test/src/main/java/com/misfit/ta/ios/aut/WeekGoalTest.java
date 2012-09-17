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
public class WeekGoalTest extends AutomationTest {

  @Test(groups = {"ios", "Prometheus", "weekgoal"})
  public void WeekGoalTest() throws InterruptedException, StopConditionException {
	  AppHelper.cleanCache();
	  ShortcutsTyper.delayTime(1000);
	  AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
				 "script/testcases/weekgoal/weekgoal.js");
  }
 
  
  
}
