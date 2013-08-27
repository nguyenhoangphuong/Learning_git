package com.misfit.ta.backend.data.profile;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.BaseResult;

public class ProfileResult extends BaseResult {

	// fields
	public ProfileData profile = new ProfileData();

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
			profile = ProfileData.fromJson(proJSON);
		} catch (Exception e) {
		}
	}
}
