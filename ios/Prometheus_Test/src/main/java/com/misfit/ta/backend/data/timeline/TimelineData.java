package com.misfit.ta.backend.data.timeline;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class TimelineData extends TimelineItemBase {

	private JSONObject data;

	public TimelineData() {
		data = new JSONObject();
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public void addValue(String key, Object value) {
		try {
			data.accumulate(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void removeValue(String key) {
		data.remove(key);
	}

	public Object getValue(String key) {
		try {
			return data.get(key);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public JSONObject toJson() {
		return data;
	}

}
