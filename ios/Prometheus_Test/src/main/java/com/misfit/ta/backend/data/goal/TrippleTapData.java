package com.misfit.ta.backend.data.goal;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class TrippleTapData {
	private Long timestamp;
	private Integer activityType;

	public TrippleTapData() {
	}

	public TrippleTapData(long timestamp, int activityType) {
		this.timestamp = timestamp;
		this.activityType = activityType;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public JSONObject toJson() {
		try {
			JSONObject object = new JSONObject();

			object.accumulate("timestamp", this.timestamp);
			object.accumulate("activityType", this.activityType);
			return object;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static TrippleTapData fromJson(JSONObject obj) {
		TrippleTapData data = new TrippleTapData();
		try {
			if (!obj.isNull("timestamp"))
				data.setTimestamp(obj.getLong("timestamp"));
			if (!obj.isNull("activityType"))
				data.setActivityType(obj.getInt("activityType"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

}
