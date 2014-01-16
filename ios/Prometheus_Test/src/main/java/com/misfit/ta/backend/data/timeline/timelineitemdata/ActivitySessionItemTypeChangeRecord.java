package com.misfit.ta.backend.data.timeline.timelineitemdata;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;


public class ActivitySessionItemTypeChangeRecord {
	

	// fields
	protected Integer act;
	protected Long ts;


	// constructors
	public ActivitySessionItemTypeChangeRecord() {
	}


	// methods
	public JSONObject toJson() {
		try {
			
			JSONObject obj = new JSONObject();

			obj.accumulate("act", act);
			obj.accumulate("ts", ts);
			
			return obj;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ActivitySessionItemTypeChangeRecord fromJson(JSONObject json) {
		
		try {
			
			if (!json.isNull("act"))
				this.setAct(json.getInt("act"));
			
			if (!json.isNull("ts"))
				this.setTs(json.getLong("ts"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	// getters setters
	public Integer getAct() {
		return act;
	}

	public void setAct(Integer act) {
		this.act = act;
	}

	public Long getTs() {
		return ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

}
