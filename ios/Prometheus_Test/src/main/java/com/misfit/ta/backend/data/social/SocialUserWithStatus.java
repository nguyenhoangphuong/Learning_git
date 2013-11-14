package com.misfit.ta.backend.data.social;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class SocialUserWithStatus extends SocialUserBase {

	// fields
	protected Integer status;

	
	// getters setters
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	// methods
	public JSONObject toJson() {
		try {
			
			JSONObject obj = super.toJson();
			if(obj == null)
				return null;
			
			obj.accumulate("status", status);
			
			return obj;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public SocialUserWithStatus fromJson(JSONObject json) {
			
		try {
			super.fromJson(json);
			
			if (!json.isNull("status"))
				setStatus(json.getInt("status"));
									
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	public static SocialUserWithStatus[] usersFromResponse(ServiceResponse response) {
		try {
			
			JSONObject jsonResponse = new JSONObject(response.getResponseString());
			JSONArray jsonUsers = jsonResponse.getJSONArray("users");
			SocialUserWithStatus[] users = new SocialUserWithStatus[jsonUsers.length()];
			
			for(int i = 0; i < jsonUsers.length(); i++) {
				users[i] = new SocialUserWithStatus();
				users[i].fromJson(jsonUsers.getJSONObject(i));
			}
			
			return users;
					
		} catch (JSONException e) {
			return null;
		}
	}

}
