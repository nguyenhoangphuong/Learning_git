package com.misfit.ta.backend.aut.test;

import java.util.Random;
import java.util.Vector;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.data.AccountResult;
import com.misfit.ta.backend.data.GoalsResult;
import com.misfit.ta.backend.data.goal.ProgressData;

public class BackendGoalTest {
	private int goalValue = 2500;
	private String localId = "mylocalid";
	private int level = 0;

//	@Test(groups = { "backend_unit_test" })
	public void createAndUpdateGoal() {
		AccountResult account = BackendHelper.signUp();
		String token = account.token;
		long now = System.currentTimeMillis();
		Vector<Integer> points = new Vector<Integer>();
		points.add(25);
		points.add(58);
		points.add(12);
		int steps = 100;
		int seconds = 200;
		ProgressData progressData = new ProgressData(points, steps, seconds);
		GoalsResult goalResult = MVPApi.createGoal(token, goalValue, now,
				now + 8400, 0, progressData, localId, level, now);
		Assert.assertTrue(goalResult.isOK(),
				"Create goal: Status code is not 200: " + goalResult.statusCode);
		verifyGoalResult(token, points, steps, seconds, goalResult);

		points.add(543);
		points.add(134);
		points.add(3);
		steps += 800;
		seconds += 500;
		now = System.currentTimeMillis();
		progressData = new ProgressData(points, steps, seconds);
		goalResult = MVPApi.updateGoal(token, now,
				goalResult.goals[0].getServerId(), goalValue, now, now + 8400,
				0, progressData, token, level);
		Assert.assertTrue(goalResult.isExisted(),
				"Update goal: Status code is not 210: " + goalResult.statusCode);
		verifyGoalResult(token, points, steps, seconds, goalResult);
	}

	@Test(groups = { "backend_unit_test" })
	public void createAndUpdateGoalSeveralTimes() {
		AccountResult account = BackendHelper.signUp();
		String token = account.token;
		long now = System.currentTimeMillis();
		Vector<Integer> points = new Vector<Integer>();
		points.add(25);
		points.add(58);
		points.add(12);
		int steps = 100;
		int seconds = 200;
		ProgressData progressData = new ProgressData(points, steps, seconds);
		GoalsResult goalResult = MVPApi.createGoal(token, goalValue, now,
				now + 8400, 0, progressData, localId, level, now);
		Assert.assertTrue(goalResult.isOK(),
				"Create goal: Status code is not 200: " + goalResult.statusCode);
		verifyGoalResult(token, points, steps, seconds, goalResult);
		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < 100; i++) {
			points.add(rand.nextInt(50));
			steps += rand.nextInt(70);
			seconds += rand.nextInt(2000);
			now = System.currentTimeMillis();
			progressData = new ProgressData(points, steps, seconds);
			goalResult = MVPApi.updateGoal(token, now,
					goalResult.goals[0].getServerId(), goalValue, now,
					now + 8400, 0, progressData, token, level);
			Assert.assertTrue(goalResult.isExisted(),
					"Update goal: Status code is not 210: "
							+ goalResult.statusCode);
			verifyGoalResult(token, points, steps, seconds, goalResult);
		}
	}

	private void verifyGoalResult(String token, Vector<Integer> expectedPoints,
			int steps, int seconds, GoalsResult goalResult) {
		goalResult = MVPApi.getGoal(token, goalResult.goals[0].getServerId());
		Assert.assertTrue(goalResult.isOK(), "Status code is not 200: "
				+ goalResult.statusCode);
		Assert.assertTrue(goalValue == (int) goalResult.goals[0].getValue(),
				"Goal value is inccorect.");
		Assert.assertTrue(steps == goalResult.goals[0].getProgressData()
				.getSteps(), "Total steps value is incorrect.");
		Assert.assertTrue(seconds == goalResult.goals[0].getProgressData()
				.getSeconds(), "Total seconds value is incorrect.");
		boolean flag = true;
		Vector<Integer> resultPoints = goalResult.goals[0].getProgressData()
				.getPoints();
		Assert.assertTrue(expectedPoints.size() == resultPoints.size(),
				"Progress data size is incorrect.");
		for (int i = 0; i < resultPoints.size(); i++) {
			if (!resultPoints.get(i).equals(expectedPoints.get(i))) {
				flag = false;
				break;
			}
		}
		Assert.assertTrue(flag, "Progress data is incorrect.");
	}

}
