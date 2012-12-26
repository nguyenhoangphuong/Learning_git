package com.misfit.ta.ios.aut;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.ShortcutsTyper;

public class CleanCacheTest extends AutomationTest {
	@Test(groups = { "ios", "Prometheus", "CleanCache" })
	public void TestCleanCache()
			throws InterruptedException, StopConditionException {
		//AppHelper.cleanCache("MVP3_staging");
		AppHelper.install("MVP3_staging");
		ShortcutsTyper.delayTime(1000);
	}
}