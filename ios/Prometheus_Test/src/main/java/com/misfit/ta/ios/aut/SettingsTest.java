package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

public class SettingsTest extends AutomationTest 
{
	@Test(groups = { "ios", "Prometheus", "MVP3", "settings" })
	public void VerifyNonTroublemakerTryOutFeedback() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/feedback/settings_TCs_feedback_non-troublemaker_tryout.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP3", "settings" })
	public void VerifyTroublemakerFeedback() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/feedback/settings_TCs_feedback_troublemaker.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP3", "settings" })
	public void VerifyOtherButtons() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/settings_TCs_1.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP3", "settings" })
	public void VerifyLikeButton() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/settings_TCs_2_Like.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP3", "settings" })
	public void VerifyWebsiteButton() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/settings_TCs_3_Web.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP3", "settings" })
	public void VerifyRateButton() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/settings_TCs_4_Rate.js");
	}
}