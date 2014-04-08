package com.misfit.ta.backend.data.goalprogress.weight;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.goalprogress.GoalSettings;

public class WeightGoalSettings extends GoalSettings {

	// fields
	protected WeightGoalSettingsOtherSettings others;
	public static final int TREND_TYPE_LOSE_WEIGHT = 1;
	public static final int TREND_TYPE_GAIN_WEIGHT = 2;

	
	// methods
	public JSONObject toJson() {
		
		try {
			
			JSONObject object = super.toJson();
			
			if(others != null)
				object.accumulate("others", others.toJson());
			
			return object;
			
		} catch (JSONException e) {
			e.printStackTrace();

			return null;
		}
	}

	public WeightGoalSettings fromJson(JSONObject objJson) {
		
		try {
			
			super.fromJson(objJson);
			
			if (!objJson.isNull("others")) {
				this.others = new WeightGoalSettingsOtherSettings();
				this.others.fromJson(objJson.getJSONObject("others"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	public static WeightGoalSettings fromResponse(ServiceResponse response) {
		
		try {
			JSONObject responseBody = new JSONObject(response.getResponseString());
			JSONObject json = responseBody.getJSONObject("goal_settings");

			WeightGoalSettings settings = new WeightGoalSettings();
			settings.fromJson(json);

			return settings;
		}
		catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public static WeightGoalSettings getDefaultWeightGoalSettings() {
		
		WeightGoalSettings settings = new WeightGoalSettings();
		settings.setGoalValue(120);
		settings.setOthers(new WeightGoalSettingsOtherSettings());
		settings.getOthers().setTrendType(TREND_TYPE_LOSE_WEIGHT);
		
		return settings;
	}
	
	
	// getters setters
	public WeightGoalSettingsOtherSettings getOthers() {
		return others;
	}

	public void setOthers(WeightGoalSettingsOtherSettings others) {
		this.others = others;
	}
	
}