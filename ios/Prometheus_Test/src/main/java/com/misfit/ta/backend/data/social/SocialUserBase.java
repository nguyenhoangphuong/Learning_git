package com.misfit.ta.backend.data.social;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class SocialUserBase {

	// fields
	protected String uid;
	protected String handle;
	protected String name;
	protected String avatar;
	
	
	// getters setters
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	
	// methods
	public JSONObject toJson() {
		try {
			
			JSONObject obj = new JSONObject();
			
			obj.accumulate("uid", uid);
			obj.accumulate("handle", handle);
			obj.accumulate("name", name);
			obj.accumulate("avatar", avatar);

			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public SocialUserBase fromJson(JSONObject json) {
		
		try {
			
			if (!json.isNull("uid"))
				setUid(json.getString("uid"));
			
			if (!json.isNull("handle"))
				setHandle(json.getString("handle"));
			
			if (!json.isNull("name"))
				setName(json.getString("name"));
			
			if (!json.isNull("avatar"))
				setAvatar(json.getString("avatar"));
									
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	public static SocialUserBase[] usersFromResponse(ServiceResponse response) {
		try {
			
			JSONObject jsonResponse = new JSONObject(response.getResponseString());
			JSONArray jsonUsers = jsonResponse.getJSONArray("users");
			SocialUserBase[] users = new SocialUserBase[jsonUsers.length()];
			
			for(int i = 0; i < jsonUsers.length(); i++) {
				users[i] = new SocialUserBase();
				users[i].fromJson(jsonUsers.getJSONObject(i));
			}
			
			return users;
					
		} catch (JSONException e) {
			return null;
		}
	}

}
