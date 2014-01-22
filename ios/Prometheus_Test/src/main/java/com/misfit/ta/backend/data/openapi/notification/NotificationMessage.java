package com.misfit.ta.backend.data.openapi.notification;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class NotificationMessage {

	private String type;
	private String action;
	private String id;
	private String ownerId;
	private String updatedAt;
	
	public JSONObject toJson() {
		
		try {
			JSONObject object = new JSONObject();

			object.accumulate("type", type);
			object.accumulate("action", action);
			object.accumulate("id", id);
			object.accumulate("ownerId", ownerId);			
			object.accumulate("updatedAt", updatedAt);
			return object;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public NotificationMessage fromJson(JSONObject objJson) {
		
		try {
			if (!objJson.isNull("type"))
				this.setType(objJson.getString("type"));
			
			if (!objJson.isNull("action"))
				this.setAction(objJson.getString("action"));
			
			if (!objJson.isNull("id"))
				this.setId(objJson.getString("id"));
			
			if (!objJson.isNull("ownerId"))
				this.setOwnerId(objJson.getString("ownerId"));
			
			if (!objJson.isNull("updatedAt"))
				this.setUpdatedAt(objJson.getString("updatedAt"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	
	// getters setters
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

}
