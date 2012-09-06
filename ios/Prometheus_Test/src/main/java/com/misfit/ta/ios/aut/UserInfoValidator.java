package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

public class UserInfoValidator extends AutomationTest {
	  
	// C2074	Check default value: Height, Weight, Age, Gender, Unit (need to update default value of height, weight, age)
	// C2079	Verify input: Height
	// C2080	Verify input: Weight
	// C2081	Verify input: Age
	// C2082	Verify input: Gender
	// C2083	Verify input: Unit
	// C2073	Interruptions
	// C2098	Verify "Done" button action
	@Test(groups = { "ios", "Prometheus", "MVP1", "userinfo" })
	public void runAllUserInfoTCs() throws InterruptedException, StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
					 "script/testcases/userinfo/userinfo_testcase.js");
	}
}
