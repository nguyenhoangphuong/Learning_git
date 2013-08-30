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
	
	public static String createAllTimelineItemsForAccount(String email, String password) {
		
		String token = MVPApi.signIn(email, password).token;
		MVPApi.createTimelineItems(token, DefaultValues.AllTimelineItems());
		
		return token;
	}
	
	public static void unlink(String email, String password, String serialNumber) {
		
		String token = MVPApi.signIn(email, password).token;
		MVPApi.unlinkDevice(token, serialNumber);
	}
	
	public static void link(String email, String password, String serialNumber) {
		
		long now = System.currentTimeMillis() / 1000;
		String token = MVPApi.signIn(email, password).token;
		MVPApi.createPedometer(token, serialNumber, "0.0.36r", now, null, now, "pedometer-" + System.nanoTime(), null, now);
	
	}
	
}
