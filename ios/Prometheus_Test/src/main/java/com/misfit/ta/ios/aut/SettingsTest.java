package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;
import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;


public class SettingsTest extends AutomationTest
{
	
	// settings: navigation between views
	@Test(groups = { "ios", "Prometheus", "settings" })
	public void Settings_Navigation() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/settings/settings-navigation.js");
	}
	
	
	// settings: check profile is saved
	@Test(groups = { "ios", "Prometheus", "settings" })
	public void Settings_Profile() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/automation/settings/settings-profile.js");
	}

}