package com.misfit.ta.backend.data.beddit;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class BedditSleepSessionProperties {

	// fields
	protected Integer normalizedSleepQuality;
	
       
    // methods
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.accumulate("normalizedSleepQuality", normalizedSleepQuality);
            
            return object;
            
        } catch (JSONException e) {

            e.printStackTrace();
            return null;
        }
    }
    
	public BedditSleepSessionProperties fromJson(JSONObject json) {
		
		try {
			
			if (!json.isNull("normalizedSleepQuality"))
				this.setNormalizedSleepQuality(json.getInt("normalizedSleepQuality"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	
    // getters setters
	public Integer getNormalizedSleepQuality() {
		return normalizedSleepQuality;
	}

	public void setNormalizedSleepQuality(Integer normalizedSleepQuality) {
		this.normalizedSleepQuality = normalizedSleepQuality;
	}
	
}
