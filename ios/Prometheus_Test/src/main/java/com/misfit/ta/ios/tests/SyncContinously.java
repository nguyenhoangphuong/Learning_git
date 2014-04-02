package com.misfit.ta.ios.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ta.Gui;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.sync.SyncDebugLog;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.InstrumentHelper;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Sync;
import com.misfit.ta.report.TRS;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ShortcutsTyper;

public class SyncContinously {

	public int numberOfSync = 50;
	public String deviceIp = "192.168.1.114";
	private InstrumentHelper instrument = new InstrumentHelper();
	

	@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "QuietSync", "SyncContinously" })
	public void QuietSyncContinously() {
		
		for (int i = 0; i < numberOfSync; i++) {

			System.out.println("--------------------- Quiet Sync: " + i + " ----------------------");

			// delete trace
			Files.delete("instrumentscli0.trace");
						
			// start app, note that we load app in another thread
			// so we must wait about 15s for app to be fully loaded
			instrument.start();
			ShortcutsTyper.delayTime(15000);
			
			// do whatever you want here: connect Nu, check for views...
			// currently we just do wait for quiet sync to finish
			ShortcutsTyper.delayTime(10 * 1000);
			
			// kill app
			instrument.stop();
			instrument.kill();
			ShortcutsTyper.delayTime(5 * 1000);
		}
	}
	
	@Test(groups = { "iOS", "Prometheus", "Syncing", "iOSAutomation", "ContinuousSyncing", "SyncContinously" })
	public void ManualSyncContinously() throws InterruptedException, StopConditionException, IOException {

		// connect Nu
		// NOTE: you have to open app by yourself
		Gui.init(deviceIp);
		
		// user info
		String email = "science018@qa.com";
		String password = "qqqqqq";
		String serialNumber = "science018";
		
		// set up: link shine to account	
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
		
		SyncDebugLog[] syncLogs = new SyncDebugLog[numberOfSync];
		long[] uiStartTime = new long[numberOfSync];
		long[] uiEndTime = new long[numberOfSync];
		boolean[] statusPassed = new boolean[numberOfSync];

		// start test
		ShortcutsTyper.delayTime(2000);
		try {
			long begin = System.currentTimeMillis() / 1000 - 720;

			PrometheusHelper.signIn(email, password);
			for (int i = 0; i < numberOfSync; i++) {
				
				TRS.instance().addStep("Sync number: " + (i + 1), null);

				long start = 0;
				long end = 0;
				boolean passed = true;

				Sync.openSyncView();
				Sync.tapToSync();
				start = System.currentTimeMillis();

				while (Gui.getProperty("PTAECASyncAnimationView", 0, "alpha").equals("1")) {
					ShortcutsTyper.delayTime(100);
				}
				end = System.currentTimeMillis();
				ShortcutsTyper.delayOne();
				
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
	
	@Test
	public void QuickManualSyncContinously() throws InterruptedException, StopConditionException, IOException {

		// connect Nu
		// NOTE: you have to open app first
		Gui.init(deviceIp);
		
		// tracking
		int successfulSyncCount = 0;
		int failedSyncCount = 0;
		
		// start test
		try {

			for (int i = 0; i < numberOfSync; i++) {
				
				TRS.instance().addStep("Sync number: " + (i + 1), null);

				Sync.openSyncView();
				Sync.tapToSync();

				while (Gui.getProperty("PTAECASyncAnimationView", 0, "alpha").equals("1")) {
					ShortcutsTyper.delayTime(100);
				}
				ShortcutsTyper.delayOne();

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
			fr.print("Total test: " + numberOfSync + "\n");
			fr.print("Fail number: " + failedSyncCount + "\n");
			fr.print("Success number: " + successfulSyncCount + "\n");
			fr.print('\n');

			fr.print("No.\tStatus\tNumberOfRetries\tRetryReasons\tUITotalTime\tScanningTime\tGetSettingsTime\tTransferDataTime\tSetSettingsAndCleanupTime\n");
			for (int i = 0; i < numberOfSync; i++) {

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
	
	
	public static void main(String[] args) throws InterruptedException, StopConditionException, IOException {
		
		if(args.length == 0)
			args = new String[] {"help", ""};
		
		String deviceIp = args[0];
		String mode = (args.length < 2 ? "manual" : args[1].toLowerCase());
		int numberOfSync = (args.length < 3 ? 50 : Integer.parseInt(args[2]));
		
		if(mode.equals("help")) {
			
			System.out.println("How to use:");
			System.out.println("SyncContinously [deviceip] [mode] [numberOfSync]");
			System.out.println("deviceip: 192.168.1.123");
			System.out.println("mode: manual/quiet, default = manual");
			System.out.println("numberOfSync: default = 50");
		}
		else if(mode.equals("manual")) {
			
			SyncContinously test = new SyncContinously();
			test.deviceIp = deviceIp;
			test.numberOfSync = numberOfSync;
			test.QuickManualSyncContinously();
		}
		else if(mode.equals("quiet")) {
			
			SyncContinously test = new SyncContinously();
			test.deviceIp = deviceIp;
			test.numberOfSync = numberOfSync;
			test.QuietSyncContinously();
		}
	}

}
