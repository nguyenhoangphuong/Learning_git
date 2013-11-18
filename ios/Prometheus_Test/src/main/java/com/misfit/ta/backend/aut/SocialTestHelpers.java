package com.misfit.ta.backend.aut;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.social.SocialAPI;
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
		String tungEmail = "tung.social.misfit@gmail.com";
		String thyEmail = "thy.social.misfit@gmail.com";
				
		String misfitToken = MVPApi.signIn(misfitEmail, "qqqqqq").token;
		String tungToken = MVPApi.signIn(tungEmail, "qqqqqq").token;
		String thyToken = MVPApi.signIn(thyEmail, "qqqqqq").token;

		
		// prepare test data
		HashMap<String, HashMap<String, Object>> mapNameData = new HashMap<String, HashMap<String,Object>>();
		
		HashMap<String, Object> misfitData = new HashMap<String, Object>();
		misfitData.put("fuid", "528590845138104fa4000338");
		misfitData.put("email", misfitEmail);
		misfitData.put("token", misfitToken);
		misfitData.put("isUsingApp", true);
		
		HashMap<String, Object> tungData = new HashMap<String, Object>();
		tungData.put("fuid", "5285e6f6513810db3e0002d7");
		tungData.put("email", tungEmail);
		tungData.put("token", tungToken);
		tungData.put("isUsingApp", true);
		
		HashMap<String, Object> thyData = new HashMap<String, Object>();
		thyData.put("fuid", "5285e645513810db3e0002cc");
		thyData.put("email", thyEmail);
		thyData.put("token", thyToken);
		thyData.put("isUsingApp", true);
	
		
		// map data
		mapNameData = new HashMap<String, HashMap<String, Object>>();
		mapNameData.put("misfit", misfitData);
		mapNameData.put("tung", tungData);
		mapNameData.put("thy", thyData);
		
		
		// delete all 'auto friends' from mfwcqa.social
		SocialAPI.deleteFriend(misfitToken, (String) tungData.get("fuid"));
		SocialAPI.deleteFriend(misfitToken, (String) thyData.get("fuid"));
		
		return mapNameData;
	}

	public static void restoreSocialTestData(HashMap<String, HashMap<String, Object>> testData) {
		
		// TODO:
	}

	public static void deleteFriendRequest(String toUserToken, String fromUserUid) {
		
		// TODO: work around to remove a pending / ignored request
		// accept first then delete
		SocialAPI.acceptFriendRequest(toUserToken, fromUserUid);
		SocialAPI.deleteFriend(toUserToken, fromUserUid);
	}
	
}
