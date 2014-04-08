package com.misfit.ta.backend.data.goalprogress;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class GoalSettings {

	public static final int GOAL_SETTINGS_TYPE_SLEEP = 2;
	public static final int GOAL_SETTINGS_TYPE_WEIGHT = 3;
	
	
	// fields
	protected Long appliedFrom;
	protected Integer goalValue;

	
	// methods
	public JSONObject toJson() {
		try {
			
			JSONObject object = new JSONObject();
			
			object.accumulate("appliedFrom", appliedFrom);
			object.accumulate("goalValue", goalValue);
			
			return object;
			
		} catch (JSONException e) {
			e.printStackTrace();

			return null;
		}
	}

	public GoalSettings fromJson(JSONObject objJson) {
		
		GoalSettings goal = this;
		try {
			
			if (!objJson.isNull("appliedFrom"))
				goal.setAppliedFrom(objJson.getLong("appliedFrom"));

			if (!objJson.isNull("goalValue"))
				goal.setGoalValue(objJson.getInt("goalValue"));

			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return goal;
	}

	
	// getters setters
	public Long getAppliedFrom() {
		return appliedFrom;
	}

	public void setAppliedFrom(Long appliedFrom) {
		this.appliedFrom = appliedFrom;
	}

	public Integer getGoalValue() {
		return goalValue;
	}

	public void setGoalValue(Integer goalValue) {
		this.goalValue = goalValue;
	}
	
}