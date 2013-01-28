package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;
import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;


public class SignupTest extends AutomationTest
{
	// register view - input test
	@Test(groups = { "ios", "Prometheus", "signup" })
	public void Register_Input() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/signup/register-input.js");
	}
	
	// register view - validation for password and email test
	@Test(groups = { "ios", "Prometheus", "signup" })
	public void Register_Validation() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/signup/register-validation.js");
	}
	
	// step 1 view - input test
	@Test(groups = { "ios", "Prometheus", "signup" })
	public void Step1_Input() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/signup/step1-input.js");
	}

	// sign up all views (register + step 1 - 4) integration test
	@Test(groups = { "ios", "Prometheus", "signup" })
	public void Signup_Integration() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/signup/signup-integration.js");
	}

}