package com.misfit.ta.backend.data.profile;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.statistics.Statistics;

public class ProfileResult extends BaseResult {

	// fields
	public ProfileData profile;
	public Statistics statistics;


	// constructor
	public ProfileResult(ServiceResponse response) {
		super(response);

		try {
			// invalid token
			if (!json.isNull("profile")) {
				
				if(json.getString("profile").equals("null"))
					profile = null;
				else {
					profile = new ProfileData();
					JSONObject proJSON = json.getJSONObject("profile");
					profile.fromJson(proJSON);
				}
			}
			
			if (!json.isNull("statistics")) {
				
				if(json.getString("statistics").equals("null"))
					statistics = null;
				else {
					statistics = new Statistics();
					JSONObject statisticsObj = json.getJSONObject("statistics");
		            statistics.fromJson(statisticsObj);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
