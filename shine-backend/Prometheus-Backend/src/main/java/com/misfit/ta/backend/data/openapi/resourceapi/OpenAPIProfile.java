package com.misfit.ta.backend.data.openapi.resourceapi;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class OpenAPIProfile {

	// fields
	protected String email;
	protected String name;
	protected String birthday;
	protected String gender;
	
	
	// methods
	public JSONObject toJson() {
		try {
			JSONObject object = new JSONObject();

			object.accumulate("email", email);
			object.accumulate("name", name);
			object.accumulate("birthday", birthday);
			object.accumulate("gender", gender);

			return object;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public OpenAPIProfile fromJson(JSONObject json) {
		OpenAPIProfile obj = this;
		try {

			if (!json.isNull("email"))
				obj.setEmail(json.getString("email"));
			
			if (!json.isNull("name"))
				obj.setName(json.getString("name"));
			
			if (!json.isNull("birthday"))
				obj.setBirthday(json.getString("birthday"));
			
			if (!json.isNull("gender"))
				obj.setGender(json.getString("gender"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}

	public static OpenAPIProfile fromResponse(ServiceResponse response) {
		try {
			JSONObject jsonResponse = new JSONObject(response.getResponseString());
			
			OpenAPIProfile profile = new OpenAPIProfile();
			return profile.fromJson(jsonResponse);
			
		} catch (JSONException e) {
			return null;
		}
	}


	// getters setters
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
}
