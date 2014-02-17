package com.misfit.ta.backend.data.openapi.resourceapi;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class OpenAPIGoal {

	
	// fields
	private String id;
	private String date;
	private Double points;
	private Double targetPoints;
	
	
	// methods
	public JSONObject toJson() {
		
		try {
			JSONObject object = new JSONObject();

			object.accumulate("id", id);
			object.accumulate("date", date);
			object.accumulate("points", points);
			object.accumulate("targetPoints", targetPoints);
			return object;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public OpenAPIGoal fromJson(JSONObject objJson) {
		
		try {
			if (!objJson.isNull("id"))
				this.setId(objJson.getString("id"));
			
			if (!objJson.isNull("date"))
				this.setDate(objJson.getString("date"));
			
			if (!objJson.isNull("points"))
				this.setPoints(objJson.getDouble("points"));
			
			if (!objJson.isNull("targetPoints"))
				this.setTargetPoints(objJson.getDouble("targetPoints"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}
	
	public static OpenAPIGoal getGoalFromResponse(ServiceResponse response) {
		
		try {
			JSONObject objJson = new JSONObject(response.getResponseString());
			OpenAPIGoal goal = new OpenAPIGoal();
			return goal.fromJson(objJson);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static List<OpenAPIGoal> getGoalsFromResponse(ServiceResponse response) {
		
		try {
			JSONObject objJson = new JSONObject(response.getResponseString());
			if (!objJson.isNull("goals")) {
				JSONArray jarr = objJson.getJSONArray("goals");
				List<OpenAPIGoal> goals = new ArrayList<OpenAPIGoal>();

				for (int i = 0; i < jarr.length(); i++) {
					
					OpenAPIGoal goal = new OpenAPIGoal();
					goal.fromJson(jarr.getJSONObject(i));
					goals.add(goal);
				}

				return goals;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	// get set
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public Double getTargetPoints() {
		return targetPoints;
	}

	public void setTargetPoints(Double targetPoints) {
		this.targetPoints = targetPoints;
	}

}
