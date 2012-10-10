package com.misfit.ta.ios.aut;

//-----------------import-------------------------------//
import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;
import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

//------------------class-------------------------------//
public class LoginTest extends AutomationTest
{
	 
	 //--------------back end---------------------//
	@Test(groups = { "ios", "Prometheus", "MVP2", "login" })
	public void LoginBackendTest() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/testcases/login/login_backendVerification_TCs.js");
	}
	
	 //--------------client---------------------//
	@Test(groups = { "ios", "Prometheus", "MVP2", "login" })
	public void LoginClientTest() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/testcases/login/login_clientVerification_TCs.js");
	}
	
	 //--------------translation---------------------//
	@Test(groups = { "ios", "Prometheus", "MVP2", "login" })
	public void LoginTranslationTest() throws InterruptedException, StopConditionException 
	{
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
			AppHelper.getAppPath(), "script/testcases/login/login_translation_TCs.js");		
	}
}