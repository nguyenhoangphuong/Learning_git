package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;
import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;


public class Progress extends AutomationTest
{
	// progress integration
	@Test(groups = { "ios", "Prometheus", "progress" })
	public void ProgressIntegration() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/progress/progress-integration.js");
	}
	
	// progress facebook integration
	@Test(groups = { "ios", "Prometheus", "progress" })
	public void ProgressFacebookIntegration() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/progress/progress-integration.js");
	}
	
}