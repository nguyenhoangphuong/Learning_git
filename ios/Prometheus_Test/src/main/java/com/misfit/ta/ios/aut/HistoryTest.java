package com.misfit.ta.ios.aut;

//-----------------import-------------------------------//
import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;
import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

//------------------class-------------------------------//
public class HistoryTest extends AutomationTest {

	// --------------invalid---------------------//
	@Test(groups = { "ios", "Prometheus", "MVP3", "history" })
	public void SignupInvalidValidationTest() throws InterruptedException,
			StopConditionException {
		AppHelper.cleanCache();
		ShortcutsTyper.delayTime(1000);
		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
				AppHelper.getAppPath(),
				"script/testcases/history/history_TCs.js");

	}

}