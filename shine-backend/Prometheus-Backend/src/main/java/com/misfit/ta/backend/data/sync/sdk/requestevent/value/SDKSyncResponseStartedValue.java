package com.misfit.ta.backend.data.sync.sdk.requestevent.value;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class SDKSyncResponseStartedValue {

	protected String data;

	
	// json
	public Object toJson() {
		
		try {
			JSONObject obj = new JSONObject();
			obj.accumulate("data", data);
			
			return obj;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public SDKSyncResponseStartedValue fromJson(JSONObject json) {
		
		try {
			if(!json.isNull("data"))
				this.data = json.getString("data");

			return this;
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	// getters setters
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
