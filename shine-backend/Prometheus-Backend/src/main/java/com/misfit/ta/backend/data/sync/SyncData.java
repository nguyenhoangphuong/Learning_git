package com.misfit.ta.backend.data.sync;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class SyncData {

	// fields
	protected Integer syncMode;
	protected String deviceInfo;
	protected String appVersion;
	protected Integer failureReason;
	protected String iosVersion;
	protected String hardwareLog;
	protected String failureLog;
	protected List<SyncFileData> fileData;
	
	
	// methods
    public JSONObject toJson() 
    {
        JSONObject object = new JSONObject();
        try 
        {
            object.accumulate("syncMode", syncMode);
            object.accumulate("deviceInfo", deviceInfo);
            object.accumulate("appVersion", appVersion);
            object.accumulate("failureReason", failureReason);
            object.accumulate("iosVersion", iosVersion);
            object.accumulate("hardwareLog", hardwareLog);
            object.accumulate("failureLog", failureLog);
            
			if (fileData != null) 
			{
				List<JSONObject> arr = new ArrayList<JSONObject>();
				for (int i = 0; i < fileData.size(); i++)
					arr.add(fileData.get(i).toJson());
				
				object.accumulate("fileData", arr);
			}
            
            return object;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }
	
	
	// getters setters
	public Integer getSyncMode() {
		return syncMode;
	}
	
	public void setSyncMode(Integer syncMode) {
		this.syncMode = syncMode;
	}
	
	public String getDeviceInfo() {
		return deviceInfo;
	}
	
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	
	public String getAppVersion() {
		return appVersion;
	}
	
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	public Integer getFailureReason() {
		return failureReason;
	}
	
	public void setFailureReason(Integer failureReason) {
		this.failureReason = failureReason;
	}
	
	public String getIosVersion() {
		return iosVersion;
	}
	
	public void setIosVersion(String iosVersion) {
		this.iosVersion = iosVersion;
	}
	
	public String getHardwareLog() {
		return hardwareLog;
	}
	
	public void setHardwareLog(String hardwareLog) {
		this.hardwareLog = hardwareLog;
	}
	
	public String getFailureLog() {
		return failureLog;
	}
	
	public void setFailureLog(String failureLog) {
		this.failureLog = failureLog;
	}
	
	public List<SyncFileData> getFileData() {
		return fileData;
	}
	
	public void setFileData(List<SyncFileData> fileData) {
		this.fileData = fileData;
	}
	
}
