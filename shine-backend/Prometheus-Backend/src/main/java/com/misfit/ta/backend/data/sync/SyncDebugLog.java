package com.misfit.ta.backend.data.sync;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.internalapi.MVPApi;

public class SyncDebugLog {

	// logger
	private static Logger logger = Util.setupLogger(MVPApi.class);
	public static int APP_MODE_BACKGROUND = 0;
	public static int APP_MODE_FOREGROUND = 1;
	public static int SYNC_MODE_AUTO = 2;
	public static int SYNC_MODE_MANUAL = 3;

	// timestamp
	public long startSyncSessionTimestamp = 0;
	public long userTriggerSyncTimestamp = 0;
	public long shineConnectedTimestamp = 0;
	public long handShakeTimestamp = 0;

	public long sendPlayingSyncAnimationTimestamp = 0;
	public long receivePlayingSyncAnimationTimestamp = 0;
	public long sendGetTimeTimestamp = 0;
	public long receiveGetTimeTimestamp = 0;
	public long sendGetGoalTimestamp = 0;
	public long receiveGetGoalTimestamp = 0;
	public long sendGetPointTimestamp = 0;
	public long receiveGetPointTimestamp = 0;
	public long sendGetFileListTimestamp = 0;
	public long receiveGetFileListTimestamp = 0;
	public long sendGetBatteryTimestamp = 0;
	public long receiveGetBatteryTimestamp = 0;

	public long sendSetGoalTimestamp = 0;
	public long receiveSetGoalTimestamp = 0;
	public long sendSetPointTimestamp = 0;
	public long receiveSetPointTimestamp = 0;
	public long sendSetTimeTimestamp = 0;
	public long receiveSetTimeTimestamp = 0;
	public long sendSetDoubleTapsSettingTimestamp = 0;
	public long receiveSetDoubleTapsSettingTimestamp = 0;
	public long sendSetTripleTapsSettingTimestamp = 0;
	public long receiveSetTripleTapsSettingTimestamp = 0;

	public long sendGetDebugFileTimestamp = 0;
	public long receiveGetDebugFileTimestamp = 0;
	public long sendEraseDebugFileTimestamp = 0;
	public long receiveEraseDebugFileTimestamp = 0;
	public long sendGetTimeAfterSyncTimestamp = 0;
	public long receiveGetTimeAfterSyncTimestamp = 0;
	public long sendGetGoalAfterSyncTimestamp = 0;
	public long receiveGetGoalAfterSyncTimestamp = 0;
	public long sendGetPointAfterSyncTimestamp = 0;
	public long receiveGetPointAfterSyncTimestamp = 0;
	public long sendStopAllAnimationTimestamp = 0;
	public long receiveStopAllAnimationTimestamp = 0;

	public long cleanUpConnectionTimestamp = 0;

	// additional data
	public int appMode = SYNC_MODE_MANUAL;
	public int syncMode = APP_MODE_FOREGROUND;
	public int batteryLevel = -1;
	public int doubleTapsValue = -1;
	public int tripleTapsValue = -1;
	public int preSyncPoint = -1;
	public int syncPoint = -1;
	public int postSyncPoint = -1;
	public int numberOfRetries = 0;
	public String retryReasons = "";

	public boolean isPlayingSyncAnimationOK = true;
	public boolean isGetTimeOK = true;
	public boolean isGetPointOK = true;
	public boolean isGetGoalOK = true;
	public boolean isGetFileListOK = true;
	public boolean isGetBatteryOK = true;
	public boolean isSetTimeOK = true;
	public boolean isSetGoalOK = true;
	public boolean isSetPointOK = true;
	public boolean isSetDoubleTapsOK = true;
	public boolean isSetTripleTapsOK = true;
	public boolean isGetDebugFileOK = true;
	public boolean isEraseDebugFileOK = true;
	public boolean isGetTimeAfterSyncOK = true;
	public boolean isGetPointAfterSyncOK = true;
	public boolean isGetGoalAfterSyncOK = true;
	public boolean isStopSyncAnimtaionOK = true;

