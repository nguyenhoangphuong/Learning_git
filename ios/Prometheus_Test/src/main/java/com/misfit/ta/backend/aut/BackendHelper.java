package com.misfit.ta.backend.aut;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.AccountResult;

public class BackendHelper {
	private static String password = "misfit1";

	public static AccountResult signUp() {
		String email = MVPApi.generateUniqueEmail();
		long temp = System.currentTimeMillis();
		String udid = temp + "" + temp + "" + temp + "" + temp;
		AccountResult result = MVPApi.signUp(email, password, udid);
		return result;
	}
}
