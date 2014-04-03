package com.misfit.ta.backend.data.sync.sdk.requestevent;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.sync.sdk.requestevent.value.SDKSyncResponseStartedValue;


public class SDKSyncResponseStartedEvent {

	protected SDKSyncResponseStartedValue value;
	protected Long timestamp;
	protected Integer result;
	
	
	// json
	public JSONObject toJson() {

		try {
			JSONObject obj = new JSONObject();
			
			if(value != null)
				obj.accumulate("value", value.toJson());
			obj.accumulate("timestamp", timestamp);
			obj.accumulate("result", result);
			
			return obj;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public SDKSyncResponseStartedEvent fromJson(JSONObject json) {

		try {
			if(!json.isNull("value")) {
				this.value = new SDKSyncResponseStartedValue();
				this.value.fromJson(json.getJSONObject("value"));
			}
				
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
	public SDKSyncResponseStartedValue getValue() {
		return value;
	}
	
	public void setValue(SDKSyncResponseStartedValue value) {
		this.value = value;
	}
	
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
