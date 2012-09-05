package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

public class UserInfoValidator extends AutomationTest {
	  
	@Test(groups = { "ios", "Prometheus", "MVP1", "userinfo" })
	public void runAllUserInfoTCs() throws InterruptedException, StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/userinfo/userinfo_testcase.js");
	}
}
