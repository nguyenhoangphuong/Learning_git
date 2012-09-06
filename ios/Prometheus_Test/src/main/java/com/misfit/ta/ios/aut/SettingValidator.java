package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

public class SettingValidator extends AutomationTest {
	@Test(groups = { "ios", "Prometheus", "MVP1", "setting" })
	public void runTestCase01() throws InterruptedException, StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/testcase_01.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "setting" })
	public void runTestCase02() throws InterruptedException, StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/testcase_02.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "setting" })
	public void runTestCase03() throws InterruptedException, StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/testcase_03.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "setting" })
	public void runTestCase04() throws InterruptedException, StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/testcase_04.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "setting" })
	public void runTestCase05() throws InterruptedException, StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/setting/testcase_05.js");
	}
}
