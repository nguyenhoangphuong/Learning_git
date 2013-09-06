package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.Gui;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.gui.Sync;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.ios.modelapi.sync.LinkNoShineAvailableAPI;
import com.misfit.ta.ios.modelapi.sync.LinkOneShineAvailableAPI;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ShortcutsTyper;

public class SyncTest extends AutomationTest {
	private static int NUMBER_OF_SYNC = 50;

	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Syncing", "Linking" })
	public void LinkNoShineAvailable() throws InterruptedException, StopConditionException, IOException {
		ModelHandler model = getModelhandler();
		model.add("LinkNoShineAvailable", new LinkNoShineAvailableAPI(this, Files.getFile("model/sync/LinkNoShineAvailable.graphml"), false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
		model.execute("LinkNoShineAvailable");
		Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
		String actualResult = getModelhandler().getStatistics();
		System.out.println(actualResult);
	}

	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Syncing", "Linking" })
	public void LinkOneShineAvailable() throws InterruptedException, StopConditionException, IOException {
		ModelHandler model = getModelhandler();
		model.add("LinkOneShineAvailable", new LinkOneShineAvailableAPI(this, Files.getFile("model/sync/LinkOneShineAvaiable.graphml"), false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
		model.execute("LinkOneShineAvailable");
		Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
		String actualResult = getModelhandler().getStatistics();
		System.out.println(actualResult);
	}

	@Test(groups = { "iOS", "Prometheus", "Syncing", "iOSAutomation", "ContinuousSyncing" })
	public void SyncContinously() throws InterruptedException, StopConditionException, IOException {
		
		// set up: link shine v14 to v14 account
		Long now = System.currentTimeMillis() / 1000;
		MVPApi.createPedometer(MVPApi.signIn("v14@qa.com", "test12").token, "XXXXXV0014", 
				MVPApi.LATEST_FIRMWARE_VERSION_STRING, now, null, now, MVPApi.generateLocalId(), null, now);
		
		// test
		int successfulSyncCount = 0;
		int failedSyncCount = 0;
		ResultLogger rlog = ResultLogger.getLogger("testSync_" + System.currentTimeMillis());

		try {
			Sync.signIn();
			for (int i = 0; i < NUMBER_OF_SYNC; i++) {
				if (Sync.hasAlert() && Gui.getPopupTitle() == "Update Available") {
					Gui.touchPopupButton("Upgrade Now");
				} else {
					Sync.openSyncView();
					Sync.tapToSync();
				}
				while (!(ViewUtils.isExistedView("UILabel", "Today") && ViewUtils.isExistedView("UILabel", "Week"))) {
					ShortcutsTyper.delayTime(5000);
				}
				if (Sync.hasAlert()) {
					failedSyncCount++;
					Sync.tapOK();
				} else {
					successfulSyncCount++;
				}
				ShortcutsTyper.delayTime(30000);
				if (i % 10 == 0) {
					ShortcutsTyper.delayTime(180000);
				}
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
