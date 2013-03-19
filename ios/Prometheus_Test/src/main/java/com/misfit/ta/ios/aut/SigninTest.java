package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;
import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;


public class SigninTest extends AutomationTest
{
	// sign in client and server validation
	@Test(groups = { "ios", "Prometheus", "signin" })
	public void SignInValidation() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/signup/signin-validation.js");
	}

	// sign in - forgot password validation
	@Test(groups = { "ios", "Prometheus", "signin", "forgotpassword" })
	public void ForgotPasswordValidation() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/signup/forgotpassword-validation.js");
	}
	
}