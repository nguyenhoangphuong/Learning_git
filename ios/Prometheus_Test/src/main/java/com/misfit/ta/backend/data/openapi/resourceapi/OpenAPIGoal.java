package com.misfit.ta.backend.data.openapi.resourceapi;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class OpenAPIGoal {

	
	// fields
	private String date;
	private Integer point;
	
	
	// methods
	public JSONObject toJson() {
		
		try {
			JSONObject object = new JSONObject();

			object.accumulate("date", date);
			object.accumulate("point", point);
			return object;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public OpenAPIGoal fromJson(JSONObject objJson) {
		
		try {
			if (!objJson.isNull("date"))
				this.setDate(objJson.getString("date"));
			
			if (!objJson.isNull("point"))
				this.setPoint(objJson.getInt("point"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
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
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}
	
}
