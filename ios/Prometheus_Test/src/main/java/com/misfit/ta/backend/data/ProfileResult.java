package com.misfit.ta.backend.data;

import com.google.resting.component.impl.ServiceResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ProfileResult extends BaseResult 
{
	
	// fields
	public ProfileData profile = new ProfileData();
	
	
	// constructor
	public ProfileResult(ServiceResponse response) 
	{
		super(response);
		
		// invalid token
		if(json.getString("profile") == "null")
		{
			profile = null;
			this.pairResult.put("profile","null");
			
			return;
		}
		
		formatOK();
	}

	private void formatOK()
	{
		// normal result
		JSONObject proJSON = json.getJSONObject("profile");

		if(proJSON.containsKey("serverId"))
		{
			profile.serverId = proJSON.getString("serverId");
			this.pairResult.put("serverId", profile.serverId);
		}
		
		if(proJSON.containsKey("localId"))
		{
			profile.localId = proJSON.getString("localId");
			this.pairResult.put("localId", profile.localId);
		}
		
		profile.updatedAt = proJSON.getLong("updatedAt");
		this.pairResult.put("updatedAt", profile.updatedAt);
		
		// these result only avaiable with GET-200, PUT-210, POST-210
		if(proJSON.containsKey("weight"))
		{
			profile.weight = proJSON.getDouble("weight");
			profile.height = proJSON.getDouble("height");
			profile.unit = proJSON.getInt("unit");
			profile.gender = proJSON.getInt("gender");
			profile.dateOfBirth = proJSON.getLong("dateOfBirth");
			profile.name = proJSON.getString("name");
			profile.latestVersion = proJSON.getString("latestVersion");
			profile.goalLevel = proJSON.getInt("goalLevel");
			profile.trackingDeviceId = proJSON.getString("trackingDeviceId");
					
			this.pairResult.put("weight", profile.weight);
			this.pairResult.put("height", profile.height);
			this.pairResult.put("unit", profile.unit);
			this.pairResult.put("gender", profile.gender);
			this.pairResult.put("dateOfBirth", profile.dateOfBirth);
			this.pairResult.put("name", profile.name);
			this.pairResult.put("lastestVersion", profile.latestVersion);
			this.pairResult.put("goalLevel", profile.goalLevel);
			this.pairResult.put("trackingDeviceId", profile.trackingDeviceId);
		}
		
		// direct result
		if(proJSON.containsKey("userRelativeLevelsNSData"))
		{
			if(proJSON.getString("userRelativeLevelsNSData") != "null")
			{
				JSONArray relativeLevelsJSON = proJSON.getJSONArray("userRelativeLevelsNSData");
				profile.userRelativeLevelsNSData = new RelativeLevelData[10];
				for(int i = 0; i < profile.userRelativeLevelsNSData.length; i++)
				{
					JSONObject relativeDataJSON = relativeLevelsJSON.getJSONObject(i);
					
					profile.userRelativeLevelsNSData[i] = new RelativeLevelData();
					profile.userRelativeLevelsNSData[i].level = relativeDataJSON.getString("level");
					profile.userRelativeLevelsNSData[i].absoluteLevel = relativeDataJSON.getString("absoluteLevel");
					profile.userRelativeLevelsNSData[i].point = relativeDataJSON.getString("point");
				}
					
				// hash archive
				this.pairResult.put("userRelativeLevelsNSData", profile.userRelativeLevelsNSData);
			}
			else
			{
				profile.userRelativeLevelsNSData = null;
				this.pairResult.put("userRelativeLevelsNSData", "null");
			}
		}
	}

}
