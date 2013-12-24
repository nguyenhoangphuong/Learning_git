package com.misfit.ta.backend.data.goal;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class ProgressData {

	// fields
	private Integer seconds;
	private Integer steps;
	private Double calorie;
	private Integer fullBmrCalorie;
	private Double points;
	private Double distanceMiles;

	
	// constructor
	public ProgressData() {
	}

	public ProgressData(int seconds, int steps, int fullBmrCalorie, double points) {
		this.seconds = seconds;
		this.steps = steps;
		this.fullBmrCalorie = fullBmrCalorie;
		this.points = points;
	}

	public static ProgressData getDefaultProgressData() {
		
		ProgressData progress = new ProgressData();
		progress.setSeconds(0);
		progress.setSteps(0);
		progress.setFullBmrCalorie(0);
		progress.setCalorie(0d);
		progress.setPoints(0d);
		progress.setDistanceMiles(0d);
		
		return progress;
	}
	
	
	// getters setters
	public Integer getSeconds() {
		return seconds;
	}

	public void setSeconds(Integer seconds) {
		this.seconds = seconds;
	}

	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	public Double getCalorie() {
		return calorie;
	}

	public void setCalorie(Double calorie) {
		this.calorie = calorie;
	}

	public Integer getFullBmrCalorie() {
		return fullBmrCalorie;
	}

	public void setFullBmrCalorie(Integer fullBmrCalorie) {
		this.fullBmrCalorie = fullBmrCalorie;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public Double getDistanceMiles() {
		return distanceMiles;
	}

	public void setDistanceMiles(Double distanceMiles) {
		this.distanceMiles = distanceMiles;
	}

	
	// methods
	public JSONObject toJson() {
		try {
			JSONObject object = new JSONObject();

			object.accumulate("points", this.points);
			object.accumulate("steps", this.steps);
			object.accumulate("seconds", this.seconds);
			object.accumulate("fullBmrCalorie", this.fullBmrCalorie);
			object.accumulate("distanceMiles", this.distanceMiles);
			object.accumulate("calorie", this.calorie);
			
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
			
			if(!obj.isNull("calorie"))
				data.setCalorie(obj.getDouble("calorie"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}
}
