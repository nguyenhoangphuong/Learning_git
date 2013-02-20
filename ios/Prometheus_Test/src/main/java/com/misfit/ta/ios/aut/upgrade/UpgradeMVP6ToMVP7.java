package com.misfit.ta.ios.aut.upgrade;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;
import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

public class UpgradeMVP6ToMVP7 extends AutomationTest{
	 
	@Test(groups = { "ios", "Prometheus", "MVP6", "MVP6", "signup" })
	public void upgradeFromMVP6ToMVP7() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache("MVP6");
	 	ShortcutsTyper.delayTime(3000);
	 	AppHelper.install("MVP7");
//		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
//									AppHelper.getAppPath(), "script/upgrade/FromMVP6.js");
//		ShortcutsTyper.delayTime(2000);
//		AppHelper.install("MVP7");
//		ShortcutsTyper.delayTime(3000);
//		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
//				AppHelper.getAppPath(), "script/upgrade/MVP6_checkuserinfo.js");
		
	}
}