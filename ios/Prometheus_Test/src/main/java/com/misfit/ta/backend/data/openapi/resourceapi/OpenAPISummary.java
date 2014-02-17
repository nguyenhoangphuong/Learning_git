package com.misfit.ta.backend.data.openapi.resourceapi;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class OpenAPISummary {

	
	// fields
	private Double points;
	private Integer steps;
	private Double calories;
	private Double distance;
	
	
	// methods
	public JSONObject toJson() {
		
		try {
			JSONObject object = new JSONObject();
			
			object.accumulate("points", points);
			object.accumulate("steps", steps);
			object.accumulate("calories", calories);
			object.accumulate("distance", distance);
			return object;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public OpenAPISummary fromJson(JSONObject objJson) {
		
		try {
			
			if (!objJson.isNull("points"))
				this.setPoints(objJson.getDouble("points"));
			
			if (!objJson.isNull("steps"))
				this.setSteps(objJson.getInt("steps"));
			
			if (!objJson.isNull("calories"))
				this.setCalories(objJson.getDouble("calories"));
			
			if (!objJson.isNull("distance"))
				this.setDistance(objJson.getDouble("distance"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	public static OpenAPISummary getSummary(ServiceResponse response) {
		
		try {
			JSONObject jsonResponse = new JSONObject(response.getResponseString());
			
			OpenAPISummary summary = new OpenAPISummary();
			return summary.fromJson(jsonResponse);
			
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static List<OpenAPISummary> getSummaries(ServiceResponse response) {
		
		try {
			JSONObject objJson = new JSONObject(response.getResponseString());
			if (!objJson.isNull("summary")) {
				JSONArray jarr = objJson.getJSONArray("summary");
				List<OpenAPISummary> summaries = new ArrayList<OpenAPISummary>();

				for (int i = 0; i < jarr.length(); i++) {
					
					OpenAPISummary summary = new OpenAPISummary();
					summary.fromJson(jarr.getJSONObject(i));
					summaries.add(summary);
				}

				return summaries;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
	// getters setters
	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	public Double getCalories() {
		return calories;
	}

	public void setCalories(Double calories) {
		this.calories = calories;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
	
}
