package com.misfit.ta.backend.data.timeline.timelineitemdata;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class SleepSessionItem extends TimelineItemDataBase {

	// fields
	private Integer realDeepSleepTimeInMinutes;
	private Integer realSleepTimeInMinutes;
    private Long bookmarkTime;
    private Long realEndTime;
    private Long realStartTime;
    private Long editedStartTime;
    private Long editedEndTime;
    private Boolean isFirstSleepOfDay;
    private Boolean isAutoDetected;
    private Integer normalizedSleepQuality;
    private List<Integer[]> sleepStateChanges;
    
 
    // constructor
    public SleepSessionItem() {
    }

    
    // methods
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
        	
            object.accumulate("realDeepSleepTimeInMinutes", realDeepSleepTimeInMinutes);
            object.accumulate("realSleepTimeInMinutes", realSleepTimeInMinutes);
            object.accumulate("bookmarkTime", bookmarkTime);
            object.accumulate("realEndTime", realEndTime);
            object.accumulate("realStartTime", realStartTime);
            object.accumulate("editedStartTime", editedStartTime);
            object.accumulate("editedEndTime", editedEndTime);
            object.accumulate("isFirstSleepOfDay", isFirstSleepOfDay);
            object.accumulate("isAutoDetected", isAutoDetected);
            object.accumulate("normalizedSleepQuality", normalizedSleepQuality);
            
            if(sleepStateChanges != null) {
            	
            	for(Integer[] stateChange : sleepStateChanges) {
            		JSONArray stateChangeJson = new JSONArray();
            		stateChangeJson.put(stateChange[0]);
            		stateChangeJson.put(stateChange[1]);
            		object.accumulate("sleepStateChanges", stateChangeJson);
            	}
            }
            
            return object;
            
        } catch (JSONException e) {

            e.printStackTrace();
            return null;
        }
    }
    
	public SleepSessionItem fromJson(JSONObject json) {
		
		try {
			
			if (!json.isNull("realDeepSleepTimeInMinutes"))
				this.setRealDeepSleepTimeInMinutes(json.getInt("realDeepSleepTimeInMinutes"));
			
			if (!json.isNull("realSleepTimeInMinutes"))
				this.setRealSleepTimeInMinutes(json.getInt("realSleepTimeInMinutes"));
			
			if (!json.isNull("bookmarkTime"))
				this.setBookmarkTime(json.getLong("bookmarkTime"));
			
			if (!json.isNull("realEndTime"))
				this.setRealEndTime(json.getLong("realEndTime"));
			
			if (!json.isNull("realStartTime"))
				this.setRealStartTime(json.getLong("realStartTime"));

			if (!json.isNull("editedStartTime"))
				this.setEditedStartTime(json.getLong("editedStartTime"));
			
			if (!json.isNull("editedEndTime"))
				this.setEditedEndTime(json.getLong("editedEndTime"));
			
			if (!json.isNull("isFirstSleepOfDay"))
				this.setIsFirstSleepOfDay(json.getBoolean("isFirstSleepOfDay"));
			
			if (!json.isNull("isAutoDetected"))
				this.setIsAutoDetected(json.getBoolean("isAutoDetected"));
			
			if (!json.isNull("normalizedSleepQuality"))
				this.setNormalizedSleepQuality(json.getInt("normalizedSleepQuality"));
			
			if (!json.isNull("sleepStateChanges")) {
				
				JSONArray arr = json.getJSONArray("sleepStateChanges");
				List<Integer[]> stateChanges = new ArrayList<Integer[]>();
				
				for(int i = 0; i < arr.length(); i++) {
					Integer[] stateChange = new Integer[2];
					stateChange[0] = arr.getJSONArray(i).getInt(0);
					stateChange[1] = arr.getJSONArray(i).getInt(1);
					stateChanges.add(stateChange);
				}
				
				this.setSleepStateChanges(stateChanges);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

    
    // getters setters
	public Integer getRealDeepSleepTimeInMinutes() {
		return realDeepSleepTimeInMinutes;
	}


	public void setRealDeepSleepTimeInMinutes(Integer realDeepSleepTimeInMinutes) {
		this.realDeepSleepTimeInMinutes = realDeepSleepTimeInMinutes;
	}

	public Integer getRealSleepTimeInMinutes() {
		return realSleepTimeInMinutes;
	}

	public void setRealSleepTimeInMinutes(Integer realSleepTimeInMinutes) {
		this.realSleepTimeInMinutes = realSleepTimeInMinutes;
	}

	public Long getBookmarkTime() {
		return bookmarkTime;
	}

	public void setBookmarkTime(Long bookmarkTime) {
		this.bookmarkTime = bookmarkTime;
	}

	public Long getRealEndTime() {
		return realEndTime;
	}

	public void setRealEndTime(Long realEndTime) {
		this.realEndTime = realEndTime;
	}

	public Long getRealStartTime() {
		return realStartTime;
	}

	public void setRealStartTime(Long realStartTime) {
		this.realStartTime = realStartTime;
	}

	public Boolean getIsFirstSleepOfDay() {
		return isFirstSleepOfDay;
	}

	public void setIsFirstSleepOfDay(Boolean isFirstSleepOfDay) {
		this.isFirstSleepOfDay = isFirstSleepOfDay;
	}

	public Boolean getIsAutoDetected() {
		return isAutoDetected;
	}

	public void setIsAutoDetected(Boolean isAutoDetected) {
		this.isAutoDetected = isAutoDetected;
	}

	public Integer getNormalizedSleepQuality() {
		return normalizedSleepQuality;
	}

	public void setNormalizedSleepQuality(Integer normalizedSleepQuality) {
		this.normalizedSleepQuality = normalizedSleepQuality;
	}

	public List<Integer[]> getSleepStateChanges() {
		return sleepStateChanges;
	}

	public void setSleepStateChanges(List<Integer[]> sleepStateChanges) {
		this.sleepStateChanges = sleepStateChanges;
	}

	public Long getEditedStartTime() {
		return editedStartTime;
	}

	public void setEditedStartTime(Long editedStartTime) {
		this.editedStartTime = editedStartTime;
	}

	public Long getEditedEndTime() {
		return editedEndTime;
	}

	public void setEditedEndTime(Long editedEndTime) {
		this.editedEndTime = editedEndTime;
	}
	
}
