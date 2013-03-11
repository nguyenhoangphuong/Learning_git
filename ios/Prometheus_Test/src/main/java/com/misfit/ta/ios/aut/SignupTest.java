package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;
import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;


public class SignupTest extends AutomationTest
{
	// sign up step 1
	@Test(groups = { "ios", "Prometheus", "signup" })
	public void SignUpStep1() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/signup/signup-step1.js");
	}
	
	// sign up step 2
	@Test(groups = { "ios", "Prometheus", "signup" })
	public void SignUpStep2() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/signup/signup-step2.js");
	}
	
	// sign up step 3
	@Test(groups = { "ios", "Prometheus", "signup" })
	public void SignUpStep3() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/signup/signup-step3.js");
	}
	
//	// sign up step 4
//	@Test(groups = { "ios", "Prometheus", "signup" })
//	public void SignUpStep4() throws InterruptedException, StopConditionException 
//	{
//		AppHelper.cleanCache();
//	 	ShortcutsTyper.delayTime(1000);	
//		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
//			AppHelper.getAppPath(), "script/automation/signup/signup-step4.js");
//	}
//
//	// sign up integration
//	@Test(groups = { "ios", "Prometheus", "signup" })
//	public void SignupIntegration() throws InterruptedException, StopConditionException 
//	{
//		AppHelper.cleanCache();
//	 	ShortcutsTyper.delayTime(1000);	
//		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
//			AppHelper.getAppPath(), "script/automation/signup/signup-integration.js");
//	}

}