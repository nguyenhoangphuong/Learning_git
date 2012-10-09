package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

public class SettingsTest extends AutomationTest 
{
	@Test(groups = { "ios", "Prometheus", "MVP1", "setting" })
	public void VerifyNonTroublemakerFeedback() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/feedback/settings_TCs_feedback_non-troublemaker.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "setting" })
	public void VerifyTroublemakerFeedback() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/feedback/settings_TCs_feedback_troublemaker.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "setting" })
	public void VerifyTryoutFeedback() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/feedback/settings_TCs_feedback_tryout.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "setting" })
	public void VerifyOtherButtons() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/settings_TCs_1.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "setting" })
	public void VerifyLikeButton() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/settings_TCs_2_Like.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "setting" })
	public void VerifyRateButton() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/settings_TCs_3_Rate.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "setting" })
	public void VerifyResetButton() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/settings_TCs_4_Web.js");
	}
}
