package com.misfit.ta.backend.data.goalprogress.weight;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class WeightGoalSettingsOtherSettings {

	// fields
	protected Integer trendType;

	
	// methods
	public JSONObject toJson() {
		
		try {
			
			JSONObject object = new JSONObject();
			object.accumulate("trendType", trendType);
			
			return object;
			
		} catch (JSONException e) {
			e.printStackTrace();

			return null;
		}
	}

	public WeightGoalSettingsOtherSettings fromJson(JSONObject objJson) {

		try {

			if (!objJson.isNull("trendType"))
				this.setTrendType(objJson.getInt("trendType"));

			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	
	// getters setters
	public Integer getTrendType() {
		return trendType;
	}

	public void setTrendType(Integer trendType) {
		this.trendType = trendType;
	}
	
}