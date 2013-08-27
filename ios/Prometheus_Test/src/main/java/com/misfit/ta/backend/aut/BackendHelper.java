package com.misfit.ta.backend.aut;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.account.AccountResult;

public class BackendHelper {
	private static String password = "misfit1";

	public static AccountResult signUp() {
		String email = MVPApi.generateUniqueEmail();
		AccountResult result = MVPApi.signUp(email, password);
		return result;
	}
}
