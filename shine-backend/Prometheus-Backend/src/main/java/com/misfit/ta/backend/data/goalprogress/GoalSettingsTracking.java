package com.misfit.ta.backend.data.goalprogress;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.TimestampObject;

public class GoalSettingsTracking {
	private List<TimestampObject> changes;
	
	public GoalSettingsTracking() {
		
	}
	
	public JSONObject toJson() {
		try {
			JSONObject object = new JSONObject();
			if (changes != null) {
				List<JSONObject> arr = new ArrayList<JSONObject>();
				for (int i = 0; i < changes.size(); i++)
					arr.add(changes.get(i).toJson());
				object.accumulate("changes", arr);
			}
			
			return object;
		} catch (JSONException e) {
			e.printStackTrace();

			return null;
		}
	}
	
	public List<JSONObject> getChangesJsonArray() {
		List<JSONObject> arr = new ArrayList<JSONObject>();
		try {
			if (changes != null) {
				for (int i = 0; i < changes.size(); i++)
					arr.add(changes.get(i).toJson());
			}
			return arr;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	public List<TimestampObject> getChanges() {
		return changes;
	}

	public void setChanges(List<TimestampObject> changes) {
		this.changes = changes;
	}
	

}
