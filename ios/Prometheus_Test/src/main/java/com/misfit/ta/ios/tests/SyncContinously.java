package com.misfit.ta.ios.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.Gui;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.sync.SyncDebugLog;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Sync;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.report.TRS;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ShortcutsTyper;

public class SyncContinously extends AutomationTest {

	private static int NUMBER_OF_SYNC = 50;

	@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "QuietSync", "SyncContinously" })
	public void QuietSyncContinously() {
	
		instrument.stop();
		instrument.kill();
		
		for (int i = 0; i < NUMBER_OF_SYNC; i++) {

			System.out.println("--------------------- Quiet Sync: " + i + " ----------------------");

			// delete trace
			Files.delete("instrumentscli0.trace");
						
			// start app, note that we load app in another thread
			// so we must wait about 15s for app to be fully loaded
			instrument.start();
			ShortcutsTyper.delayTime(15000);
			// do whatever you want here: connect Nu, check for views...
			// currently we just do wait for quiet sync to finish
			ShortcutsTyper.delayTime(30 * 1000);
			// kill app
			instrument.stop();
			instrument.kill();
			ShortcutsTyper.delayTime(5 * 1000);
		}
	}
	
//	@Test(groups = { "iOS", "Prometheus", "Syncing", "iOSAutomation", "ContinuousSyncing", "SyncContinously" })
	public void ManualSyncContinously() throws InterruptedException, StopConditionException, IOException {

		
		String email = "science018@qa.com";
		String password = "qqqqqq";
		String serialNumber = "science018";
		
		// set up: link shine v14 to v14 account	
		Long now = System.currentTimeMillis() / 1000;
		String token = MVPApi.signIn(email, password).token;	
		Pedometer pedo = MVPApi.getPedometer(token);
		pedo.setFirmwareRevisionString(MVPApi.LATEST_FIRMWARE_VERSION_STRING);
		pedo.setSerialNumberString(serialNumber);
		pedo.setLocalId("pedometer-" + MVPApi.generateLocalId());
		pedo.setLastSyncedTime(now);
		pedo.setLinkedTime(now);
		pedo.setUpdatedAt(now);
		MVPApi.updatePedometer(token, pedo);
		
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
		ShortcutsTyper.delayTime(2000);
		try {
			long begin = System.currentTimeMillis() / 1000 - 720;

//			Sync.signIn(email, password);
			for (int i = 0; i < NUMBER_OF_SYNC; i++) {
				
				TRS.instance().addStep("Sync number: " + (i + 1), null);

				long start = 0;
				long end = 0;
				boolean passed = true;

				Sync.openSyncView();
				Sync.tapToSync();
				start = System.currentTimeMillis();

				while (!(ViewUtils.isExistedView("UILabel", "Last synced just now") || Sync.hasAlert())) {
					ShortcutsTyper.delayTime(100);
				}
				end = System.currentTimeMillis();
				
				while (Sync.hasAlert()) {
					if (Gui.getPopupTitle().equals(DefaultStrings.BatteryLowTitle)) {
						PrometheusHelper.handleBatteryLowPopup();
					} else {
						failedSyncCount++;
						Sync.tapOK();
						passed = false;
					}
				}
				if (passed) {
					successfulSyncCount++;
				}

				// parse sync log and store the record
				ShortcutsTyper.delayTime(5000);
				String log = MVPApi.getLatestSyncLog(email, password, begin);
				uiStartTime[i] = start / 1000;
				uiEndTime[i] = end / 1000;
				statusPassed[i] = passed;
				syncLogs[i] = new SyncDebugLog(log);
				

				// result 
				System.out.println("Sync passed: " + successfulSyncCount);
				System.out.println("Sync falied: " + failedSyncCount);
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
	
//	@Test
	public void QuickManualSyncContinously() throws InterruptedException, StopConditionException, IOException {

		// tracking
		int successfulSyncCount = 0;
		int failedSyncCount = 0;
		
		// start test
		try {

			for (int i = 0; i < NUMBER_OF_SYNC; i++) {
				
				TRS.instance().addStep("Sync number: " + (i + 1), null);

				Sync.openSyncView();
				Sync.tapToSync();

				while (!(ViewUtils.isExistedView("UILabel", "Today") && ViewUtils.isExistedView("UILabel", "Week"))) {
					ShortcutsTyper.delayTime(100);
				}

				if (Sync.hasAlert()) {
					failedSyncCount++;
					Sync.tapOK();
				} else {
					successfulSyncCount++;
				}

				
				// parse sync log and store the record
				ShortcutsTyper.delayTime(5000);
				
				
				// print result
				System.out.println("-----------------------------------------------");
				System.out.println("Sync passed: " + successfulSyncCount);
				System.out.println("Sync falied: " + failedSyncCount);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
