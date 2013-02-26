package com.misfit.ta.backend.data;

import java.util.Arrays;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.resting.component.impl.ServiceResponse;

public class GoalsResult extends BaseResult
{
	// relative class data
	public class Goal
	{
		public String serverId = null;
		public String localId = null;
		public Long updatedAt = null;
		public Double goalValue = null;
		public Long startTime = null;
		public Long endTime = null;
		public Integer absoluteLevel = null;
		public Integer userRelativeLevel = null;
		public Integer timeZoneOffsetInSeconds = null;
		public String[] progressValuesInMinutesNSData = null;
		
		public String toString()
		{
			return "{ serverId: " + serverId + ", localId: " + localId + ", updatedAt: " + updatedAt + 
				   ", goalValue: " + goalValue + ", startTime: " + startTime + ". endTime: " + endTime +
				   ", absoluteLevel: " + absoluteLevel + ", userRelativeLevel: " + userRelativeLevel +
				   ", timeZoneOffsetInSeconds: " + timeZoneOffsetInSeconds +
				   ", progressValuesInMinutesNSData: " + Arrays.toString(progressValuesInMinutesNSData) + " }";
		}
	}
	
	// fields
	public Goal[] goals;
	
	// constructor
	public GoalsResult(ServiceResponse response) 
	{
		super(response);
		
		// invalid token || not found
		if(this.statusCode == 401 || this.statusCode == 404)
		{
			goals = null;
			this.pairResult.put("goals", "null");
			
			return;
		}
	
		// request ok
		formatOK();
	}
	
	private void formatOK()
	{
		// result from search (list result)
		if(json.containsKey("goals"))
		{
			JSONArray arrJson = json.getJSONArray("goals");
			goals = new Goal[arrJson.size()];
			
			for(int i = 0; i < goals.length; i++)
			{
				JSONObject objJson = arrJson.getJSONObject(i);
				goals[i] = formatResult(objJson);
				
				this.pairResult.put("goals[" + i + "]", goals[i]);
			}
			
			this.pairResult.put("goals", goals);
		}
		else
		{
			JSONObject objJson = json.getJSONObject("goal");
			goals = new Goal[1];
			goals[0] = formatResult(objJson);
		
			this.pairResult.put("goals", goals);
		}
	}
	
	private Goal formatResult(JSONObject objJson)
	{
		Goal goal = new Goal();
				
		goal.serverId = objJson.getString("serverId");
		goal.localId = objJson.getString("localId");
		goal.updatedAt = objJson.getLong("updatedAt");
		
		if(objJson.containsKey("goalValue"))
		{
			goal.goalValue = objJson.getDouble("goalValue");
			goal.startTime = objJson.getLong("startTime");
			goal.endTime = objJson.getLong("endTime");
			goal.absoluteLevel = objJson.getInt("absoluteLevel");
			goal.userRelativeLevel = objJson.getInt("userRelativeLevel");
			goal.timeZoneOffsetInSeconds = objJson.getInt("timeZoneOffsetInSeconds");
					
			JSONArray arr = objJson.getJSONArray("progressValuesInMinutesNSData");
			goal.progressValuesInMinutesNSData = new String[4];
			for(int j = 0; j < 4; j++)
				goal.progressValuesInMinutesNSData[j] = arr.getString(j);
		}
		
		return goal;
	}
	
}
