package com.misfit.ta.backend.data;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.goal.ProgressData;

public class TimestampObject {
	protected Long timestamp;

	public TimestampObject() {

	}

	public TimestampObject(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	// methods
	public JSONObject toJson() {
		try {
			JSONObject object = new JSONObject();
			object.accumulate("timestamp", this.timestamp);

			return object;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public TimestampObject fromJson(JSONObject obj) {
		TimestampObject data = this;
		try {
			if(!obj.isNull("timestamp")) {
				data.setTimestamp(obj.getLong("timestamp"));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

}
