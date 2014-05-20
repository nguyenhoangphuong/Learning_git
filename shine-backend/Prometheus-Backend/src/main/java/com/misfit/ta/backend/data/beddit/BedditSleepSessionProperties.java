package com.misfit.ta.backend.data.beddit;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class BedditSleepSessionProperties {

	// fields
	protected Integer normalizedSleepQuality;
	protected Integer nSnooze;
	protected Double restingHeartRate;
	
       
    // methods
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
        	object.accumulate("normalizedSleepQuality", normalizedSleepQuality);
        	object.accumulate("nSnooze", nSnooze);
        	object.accumulate("restingHeartRate", restingHeartRate);
            
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
			
			if (!json.isNull("nSnooze"))
				this.setnSnooze(json.getInt("nSnooze"));
			
			if (!json.isNull("restingHeartRate"))
				this.setRestingHeartRate(json.getDouble("restingHeartRate"));
			
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

	public Integer getnSnooze() {
		return nSnooze;
	}

	public void setnSnooze(Integer nSnooze) {
		this.nSnooze = nSnooze;
	}

	public Double getRestingHeartRate() {
		return restingHeartRate;
	}

	public void setRestingHeartRate(Double restingHeartRate) {
		this.restingHeartRate = restingHeartRate;
	}
	
}
