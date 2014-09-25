package com.misfit.ta.backend;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.graphwalker.Util;


public class Debug {

	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws FileNotFoundException {
		
//		String token = MVPApi.signUp("shine_backend_smoke_test_me@qa.com", "qqqqqq").token;
//		ProfileData profile = new ProfileData();
//		profile.setHandle("shine_backend_smoke_test_me");
//		profile.setName("Shine Backend Smoke Test Me");
//		
//		MVPApi.createProfile(token, DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null));
//		MVPApi.updateProfile(token, profile);
//		long value = System.currentTimeMillis();
//		long timestamp = value / 1000 - 3600 * 24 * 1;
//
//		System.out.println("System.currentTimeMillis() : " + value/1000);
//		System.out.println("Timestamp : " + timestamp);
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		//Backward 2 months ago
		cal.setTimeInMillis(cal.getTimeInMillis() - 86400 * 60 * 1000l);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		System.out.println("Before : " + cal.get(Calendar.DAY_OF_WEEK));
		System.out.println("After : " + cal.getTime());
		while(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
		
		System.out.println("After : " + cal.getTime());
//		BackendHelper.unlink(token);
//		BackendHelper.link(token, "HaiDangYeu");
//		Goal goal = Goal.getDefaultGoal(631126800l);
//		MVPApi.createGoal(token, goal);
		
		
//		BackendHelper.link("haidangyeu@qa.com", "qqqqqq", "HaiDangYeu");
//		ServerCalculationTestHelpers.createTest("tests/test0", "haidangyeu@qa.com", 17, 4, 2014, 19, 4, 2014);
		
	}
}
