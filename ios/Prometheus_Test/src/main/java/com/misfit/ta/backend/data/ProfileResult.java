package com.misfit.ta.backend.data;

import com.google.resting.component.impl.ServiceResponse;

public class ProfileResult extends BaseResult 
{
	
	// relavtive level data
	public class RelativeLevelData
	{
		public String level;
		public String absoluteLevel;
		public String point;
		
		public RelativeLevelData(Integer level, Integer absoluteLevel, Integer point)
		{
			this.level = level.toString();
			this.absoluteLevel = absoluteLevel.toString();
			this.point = point.toString();
		}
	}
	
	public class ProfileData
	{
		// fields
		public String serverId;
		public String localId;
		public long updatedAt;
		public double weight;
		public double height;
		public String unit;
		public int gender;
		public long dateOfBirth;
		public String name;
		public String lastestVersion;
		public int goalLevel;
		public String trackingDeviceId;
		public RelativeLevelData[] userRelativeLevelsNSData;
	}
	
	// fields
	ProfileData profile = new ProfileData();
	
	
	// constructor
	public ProfileResult(ServiceResponse response) 
	{
		super(response);
		
		// result
		profile.serverId = json.getString("serverId");
		profile.localId = json.getString("localId");
		profile.updatedAt = json.getLong("updatedAt");
		profile.weight = json.getDouble("weight");
		profile.height = json.getDouble("height");
		profile.unit = json.getString("unit");
		profile.gender = json.getInt("gender");
		profile.dateOfBirth = json.getLong("dateOfBirth");
		profile.name = json.getString("name");
		profile.lastestVersion = json.getString("lastestVersion");
		profile.goalLevel = json.getInt("goalLevel");
		profile.trackingDeviceId = json.getString("trackingDeviceId");
		
		// add to base hashmap
		this.pairResult.put("serverId", profile.serverId);
		this.pairResult.put("localId", profile.localId);
		this.pairResult.put("updatedAt", profile.updatedAt);
		this.pairResult.put("weight", profile.weight);
		this.pairResult.put("height", profile.height);
		this.pairResult.put("unit", profile.unit);
		this.pairResult.put("gender", profile.gender);
		this.pairResult.put("dateOfBirth", profile.dateOfBirth);
		this.pairResult.put("name", profile.name);
		this.pairResult.put("lastestVersion", profile.lastestVersion);
		this.pairResult.put("goalLevel", profile.goalLevel);
		this.pairResult.put("trackingDeviceId", profile.trackingDeviceId);
	}
}
