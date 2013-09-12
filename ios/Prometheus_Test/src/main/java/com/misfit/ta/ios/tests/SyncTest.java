package com.misfit.ta.ios.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.Gui;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.sync.SyncDebugLog;
import com.misfit.ta.gui.Sync;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.ios.modelapi.sync.LinkNoShineAvailableAPI;
import com.misfit.ta.ios.modelapi.sync.LinkOneShineAvailableAPI;
import com.misfit.ta.report.TRS;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ShortcutsTyper;

public class SyncTest extends AutomationTest {
	
	private static int NUMBER_OF_SYNC = 1;

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
		MVPApi.createPedometer(MVPApi.signIn("v14@qa.com", "test12").token, "XXXXXV0014", MVPApi.LATEST_FIRMWARE_VERSION_STRING, now, null, now, MVPApi.generateLocalId(), null, now);

		// create sync folder if not exist
		File sync = new File("sync");
		if (!sync.isDirectory())
			sync.mkdirs();

		// tracking
		int successfulSyncCount = 0;
		int failedSyncCount = 0;
		
		SyncDebugLog[] syncLogs = new SyncDebugLog[NUMBER_OF_SYNC];
		long[] uiStartTime = new long[NUMBER_OF_SYNC];
		long[] uiEndTime = new long[NUMBER_OF_SYNC];
		boolean[] statusPassed = new boolean[NUMBER_OF_SYNC];

		// start test
		try {
			long begin = System.currentTimeMillis() / 1000 - 720;

			 //Sync.signIn();
			for (int i = 0; i < NUMBER_OF_SYNC; i++) {
				
				TRS.instance().addStep("Sync number: " + (i + 1), null);

				long start = 0;
				long end = 0;
				boolean passed = true;

				// TODO: change check upgrade to adapt new flow
				// since MVP17.1 there's no alert
				// alert will be replace by popup and icon at the top
				if (Sync.hasAlert() && Gui.getPopupTitle() == "Update Available") {
					Gui.touchPopupButton("Upgrade Now");
					start = System.currentTimeMillis();
				} else {
					Sync.openSyncView();
					Sync.tapToSync();
					start = System.currentTimeMillis();
				}

				while (!(ViewUtils.isExistedView("UILabel", "Today") && ViewUtils.isExistedView("UILabel", "Week"))) {
					ShortcutsTyper.delayTime(100);
				}
				end = System.currentTimeMillis();

				if (Sync.hasAlert()) {
					failedSyncCount++;
					Sync.tapOK();
					passed = false;
				} else {
					successfulSyncCount++;
				}

				// parse sync log and store the record
				ShortcutsTyper.delayTime(15000);
				String log = MVPApi.getLatestSyncLog("v14@qa.com", "XXXXXV0014", begin);
				uiStartTime[i] = start / 1000;
				uiEndTime[i] = end / 1000;
				statusPassed[i] = passed;
				syncLogs[i] = new SyncDebugLog(log);

				if ((i + 1) % 10 == 0) {
					ShortcutsTyper.delayTime(180000);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// write the result in detail to excel file
		File file = writeResult("sync/sync_result_detail.xls", failedSyncCount, successfulSyncCount, 
				syncLogs, uiStartTime, uiEndTime, statusPassed);

		// upload file to TRS
		TRS.instance().addFileToCurrentTest(file.getAbsolutePath(), null);

	}

	private File writeResult(String filepath, int failedSyncCount, int successfulSyncCount, SyncDebugLog[] syncLogs, long[] uiStartTime, long[] uiEndTime, boolean[] statusPassed) {

		// create new file if exist
		File detailResult = new File(filepath);
		if (detailResult.exists()) {
			detailResult.delete();
			try {
				detailResult.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// write result
		PrintWriter fr;
		try {
			fr = new PrintWriter(detailResult.getAbsolutePath(), "UTF-8");
			fr.print("Total test: " + NUMBER_OF_SYNC + "\n");
			fr.print("Fail number: " + failedSyncCount + "\n");
			fr.print("Success number: " + successfulSyncCount + "\n");
			fr.print('\n');

			fr.print("No.\tStatus\tNumberOfRetries\tRetryReasons\tUITotalTime\tScanningTime\tGetSettingsTime\tTransferDataTime\tSetSettingsAndCleanupTime\n");
			for (int i = 0; i < NUMBER_OF_SYNC; i++) {

				fr.print((i + 1) + "\t");
				fr.print(statusPassed[i] + "\t");
				fr.print(syncLogs[i].numberOfRetries + "\t");
				fr.print(syncLogs[i].retryReasons + "\t");
				fr.print((uiEndTime[i] - uiStartTime[i]) + "\t");
				fr.print((syncLogs[i].handShakeTimestamp - syncLogs[i].userTriggerSyncTimestamp) + "\t");
				fr.print((syncLogs[i].receiveGetPointTimestamp - syncLogs[i].handShakeTimestamp) + "\t");
				fr.print((syncLogs[i].receiveGetBatteryTimestamp - syncLogs[i].sendGetFileListTimestamp) + "\t");
				fr.print((syncLogs[i].cleanUpConnectionTimestamp - syncLogs[i].sendSetGoalTimestamp) + "\t");
				fr.print('\n');
			}
			fr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return detailResult;
	}

}
