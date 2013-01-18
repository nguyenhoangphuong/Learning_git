package com.misfit.ta.backend.data;

import com.google.resting.component.impl.ServiceResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ProfileResult extends BaseResult 
{
	
	// relavtive level data
	public class RelativeLevelData
	{
		public String level;
		public String absoluteLevel;
		public String point;
		
		public RelativeLevelData()
		{
			
		}
		
		public RelativeLevelData(Integer level, Integer absoluteLevel, Integer point)
		{
			this.level = level.toString();
			this.absoluteLevel = absoluteLevel.toString();
			this.point = point.toString();
		}
		
		public String toString()
		{
			return "{level:" + level + ",absoluteLevel:" + absoluteLevel + ",point:" + point + "}";
		}
	}
	
	public class ProfileData
	{
		// fields
		public String serverId;
		public String localId;
		public Long updatedAt;
		public Double weight;
		public Double height;
		public String unit;
		public Integer gender;
		public Long dateOfBirth;
		public String name;
		public String latestVersion;
		public Integer goalLevel;
		public String trackingDeviceId;
		public RelativeLevelData[] userRelativeLevelsNSData;
		
		public ProfileData()
		{
			userRelativeLevelsNSData = new RelativeLevelData[10]; 
		}
	}
	
	
	// fields
	ProfileData profile = new ProfileData();
	
	
	// constructor
	public ProfileResult(ServiceResponse response) 
	{
		super(response);
		
		// invalid token
		if(this.statusCode == 401)
		{
			profile = null;
			return;
		}
		
		formatOK();
	}

	public void formatOK()
	{
		// normal result
		JSONObject proJSON = json.getJSONObject("profile");

		profile.serverId = proJSON.getString("serverId");
		profile.localId = proJSON.getString("localId");
		profile.updatedAt = proJSON.getLong("updatedAt");
		
		// add to base hashmap
		this.pairResult.put("serverId", profile.serverId);
		this.pairResult.put("localId", profile.localId);
		this.pairResult.put("updatedAt", profile.updatedAt);
		
		// these result only avaiable with GET-200, PUT-210, POST-210
		if(proJSON.containsKey("weight"))
		{
			profile.weight = proJSON.getDouble("weight");
			profile.height = proJSON.getDouble("height");
			profile.unit = proJSON.getString("unit");
			profile.gender = proJSON.getInt("gender");
			profile.dateOfBirth = proJSON.getLong("dateOfBirth");
			profile.name = proJSON.getString("name");
			profile.latestVersion = proJSON.getString("latestVersion");
			profile.goalLevel = proJSON.getInt("goalLevel");
			profile.trackingDeviceId = proJSON.getString("trackingDeviceId");
			
			JSONArray relativeLevelsJSON = proJSON.getJSONArray("userRelativeLevelsNSData");
			for(int i = 0; i < profile.userRelativeLevelsNSData.length; i++)
			{
				JSONObject relativeDataJSON = relativeLevelsJSON.getJSONObject(i);
				
				profile.userRelativeLevelsNSData[i] = new RelativeLevelData();
				profile.userRelativeLevelsNSData[i].level = relativeDataJSON.getString("level");
				profile.userRelativeLevelsNSData[i].absoluteLevel = relativeDataJSON.getString("absoluteLevel");
				profile.userRelativeLevelsNSData[i].point = relativeDataJSON.getString("point");
			}
			
			this.pairResult.put("weight", profile.weight);
			this.pairResult.put("height", profile.height);
			this.pairResult.put("unit", profile.unit);
			this.pairResult.put("gender", profile.gender);
			this.pairResult.put("dateOfBirth", profile.dateOfBirth);
			this.pairResult.put("name", profile.name);
			this.pairResult.put("lastestVersion", profile.latestVersion);
			this.pairResult.put("goalLevel", profile.goalLevel);
			this.pairResult.put("trackingDeviceId", profile.trackingDeviceId);
			this.pairResult.put("userRelativeLevelsNSData", profile.userRelativeLevelsNSData);
		}
	}

}
