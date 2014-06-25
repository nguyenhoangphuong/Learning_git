package com.misfit.ta.backend.data.goalprogress;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.TimestampObject;

public class GoalSettingsGoalValueChange extends TimestampObject {
	public Double goalValue;

	public GoalSettingsGoalValueChange(Long timestamp,
			Double goalValue) {
		super(timestamp);
		this.goalValue = goalValue;
	}

	public GoalSettingsGoalValueChange() {
		super();
	}

	// methods
	public JSONObject toJson() {
		try {
			JSONObject object = super.toJson();
			object.accumulate("goalValue", this.goalValue);
			return object;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public GoalSettingsGoalValueChange fromJson(JSONObject obj) {
		super.fromJson(obj);
		try {
			if (!obj.isNull("goalValue")) {
				this.setgoalValue(obj.getDouble("goalValue"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}

	public Double getgoalValue() {
		return goalValue;
	}

	public void setgoalValue(Double goalValue) {
		this.goalValue = goalValue;
	}
}
