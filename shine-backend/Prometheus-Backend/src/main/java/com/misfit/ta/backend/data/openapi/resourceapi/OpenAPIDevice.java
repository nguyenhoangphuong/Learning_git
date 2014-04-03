package com.misfit.ta.backend.data.openapi.resourceapi;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class OpenAPIDevice {

	// fields
	protected String deviceType;
	protected String serialNumber;
	protected String firmwareVersion;
	protected Integer batteryLevel;
	
	
	// methods
	public JSONObject toJson() {
		try {
			JSONObject object = new JSONObject();

			object.accumulate("deviceType", deviceType);
			object.accumulate("serialNumber", serialNumber);
			object.accumulate("firmwareVersion", firmwareVersion);
			object.accumulate("batteryLevel", batteryLevel);

			return object;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public OpenAPIDevice fromJson(JSONObject json) {
		OpenAPIDevice obj = this;
		try {
			if (!json.isNull("deviceType"))
				obj.setDeviceType(json.getString("deviceType"));

			if (!json.isNull("serialNumber"))
				obj.setSerialNumber(json.getString("serialNumber"));
			
			if (!json.isNull("firmwareVersion"))
				obj.setFirmwareVersion(json.getString("firmwareVersion"));
			
			if (!json.isNull("batteryLevel"))
				obj.setBatteryLevel(json.getInt("batteryLevel"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}

	public static OpenAPIDevice fromResponse(ServiceResponse response) {
		try {
			JSONObject jsonResponse = new JSONObject(response.getResponseString());
			
			OpenAPIDevice profile = new OpenAPIDevice();
			return profile.fromJson(jsonResponse);
			
		} catch (JSONException e) {
			return null;
		}
	}


	// getters setters
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
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

	public Integer getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(Integer batteryLevel) {
		this.batteryLevel = batteryLevel;
	}	

}
