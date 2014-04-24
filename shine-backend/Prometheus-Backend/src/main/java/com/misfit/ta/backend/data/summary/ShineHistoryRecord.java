package com.misfit.ta.backend.data.summary;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONObject;

public class ShineHistoryRecord {
	
	public String date;
	public Double activityPoints;
	public Integer sleepSeconds;

	public static List<ShineHistoryRecord> getSummaryFromResponse(ServiceResponse response) {
		
		try {
			
			JSONObject body = new JSONObject(response.getResponseString());
			JSONArray jsonArr = body.getJSONArray("data");
			List<ShineHistoryRecord> summary = new ArrayList<ShineHistoryRecord>();
			
			for(int i = 0; i < jsonArr.length(); i++) {
				
				ShineHistoryRecord record = new ShineHistoryRecord();
				record.date = jsonArr.getJSONArray(i).getString(0);
				record.activityPoints = jsonArr.getJSONArray(i).getDouble(1);
				record.sleepSeconds = jsonArr.getJSONArray(i).getInt(2);
				
				summary.add(record);
			}
			
			return summary;
			
		} catch (Exception e) {
			return null;
		}
	}
	
}
