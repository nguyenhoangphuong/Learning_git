package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;
import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;


public class SettingsTest extends AutomationTest
{
	// setting profile
	@Test(groups = { "ios", "Prometheus", "settings" })
	public void SettingsProfile() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/settings/settings-profile.js");
	}
	
	// setting wearing shine
	@Test(groups = { "ios", "Prometheus", "settings" })
	public void SettingsWearingShine() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/settings/settings-wearingshine.js");
	}

}