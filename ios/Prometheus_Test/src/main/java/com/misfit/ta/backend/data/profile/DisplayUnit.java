package com.misfit.ta.backend.data.profile;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class DisplayUnit {

	// fields
	private Integer weightUnit;
	private Integer distanceUnit;
	private Integer temperatureScale;

	// constructors
	public DisplayUnit() {
		this.weightUnit = 0;
		this.distanceUnit = 0;
		this.temperatureScale = 0;
	}

	public DisplayUnit(int weightUnit, int heightUnit, int temperatureScale) {
		this.weightUnit = weightUnit;
		this.distanceUnit = heightUnit;
		this.temperatureScale = temperatureScale;
	}

	// methods
	public JSONObject toJson() {
		try {
			JSONObject obj = new JSONObject();
			obj.accumulate("weightUnit", weightUnit);
			obj.accumulate("distanceUnit", distanceUnit);
			obj.accumulate("temperatureScale", temperatureScale);

			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DisplayUnit fromJson(JSONObject json) {
		DisplayUnit obj = new DisplayUnit();
		try {
			
			if(!json.isNull("weightUnit"))
				obj.setWeightUnit(json.getInt("weightUnit"));
			
			if(!json.isNull("distanceUnit"))
				obj.setDistanceUnit(json.getInt("distanceUnit"));
			
			if(!json.isNull("temperatureScale"))
				obj.setTemperatureScale(json.getInt("temperatureScale"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}

	// getters setters
	public int getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(int weightUnit) {
		this.weightUnit = weightUnit;
	}

	public int getDistanceUnit() {
		return distanceUnit;
	}

	public void setDistanceUnit(int distanceUnit) {
		this.distanceUnit = distanceUnit;
	}

	public int getTemperatureScale() {
		return temperatureScale;
	}

	public void setTemperatureScale(int temperatureScale) {
		this.temperatureScale = temperatureScale;
	}
}
