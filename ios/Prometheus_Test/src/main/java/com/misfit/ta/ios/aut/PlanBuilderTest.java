package com.misfit.ta.ios.aut;

//-----------------import-------------------------------//
import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;
import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

//------------------class-------------------------------//
public class PlanBuilderTest extends AutomationTest
{
	@Test(groups = { "ios", "Prometheus", "MVP3", "planbuilder" })
	public void PlanBuilderTest() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/testcases/PlanBuilder/planBuilder_TCs.js");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP3", "planbuilder" })
	public void PlanPickerTest() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/testcases/PlanBuilder/planPicker_TCs.js");
	}
}
