package com.misfit.ta.backend.data.timeline.timelineitemdata;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class LifetimeDistanceItem extends TimelineItemDataBase {

	// fields
	protected Integer milestoneType;
	protected Integer unitSystem;
	

	// constructor
	public LifetimeDistanceItem() {
	}
	
	
	// methods
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
        	
            object.accumulate("milestoneType", milestoneType);
            object.accumulate("unitSystem", unitSystem);
            
            return object;
            
        } catch (JSONException e) {

            e.printStackTrace();
            return null;
        }
    }
    
	public static LifetimeDistanceItem fromJson(JSONObject json) {
		
		LifetimeDistanceItem obj = new LifetimeDistanceItem();
		try {
			
			if (!json.isNull("milestoneType"))
				obj.setMilestoneType(json.getInt("milestoneType"));
			
			if (!json.isNull("unitSystem"))
				obj.setUnitSystem(json.getInt("unitSystem"));
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return obj;
	}
	
	
	// getters setters
	public Integer getMilestoneType() {
		return milestoneType;
	}

	public void setMilestoneType(Integer milestoneType) {
		this.milestoneType = milestoneType;
	}

	public Integer getUnitSystem() {
		return unitSystem;
	}

	public void setUnitSystem(Integer unitSystem) {
		this.unitSystem = unitSystem;
	}
	
}
