package com.misfit.ta.backend.data.timeline.timelineitemdata;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class MilestoneItem extends TimelineItemDataBase {

	// fields
	protected Integer eventType;
	protected MilestoneItemInfo info;
    

	// constructor
    public MilestoneItem() {
    }

       
    // methods
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
        	
            object.accumulate("eventType", eventType);
            
            if(info != null)
            	object.accumulate("info", info.toJson());
            
            return object;
            
        } catch (JSONException e) {

            e.printStackTrace();
            return null;
        }
    }
    
	public MilestoneItem fromJson(JSONObject json) {
		
		try {
			
			if (!json.isNull("eventType"))
				this.setEventType(json.getInt("eventType"));
			
			if (!json.isNull("info")) {
				MilestoneItemInfo info = new MilestoneItemInfo();
				this.setInfo(info.fromJson(json.getJSONObject("info")));
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}
    
    
    // getters setters
	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	public MilestoneItemInfo getInfo() {
		return info;
	}

	public void setInfo(MilestoneItemInfo info) {
		this.info = info;
	}
	
}
