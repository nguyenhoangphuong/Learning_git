package com.misfit.ta.backend.data.goalprogress;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class GoalProgress {

	public static final int GOAL_PROGRESS_TYPE_SLEEP = 2;
	public static final int GOAL_PROGRESS_TYPE_WEIGHT = 3;
	
	
	// fields
	protected String serverId;
	protected String localId;
	protected Long updatedAt;
	
	protected Long timestamp;
	protected Integer duration;
	protected Integer progressValue;

	
	// methods
	public JSONObject toJson() {
		try {
			
			JSONObject object = new JSONObject();
			
			object.accumulate("localId", localId);
			object.accumulate("serverId", serverId);
			object.accumulate("updatedAt", updatedAt);
			
			object.accumulate("timestamp", timestamp);
			object.accumulate("duration", duration);
			object.accumulate("progressValue", progressValue);
			
			return object;
			
		} catch (JSONException e) {
			e.printStackTrace();

			return null;
		}
	}

	public GoalProgress fromJson(JSONObject objJson) {
		
		try {
			
			if (!objJson.isNull("serverId"))
				this.setServerId(objJson.getString("serverId"));

			if (!objJson.isNull("localId"))
				this.setLocalId(objJson.getString("localId"));

			if (!objJson.isNull("updatedAt"))
				this.setUpdatedAt(objJson.getLong("updatedAt"));

			if (!objJson.isNull("timestamp"))
				this.setTimestamp(objJson.getLong("timestamp"));

			if (!objJson.isNull("duration"))
				this.setDuration(objJson.getInt("duration"));

			if (!objJson.isNull("progressValue"))
				this.setProgressValue(objJson.getInt("progressValue"));

			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}
	
	
	// getters setters
	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getProgressValue() {
		return progressValue;
	}

	public void setProgressValue(Integer progressValue) {
		this.progressValue = progressValue;
	}

}
