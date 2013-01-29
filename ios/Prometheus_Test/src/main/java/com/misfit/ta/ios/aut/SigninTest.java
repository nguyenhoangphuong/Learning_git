package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;
import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;


public class SigninTest extends AutomationTest
{
	// sign in view: input, client validation, integration
	@Test(groups = { "ios", "Prometheus", "signin" })
	public void Register_Input() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/signin/signin-input-clientvalidation-integration.js");
	}
	
	// sign in view: server validation
	@Test(groups = { "ios", "Prometheus", "signin" })
	public void Register_Validation() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/signin/signin-servervalidation.js");
	}
	
	// forgot password view: input and validation
	@Test(groups = { "ios", "Prometheus", "signin" })
	public void Step1_Input() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/signin/forgotpassword-input-validation.js");
	}

}