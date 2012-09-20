package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

public class LastFAIL extends AutomationTest {
	  
	// C2193	Verify "Rate our App" button's action 
	@Test(groups = { "ios", "Prometheus", "MVP1", "fail" })
	public void VerifyRateButton() throws InterruptedException, StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/testcase_01.js");
	}
	
	// C2195	Verify "Like our App" button's action 
	@Test(groups = { "ios", "Prometheus", "MVP1", "fail" })
	public void VerifyLikeButton() throws InterruptedException, StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/testcase_04.js");
	}
	
	// C2196	Verify "Reset Plan" button's action 
	@Test(groups = { "ios", "Prometheus", "MVP1", "fail" })
	public void VerifyResetButton() throws InterruptedException, StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/testcase_05.js");
	}
	
	  @Test(groups = {"ios", "Prometheus", "MVP1", "fail"})
	  public void GoalPlanTest() throws InterruptedException, StopConditionException {
		  AppHelper.cleanCache();
		  ShortcutsTyper.delayTime(1000);
		  AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/goalplan/goalplan_tcs.js");
	  }
	  
		@Test(groups = { "ios", "Prometheus", "MVP1", "fail" })
		public void runAllUserInfoTCs() throws InterruptedException, StopConditionException {
			AppHelper.cleanCache();
		 	ShortcutsTyper.delayTime(1000);
			AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
						 "script/testcases/userinfo/userinfo_testcase.js");
		}
}