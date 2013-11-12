package com.misfit.ta.backend.data.profile;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class PersonalRecord {

	// fields
	Double personalBestRecordsInPoint;

	// constructors
	public PersonalRecord() {

	}

	public PersonalRecord(double personalBestRecordsInPoint) {
		this.personalBestRecordsInPoint = personalBestRecordsInPoint;
	}

	// methods
	public JSONObject toJson() {
		try {
			JSONObject obj = new JSONObject();
			obj.accumulate("personalBestRecordsInPoint", personalBestRecordsInPoint);

			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static PersonalRecord fromJson(JSONObject json) {
		PersonalRecord obj = new PersonalRecord();
		try {
			if (!json.isNull("localId"))
				obj.setPersonalBestRecordsInPoint(json.getDouble("personalBestRecordsInPoint"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}

	// getters setters
	public Double getPersonalBestRecordsInPoint() {
		return personalBestRecordsInPoint;
	}

	public void setPersonalBestRecordsInPoint(Double personalBestRecordsInPoint) {
		this.personalBestRecordsInPoint = personalBestRecordsInPoint;
	}

}
