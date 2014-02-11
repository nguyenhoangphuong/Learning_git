package com.misfit.ta.backend.data.openapi;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class OpenAPIThirdPartyApp {

	// fields
	protected String name;
	protected String clientKey;
	protected String clientSecret;
	protected String id;
	protected String updatedAt;
	
	
	// methods
	public JSONObject toJson() {
		try {
			JSONObject object = new JSONObject();

			object.accumulate("name", name);
			object.accumulate("c_key", clientKey);
			object.accumulate("c_secret", clientSecret);
			object.accumulate("_id", id);
			object.accumulate("updated_at", updatedAt);

			return object;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public OpenAPIThirdPartyApp fromJson(JSONObject json) {
		OpenAPIThirdPartyApp obj = this;
		try {
			if (!json.isNull("name"))
				obj.setName(json.getString("name"));

			if (!json.isNull("c_key"))
				obj.setClientKey(json.getString("c_key"));
			
			if (!json.isNull("c_sec"))
				obj.setClientSecret(json.getString("c_sec"));
			
			if (!json.isNull("_id"))
				obj.setId(json.getString("_id"));
			
			if (!json.isNull("updated_at"))
				obj.setUpdatedAt(json.getString("updated_at"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}

	public static OpenAPIThirdPartyApp getAppFromResponse(ServiceResponse response) {
		try {
			JSONObject jsonResponse = new JSONObject(response.getResponseString());
			OpenAPIThirdPartyApp app = new OpenAPIThirdPartyApp();
			return app.fromJson(jsonResponse);
			
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static List<OpenAPIThirdPartyApp> getAppsFromResponse(ServiceResponse response) {
		
		try {
			JSONArray appsJson = new JSONArray(response.getResponseString());
			List<OpenAPIThirdPartyApp> apps = new ArrayList<OpenAPIThirdPartyApp>();

			for (int i = 0; i < appsJson.length(); i++) {

				OpenAPIThirdPartyApp app = new OpenAPIThirdPartyApp();
				app.fromJson(appsJson.getJSONObject(i));
				apps.add(app);
			}

			return apps;
		}
		catch(Exception e) {
			
			e.printStackTrace();
			return null;
		}

	}

	
	// getters setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClientKey() {
		return clientKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

}
