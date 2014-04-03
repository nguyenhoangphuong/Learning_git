package com.misfit.ta.backend.data.sync.sdk.requestevent;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;


public class SDKSyncRequestFinishedEvent {

	protected Long timestamp;
	protected Integer result;
	
	
	// json
	public JSONObject toJson() {

		try {
			JSONObject obj = new JSONObject();
			obj.accumulate("timestamp", timestamp);
			obj.accumulate("result", result);
			return obj;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public SDKSyncRequestFinishedEvent fromJson(JSONObject json) {

		try {
			if(!json.isNull("timestamp"))
				this.timestamp = json.getLong("timestamp");
			
			if(!json.isNull("result"))
				this.result = json.getInt("result");

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
	
	public Integer getResult() {
		return result;
	}
	
	public void setResult(Integer result) {
		this.result = result;
	}	
}