	// constructor
	public SyncDebugLog(String log) {

		List<String> list = Arrays.asList(log.split("\n"));
		Collections.reverse(list);
		String[] lines = list.toArray(new String[list.size()]);

		boolean getTimeRead = true;
		boolean getGoalRead = true;
		boolean getPointRead = true;

		for (int i = 0; i < lines.length; i++) {

			if (lines[i].contains("START NEW SYNC SESSION")) {
				this.startSyncSessionTimestamp = getTimestamp(lines[i - 1]);

				this.appMode = getAppMode(lines[i]);
				this.syncMode = getSyncMode(lines[i]);
			}

			if (lines[i].contains("USER TRIGGERED"))
				this.userTriggerSyncTimestamp = getTimestamp(lines[i - 1]);

			if (lines[i].contains("SHINE CONNECTED"))
				this.shineConnectedTimestamp = getTimestamp(lines[i - 1]);

			if (lines[i].contains("HANDSHAKED WITH SHINE"))
				this.handShakeTimestamp = getTimestamp(lines[i - 1]);

			if (lines[i].contains("SEND REQUEST: PLAY PAIRING ANIMATION") || lines[i].contains("SEND REQUEST: PLAY SYNCING ANIMATION")) {
				this.sendPlayingSyncAnimationTimestamp = getTimestamp(lines[i - 1]);
				this.receivePlayingSyncAnimationTimestamp = getTimestamp(lines[i + 1]);
				this.isPlayingSyncAnimationOK = isStatusSuccess(lines[i + 2]);
			}

			if (lines[i].contains("SEND REQUEST: GET TIME")) {
				if (getTimeRead) {
					this.sendGetTimeTimestamp = getTimestamp(lines[i - 1]);
					this.receiveGetTimeTimestamp = getTimestamp(lines[i + 1]);
					this.isGetTimeOK = isStatusSuccess(lines[i + 2]);
					getTimeRead = false;
				} else {
					this.sendGetTimeAfterSyncTimestamp = getTimestamp(lines[i - 1]);
					this.receiveGetTimeAfterSyncTimestamp = getTimestamp(lines[i + 1]);
					this.isGetTimeAfterSyncOK = isStatusSuccess(lines[i + 2]);
				}
			}

			if (lines[i].contains("SEND REQUEST: GET GOAL")) {
				if (getGoalRead) {
					this.sendGetGoalTimestamp = getTimestamp(lines[i - 1]);
					this.receiveGetGoalTimestamp = getTimestamp(lines[i + 1]);
					this.isGetGoalAfterSyncOK = isStatusSuccess(lines[i + 2]);
					getGoalRead = false;
				} else {
					this.sendGetGoalAfterSyncTimestamp = getTimestamp(lines[i - 1]);
					this.receiveGetGoalAfterSyncTimestamp = getTimestamp(lines[i + 1]);
					this.isGetGoalAfterSyncOK = isStatusSuccess(lines[i + 2]);
				}
			}

			if (lines[i].contains("SEND REQUEST: GET POINT")) {
				if (getPointRead) {
					this.sendGetPointTimestamp = getTimestamp(lines[i - 1]);
					this.receiveGetPointTimestamp = getTimestamp(lines[i + 1]);
					this.isGetPointAfterSyncOK = isStatusSuccess(lines[i + 2]);
					getPointRead = false;
				} else {
					this.sendGetPointAfterSyncTimestamp = getTimestamp(lines[i - 1]);
					this.receiveGetPointAfterSyncTimestamp = getTimestamp(lines[i + 1]);
					this.isGetPointAfterSyncOK = isStatusSuccess(lines[i + 2]);
				}
			}

			if (lines[i].contains("SEND REQUEST: GET FILE LIST")) {
				this.sendGetFileListTimestamp = getTimestamp(lines[i - 1]);
				this.receiveGetFileListTimestamp = getTimestamp(lines[i + 1]);
				this.isGetFileListOK = isStatusSuccess(lines[i + 2]);
			}

			if (lines[i].contains("SEND REQUEST: GET BATTERY")) {
				this.sendGetBatteryTimestamp = getTimestamp(lines[i - 1]);
				this.receiveGetBatteryTimestamp = getTimestamp(lines[i + 1]);
				this.isGetBatteryOK = isStatusSuccess(lines[i + 2]);
			}

			if (lines[i].contains("BATTERY:")) {
				this.batteryLevel = getBatteryLevel(lines[i]);
			}

			if (lines[i].contains("SEND REQUEST: SET TIME")) {
				this.sendSetTimeTimestamp = getTimestamp(lines[i - 1]);
				this.receiveSetTimeTimestamp = getTimestamp(lines[i + 1]);
				this.isSetTimeOK = isStatusSuccess(lines[i + 2]);
			}

			if (lines[i].contains("SEND REQUEST: SET GOAL")) {
				this.sendSetGoalTimestamp = getTimestamp(lines[i - 1]);
				this.receiveSetGoalTimestamp = getTimestamp(lines[i + 1]);
				this.isSetGoalOK = isStatusSuccess(lines[i + 2]);
			}

			if (lines[i].contains("SEND REQUEST: SET POINT")) {
				this.sendSetPointTimestamp = getTimestamp(lines[i - 1]);
				this.receiveSetPointTimestamp = getTimestamp(lines[i + 3]);
				this.isSetPointOK = isStatusSuccess(lines[i + 4]);

				int[] points = getSyncPointInfo(lines[i + 1]);
				this.preSyncPoint = points[0];
				this.syncPoint = points[1];
				this.postSyncPoint = points[2];
			}

			if (lines[i].contains("SEND REQUEST: SET CLOCK")) {
				this.sendSetDoubleTapsSettingTimestamp = getTimestamp(lines[i - 1]);
				this.receiveSetDoubleTapsSettingTimestamp = getTimestamp(lines[i + 1]);
				this.isSetDoubleTapsOK = isStatusSuccess(lines[i + 2]);
				this.doubleTapsValue = getDoubleTapsValue(lines[i]);
			}

			if (lines[i].contains("SEND REQUEST: SET TRIPLE TAP")) {
				this.sendSetTripleTapsSettingTimestamp = getTimestamp(lines[i - 1]);
				this.receiveSetTripleTapsSettingTimestamp = getTimestamp(lines[i + 1]);
				this.isSetTripleTapsOK = isStatusSuccess(lines[i + 2]);
				this.tripleTapsValue = getTripleTapsValue(lines[i]);
			}

			if (lines[i].contains("SEND REQUEST: GET DEBUG FILE")) {
				this.sendGetDebugFileTimestamp = getTimestamp(lines[i - 1]);
				this.receiveGetDebugFileTimestamp = getTimestamp(lines[i + 1]);
				this.isGetDebugFileOK = isStatusSuccess(lines[i + 2]);
			}

			if (lines[i].contains("SEND REQUEST: ERASE DEBUG FILE")) {
				this.sendEraseDebugFileTimestamp = getTimestamp(lines[i - 1]);
				this.receiveEraseDebugFileTimestamp = getTimestamp(lines[i + 1]);
				this.isEraseDebugFileOK = isStatusSuccess(lines[i + 2]);
			}

			if (lines[i].contains("SEND REQUEST: STOP ALL ANIMATIONS")) {
				this.sendStopAllAnimationTimestamp = getTimestamp(lines[i - 1]);
				this.receiveStopAllAnimationTimestamp = getTimestamp(lines[i + 1]);
				this.isStopSyncAnimtaionOK = isStatusSuccess(lines[i + 2]);
			}

			if (lines[i].contains("CLEAN UP CONNECTIONS"))
				this.cleanUpConnectionTimestamp = getTimestamp(lines[i]);

			// retry
			if (lines[i].contains("DROP CONNECTION AND RETRY"))
				this.numberOfRetries++;
			
			if (lines[i].contains(". RETRY")) {
				if(this.retryReasons != "")
					this.retryReasons += ", ";
				
				this.retryReasons += getRetryReason(lines[i]);
			}
				
		}
	}

