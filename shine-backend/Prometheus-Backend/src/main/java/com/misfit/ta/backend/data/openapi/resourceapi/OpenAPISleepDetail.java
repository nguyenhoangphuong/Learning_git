package com.misfit.ta.backend.data.openapi.resourceapi;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class OpenAPISleepDetail {

	// fields
	protected String datetime;
	protected Integer value;
	
	
	// methods
	public JSONObject toJson() {
		try {
			JSONObject object = new JSONObject();

			object.accumulate("datetime", datetime);
			object.accumulate("value", value);
			
			return object;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public OpenAPISleepDetail fromJson(JSONObject json) {
		OpenAPISleepDetail obj = this;
		try {
			if (!json.isNull("datetime"))
				obj.setDatetime(json.getString("datetime"));

			if (!json.isNull("value"))
				obj.setValue(json.getInt("value"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}


	// getters setters
	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
