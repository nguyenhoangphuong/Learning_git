package com.misfit.ta.backend.data.timeline.timelineitemdata;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class MilestoneItemInfo {
	
	// fields
    private Integer point;
    private Integer exceededAmount;
    private Integer streakNumber;
    
    
    // constructor
    public MilestoneItemInfo() {
	}
    
    
    // methods
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
        	
            object.accumulate("point", point);
            object.accumulate("exceededAmount", exceededAmount);
            object.accumulate("streakNumber", streakNumber);
            
            return object;
            
        } catch (JSONException e) {

            e.printStackTrace();
            return null;
        }
    }
    
	public static MilestoneItemInfo fromJson(JSONObject json) {
		
		MilestoneItemInfo obj = new MilestoneItemInfo();
		try {
			
			if (!json.isNull("point"))
				obj.setPoint(json.getInt("point"));
			
			if (!json.isNull("exceededAmount"))
				obj.setExceededAmount(json.getInt("exceededAmount"));
			
			if (!json.isNull("streakNumber"))
				obj.setStreakNumber(json.getInt("streakNumber"));
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return obj;
	}
    
    
    // getters setters
    public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public Integer getExceededAmount() {
		return exceededAmount;
	}

	public void setExceededAmount(Integer exceededAmount) {
		this.exceededAmount = exceededAmount;
	}

	public Integer getStreakNumber() {
		return streakNumber;
	}

	public void setStreakNumber(Integer streakNumber) {
		this.streakNumber = streakNumber;
	}

}
