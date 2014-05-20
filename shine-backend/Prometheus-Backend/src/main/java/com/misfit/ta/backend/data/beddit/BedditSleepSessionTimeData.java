package com.misfit.ta.backend.data.beddit;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class BedditSleepSessionTimeData {

	// fields
	protected Long realStartTime;
	protected Long realEndTime;
	protected Long editedStartTime;
	protected Long editedEndTime;
	protected Long alarmTime;
	protected Long realAlarmTime;
	

    // methods
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
        	
        	object.accumulate("realStartTime", realStartTime);
        	object.accumulate("realEndTime", realEndTime);
        	object.accumulate("editedStartTime", editedStartTime);
        	object.accumulate("editedEndTime", editedEndTime);
        	object.accumulate("alarmTime", alarmTime);
        	object.accumulate("realAlarmTime", realAlarmTime);
            
            return object;
            
        } catch (JSONException e) {

            e.printStackTrace();
            return null;
        }
    }
    
	public BedditSleepSessionTimeData fromJson(JSONObject json) {
		
		try {
			
			if (!json.isNull("realStartTime"))
				this.setRealStartTime(json.getLong("realStartTime"));
			
			if (!json.isNull("realEndTime"))
				this.setRealEndTime(json.getLong("realEndTime"));
			
			if (!json.isNull("editedStartTime"))
				this.setEditedStartTime(json.getLong("editedStartTime"));
			
			if (!json.isNull("editedEndTime"))
				this.setEditedEndTime(json.getLong("editedEndTime"));
			
			if (!json.isNull("alarmTime"))
				this.setAlarmTime(json.getLong("alarmTime"));
			
			if (!json.isNull("realAlarmTime"))
				this.setRealAlarmTime(json.getLong("realAlarmTime"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}
    
    
    // getters setters
	public Long getRealStartTime() {
		return realStartTime;
	}

	public void setRealStartTime(Long realStartTime) {
		this.realStartTime = realStartTime;
	}

	public Long getRealEndTime() {
		return realEndTime;
	}

	public void setRealEndTime(Long realEndTime) {
		this.realEndTime = realEndTime;
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

	public Long getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(Long alarmTime) {
		this.alarmTime = alarmTime;
	}

	public Long getRealAlarmTime() {
		return realAlarmTime;
	}

	public void setRealAlarmTime(Long realAlarmTime) {
		this.realAlarmTime = realAlarmTime;
	}

}
