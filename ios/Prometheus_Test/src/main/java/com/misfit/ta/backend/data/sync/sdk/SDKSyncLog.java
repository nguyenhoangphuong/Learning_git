package com.misfit.ta.backend.data.sync.sdk;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class SDKSyncLog {

	protected String platform;
	protected String systemVersion;
	protected String deviceModel;
	protected String sdkVersion;
	protected String userId;
	protected String serialNumber;
	protected String firmwareVersion;
	protected Long startAt;
	protected Long endAt;
	protected List<SDKSyncEvent> events;


	// json
	public JSONObject toJson() {

		try {
			JSONObject obj = new JSONObject();

			obj.accumulate("platform", platform);
			obj.accumulate("system_version", systemVersion);
			obj.accumulate("device_model", deviceModel);
			obj.accumulate("sdk_version", sdkVersion);
			obj.accumulate("user_id", userId);
			obj.accumulate("serial_number", serialNumber);
			obj.accumulate("firmware_version", firmwareVersion);
			obj.accumulate("start_at", startAt);
			obj.accumulate("end_at", endAt);

			if(events != null) {
				JSONArray eventsJson = new JSONArray();
				for(SDKSyncEvent event : events)
					eventsJson.put(event.toJson());
				
				obj.accumulate("events", eventsJson);
			}

			return obj;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public SDKSyncLog fromJson(JSONObject json) {

		try {
			if(!json.isNull("platform")) 
				this.platform = json.getString("platform");

			if(!json.isNull("system_version")) 
				this.systemVersion = json.getString("system_version");

			if(!json.isNull("device_model")) 
				this.deviceModel = json.getString("device_model");

			if(!json.isNull("sdk_version")) 
				this.sdkVersion = json.getString("sdk_version");

			if(!json.isNull("user_id")) 
				this.userId = json.getString("user_id");

			if(!json.isNull("serial_number")) 
				this.serialNumber = json.getString("serial_number");

			if(!json.isNull("firmware_version")) 
				this.firmwareVersion = json.getString("firmware_version");

			if(!json.isNull("start_at")) 
				this.startAt = json.getLong("start_at");

			if(!json.isNull("end_at")) 
				this.endAt = json.getLong("end_at");

			if(!json.isNull("events")) {

				JSONArray jsonArr = json.getJSONArray("events");
				this.events = new ArrayList<SDKSyncEvent>();

				for(int i = 0; i < jsonArr.length(); i++) {

					SDKSyncEvent event = new SDKSyncEvent();
					event.fromJson(jsonArr.getJSONObject(i));
					this.events.add(event);
				}
			}

			return this;
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}


	// getters setters
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getSdkVersion() {
		return sdkVersion;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public Long getStartAt() {
		return startAt;
	}

	public void setStartAt(Long startAt) {
		this.startAt = startAt;
	}

	public Long getEndAt() {
		return endAt;
	}

	public void setEndAt(Long endAt) {
		this.endAt = endAt;
	}

	public List<SDKSyncEvent> getEvents() {
		return events;
	}

	public void setEvents(List<SDKSyncEvent> events) {
		this.events = events;
	}
}
