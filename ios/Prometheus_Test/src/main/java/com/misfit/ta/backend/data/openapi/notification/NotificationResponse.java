package com.misfit.ta.backend.data.openapi.notification;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class NotificationResponse {

	private List<NotificationMessage> Message;
	
	
	// methods
	public JSONObject toJson() {
		
		try {
			JSONObject object = new JSONObject();

			if (Message != null) {
				List<JSONObject> arr = new ArrayList<JSONObject>();
				for (int i = 0; i < Message.size(); i++)
					arr.add(Message.get(i).toJson());
				object.accumulate("Message", arr);
			}
			
			return object;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public NotificationResponse fromJson(JSONObject objJson) {
		
		try {
			if (!objJson.isNull("Message")) {
				JSONArray jarr = objJson.getJSONArray("Message");
				List<NotificationMessage> messages = new ArrayList<NotificationMessage>();

				for (int i = 0; i < jarr.length(); i++) {
					
					NotificationMessage mess = new NotificationMessage();
					mess.fromJson(jarr.getJSONObject(i));
					messages.add(mess);
				}

				this.setMessage(messages);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}
	
	public NotificationResponse fromResponse(ServiceResponse response) {
		
		try {
			
			JSONObject jsonResponse = new JSONObject(response.getResponseString());
			NotificationResponse profile = new NotificationResponse();
			return profile.fromJson(jsonResponse);
			
		} catch (JSONException e) {
			return null;
		}	
	}

	
	// getters setters
	public List<NotificationMessage> getMessage() {
		return Message;
	}

	public void setMessage(List<NotificationMessage> message) {
		Message = message;
	}

	
}
