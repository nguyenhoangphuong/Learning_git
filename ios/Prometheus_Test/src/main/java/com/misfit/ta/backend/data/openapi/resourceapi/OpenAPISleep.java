package com.misfit.ta.backend.data.openapi.resourceapi;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class OpenAPISleep {

	
	// fields
	private Boolean autoDetected;
	private String startTime;
	private Integer duration;
	private List<OpenAPISleepDetail> sleepDetails;
	
	
	// methods
	public JSONObject toJson() {
		
		try {
			JSONObject object = new JSONObject();

			object.accumulate("autoDetected", autoDetected);
			object.accumulate("startTime", startTime);
			object.accumulate("duration", duration);
			
			if(sleepDetails != null) {
				
				JSONArray sleepDetailsArr = new JSONArray();
				for(OpenAPISleepDetail detail : sleepDetails) {
					sleepDetailsArr.put(detail.toJson());
				}

				object.accumulate("sleepDetails", sleepDetailsArr);
			}
			
			return object;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public OpenAPISleep fromJson(JSONObject objJson) {
		
		try {
			if (!objJson.isNull("autoDetected"))
				this.setAutoDetected(objJson.getBoolean("autoDetected"));
			
			if (!objJson.isNull("startTime"))
				this.setStartTime(objJson.getString("startTime"));
			
			if (!objJson.isNull("duration"))
				this.setDuration(objJson.getInt("duration"));
			
			if (!objJson.isNull("sleepDetails")) {
				
				JSONArray sleepDetailsArr = objJson.getJSONArray("sleepDetails");
				sleepDetails = new ArrayList<OpenAPISleepDetail>();
				
				for(int i = 0; i < sleepDetailsArr.length(); i++) {
					
					OpenAPISleepDetail detail = new OpenAPISleepDetail();
					detail.fromJson(sleepDetailsArr.getJSONObject(i));
					
					sleepDetails.add(detail);
				}
			}				

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	public static OpenAPISleep getSleepFromResponse(ServiceResponse response) {
		
		try {
			JSONObject objJson = new JSONObject(response.getResponseString());
			OpenAPISleep sleep = new OpenAPISleep();
			return sleep.fromJson(objJson);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static List<OpenAPISleep> getSleepsFromResponse(ServiceResponse response) {
		
		try {
			JSONObject objJson = new JSONObject(response.getResponseString());
			if (!objJson.isNull("sleeps")) {
				JSONArray jarr = objJson.getJSONArray("sleeps");
				List<OpenAPISleep> sleeps = new ArrayList<OpenAPISleep>();

				for (int i = 0; i < jarr.length(); i++) {
					
					OpenAPISleep sleep = new OpenAPISleep();
					sleep.fromJson(jarr.getJSONObject(i));
					sleeps.add(sleep);
				}

				return sleeps;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	

	// getters setters
	public Boolean getAutoDetected() {
		return autoDetected;
	}

	public void setAutoDetected(Boolean autoDetected) {
		this.autoDetected = autoDetected;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public List<OpenAPISleepDetail> getSleepDetails() {
		return sleepDetails;
	}

	public void setSleepDetails(List<OpenAPISleepDetail> sleepDetails) {
		this.sleepDetails = sleepDetails;
	}
	
}
