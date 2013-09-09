package com.misfit.ta.backend.data.sync;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.MVPApi;

public class SyncDebugLog {

	// logger
	private static Logger logger = Util.setupLogger(MVPApi.class);
		
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
	
	
	public SyncDebugLog(String log) {
		
		List<String> list = Arrays.asList(log.split("\n"));
		Collections.reverse(list);
		String[] lines = list.toArray(new String[list.size()]);
		
		boolean getTimeRead = true;
		boolean getGoalRead = true;
		boolean getPointRead = true;
		
		for(int i = 0; i < lines.length; i++) {
			
			if(lines[i].contains("START NEW SYNC SESSION"))
				this.startSyncSessionTimestamp = getTimestamp(lines[i - 1]);
			
			if(lines[i].contains("USER TRIGGERED SYNCING"))
				this.userTriggerSyncTimestamp = getTimestamp(lines[i - 1]);
			
			if(lines[i].contains("SHINE CONNECTED"))
				this.shineConnectedTimestamp = getTimestamp(lines[i - 1]);
			
			if(lines[i].contains("HANDSHAKED WITH SHINE"))
				this.handShakeTimestamp = getTimestamp(lines[i - 1]);
			
			if(lines[i].contains("SEND REQUEST: PLAY SYNCING ANIMATION")) {
				this.sendPlayingSyncAnimationTimestamp = getTimestamp(lines[i - 1]);
				this.receivePlayingSyncAnimationTimestamp = getTimestamp(lines[i + 1]);
			}
					
			if(lines[i].contains("SEND REQUEST: GET TIME")) {
				if(getTimeRead) {
					this.sendGetTimeTimestamp = getTimestamp(lines[i - 1]);
					this.receiveGetTimeTimestamp = getTimestamp(lines[i + 1]);
					getTimeRead = false;
				}
				else {
					this.sendGetTimeAfterSyncTimestamp = getTimestamp(lines[i - 1]);
					this.receiveGetTimeAfterSyncTimestamp = getTimestamp(lines[i + 1]);
				}
			}
			
			if(lines[i].contains("SEND REQUEST: GET GOAL")){
				if(getGoalRead) {
					this.sendGetGoalTimestamp = getTimestamp(lines[i - 1]);
					this.receiveGetGoalTimestamp = getTimestamp(lines[i + 1]);
					getGoalRead = false;
				}
				else {
					this.sendGetGoalAfterSyncTimestamp = getTimestamp(lines[i - 1]);
					this.receiveGetGoalAfterSyncTimestamp = getTimestamp(lines[i + 1]);
				}
			}
			
			if(lines[i].contains("SEND REQUEST: GET POINT")){
				if(getPointRead) {
					this.sendGetPointTimestamp = getTimestamp(lines[i - 1]);
					this.receiveGetPointTimestamp = getTimestamp(lines[i + 1]);
					getPointRead = false;
				}
				else {
					this.sendGetPointAfterSyncTimestamp = getTimestamp(lines[i - 1]);
					this.receiveGetPointAfterSyncTimestamp = getTimestamp(lines[i + 1]);
				}
			}
			
			if(lines[i].contains("SEND REQUEST: GET FILE LIST")) {
				this.sendGetFileListTimestamp = getTimestamp(lines[i - 1]);
				this.receiveGetFileListTimestamp = getTimestamp(lines[i + 1]);
			}
			
			if(lines[i].contains("SEND REQUEST: GET BATTERY")) {
				this.sendGetBatteryTimestamp = getTimestamp(lines[i - 1]);
				this.receiveGetBatteryTimestamp = getTimestamp(lines[i + 1]);
			}
			
			if(lines[i].contains("SEND REQUEST: SET TIME")) {
				this.sendSetTimeTimestamp = getTimestamp(lines[i - 1]);
				this.receiveSetTimeTimestamp = getTimestamp(lines[i + 1]);
			}
			
			if(lines[i].contains("SEND REQUEST: SET GOAL")) {
				this.sendSetGoalTimestamp = getTimestamp(lines[i - 1]);
				this.receiveSetGoalTimestamp = getTimestamp(lines[i + 1]);
			}
			
			if(lines[i].contains("SEND REQUEST: SET POINT")) {
				this.sendSetPointTimestamp = getTimestamp(lines[i - 1]);
				this.receiveSetPointTimestamp = getTimestamp(lines[i + 3]);
			}
			
			if(lines[i].contains("SEND REQUEST: SET CLOCK")) {
				this.sendSetDoubleTapsSettingTimestamp = getTimestamp(lines[i - 1]);
				this.receiveSetDoubleTapsSettingTimestamp = getTimestamp(lines[i + 1]);
			}
			
			if(lines[i].contains("SEND REQUEST: SET TRIPLE TAP")) {
				this.sendSetTripleTapsSettingTimestamp = getTimestamp(lines[i - 1]);
				this.receiveSetTripleTapsSettingTimestamp = getTimestamp(lines[i + 1]);
			}
			
			if(lines[i].contains("SEND REQUEST: GET DEBUG FILE")) {
				this.sendGetDebugFileTimestamp = getTimestamp(lines[i - 1]);
				this.receiveGetDebugFileTimestamp = getTimestamp(lines[i + 1]);
			}
			
			if(lines[i].contains("SEND REQUEST: ERASE DEBUG FILE")) {
				this.sendEraseDebugFileTimestamp = getTimestamp(lines[i - 1]);
				this.receiveEraseDebugFileTimestamp = getTimestamp(lines[i + 1]);
			}
			
			if(lines[i].contains("SEND REQUEST: STOP ALL ANIMATIONS")) {
				this.sendStopAllAnimationTimestamp = getTimestamp(lines[i - 1]);
				this.receiveStopAllAnimationTimestamp = getTimestamp(lines[i + 1]);
			}
			
			if(lines[i].contains("CLEAN UP CONNECTIONS"))
				this.cleanUpConnectionTimestamp = getTimestamp(lines[i]);
		}
	}
	
	private long getTimestamp(String line) {
		
		String[] parts = line.split(":");
		String timestamp = parts[1].trim();
		
		return Long.valueOf(timestamp);
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
		
		
	}
		
}
