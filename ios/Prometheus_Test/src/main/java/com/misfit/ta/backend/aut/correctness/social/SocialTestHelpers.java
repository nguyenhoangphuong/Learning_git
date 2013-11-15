package com.misfit.ta.backend.aut.correctness.social;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

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
		
		// connect facebook
		String socialEmail = "mfwcqa.social@gmail.com";
		String tungEmail = "tung+1@misfitwearables.com";
		String haiEmail = "nhhai16991@gmail.com";
		String thinhEmail = "thinh@misfitwearables.com";
		String qaEmail = "mfwcqa@misfitwearables.com";
				
		String socialAccessToken = "CAAG661ngu9YBAJBNEYIhMYsYwgOtWvIWbN6lK7Bcr77mAFPft7yDTNvCoHEUA52eyItGc3al8jOkx85k8ZBdDZAiACoF0pVNOZA5WIbX4AVISvSeiQWoeYZC4cmZCopSfgAig9DT9Rin4RXPfjf5BHdLzEZCnlTAAh9Y7ZAv40ZA83JulIBA7M5Ov5aCPYxuqrvVaO2cFnO3MnakmL6OBltqVT5HGmR21TWEflGHZATsSeQZDZD";
		String tungAccessToken = "CAAG661ngu9YBANCSXScAQWekaj5wGrAyVaYYeAtLquZA7DeT4nZA1u8rR0Cxm7qVVTZB3gPZCk3DId9FK6lZAJgCt1ZCnHcbnQfM3n5dDiAIRpfFgpQvceCu9RBQxlu1tbebIObczlfrZBwMLZBZBxQsHYXh6ABNBHfQipstNNOEYUiJqu9RGsfjH4Y1lezFCFMC9JB4rol7nXpaBGnBE8xuvFbUsZAiefBau16owhHDKtmZAPuVgJyWHz5";
		String haiAccessToken = "CAAG661ngu9YBAMAvS8j6ZC6ncT40seuPO4SJfCrCFvZAgiD0e4pZAQO06zjGvmH2yMpV900YmgISQ1d2ZCUokwjP50ZAgx0GCinDOTCxV2Nu8Db3l9ZA3DAEV1ckY1gFGVosQHhQj5nSEdNAdYAdQxsckL3bgawEWhwaOPpDqlpjoMybCP9UqFZCTaVy4iG83GV6jXxVjqP6cE1XUOZCDa6H35lJ1pDsUZCjZAZA0C7Gr8m7wZDZD";
		String thinhAccessToken = "CAAG661ngu9YBAHJgM0ZC5YfKd2fN90TTVYUe8sdRnauZAc8XacHiw5SmHiI6uOBBKC664skPwn5zYwHzszJRu0IvYP3YA3v5zZCVzTAMI7QVXwr3PjHqj8TKYJEUA6kZBcWBZBNS9QWAhKYHDXuCOVUjMr54xlG0PichZBo3xhFRFoKAkfK2uKVfbF5x9macmA17A5lZC3mgxEdH5SItrWp1hzs8MaX9EmM1gIvbUxocLudFwza3PgM";
		String qaAccessToken = "CAAG661ngu9YBAP5IJvB5apKBKAZAZBhoGnRCGBi733n9Vh4UbMlAFi3ZAZCgqbmk2QzGVONskvpnSpXrT5ZCzO9CzluYD4GEnt4e4C7IJ5LcjcyJi0KHKIuIp6nJPzH7lVsevoMR2pOcO3iGerQVdi2E1UXBnhHmPe3ZBSexMNkbQkZCK954hXLZBynsTi9dCWMNbY9WPNZBUxcGPxGwQ6b432QLIKZCIinnKGZB9WUETJEcQZDZD";
		
		String socialToken = SocialAPI.connectFacebook(socialEmail, socialAccessToken, "").token;
		String tungToken = SocialAPI.connectFacebook(tungEmail, tungAccessToken, "").token;
		String haiToken = SocialAPI.connectFacebook(haiEmail, haiAccessToken, "").token;
		String thinhToken = SocialAPI.connectFacebook(thinhEmail, thinhAccessToken, "").token;
		String qaToken = SocialAPI.connectFacebook(qaEmail, qaAccessToken, "").token;

		
		// prepare test data
		HashMap<String, HashMap<String, Object>> mapNameData = new HashMap<String, HashMap<String,Object>>();
		
		HashMap<String, Object> socialData = new HashMap<String, Object>();
		socialData.put("fuid", "528590845138104fa4000338");
		socialData.put("email", socialEmail);
		socialData.put("accessToken", socialAccessToken);
		socialData.put("token", socialToken);
		
		HashMap<String, Object> tungData = new HashMap<String, Object>();
		tungData.put("fuid", "527767e85138105a76000020");
		tungData.put("email", tungEmail);
		tungData.put("accessToken", tungAccessToken);
		tungData.put("token", tungToken);
		
		HashMap<String, Object> haiData = new HashMap<String, Object>();
		haiData.put("fuid", "51cd11d95138105d0300066d");
		haiData.put("email", haiEmail);
		haiData.put("accessToken", haiAccessToken);
		haiData.put("token", haiToken);
		
		HashMap<String, Object> thinhData = new HashMap<String, Object>();
		thinhData.put("fuid", "51b1d2c35138106d210000d8");
		thinhData.put("email", thinhEmail);
		thinhData.put("accessToken", thinhAccessToken);
		thinhData.put("token", thinhToken);
		
		HashMap<String, Object> qaData = new HashMap<String, Object>();
		qaData.put("fuid", "519ed88b9f12e53fe40001bc");
		qaData.put("email", qaEmail);
		qaData.put("accessToken", qaAccessToken);
		qaData.put("token", qaToken);
		
		
		// map data
		mapNameData = new HashMap<String, HashMap<String, Object>>();
		mapNameData.put("social", socialData);
		mapNameData.put("tung", tungData);
		mapNameData.put("hai", haiData);
		mapNameData.put("thinh", thinhData);
		mapNameData.put("qa", qaData);
		
		return mapNameData;
		
	}

	public static void restoreSocialTestData(HashMap<String, HashMap<String, Object>> testData) {
		
		// TODO:
	}
	
}
