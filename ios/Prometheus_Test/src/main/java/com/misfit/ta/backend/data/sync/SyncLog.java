package com.misfit.ta.backend.data.sync;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class SyncLog {

	// fields
	protected Long startTime;
	protected Long endTime;
	protected String firmwareRevisionString;
	protected Integer isSuccessful;
	protected String serialNumberString;
	protected String log;
	protected SyncData data;
	
	
	// methods
    public JSONObject toJson() 
    {
        JSONObject object = new JSONObject();
        try 
        {
            object.accumulate("startTime", startTime);
            object.accumulate("endTime", endTime);
            object.accumulate("firmwareRevisionString", firmwareRevisionString);
            object.accumulate("isSuccessful", isSuccessful);
            object.accumulate("serialNumberString", serialNumberString);
            object.accumulate("log", log);
            
			if (data != null) 			
				object.accumulate("xdata", data.toJson());
            
            return object;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }
	
	
	// getters setters
	public Long getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	
	public Long getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	
	public String getFirmwareRevisionString() {
		return firmwareRevisionString;
	}
	
	public void setFirmwareRevisionString(String firmwareRevisionString) {
		this.firmwareRevisionString = firmwareRevisionString;
	}
	
	public Integer getIsSuccessful() {
		return isSuccessful;
	}
	
	public void setIsSuccessful(Integer isSuccessful) {
		this.isSuccessful = isSuccessful;
	}
	
	public String getSerialNumberString() {
		return serialNumberString;
	}
	
	public void setSerialNumberString(String serialNumberString) {
		this.serialNumberString = serialNumberString;
	}
	
	public String getLog() {
		return log;
	}
	
	public void setLog(String log) {
		this.log = log;
	}
	
	public SyncData getData() {
		return data;
	}
	
	public void setData(SyncData data) {
		this.data = data;
	}
		
}
