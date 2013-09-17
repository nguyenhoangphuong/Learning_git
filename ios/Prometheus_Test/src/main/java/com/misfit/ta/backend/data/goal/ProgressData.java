package com.misfit.ta.backend.data.goal;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class ProgressData {

	private Integer seconds;
	private Integer steps;
	private Integer fullBmrCalorie;
	private Double points;
	private Double distanceMiles;

	public ProgressData() {

	}

	public ProgressData(int seconds, int steps, int fullBmrCalorie, double points) {
		this.seconds = seconds;
		this.steps = steps;
		this.fullBmrCalorie = fullBmrCalorie;
		this.points = points;
	}

	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public int getFullBmrCalorie() {
		return this.fullBmrCalorie;
	}

	public void setFullBmrCalorie(int bmr) {
		this.fullBmrCalorie = bmr;
	}
	
	public Double getDistanceMiles() {
		return distanceMiles;
	}

	public void setDistanceMiles(Double distanceMiles) {
		this.distanceMiles = distanceMiles;
	}

	public JSONObject toJson() {
		try {
			JSONObject object = new JSONObject();

			object.accumulate("points", this.points);
			object.accumulate("steps", this.steps);
			object.accumulate("seconds", this.seconds);
			object.accumulate("fullBmrCalorie", this.fullBmrCalorie);
			object.accumulate("distanceMiles", this.distanceMiles);
			return object;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ProgressData fromJson(JSONObject obj) {
		ProgressData data = new ProgressData();
		try {
			if(!obj.isNull("points"))
				data.setPoints(obj.getDouble("points"));
			
			if(!obj.isNull("seconds"))
				data.setSeconds(obj.getInt("seconds"));
			
			if(!obj.isNull("steps"))
				data.setSteps(obj.getInt("steps"));
			
			if(!obj.isNull("fullBmrCalorie"))
				data.setFullBmrCalorie(obj.getInt("fullBmrCalorie"));
			
			if(!obj.isNull("distanceMiles"))
				data.setDistanceMiles(obj.getDouble("distanceMiles"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}
}
