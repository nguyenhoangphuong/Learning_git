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
public class C1157 extends AutomationTest {

  @Test(groups = {"ios", "Perry"})
  public void c1157() throws InterruptedException, StopConditionException {
//	  AppHelper.install(AppHelper.getCurrentUdid(), AppHelper.getAppPath());
//	  ShortcutsTyper.delayTime(3000);
	  AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
				 "script/C1157.js");
  }
  
  
}
