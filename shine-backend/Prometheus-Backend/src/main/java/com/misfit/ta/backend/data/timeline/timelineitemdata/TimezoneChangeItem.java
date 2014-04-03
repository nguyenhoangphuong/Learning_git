package com.misfit.ta.backend.data.timeline.timelineitemdata;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class TimezoneChangeItem extends TimelineItemDataBase {

	// fields
	protected Integer afterTimeZoneOffset;
	protected Integer beforeTimeZoneOffset;
	

	// constructor
	public TimezoneChangeItem() {
	}
	
	
	// methods
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
        	
            object.accumulate("afterTimeZoneOffset", afterTimeZoneOffset);
            object.accumulate("beforeTimeZoneOffset", beforeTimeZoneOffset);
            
            return object;
            
        } catch (JSONException e) {

            e.printStackTrace();
            return null;
        }
    }
    
	public TimezoneChangeItem fromJson(JSONObject json) {
		
		try {
			
			if (!json.isNull("afterTimeZoneOffset"))
				this.setAfterTimeZoneOffset(json.getInt("afterTimeZoneOffset"));
			
			if (!json.isNull("beforeTimeZoneOffset"))
				this.setBeforeTimeZoneOffset(json.getInt("beforeTimeZoneOffset"));
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return this;
	}

	
	// getters setters
	public Integer getAfterTimeZoneOffset() {
		return afterTimeZoneOffset;
	}


	public void setAfterTimeZoneOffset(Integer afterTimeZoneOffset) {
		this.afterTimeZoneOffset = afterTimeZoneOffset;
	}


	public Integer getBeforeTimeZoneOffset() {
		return beforeTimeZoneOffset;
	}


	public void setBeforeTimeZoneOffset(Integer beforeTimeZoneOffset) {
		this.beforeTimeZoneOffset = beforeTimeZoneOffset;
	}
	
}
