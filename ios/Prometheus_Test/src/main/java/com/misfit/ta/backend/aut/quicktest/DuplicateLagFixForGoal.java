package com.misfit.ta.backend.aut.quicktest;

import org.testng.Assert;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.ProgressData;

public class DuplicateLagFixForGoal {

	public static void main(String[] args) {
		
		runTest();
	}

	public static void assertGoals(Goal expected, Goal acutal) {

		Assert.assertEquals(expected.getServerId(), acutal.getServerId(), "Server id");
		Assert.assertEquals(expected.getLocalId(), acutal.getLocalId(), "Local id");
		Assert.assertEquals(expected.getUpdatedAt(), acutal.getUpdatedAt(), "Updated at");
		Assert.assertEquals(expected.getValue(), acutal.getValue(), "Value");
		Assert.assertEquals(expected.getStartTime(), acutal.getStartTime(), "Start time");
		Assert.assertEquals(expected.getEndTime(), acutal.getEndTime(), "End time");
		Assert.assertEquals(expected.getProgressData().getPoints(), acutal.getProgressData().getPoints(), "Progress points");
	}

	public static void runTest() {
		
		// create user
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		int numberOfGoals = 10;
		int numberOfUpdate = 10;
		int numberOfRetry = 1;


		// create goal
		long timestamp = System.currentTimeMillis() / 1000;
		for(int i = numberOfGoals - 1; i >= 0; i--) {

			Goal goal = Goal.getDefaultGoal(timestamp - i * 3600 * 24);
			BaseResult result = MVPApi.createGoal(token, goal);

			Goal tmp = Goal.getGoal(result.response);
			goal.setUpdatedAt(tmp.getUpdatedAt());
			goal.setServerId(tmp.getServerId());

			// query
			for(int j = 0; j < numberOfRetry; j++) {

				// user info
				result = MVPApi.userInfo(token);
				Goal latestGoal = Goal.getGoal(result.response);

				assertGoals(latestGoal, goal);

				// search goal by start time end time
				result = MVPApi.searchGoal(token, 0l, Long.MAX_VALUE, null);
				Goal[] goals = Goal.getGoals(result.response);

				latestGoal = goals[0];
				Assert.assertEquals(goals.length, numberOfGoals - i, "Number of goals");
				assertGoals(latestGoal, goal);

				// search goal by since time
				result = MVPApi.searchGoal(token, null, null, 0l);
				goals = Goal.getGoals(result.response);

				latestGoal = goals[0];
				Assert.assertEquals(goals.length, numberOfGoals - i, "Number of goals");
				assertGoals(latestGoal, goal);
			}
		}


		// update goal
		Goal goal = Goal.getGoal(MVPApi.userInfo(token).response);
		for(int i = 1; i <= numberOfUpdate; i++) {

			Goal tmp = new Goal();
			tmp.setProgressData(new ProgressData());
			tmp.getProgressData().setPoints(100d * i);
			tmp.setServerId(goal.getServerId());

			goal.getProgressData().setPoints(100d * i);
			tmp = Goal.getGoal(MVPApi.updateGoal(token, tmp).response);
			goal.setUpdatedAt(tmp.getUpdatedAt());

			// query
			for(int j = 0; j < numberOfRetry; j++) {

				// user info
				BaseResult result = MVPApi.userInfo(token);
				Goal latestGoal = Goal.getGoal(result.response);

				assertGoals(latestGoal, goal);

				// search goal by start time end time
				result = MVPApi.searchGoal(token, 0l, Long.MAX_VALUE, null);
				Goal[] goals = Goal.getGoals(result.response);

				latestGoal = goals[0];
				assertGoals(latestGoal, goal);

				// search goal by since time
				result = MVPApi.searchGoal(token, null, null, 0l);
				goals = Goal.getGoals(result.response);

				latestGoal = goals[0];
				assertGoals(latestGoal, goal);
			}
		}
	}
}
