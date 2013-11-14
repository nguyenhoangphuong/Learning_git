package com.misfit.ta.backend.data.social;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class SocialUserFromSearchResult extends SocialUserBase {

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

	public SocialUserFromSearchResult fromJson(JSONObject json) {
			
		try {
			super.fromJson(json);
			
			if (!json.isNull("status"))
				setStatus(json.getInt("status"));
									
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	public static SocialUserFromSearchResult[] usersFromResponse(ServiceResponse response) {
		try {
			
			JSONObject jsonResponse = new JSONObject(response.getResponseString());
			JSONArray jsonUsers = jsonResponse.getJSONArray("users");
			SocialUserFromSearchResult[] users = new SocialUserFromSearchResult[jsonUsers.length()];
			
			for(int i = 0; i < jsonUsers.length(); i++) {
				users[i] = new SocialUserFromSearchResult();
				users[i].fromJson(jsonUsers.getJSONObject(i));
			}
			
			return users;
					
		} catch (JSONException e) {
			return null;
		}
	}

}
