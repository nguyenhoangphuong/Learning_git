
package com.misfit.ta.ios.aut;


//-----------------import-------------------------------//

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;
import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

//------------------class-------------------------------//
public class GoalPlanTest extends AutomationTest {

	//---------------------------------alert interrupt plan data------------//
	@Test(groups = {"ios", "Prometheus", "goalplan"})
	public void GoalPlanAlertInterruptTest() throws InterruptedException, StopConditionException {
	AppHelper.cleanCache();
	ShortcutsTyper.delayTime(1000);
	AppHelper.launchInstrument(AppHelper.getCurrentUdid(), 
							AppHelper.getAppPath(), "script/testcases/goalplan/goalplan_alert_interruption_plandata_TCs.js");
							
	}
	
	//---------------------------------Goal plan button slider------------//
	@Test(groups = {"ios", "Prometheus", "goalplan"})
	public void GoalPlanButtonsSlidersTest() throws InterruptedException, StopConditionException {
	AppHelper.cleanCache();
	ShortcutsTyper.delayTime(1000);
	AppHelper.launchInstrument(AppHelper.getCurrentUdid(), 
							AppHelper.getAppPath(), "script/testcases/goalplan/goalplan_buttons_sliders_TCs.js");
							
	}
	
	//---------------------------------Goal adjustment------------//
	@Test(groups = {"ios", "Prometheus", "goalplan"})
	public void GoalPlanGoalAdjustmentTest() throws InterruptedException, StopConditionException {
	AppHelper.cleanCache();
	ShortcutsTyper.delayTime(1000);
	AppHelper.launchInstrument(AppHelper.getCurrentUdid(), 
							AppHelper.getAppPath(), "script/testcases/goalplan/goalplan_goalAdjustment_TCs.js");
							
	}
	
   
  
  
}
