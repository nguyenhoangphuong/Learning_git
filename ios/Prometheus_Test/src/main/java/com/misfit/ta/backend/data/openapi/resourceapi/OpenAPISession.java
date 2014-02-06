package com.misfit.ta.backend.data.openapi.resourceapi;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class OpenAPISession {

	
	// fields
	private String activityType;
	private String startTime;
	private Integer duration;
	private Double points;
	private Integer steps;
	private Double calories;
	private Double distance;
	
	
	// methods
	public JSONObject toJson() {
		
		try {
			JSONObject object = new JSONObject();

			object.accumulate("activityType", activityType);
			object.accumulate("startTime", startTime);
			object.accumulate("duration", duration);
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
	
	public OpenAPISession fromJson(JSONObject objJson) {
		
		try {
			if (!objJson.isNull("activityType"))
				this.setActivityType(objJson.getString("activityType"));
			
			if (!objJson.isNull("startTime"))
				this.setStartTime(objJson.getString("startTime"));
			
			if (!objJson.isNull("duration"))
				this.setDuration(objJson.getInt("duration"));
			
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

	public static List<OpenAPISession> getSessionsFromResponse(ServiceResponse response) {
		
		try {
			JSONObject objJson = new JSONObject(response.getResponseString());
			if (!objJson.isNull("sessions")) {
				JSONArray jarr = objJson.getJSONArray("sessions");
				List<OpenAPISession> sessions = new ArrayList<OpenAPISession>();

				for (int i = 0; i < jarr.length(); i++) {
					
					OpenAPISession session = new OpenAPISession();
					session.fromJson(jarr.getJSONObject(i));
					sessions.add(session);
				}

				return sessions;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
	// getters setters
	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

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
