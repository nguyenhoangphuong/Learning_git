package com.misfit.ta.backend.data.sync.sdk.requestevent;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;


public class SDKSyncRequestStartedEvent {

	protected Long timestamp;

	
	// json
	public JSONObject toJson() {
		
		try {
			JSONObject obj = new JSONObject();
			obj.accumulate("timestamp", timestamp);
			return obj;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public SDKSyncRequestStartedEvent fromJson(JSONObject json) {
		
		try {
			if(!json.isNull("timestamp"))
				this.timestamp = json.getLong("timestamp");

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
}
