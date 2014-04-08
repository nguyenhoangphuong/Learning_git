package com.misfit.ta.backend.data.goalprogress.sleep;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.goalprogress.GoalSettings;

public class SleepGoalSettings extends GoalSettings {

	// fields
	protected SleepGoalSettingsOtherSettings others;

	
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

	public SleepGoalSettings fromJson(JSONObject objJson) {
		
		try {
			
			super.fromJson(objJson);
			
			if (!objJson.isNull("others")) {
				this.others = new SleepGoalSettingsOtherSettings();
				this.others.fromJson(objJson.getJSONObject("others"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	public static SleepGoalSettings fromResponse(ServiceResponse response) {
	
		try {
			JSONObject responseBody = new JSONObject(response.getResponseString());
			JSONObject json = responseBody.getJSONObject("goal_settings");

			SleepGoalSettings settings = new SleepGoalSettings();
			settings.fromJson(json);

			return settings;
		}
		catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public static SleepGoalSettings getDefaultSleepGoalSettings(long appliedFrom) {
		
		SleepGoalSettings settings = new SleepGoalSettings();
		settings.setAppliedFrom(appliedFrom);
		settings.setGoalValue(8 * 60);
		settings.setOthers(new SleepGoalSettingsOtherSettings());
		
		return settings;
	}
	
	
	// getters setters
	public SleepGoalSettingsOtherSettings getOthers() {
		return others;
	}

	public void setOthers(SleepGoalSettingsOtherSettings others) {
		this.others = others;
	}
	
}