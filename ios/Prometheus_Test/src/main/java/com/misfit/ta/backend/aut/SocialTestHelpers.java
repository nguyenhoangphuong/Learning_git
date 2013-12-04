package com.misfit.ta.backend.aut;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.social.SocialUserBase;

public class SocialTestHelpers {

	protected static Logger logger = Util.setupLogger(SocialTestHelpers.class);
	
	// helpers
	public static void printUsers(SocialUserBase[] users) {
		
		logger.info("-----------------------------------------------------------------------");
		for(SocialUserBase user : users) {
			logger.info(user.toJson().toString());
		}
		logger.info("-----------------------------------------------------------------------\n\n");
	}
	
	public static HashMap<String, HashMap<String, Object>> getSocialInitialTestData() {
		
		// NOTE:
		// mfwcqa.social makes friend with all other accounts
		// all other accounts make friend with only mfwcqa.social
		
		// connect facebook
		String misfitEmail = "mfwcqa.social@gmail.com";
		String tungEmail = MVPApi.generateUniqueEmail();
		String thyEmail = MVPApi.generateUniqueEmail();
				
		String misfitToken = MVPApi.signIn(misfitEmail, "qqqqqq").token;
		String tungToken = MVPApi.signUp(tungEmail, "qqqqqq").token;
		String thyToken = MVPApi.signUp(thyEmail, "qqqqqq").token;
		
		
		// create profile
		ProfileData tungProfile = DataGenerator.generateRandomProfile(System.currentTimeMillis(), null);
		ProfileData thyProfile = DataGenerator.generateRandomProfile(System.currentTimeMillis(), null);
		MVPApi.createProfile(tungToken, tungProfile);
		MVPApi.createProfile(thyToken, thyProfile);

		
		// prepare test data
		HashMap<String, HashMap<String, Object>> mapNameData = new HashMap<String, HashMap<String,Object>>();
		
		HashMap<String, Object> misfitData = new HashMap<String, Object>();
		misfitData.put("fuid", "528590845138104fa4000338");
		misfitData.put("email", misfitEmail);
		misfitData.put("token", misfitToken);
		misfitData.put("isUsingApp", true);
		
		HashMap<String, Object> tungData = new HashMap<String, Object>();
		tungData.put("fuid", MVPApi.getUserId(tungToken));
		tungData.put("email", tungEmail);
		tungData.put("token", tungToken);
		tungData.put("isUsingApp", true);
		
		HashMap<String, Object> thyData = new HashMap<String, Object>();
		thyData.put("fuid", MVPApi.getUserId(thyToken));
		thyData.put("email", thyEmail);
		thyData.put("token", thyToken);
		thyData.put("isUsingApp", true);
	
		
		// map data
		mapNameData = new HashMap<String, HashMap<String, Object>>();
		mapNameData.put("misfit", misfitData);
		mapNameData.put("tung", tungData);
		mapNameData.put("thy", thyData);
				
		return mapNameData;
	}

	public static void restoreSocialTestData(HashMap<String, HashMap<String, Object>> testData) {
		
		// TODO:
	}

}
