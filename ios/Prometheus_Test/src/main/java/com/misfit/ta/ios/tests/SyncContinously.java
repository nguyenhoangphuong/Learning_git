package com.misfit.ta.ios.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.sync.SyncDebugLog;
import com.misfit.ta.gui.Sync;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.report.TRS;
import com.misfit.ta.utils.ShortcutsTyper;

public class SyncContinously extends AutomationTest {

	private static int NUMBER_OF_SYNC = 30;
	
	@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "QuietSync", "SyncContinously" })
	public void QuietSyncContinously() {

		for (int i = 0; i < NUMBER_OF_SYNC; i++) {

			System.out.println("--------------------- Quiet Sync: " + i + " ----------------------");
			instrument.kill();
			AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), "script/quietsync.js");
			
			// wait for next quiet sync
			ShortcutsTyper.delayTime(2000);
		}
	}
	
	@Test(groups = { "iOS", "Prometheus", "Syncing", "iOSAutomation", "ContinuousSyncing", "SyncContinously" })
	public void ManualSyncContinously() throws InterruptedException, StopConditionException, IOException {

		
		String email = "nhhai16991@gmail.com";
		String password = "qqqqqq";
		String serialNumber = "science003";
		
		// set up: link shine v14 to v14 account
		Long now = System.currentTimeMillis() / 1000;
		MVPApi.createPedometer(MVPApi.signIn(email, password).token, serialNumber, 
				MVPApi.LATEST_FIRMWARE_VERSION_STRING, now, null, now, 
				MVPApi.generateLocalId(), null, now);

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

			Sync.signIn(email, password);
			for (int i = 0; i < NUMBER_OF_SYNC; i++) {
				
				TRS.instance().addStep("Sync number: " + (i + 1), null);

				long start = 0;
				long end = 0;
				boolean passed = true;

				Sync.openSyncView();
				Sync.tapToSync();
				start = System.currentTimeMillis();

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
				ShortcutsTyper.delayTime(5000);
				String log = MVPApi.getLatestSyncLog(email, password, begin);
				uiStartTime[i] = start / 1000;
				uiEndTime[i] = end / 1000;
				statusPassed[i] = passed;
				syncLogs[i] = new SyncDebugLog(log);
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

	public static void main(String[] args) {
		
		SyncContinously syncTest = new SyncContinously();
		syncTest.QuietSyncContinously();
	}
	
}
