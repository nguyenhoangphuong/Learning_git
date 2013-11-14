package com.misfit.ta.backend.data.social;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class SocialUserLeaderBoardEvent extends SocialUserBase {

	// fields
	protected Integer points;
	protected Integer timezone;
	protected Integer currentTime;

	
	// getters setters
	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getTimezone() {
		return timezone;
	}

	public void setTimezone(Integer timezone) {
		this.timezone = timezone;
	}

	public Integer getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Integer currentTime) {
		this.currentTime = currentTime;
	}
	
	
	// methods
	public JSONObject toJson() {
		try {
			
			JSONObject obj = super.toJson();
			if(obj == null)
				return null;
			
			obj.accumulate("points", points);
			obj.accumulate("timezone", timezone);
			obj.accumulate("currentTime", currentTime);
			
			return obj;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public SocialUserLeaderBoardEvent fromJson(JSONObject json) {
				
		try {
			
			super.fromJson(json);
			
			if (!json.isNull("points"))
				setPoints(json.getInt("points"));
			
			if (!json.isNull("timezone"))
				setTimezone(json.getInt("timezone"));
			
			if (!json.isNull("currentTime"))
				setCurrentTime(json.getInt("currentTime"));
									
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	public static SocialUserLeaderBoardEvent[] usersFromResponse(ServiceResponse response) {
		try {
			
			JSONObject jsonResponse = new JSONObject(response.getResponseString());
			JSONArray jsonUsers = jsonResponse.getJSONArray("users");
			SocialUserLeaderBoardEvent[] users = new SocialUserLeaderBoardEvent[jsonUsers.length()];
			
			for(int i = 0; i < jsonUsers.length(); i++) {
				users[i] = new SocialUserLeaderBoardEvent();
				users[i].fromJson(jsonUsers.getJSONObject(i));
			}
			
			return users;
					
		} catch (JSONException e) {
			return null;
		}
	}

}
