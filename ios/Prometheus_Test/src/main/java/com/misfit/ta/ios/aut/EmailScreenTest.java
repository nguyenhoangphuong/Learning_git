package com.misfit.ta.ios.aut;


import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;
public class EmailScreenTest extends AutomationTest{
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "email" })
	public void gotoLegalScreenTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/email/GoToLegalScreen.js");
	
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "email" })
	public void GenaralEmailTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/email/GeneralEmailTest.js");
	
	}
}
