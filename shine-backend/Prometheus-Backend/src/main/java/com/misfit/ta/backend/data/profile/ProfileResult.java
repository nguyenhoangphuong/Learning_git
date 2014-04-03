package com.misfit.ta.backend.data.profile;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.statistics.Statistics;

public class ProfileResult extends BaseResult {

	// fields
	public ProfileData profile = new ProfileData();
	public Statistics statistics = new Statistics();


	// constructor
	public ProfileResult(ServiceResponse response) {
		super(response);

		try {
			// invalid token
			if (json.getString("profile") == "null") {
				profile = null;
				this.pairResult.put("profile", "null");
				return;
			}

			formatOK();
		} catch (Exception e) {
		}
	}

	private void formatOK() {
		try {
			JSONObject proJSON = json.getJSONObject("profile");
			profile.fromJson(proJSON);
			
			JSONObject statisticsObj = json.getJSONObject("statistics");
            statistics.fromJson(statisticsObj);
			
		} catch (Exception e) {
		}
	}
}
