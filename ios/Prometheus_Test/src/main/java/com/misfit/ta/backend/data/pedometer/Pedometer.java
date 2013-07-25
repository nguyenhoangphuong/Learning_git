package com.misfit.ta.backend.data.pedometer;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class Pedometer {
	private String serialNumberString;
	private String firmwareRevisionString;
	private Long linkedTime;
	private Long unlinkedTime;
	private Long lastSyncedTime;
	private String localId;
	private String serverId;
	private long updatedAt;

	public Pedometer(String serialNumberString, String firmwareRevisionString,
			Long linkedTime, Long unlinkedTime, Long lastSyncedTime,
			String localId, String serverId, long updatedAt) {
		super();
		this.serialNumberString = serialNumberString;
		this.firmwareRevisionString = firmwareRevisionString;
		this.linkedTime = linkedTime;
		this.unlinkedTime = unlinkedTime;
		this.lastSyncedTime = lastSyncedTime;
		this.localId = localId;
		this.serverId = serverId;
		this.updatedAt = updatedAt;
	}

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

	public JSONObject toJson() {
		try {
			JSONObject object = new JSONObject();
			object.accumulate("serverId", serverId);
			object.accumulate("localId", localId);
			object.accumulate("updatedAt", updatedAt);
			object.accumulate("serialNumberString", serialNumberString);
			object.accumulate("firmwareRevisionString", firmwareRevisionString);
			object.accumulate("linkedTime", linkedTime);
			object.accumulate("unlinkedTime", unlinkedTime);
			object.accumulate("lastSyncedTime", lastSyncedTime);
			return object;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Pedometer getPedometer(ServiceResponse response)
			throws JSONException {
		JSONObject responseBody = new JSONObject(response.getResponseString());
		JSONObject obj = responseBody.getJSONObject("pedometer");
		Pedometer pedometer = new Pedometer(
				obj.isNull("serialNumberString") ? null : obj.getString("serialNumberString"),
				obj.isNull("firmwareRevisionString") ? null : obj.getString("firmwareRevisionString"),
				obj.isNull("linkedTime") ? null : obj.getLong("linkedTime"),
				obj.isNull("unlinkedTime") ? null : obj.getLong("unlinkedTime"),
				obj.isNull("lastSyncedTime") ? null : obj
						.getLong("lastSyncedTime"), obj.getString("localId"),
				obj.getString("serverId"), obj.getLong("updatedAt"));
		return pedometer;
	}

	public static String getMessage(ServiceResponse response)
			throws JSONException {
		JSONObject responseBody = new JSONObject(response.getResponseString());
		return responseBody.getString("message");
	}

	public static int getResult(ServiceResponse response) throws JSONException {
		JSONObject responseBody = new JSONObject(response.getResponseString());
		return responseBody.getInt("result");
	}
}
