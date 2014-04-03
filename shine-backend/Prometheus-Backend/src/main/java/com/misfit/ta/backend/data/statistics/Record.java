package com.misfit.ta.backend.data.statistics;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class Record {

	// fields
	protected Double point;
	protected Long timestamp;


	// constructors
	public Record() {
	}


	// methods
	public JSONObject toJson() {
		try {
			
			JSONObject obj = new JSONObject();
			
			obj.accumulate("point", point);
			obj.accumulate("timestamp", timestamp);
			
			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Record fromJson(JSONObject json) {
		Record obj = new Record();
		try {
			
			if (!json.isNull("point"))
				obj.setPoint(json.getDouble("point"));
			
			if (!json.isNull("timestamp"))
				obj.setTimestamp(json.getLong("timestamp"));
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return obj;
	}

	// getters setters
	public Double getPoint() {
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
