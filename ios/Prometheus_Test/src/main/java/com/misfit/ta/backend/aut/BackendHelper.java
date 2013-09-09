package com.misfit.ta.backend.aut;

import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.account.AccountResult;

public class BackendHelper {

	private static String password = "misfit1";

	public static JSONObject removeKeysHaveNullValue(JSONObject src) {

		JSONObject json = new JSONObject(src, JSONObject.getNames(src));
		JSONArray keys = json.names();
		for (int i = 0; i < keys.length(); i++) {
			try {
				String key = keys.getString(i);
				if (json.get(key) == null)
					json.remove(key);
			} catch (JSONException e) {
			}
		}
		return json;
	}

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

	public static void unlink(String email, String password) {

		String token = MVPApi.signIn(email, password).token;
		MVPApi.unlinkDevice(token);
	}

	public static void link(String email, String password, String serialNumber) {

		long now = System.currentTimeMillis() / 1000;
		String token = MVPApi.signIn(email, password).token;
		MVPApi.createPedometer(token, serialNumber, "0.0.36r", now, null, now, "pedometer-" + System.nanoTime(), null, now);

	}

}