	// parsers
	private long getTimestamp(String line) {

		String[] parts = line.split(":");
		String timestamp = parts[1].trim();

		return Long.valueOf(timestamp);
	}

	private int getAppMode(String line) {

		return line.contains("FOREGROUND") ? APP_MODE_FOREGROUND : APP_MODE_BACKGROUND;
	}

	private int getSyncMode(String line) {

		return line.contains("MANUAL") ? SYNC_MODE_MANUAL : SYNC_MODE_AUTO;
	}

	private int getBatteryLevel(String line) {

		line = line.replace("%", "");
		String[] parts = line.split(":");
		return Integer.valueOf(parts[1].trim());
	}

	private int getDoubleTapsValue(String line) {

		String[] parts = line.split(":");
		return Integer.valueOf(parts[2].trim());
	}

	private int getTripleTapsValue(String line) {

		String[] parts = line.split(":");
		return Integer.valueOf(parts[2].trim());
	}

	private int[] getSyncPointInfo(String line) {

		line = line.replace(",", ":");
		String[] parts = line.split(":");
		int[] points = new int[3];
		points[0] = Integer.valueOf(parts[2].trim());
		points[1] = Integer.valueOf(parts[4].trim());
		points[2] = Integer.valueOf(parts[6].trim());

		return points;
	}
	
