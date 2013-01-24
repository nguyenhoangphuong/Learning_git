package com.misfit.ta.ios.aut.upgrade;

//-----------------import-------------------------------//
import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;
import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

//------------------class-------------------------------//
public class UpgradeTest_MVP5_MVP6 extends AutomationTest{
	 
	 //--------------invalid---------------------//
//	@Test(groups = { "ios", "Prometheus", "MVP5", "MVP6", "signup" })
//	public void SignupInvalidValidationTest() throws InterruptedException,
//			StopConditionException {
//		AppHelper.cleanCache("MVP5");
//	 	ShortcutsTyper.delayTime(1000);	
//		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
//									AppHelper.getAppPath(), "script/testcases/SignUp/signup_invalidValidation_TCs.js");
//	
//	}

	 //--------------Valid---------------------//
	@Test(groups = { "ios", "Prometheus", "MVP5", "MVP6", "signup" })
	public void upgradeFromMVP5ToMVP6() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache("MVP5");
	 	ShortcutsTyper.delayTime(2000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
									AppHelper.getAppPath(), "script/upgrade/fromMVP5.js");
		ShortcutsTyper.delayTime(2000);
		AppHelper.install("MVP6");
		ShortcutsTyper.delayTime(3000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/upgrade/MVP6_checkuserinfo.js");
		
	}
}