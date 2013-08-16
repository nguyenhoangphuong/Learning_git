package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.gui.Sync;
import com.misfit.ta.ios.AutomationTest;

public class SyncTest extends AutomationTest {
	private static int NUMBER_OF_SYNC = 50;

	@Test(groups = { "iOS", "Prometheus", "Syncing" })
	public void sync() throws InterruptedException,
			StopConditionException, IOException {
		int successfulSyncCount = 0;
		int failedSyncCount = 0;
		ResultLogger rlog = ResultLogger
				.getLogger("testSync_" + System.currentTimeMillis());

		try {
			Sync.signIn();
			for (int i = 0; i < NUMBER_OF_SYNC; i++) {
				Sync.openSyncView();
				Sync.tapToSync();
				while (!(ViewUtils.isExistedView("UILabel", "Today") && ViewUtils
						.isExistedView("UILabel", "Week"))) {
					Thread.sleep(5000);
				}
				if (Sync.hasAlert()) {
					failedSyncCount++;
					Sync.tapOK();
				} else {
					successfulSyncCount++;
				}
				Thread.sleep(30000);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			rlog.log("OOPS test was terminated due to this exception: ");
			rlog.log(ex.toString());
		} finally {
			rlog.log("--- SYNCING RESULT ---");
			rlog.log("Number of expected tests: " + NUMBER_OF_SYNC);
			int totalTests = successfulSyncCount + failedSyncCount;
			rlog.log("Number of executed tests: " + totalTests);
			rlog.log("Number of sucessful tests: " + successfulSyncCount);
			rlog.log("Number of failed tests: " + failedSyncCount);
		}
	}

}
