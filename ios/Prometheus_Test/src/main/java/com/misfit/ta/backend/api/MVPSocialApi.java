package com.misfit.ta.backend.api;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;

public class MVPSocialApi extends MVPApi {

	public static BaseResult getFriends(String token) {
		
		// prepare
		String url = baseAddress + "friends";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		// post and receive raw data
		ServiceResponse response = get(url, port, requestInf);

		// format data
		BaseResult result = new BaseResult(response);
		return result;
	}
	
}
