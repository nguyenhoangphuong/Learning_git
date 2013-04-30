package com.misfit.ta.ios.tests;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.utils.ShortcutsTyper;

public class OldVersionTest 
{
	// sign in client and server validation
	@Test(groups = { "ios", "OldInstrument", "signin" })
	public void SignInValidation() throws InterruptedException, StopConditionException
	{
		AppHelper.cleanCache();
		ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/automation/smoketest.js");
	}
	
}
