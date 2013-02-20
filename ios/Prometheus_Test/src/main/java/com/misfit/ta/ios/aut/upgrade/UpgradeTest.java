package com.misfit.ta.ios.aut.upgrade;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;
import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

public class UpgradeTest extends AutomationTest
{
	private static final Logger logger = Util.setupLogger(UpgradeTest.class);
	
	@Test(groups = { "ios", "Prometheus", "MVP5", "MVP7", "upgrade" })
	public void Upgrade_MVP5_MVP7() throws InterruptedException, StopConditionException 
	{
		logger.info("Installing old version app");
		AppHelper.cleanCache("MVP5");
	 	ShortcutsTyper.delayTime(3000);	
	 	
	 	logger.info("Register user in old version app");
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
									AppHelper.getAppPath(), "script/automation/upgrade/fromMVP5.js");
		ShortcutsTyper.delayTime(2000);

		logger.info("Installing latest app");
		AppHelper.install();
		ShortcutsTyper.delayTime(3000);
		
		logger.info("Check valid user info");
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/automation/upgrade/CheckUserInfo.js");
		
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP6", "MVP7", "upgrade" })
	public void Upgrade_MVP6_MVP7() throws InterruptedException, StopConditionException 
	{
		logger.info("Installing old version app");
		AppHelper.cleanCache("MVP6");
	 	ShortcutsTyper.delayTime(3000);	
	 	
	 	logger.info("Register user in old version app");
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
									AppHelper.getAppPath(), "script/automation/upgrade/fromMVP6.js");
		ShortcutsTyper.delayTime(2000);
		
		logger.info("Installing latest app");
		AppHelper.install();
		ShortcutsTyper.delayTime(3000);
		
		logger.info("Check valid user info");
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/automation/upgrade/CheckUserInfo.js");
		
	}
}