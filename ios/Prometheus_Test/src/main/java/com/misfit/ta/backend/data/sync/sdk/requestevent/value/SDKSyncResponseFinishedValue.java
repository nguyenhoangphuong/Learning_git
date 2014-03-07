package com.misfit.ta.backend.data.sync.sdk.requestevent.value;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class SDKSyncResponseFinishedValue {

	protected Long timestamp;
	protected String activityData;
	protected String handle;
	protected Integer status;
	protected Integer timezoneOffsetInMinutes;
	protected Integer length;
	protected Integer miliseconds;
	protected String fileFormat;
	protected String fileHandle;

	
	// json
	public Object toJson() {
		
		try {
			JSONObject obj = new JSONObject();
			obj.accumulate("timestamp", timestamp);
			obj.accumulate("activityData", activityData);
			obj.accumulate("handle", handle);
			obj.accumulate("status", status);
			obj.accumulate("timezoneOffsetInMinutes", timezoneOffsetInMinutes);
			obj.accumulate("length", length);
			obj.accumulate("miliseconds", miliseconds);
			obj.accumulate("fileFormat", fileFormat);
			obj.accumulate("fileHandle", fileHandle);
			
			return obj;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public SDKSyncResponseFinishedValue fromJson(JSONObject json) {
		
		try {
			if(!json.isNull("timestamp"))
				this.timestamp = json.getLong("timestamp");
			
			if(!json.isNull("activityData"))
				this.activityData = json.getString("activityData");
			
			if(!json.isNull("handle"))
				this.handle = json.getString("handle");
			
			if(!json.isNull("status"))
				this.status = json.getInt("status");
			
			if(!json.isNull("timezoneOffsetInMinutes"))
				this.timezoneOffsetInMinutes = json.getInt("timezoneOffsetInMinutes");
			
			if(!json.isNull("length"))
				this.length = json.getInt("length");
			
			if(!json.isNull("miliseconds"))
				this.miliseconds = json.getInt("miliseconds");
			
			if(!json.isNull("fileFormat"))
				this.fileFormat = json.getString("fileFormat");
			
			if(!json.isNull("fileHandle"))
				this.fileHandle = json.getString("fileHandle");

			return this;
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	// getters setters
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getActivityData() {
		return activityData;
	}

	public void setActivityData(String activityData) {
		this.activityData = activityData;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTimezoneOffsetInMinutes() {
		return timezoneOffsetInMinutes;
	}

	public void setTimezoneOffsetInMinutes(Integer timezoneOffsetInMinutes) {
		this.timezoneOffsetInMinutes = timezoneOffsetInMinutes;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getMiliseconds() {
		return miliseconds;
	}

	public void setMiliseconds(Integer miliseconds) {
		this.miliseconds = miliseconds;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public String getFileHandle() {
		return fileHandle;
	}

	public void setFileHandle(String fileHandle) {
		this.fileHandle = fileHandle;
	}
}