	private String getRetryReason(String line) {
		
		String[] parts = line.split(".");
		return parts[1].trim();
	}

	private boolean isStatusSuccess(String line) {

		return line.contains("SUCCESS");
	}

	public void info() {

		logger.info("---------------");
		logger.info(this.startSyncSessionTimestamp);
		logger.info(this.userTriggerSyncTimestamp);
		logger.info(this.shineConnectedTimestamp);
		logger.info(this.handShakeTimestamp);
		logger.info(this.sendPlayingSyncAnimationTimestamp);
		logger.info(this.receivePlayingSyncAnimationTimestamp);
		logger.info("---------------");
		logger.info(this.sendGetTimeTimestamp);
		logger.info(this.receiveGetTimeTimestamp);
		logger.info("---------------");
		logger.info(this.sendGetGoalTimestamp);
		logger.info(this.receiveGetGoalTimestamp);
		logger.info("---------------");
		logger.info(this.sendGetPointTimestamp);
		logger.info(this.receiveGetPointTimestamp);
		logger.info("---------------");
		logger.info(this.sendGetFileListTimestamp);
		logger.info(this.receiveGetFileListTimestamp);
		logger.info("---------------");
		logger.info(this.sendGetBatteryTimestamp);
		logger.info(this.receiveGetBatteryTimestamp);
		logger.info("---------------");
		logger.info(this.sendSetGoalTimestamp);
		logger.info(this.receiveSetGoalTimestamp);
		logger.info("---------------");
		logger.info(this.sendSetPointTimestamp);
		logger.info(this.receiveSetPointTimestamp);
		logger.info("---------------");
		logger.info(this.sendSetTimeTimestamp);
		logger.info(this.receiveSetTimeTimestamp);
		logger.info("---------------");
		logger.info(this.sendSetDoubleTapsSettingTimestamp);
		logger.info(this.receiveSetDoubleTapsSettingTimestamp);
		logger.info("---------------");
		logger.info(this.sendSetTripleTapsSettingTimestamp);
		logger.info(this.receiveSetTripleTapsSettingTimestamp);
		logger.info("---------------");
		logger.info(this.sendGetDebugFileTimestamp);
		logger.info(this.receiveGetDebugFileTimestamp);
		logger.info("---------------");
		logger.info(this.sendEraseDebugFileTimestamp);
		logger.info(this.receiveEraseDebugFileTimestamp);
		logger.info("---------------");
		logger.info(this.sendGetTimeAfterSyncTimestamp);
		logger.info(this.receiveGetTimeAfterSyncTimestamp);
		logger.info("---------------");
		logger.info(this.sendGetGoalAfterSyncTimestamp);
		logger.info(this.receiveGetGoalAfterSyncTimestamp);
		logger.info("---------------");
		logger.info(this.sendGetPointAfterSyncTimestamp);
		logger.info(this.receiveGetPointAfterSyncTimestamp);
		logger.info("---------------");
		logger.info(this.sendStopAllAnimationTimestamp);
		logger.info(this.receiveStopAllAnimationTimestamp);
		logger.info("---------------");
		logger.info(this.cleanUpConnectionTimestamp);
		logger.info("---------------");
		logger.info(this.isPlayingSyncAnimationOK);
		logger.info(this.isGetTimeOK);
		logger.info(this.isGetPointOK);
		logger.info(this.isGetGoalOK);
		logger.info(this.isGetFileListOK);
		logger.info(this.isGetBatteryOK);
		logger.info(this.isSetTimeOK);
		logger.info(this.isSetGoalOK);
		logger.info(this.isSetPointOK);
		logger.info(this.isSetDoubleTapsOK);
		logger.info(this.isSetTripleTapsOK);
		logger.info(this.isGetDebugFileOK);
		logger.info(this.isEraseDebugFileOK);
		logger.info(this.isGetTimeAfterSyncOK);
		logger.info(this.isGetPointAfterSyncOK);
		logger.info(this.isGetGoalAfterSyncOK);
		logger.info(this.isStopSyncAnimtaionOK);
		logger.info("---------------");
		logger.info(this.appMode);
		logger.info(this.syncMode);
		logger.info(this.batteryLevel);
		logger.info(this.doubleTapsValue);
		logger.info(this.tripleTapsValue);
		logger.info(this.preSyncPoint);
		logger.info(this.syncPoint);
		logger.info(this.postSyncPoint);
		logger.info("---------------");

	}

};
