package com.misfit.ta.ios.aut;


import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;
public class SignUpTest extends AutomationTest{
	
	@Test(groups = { "ios", "MVP", "MVP#1" })
	public void ValidEmailTest() throws InterruptedException,
			StopConditionException {
	//	AppHelper.cleanCache();
	//bn 	ShortcutsTyper.delayTime(1000);
		//AppHelper.install(AppHelper.getCurrentUdid(), AppHelper.getAppPath());
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/email/emailValidation.js");
	
	}
}
