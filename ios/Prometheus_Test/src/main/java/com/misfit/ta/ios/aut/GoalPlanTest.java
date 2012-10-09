package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;
import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

public class GoalPlanTest extends AutomationTest 
{
	@Test(groups = {"ios", "Prometheus", "goalplan"})
	public void GoalPlanButtonSliderAlertInterruptDataTest() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
		ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), 
								AppHelper.getAppPath(), "script/testcases/goalplan/goalplan_TCs_1.js");
								
	}
	
	@Test(groups = {"ios", "Prometheus", "goalplan"})
	public void GoalPlanGoalAdjustmentTest() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
		ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), 
								AppHelper.getAppPath(), "script/testcases/goalplan/goalplan_TCs_2.js");
								
	}
}
