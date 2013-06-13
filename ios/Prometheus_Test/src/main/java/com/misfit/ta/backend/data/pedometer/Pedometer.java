package com.misfit.ta.backend.data.pedometer;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class Pedometer {
	private String serialNumberString;
	private String firmwareRevisionString;
	private Integer linkedTime;
	private Integer unlinkedTime;
	private Integer lastSyncedTime;
	private boolean current;
	private String localId;
	private String serverId;
	private long updatedAt;
    private long timestamp;
    
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
	public Integer getLinkedTime() {
		return linkedTime;
	}
	public void setLinkedTime(Integer linkedTime) {
		this.linkedTime = linkedTime;
	}
	public Integer getUnlinkedTime() {
		return unlinkedTime;
	}
	public void setUnlinkedTime(Integer unlinkedTime) {
		this.unlinkedTime = unlinkedTime;
	}
	public Integer getLastSyncedTime() {
		return lastSyncedTime;
	}
	public void setLastSyncedTime(Integer lastSyncedTime) {
		this.lastSyncedTime = lastSyncedTime;
	}
	public boolean isCurrent() {
		return current;
	}
	public void setCurrent(boolean current) {
		this.current = current;
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
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public JSONObject toJson() {
        try {
        	JSONObject object = new JSONObject();
        	
            object.accumulate("timestamp", timestamp);
            object.accumulate("serverId", serverId);
            object.accumulate("localId", localId);
            object.accumulate("updatedAt", updatedAt);
            object.accumulate("isCurrent", current);
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
}
