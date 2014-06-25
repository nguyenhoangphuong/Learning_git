package com.misfit.ta.backend.data.goalprogress;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.TimestampObject;

public class GoalSettingsAutoSleepStateChange extends TimestampObject {
	public Integer autosleepState;

	public GoalSettingsAutoSleepStateChange(Long timestamp,
			Integer autosleepState) {
		super(timestamp);
		this.autosleepState = autosleepState;
	}

	public GoalSettingsAutoSleepStateChange() {
		super();
	}

	// methods
	public JSONObject toJson() {
		try {
			JSONObject object = super.toJson();
			object.accumulate("autosleepState", this.autosleepState);
			return object;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public GoalSettingsAutoSleepStateChange fromJson(JSONObject obj) {
		super.fromJson(obj);
		try {
			if (!obj.isNull("autosleepState")) {
				this.setautosleepState(obj.getInt("autosleepState"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}

	public Integer getautosleepState() {
		return autosleepState;
	}

	public void setautosleepState(Integer autosleepState) {
		this.autosleepState = autosleepState;
	}
}
