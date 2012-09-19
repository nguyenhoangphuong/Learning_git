package com.misfit.ta.ios.aut;


import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;
public class ValidEmailValidate extends AutomationTest{
	
	
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "email" })
	public void CharsLocalTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);		
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/email/64CharLocalEmail.js");
	
	}
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "email" })
	public void CharsDomainTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);		
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/email/255CharDomainEmail.js");
	
	}
	
	
	@Test(groups = { "ios", "Prometheus", "MVP1", "email" })
	public void DashDomainTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);		
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/email/DashDomain.js");
	
	}
	@Test(groups = { "ios", "Prometheus", "MVP1", "email" })
	public void DashUsernameTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/email/DashUsername.js");
	
	}
	@Test(groups = { "ios", "Prometheus", "MVP1", "email" })
	public void DigitUsernameTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/email/DigitUsername.js");
	
	}
	@Test(groups = { "ios", "Prometheus", "MVP1", "email" })
	public void DotSubDomainTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/email/DotSubDomain.js");
	
	}
	@Test(groups = { "ios", "Prometheus", "MVP1", "email" })
	public void DotUserNameTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/email/DotUserName.js");
	
	}
	@Test(groups = { "ios", "Prometheus", "MVP1", "email" })
	public void UnderscoresUsernameTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/email/UnderscoresUsername.js");
	
	} 
	@Test(groups = { "ios", "Prometheus", "MVP1", "email" })
	public void UnicodeDomainTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/email/UnicodeDomain.js");
	
	} 
	@Test(groups = { "ios", "Prometheus", "MVP1", "email" })
	public void UnicodeUsernameTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
	 	ShortcutsTyper.delayTime(1000);	
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(), "script/testcases/email/UnicodeUsername.js");
	
	}
	
	
}
