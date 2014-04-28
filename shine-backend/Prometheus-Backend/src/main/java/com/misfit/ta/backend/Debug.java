package com.misfit.ta.backend;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.goal.Goal;

public class Debug {

	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws FileNotFoundException {
			
		String token = MVPApi.signIn("nhhai16991@gmail.com", "qqqqqq").token;
		MVPApi.searchBedditSleepSessions(token, null, null, null);
//		BackendHelper.unlink(token);
//		BackendHelper.link(token, "HaiDangYeu");
//		Goal goal = Goal.getDefaultGoal(631126800l);
//		MVPApi.createGoal(token, goal);
		
		
//		BackendHelper.link("haidangyeu@qa.com", "qqqqqq", "HaiDangYeu");
//		ServerCalculationTestHelpers.createTest("tests/test0", "haidangyeu@qa.com", 17, 4, 2014, 19, 4, 2014);
		
	}
}
