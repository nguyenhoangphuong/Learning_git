package com.misfit.ta.backend.data.social;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class SocialUserEvent extends SocialUserBase {

	// fields
	protected String message;
	protected Long timestamp;

	
	// getters setters
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	

	// methods
	public JSONObject toJson() {
		try {
			
			JSONObject obj = super.toJson();
			if(obj == null)
				return null;
			
			obj.accumulate("message", message);
			obj.accumulate("timestamp", timestamp);
			
			return obj;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public SocialUserEvent fromJson(JSONObject json) {
				
		try {
			
			super.fromJson(json);
			
			if (!json.isNull("message"))
				setMessage(json.getString("message"));
			
			if (!json.isNull("timestamp"))
				setTimestamp(json.getLong("timestamp"));
												
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	public static SocialUserEvent[] usersFromResponse(ServiceResponse response) {
		try {
			
			JSONObject jsonResponse = new JSONObject(response.getResponseString());
			JSONArray jsonUsers = jsonResponse.getJSONArray("events");
			SocialUserEvent[] users = new SocialUserEvent[jsonUsers.length()];
			
			for(int i = 0; i < jsonUsers.length(); i++) {
				users[i] = new SocialUserEvent();
				users[i].fromJson(jsonUsers.getJSONObject(i));
			}
			
			return users;
					
		} catch (JSONException e) {
			return null;
		}
	}

}
