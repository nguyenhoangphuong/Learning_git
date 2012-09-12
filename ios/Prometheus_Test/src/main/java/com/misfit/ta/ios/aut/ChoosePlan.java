package com.misfit.ta.ios.aut;


import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;
public class ChoosePlan extends AutomationTest{
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "chooseplan" })
	public void PlanEasyTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/chooseplan/PlanOther.js");
	
	}
	@Test(groups = { "ios", "Prometheus", "MVP1", "chooseplan" })
	public void PlanNormalTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/chooseplan/PlanNormal.js");
	
	}
	@Test(groups = { "ios", "Prometheus", "MVP1", "chooseplan" })
	public void PlanActiveTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/chooseplan/PlanActive.js");
	
	}
}
	