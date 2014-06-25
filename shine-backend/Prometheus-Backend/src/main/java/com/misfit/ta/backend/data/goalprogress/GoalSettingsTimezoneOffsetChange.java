package com.misfit.ta.backend.data.goalprogress;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.TimestampObject;

public class GoalSettingsTimezoneOffsetChange extends TimestampObject {
	public Integer timeZoneOffsetInSeconds;

	public GoalSettingsTimezoneOffsetChange(Long timestamp,
			Integer timeZoneOffsetInSeconds) {
		super(timestamp);
		this.timeZoneOffsetInSeconds = timeZoneOffsetInSeconds;
	}

	public GoalSettingsTimezoneOffsetChange() {
		super();
	}

	// methods
	public JSONObject toJson() {
		try {
			JSONObject object = super.toJson();
			object.accumulate("timeZoneOffsetInSeconds", this.timeZoneOffsetInSeconds);
			return object;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public GoalSettingsTimezoneOffsetChange fromJson(JSONObject obj) {
		super.fromJson(obj);
		try {
			if (!obj.isNull("timeZoneOffsetInSeconds")) {
				this.setTimeZoneOffsetInSeconds(obj.getInt("timeZoneOffsetInSeconds"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}

	public Integer getTimeZoneOffsetInSeconds() {
		return timeZoneOffsetInSeconds;
	}

	public void setTimeZoneOffsetInSeconds(Integer timeZoneOffsetInSeconds) {
		this.timeZoneOffsetInSeconds = timeZoneOffsetInSeconds;
	}
}
