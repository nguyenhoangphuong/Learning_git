package com.misfit.ta.backend.data.timeline.timelineitemdata;

import com.google.resting.json.JSONObject;

public class FoodTrackingItem extends TimelineItemDataBase {

	// fields
    
 
    // constructor
    public FoodTrackingItem() {
    }

    
    // methods
    public JSONObject toJson() {
    	
        JSONObject object = new JSONObject();
        return object;
    }
    
	public FoodTrackingItem fromJson(JSONObject json) {
		
		return this;
	}

    
    // getters setters
	
}
