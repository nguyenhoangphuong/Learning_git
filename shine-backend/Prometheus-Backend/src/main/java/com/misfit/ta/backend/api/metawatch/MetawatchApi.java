package com.misfit.ta.backend.api.metawatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.MetawatchModel.MetawatchProfileModel;

public class MetawatchApi extends MVPApi {
	// logger
	protected static Logger logger = Util.setupLogger(MetawatchApi.class);

	// fields
	 public static String baseAddress = Settings
	 .getValue("MVPOpenAPIMetawatchRegister");
//	public static String baseAddress = "http://192.168.18.255:2100/auth/";

	public static Integer port = Settings.getValue("MVPBackendPort") == null ? null
			: Integer.parseInt(Settings.getValue("MVPBackendPort"));

	public static BaseResult registerMetawatch(String token, String userID) {
		String url = baseAddress + "register/metawatch";

		BaseParams requestInfo = new MetawatchBaseParams();
		requestInfo.addHeader("user_id", userID);

		ServiceResponse response = MVPApi.get(url, port, requestInfo);
		return new BaseResult(response);
	}

	public static BaseResult signUpMetawatch(String email, String password,
			String userId) {
		String url = baseAddress + "signup/metawatch";
		BaseParams requestInfo = new MetawatchBaseParams();
		requestInfo.addParam("email", email);
		requestInfo.addParam("password", password);

		requestInfo.addHeader("user_id", userId);
		ServiceResponse response = MVPApi.post(url, port, requestInfo);

		// EntityBuilder entityBuilder =
		// org.apache.http.client.entity.EntityBuilder.create();
		// NameValuePair p1 = new BasicNameValuePair("email",
		// "thinh@misfit.com");
		// NameValuePair p2 = new BasicNameValuePair("password", "misfit1");
		// List<NameValuePair> list = new Vector<NameValuePair>();
		// list.add(p1);
		// list.add(p2);
		// entityBuilder.setParameters(list);
		//
		// HttpPost httpPost = new HttpPost(url);
		// httpPost.setHeaders(requestInfo.headers.toArray(new
		// Header[requestInfo.headers.size()]));
		// httpPost.setEntity(entityBuilder.build());
		//
		// ServiceResponse response = excuteHttpRequest(httpPost);
		// System.out.println("LOG [MetawatchApi.enclosing_method]: error code: "
		// + response.getStatusCode());
		return new BaseResult(response);
	}

	public static BaseResult createProfileMetawatch(String email,
			String password, MetawatchProfileModel profile, String userId) {
		// String url = baseAddress + "profile/metawatch";
		String redirect_url = baseAddress + "metawatch/exchange";
		String scope = "public,email,birthday,measurements,tracking,sessions,sleeps";

		String url = baseAddress
				+ "api/signup/metawatch?response_type=code&client_id=metawatch&redirect_uri="
				+ redirect_url + "&scope=" + scope;
		BaseParams requestInfo = new MetawatchBaseParams();
		requestInfo.addHeader("user_id", userId);

		ServiceResponse response;
		profile.setEmail(email);
		profile.setPassword(password);
		response = MVPApi.post(url, port, requestInfo, profile.toJson()
				.toString());
		return new BaseResult(response);
	}

	// profile
	static public MetawatchProfileModel DefaultProfile() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		MetawatchProfileModel profile = new MetawatchProfileModel();

		profile.setWeight(144.4);
		profile.setHeight(66.0);
		profile.setGender(0);
		profile.setDateOfBirth(sdf.format(new Date()));
		profile.setDistanceUnit(0);
		profile.setWeightUnit(0);
		profile.setUserHeightInches(1);
		return profile;
	}

	public static BaseResult getExchangeMetawatch(String url) {
		ServiceResponse response = MVPApi.get(url, port, null);
		return new BaseResult(response);
	}

	public static BaseResult signInMetawatch(String userId, String email,
			String password, boolean isShareProfile) {
		// String url = baseAddress + "users/session/metawatch";
		String redirect_uri = baseAddress
				+ "metawatch/exchange&scope=public,email,birthday,measurements,tracking,sessions,sleeps";
		String url = baseAddress
				+ "api/users/session/metawatch?response_type=code&client_id=metawatch&redirect_uri="
				+ redirect_uri;
		if (isShareProfile == false) {
			url = baseAddress + "api/users/session/metawatch";
		}
		BaseParams requestInfo = new MetawatchBaseParams();
		requestInfo.addHeader("user_id", userId);
		requestInfo.addParam("email", email);
		requestInfo.addParam("password", password);

		ServiceResponse response = MVPApi.post(url, port, requestInfo);
		return new BaseResult(response);
	}

	public static void main(String[] args) {
		// registerMetawatch("mytoken", "thinh");
		String email = "test" + System.currentTimeMillis() + "@misfitqa.com";
		System.out.println("LOG [MetawatchApi.main]: email: " + email);
		// signUpMetawatch(email, "misfit1");

		// OpenAPI.logIn("thinh@misfitwearables.com", "misfit1");
	}
}
