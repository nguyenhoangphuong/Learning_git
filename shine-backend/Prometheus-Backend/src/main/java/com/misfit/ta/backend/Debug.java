package com.misfit.ta.backend;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.profile.ProfileData;

public class Debug {

	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws FileNotFoundException {
		
		String token = MVPApi.signUp("shine_backend_smoke_test_you@qa.com", "qqqqqq").token;
		ProfileData profile = new ProfileData();
		profile.setHandle("shine_backend_smoke_test_you");
		profile.setName("Shine Backend Smoke Test You");
		
		MVPApi.createProfile(token, DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null));
		MVPApi.updateProfile(token, profile);
		
//		BackendHelper.unlink(token);
//		BackendHelper.link(token, "HaiDangYeu");
//		Goal goal = Goal.getDefaultGoal(631126800l);
//		MVPApi.createGoal(token, goal);
		
		
//		BackendHelper.link("haidangyeu@qa.com", "qqqqqq", "HaiDangYeu");
//		ServerCalculationTestHelpers.createTest("tests/test0", "haidangyeu@qa.com", 17, 4, 2014, 19, 4, 2014);
		
	}
}
