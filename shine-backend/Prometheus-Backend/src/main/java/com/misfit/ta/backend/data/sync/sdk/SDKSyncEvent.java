package com.misfit.ta.backend.data.sync.sdk;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.sync.sdk.requestevent.SDKSyncRequestFinishedEvent;
import com.misfit.ta.backend.data.sync.sdk.requestevent.SDKSyncRequestStartedEvent;
import com.misfit.ta.backend.data.sync.sdk.requestevent.SDKSyncResponseFinishedEvent;
import com.misfit.ta.backend.data.sync.sdk.requestevent.SDKSyncResponseStartedEvent;

public class SDKSyncEvent {

	protected SDKSyncRequestStartedEvent requestStarted;
	protected SDKSyncRequestFinishedEvent requestFinished;
	protected SDKSyncResponseStartedEvent responseStarted;
	protected SDKSyncResponseFinishedEvent responseFinished;
	protected String event;
	
	
	// json
	public JSONObject toJson() {
		
		try {
			JSONObject obj = new JSONObject();
			
			if(requestStarted != null)
				obj.accumulate("requestStarted", requestStarted.toJson());
			
			if(requestFinished != null)
				obj.accumulate("requestFinished", requestFinished.toJson());
			
			if(responseStarted != null)
				obj.accumulate("responseStarted", responseStarted.toJson());
			
			if(responseFinished != null)
				obj.accumulate("responseFinished", responseFinished.toJson());
			
			obj.accumulate("event", event);
			return obj;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public SDKSyncEvent fromJson(JSONObject json) {
		
		try {
			if(!json.isNull("requestStarted")) {
				this.requestStarted = new SDKSyncRequestStartedEvent();
				this.requestStarted.fromJson(json.getJSONObject("requestStarted"));
			}
			
			if(!json.isNull("requestFinished")) {
				this.requestFinished = new SDKSyncRequestFinishedEvent();
				this.requestFinished.fromJson(json.getJSONObject("requestFinished"));
			}
			
			if(!json.isNull("responseStarted")) {
				this.responseStarted = new SDKSyncResponseStartedEvent();
				this.responseStarted.fromJson(json.getJSONObject("responseStarted"));
			}
			
			if(!json.isNull("responseFinished")) {
				this.responseFinished = new SDKSyncResponseFinishedEvent();
				this.responseFinished.fromJson(json.getJSONObject("responseFinished"));
			}
			
			if(!json.isNull("event")) 
				this.event = json.getString("event");

			return this;
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	// getters setters
	public SDKSyncRequestStartedEvent getRequestStarted() {
		return requestStarted;
	}
	
	public void setRequestStarted(SDKSyncRequestStartedEvent requestStarted) {
		this.requestStarted = requestStarted;
	}

	public SDKSyncRequestFinishedEvent getRequestFinished() {
		return requestFinished;
	}

	public void setRequestFinished(SDKSyncRequestFinishedEvent requestFinished) {
		this.requestFinished = requestFinished;
	}

	public SDKSyncResponseStartedEvent getResponseStarted() {
		return responseStarted;
	}

	public void setResponseStarted(SDKSyncResponseStartedEvent responseStarted) {
		this.responseStarted = responseStarted;
	}

	public SDKSyncResponseFinishedEvent getResponseFinished() {
		return responseFinished;
	}

	public void setResponseFinished(SDKSyncResponseFinishedEvent responseFinished) {
		this.responseFinished = responseFinished;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
}
