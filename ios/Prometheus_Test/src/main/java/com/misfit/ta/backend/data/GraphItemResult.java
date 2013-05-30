package com.misfit.ta.backend.data;

import java.util.ArrayList;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class GraphItemResult extends BaseResult {
    // fields
    private Object timestamp;
    private Object clientId;
    private Object averageValue;
    private Object userId;
    private Object updatedAt;
    private Object createdAt;

    public Object getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Object timestamp) {
		this.timestamp = timestamp;
	}

	public Object getClientId() {
		return clientId;
	}

	public void setClientId(Object clientId) {
		this.clientId = clientId;
	}

	public Object getAverageValue() {
		return averageValue;
	}

	public void setAverageValue(Object averageValue) {
		this.averageValue = averageValue;
	}

	public Object getUserId() {
		return userId;
	}

	public void setUserId(Object userId) {
		this.userId = userId;
	}

	public Object getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Object updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Object getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Object createdAt) {
		this.createdAt = createdAt;
	}

	// constructor
    public GraphItemResult(ServiceResponse response) {
        super(response);
        
        // results
        this.timestamp = json.getString("timestamp");
        this.clientId = json.getString("client_id");
        this.averageValue = json.getString("average_value");
        this.userId = json.getString("user_id");
        this.updatedAt = json.getString("updated_at");
        this.createdAt = json.getString("created_at");
        
        // add to base hashmap
        this.pairResult.put("timestamp", this.timestamp);
        this.pairResult.put("client_id", this.clientId);
        this.pairResult.put("average_value", this.averageValue);
        this.pairResult.put("user_id", this.userId);
        this.pairResult.put("updated_at", this.updatedAt);
        this.pairResult.put("created_at", this.createdAt);
    }
    
    public String toJson() {
		JSONBuilder builder = new JSONBuilder();
		
		builder.addValue("timestamp", this.timestamp);
		builder.addValue("client_id", this.clientId);
		builder.addValue("average_value", this.averageValue);
		builder.addValue("user_id", this.userId);
		builder.addValue("updated_at", this.updatedAt);
		builder.addValue("created_at", this.createdAt);
		
		return builder.toJSONString();
	}
    
    public static ArrayList<GraphItemResult> getActivityResults(ServiceResponse response) throws JSONException {
        ArrayList<GraphItemResult> graphItems = new ArrayList<GraphItemResult>();
        JSONObject responseBody = new JSONObject(response.getResponseString());
        JSONArray jsonGraphItems = responseBody.getJSONArray("activities");

        for (int i = 0; i < jsonActivities.length(); i++) {
            ActivityResult activity = ActivityResult.getFields(jsonActivities.getJSONObject(i), response);

            activities.add(activity);
        }

        return activities;
    }
}
