package com.misfit.ta.ios.aut;


import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;
public class PlanChooser extends AutomationTest
{
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "planchooser" })
	public void PlanEasyTest() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/chooseplan/PlanEasy.js");
	
	}
	
	
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "planchooser" })
	public void PlanNormalTest() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/chooseplan/PlanNormal.js");
	
	}
	@Test(groups = { "ios", "Prometheus", "MVP1", "planchooser" })
	public void PlanActiveTest() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/chooseplan/PlanActive.js");
	
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "planchooser" })
	public void PlanOtherTest() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
		ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(),
				"script/testcases/chooseplan/PlanOther.js");

	}
}
	