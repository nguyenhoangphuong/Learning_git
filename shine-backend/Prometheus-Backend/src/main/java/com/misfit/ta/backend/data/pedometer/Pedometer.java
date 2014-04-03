package com.misfit.ta.backend.data.pedometer;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class Pedometer {
	
	private String serialNumberString;
	private String firmwareRevisionString;
	private Long linkedTime;
	private Long unlinkedTime;
	private Long lastSyncedTime;
	private Long lastSuccessfulTime;
	private Integer clockState;
	private Integer bookmarkState;
	private Integer batteryLevel;
	private String localId;
	private String serverId;
	private Long updatedAt;

	
	// constructors
	public Pedometer() {
		
	}
	
	public Pedometer(String serialNumberString, String firmwareRevisionString, Long linkedTime, Long unlinkedTime, Long lastSyncedTime, String localId, String serverId, Long updatedAt) {
		super();
		this.serialNumberString = serialNumberString;
		this.firmwareRevisionString = firmwareRevisionString;
		this.linkedTime = linkedTime;
		this.unlinkedTime = unlinkedTime;
		this.lastSyncedTime = lastSyncedTime;
		this.localId = localId;
		this.serverId = serverId;
		this.updatedAt = updatedAt;
		this.bookmarkState = 0;
		this.batteryLevel = 100;
		this.clockState = 0;
	}

	
	// getters setters
	public String getSerialNumberString() {
		return serialNumberString;
	}

	public void setSerialNumberString(String serialNumberString) {
		this.serialNumberString = serialNumberString;
	}

	public String getFirmwareRevisionString() {
		return firmwareRevisionString;
	}

	public void setFirmwareRevisionString(String firmwareRevisionString) {
		this.firmwareRevisionString = firmwareRevisionString;
	}

	public Long getLinkedTime() {
		return linkedTime;
	}

	public void setLinkedTime(Long linkedTime) {
		this.linkedTime = linkedTime;
	}

	public Long getUnlinkedTime() {
		return unlinkedTime;
	}

	public void setUnlinkedTime(Long unlinkedTime) {
		this.unlinkedTime = unlinkedTime;
	}

	public Long getLastSyncedTime() {
		return lastSyncedTime;
	}

	public void setLastSyncedTime(Long lastSyncedTime) {
		this.lastSyncedTime = lastSyncedTime;
	}
	
	public Long getLastSuccessfulTime() {
		return lastSuccessfulTime;
	}

	public void setLastSuccessfulTime(Long lastSuccessfulTime) {
		this.lastSuccessfulTime = lastSuccessfulTime;
	}
	
	public Integer getClockState() {
		return clockState;
	}

	public void setClockState(Integer clockState) {
		this.clockState = clockState;
	}

	public Integer getBookmarkState() {
		return bookmarkState;
	}

	public void setBookmarkState(Integer bookmarkState) {
		this.bookmarkState = bookmarkState;
	}

	public Integer getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(Integer batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}

	
	// methods
	public JSONObject toJson() {
		try {
			JSONObject object = new JSONObject();
			object.accumulate("serverId", serverId);
			object.accumulate("localId", localId);
			object.accumulate("updatedAt", updatedAt);
			object.accumulate("serialNumberString", serialNumberString);
			object.accumulate("firmwareRevisionString", firmwareRevisionString);
			object.accumulate("clockState", clockState);
			object.accumulate("bookmarkState", bookmarkState);
			object.accumulate("batteryLevel", batteryLevel);
			object.accumulate("linkedTime", linkedTime);
			object.accumulate("unlinkedTime", unlinkedTime);
			object.accumulate("lastSyncedTime", lastSyncedTime);
			object.accumulate("lastSuccessfulTime", lastSuccessfulTime);
			return object;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public JSONObject toJsonIncludeNull() {
		try {
			JSONObject object = new JSONObject();
			object.accumulate("serverId", serverId != null ? serverId : JSONObject.NULL);
			object.accumulate("localId", localId != null ? localId : JSONObject.NULL);
			object.accumulate("updatedAt", updatedAt != null ? updatedAt : JSONObject.NULL);
			object.accumulate("serialNumberString", serialNumberString != null ? serialNumberString : JSONObject.NULL);
			object.accumulate("firmwareRevisionString", firmwareRevisionString != null ? firmwareRevisionString : JSONObject.NULL);
			object.accumulate("clockState", clockState != null ? clockState : JSONObject.NULL);
			object.accumulate("bookmarkState", bookmarkState != null ? bookmarkState : JSONObject.NULL);
			object.accumulate("batteryLevel", batteryLevel != null ? batteryLevel : JSONObject.NULL);
			object.accumulate("linkedTime", linkedTime != null ? linkedTime : JSONObject.NULL);
			object.accumulate("unlinkedTime", unlinkedTime != null ? unlinkedTime : JSONObject.NULL);
			object.accumulate("lastSyncedTime", lastSyncedTime != null ? lastSyncedTime : JSONObject.NULL);
			object.accumulate("lastSuccessfulTime", lastSuccessfulTime != null ? lastSuccessfulTime : JSONObject.NULL);
			return object;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Pedometer fromJson(JSONObject obj) {
		try {
			Pedometer pedometer = new Pedometer();
			if(!obj.isNull("serialNumberString"))
				pedometer.setSerialNumberString(obj.getString("serialNumberString"));
			if(!obj.isNull("firmwareRevisionString"))
				pedometer.setFirmwareRevisionString(obj.getString("firmwareRevisionString"));
			if(!obj.isNull("linkedTime"))
				pedometer.setLinkedTime(obj.getLong("linkedTime"));
			if(!obj.isNull("unlinkedTime"))
				pedometer.setUnlinkedTime(obj.getLong("unlinkedTime"));
			if(!obj.isNull("lastSyncedTime"))
				pedometer.setLastSyncedTime(obj.getLong("lastSyncedTime"));
			if(!obj.isNull("clockState"))
				pedometer.setClockState(obj.getInt("clockState"));
			if(!obj.isNull("bookmarkState"))
				pedometer.setBookmarkState(obj.getInt("bookmarkState"));
			if(!obj.isNull("batteryLevel"))
				pedometer.setBatteryLevel(obj.getInt("batteryLevel"));
			if(!obj.isNull("localId"))
				pedometer.setLocalId(obj.getString("localId"));
			if(!obj.isNull("serverId"))
				pedometer.setServerId(obj.getString("serverId"));
			if(!obj.isNull("updatedAt"))
				pedometer.setUpdatedAt(obj.getLong("updatedAt"));
			
			return pedometer;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Pedometer getPedometer(ServiceResponse response) {
		try {
			JSONObject responseBody = new JSONObject(response.getResponseString());
			JSONObject obj = responseBody.getJSONObject("pedometer");
			Pedometer pedometer = fromJson(obj);
			
			return pedometer;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getMessage(ServiceResponse response) throws JSONException {
		JSONObject responseBody = new JSONObject(response.getResponseString());
		return responseBody.getString("message");
	}

	public static int getResult(ServiceResponse response) throws JSONException {
		JSONObject responseBody = new JSONObject(response.getResponseString());
		return responseBody.getInt("result");
	}

}
