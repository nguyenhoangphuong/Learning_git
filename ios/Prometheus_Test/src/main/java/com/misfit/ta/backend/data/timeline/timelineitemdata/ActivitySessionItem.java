package com.misfit.ta.backend.data.timeline.timelineitemdata;

import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class ActivitySessionItem extends TimelineItemDataBase {

	// fields
	private Integer point;
    private Double distance;
    private Integer rawPoint;
    private Boolean isBestRecord;
    private Double calories;
    private Integer activityType;
    private ActivitySessionItemTypeChangeRecord[] typeChanges;
    private Integer duration;
    private Integer steps;
    
 
    // constructor
    public ActivitySessionItem() {
    }

    
    // methods
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
        	
            object.accumulate("point", point);
            object.accumulate("distance", distance);
            object.accumulate("rawPoint", rawPoint);
            object.accumulate("isBestRecord", isBestRecord);
            object.accumulate("calories", calories);          
            object.accumulate("activityType", activityType);
            
            if(typeChanges != null) {
            	
            	for(ActivitySessionItemTypeChangeRecord typeChange : typeChanges)
            		object.accumulate("typeChanges", typeChange.toJson());
            }
            
            object.accumulate("duration", duration);
            object.accumulate("steps", steps);
            
            return object;
            
        } catch (JSONException e) {

            e.printStackTrace();
            return null;
        }
    }
    
	public ActivitySessionItem fromJson(JSONObject json) {
		
		try {
			
			if (!json.isNull("point"))
				this.setPoint(json.getInt("point"));
			
			if (!json.isNull("distance"))
				this.setDistance(json.getDouble("distance"));
			
			if (!json.isNull("rawPoint"))
				this.setRawPoint(json.getInt("rawPoint"));
			
			if (!json.isNull("isBestRecord"))
				this.setIsBestRecord(json.getBoolean("isBestRecord"));
			
			if (!json.isNull("calories"))
				this.setCalories(json.getDouble("calories"));
			
			if (!json.isNull("activityType"))
				this.setActivityType(json.getInt("activityType"));
			
			if (!json.isNull("typeChanges")) {
				
				JSONArray arr = json.getJSONArray("typeChanges");
				ActivitySessionItemTypeChangeRecord[] records = new ActivitySessionItemTypeChangeRecord[arr.length()];
				
				for(int i = 0; i < arr.length(); i++) {
					records[i] = new ActivitySessionItemTypeChangeRecord();
					records[i].fromJson(arr.getJSONObject(i));
				}
				
				this.setTypeChanges(records);
			}
			
			if (!json.isNull("duration"))
				this.setDuration(json.getInt("duration"));
			
			if (!json.isNull("steps"))
				this.setSteps(json.getInt("steps"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

    
    // getters setters
    public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Integer getRawPoint() {
		return rawPoint;
	}

	public void setRawPoint(Integer rawPoint) {
		this.rawPoint = rawPoint;
	}

	public Boolean getIsBestRecord() {
		return isBestRecord;
	}

	public void setIsBestRecord(Boolean isBestRecord) {
		this.isBestRecord = isBestRecord;
	}

	public Double getCalories() {
		return calories;
	}

	public void setCalories(Double calories) {
		this.calories = calories;
	}

	public Integer getActivityType() {
		return activityType;
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}

	public ActivitySessionItemTypeChangeRecord[] getTypeChanges() {
		return typeChanges;
	}

	public void setTypeChanges(ActivitySessionItemTypeChangeRecord[] typeChanges) {
		this.typeChanges = typeChanges;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}
   
}
