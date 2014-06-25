package com.misfit.ta.backend.data.goalprogress;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.TimestampObject;

public class GoalSettingsTripleTapTypeChange extends TimestampObject{
	public Integer tripleTapType;

	public GoalSettingsTripleTapTypeChange(Long timestamp,
			Integer tripleTapType) {
		super(timestamp);
		this.tripleTapType = tripleTapType;
	}

	public GoalSettingsTripleTapTypeChange() {
		super();
	}

	// methods
	public JSONObject toJson() {
		try {
			JSONObject object = super.toJson();
			object.accumulate("tripleTapType", this.tripleTapType);
			return object;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public GoalSettingsTripleTapTypeChange fromJson(JSONObject obj) {
		super.fromJson(obj);
		try {
			if (!obj.isNull("tripleTapType")) {
				this.settripleTapType(obj.getInt("tripleTapType"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}

	public Integer gettripleTapType() {
		return tripleTapType;
	}

	public void settripleTapType(Integer tripleTapType) {
		this.tripleTapType = tripleTapType;
	}
}
