package com.misfit.ta.ios.modelapi.social.board;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.ProgressData;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.social.LeaderboardView;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;

public class LeaderboardAPI extends ModelAPI {
	
	protected static final Logger logger = Util.setupLogger(LeaderboardAPI.class);
	
	private String emailA = "aut_sociala@a.a";
	private String emailB = "aut_socialb@a.a";
	private String emailC = "aut_socialc@a.a";
	
	private String handleA = "aut_sociala";
	private String handleB = "aut_socialb";
	private String handleC = "aut_socialc";
	
	private String tokenA, tokenB, tokenC;
	private Goal todayGoalA;
	
	public LeaderboardAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	
	protected Goal getOrCreateEmptyGoal(String token, long timestamp) {
		
		long startDayEpoch = MVPApi.getDayStartEpoch(timestamp);
		long endDayEpoch = MVPApi.getDayEndEpoch(timestamp);
		Goal[] goals = MVPApi.searchGoal(token, startDayEpoch, endDayEpoch, 0l).goals;
		
		// if no result, create one with progress == 0
		if(goals == null || goals.length == 0) {
			
			Goal goal = Goal.getDefaultGoal(timestamp);
			Goal goal_meta = MVPApi.createGoal(token, goal).goals[0];
			goal.setServerId(goal_meta.getServerId());
			
			return goal;
		}
		
		// else update current goal with progress == 0
		Goal goal = goals[0];
		goal.setProgressData(ProgressData.getDefaultProgressData());

		MVPApi.updateGoal(token, goal);
		
		return goal;
	}
	
	protected Goal getOrCreateEmptyGoalForToday(String token) {
		
		long timestamp = System.currentTimeMillis() / 1000;
		return getOrCreateEmptyGoal(token, timestamp);
	}
	
	protected Goal getOrCreateEmptyGoalForYesterday(String token) {
		
		long timestamp = System.currentTimeMillis() / 1000 - 3600 * 24;
		return getOrCreateEmptyGoal(token, timestamp);
	}
	
	protected void updateGoalWithNewPoint(String token, Goal goal, double newPoint) {
		
		goal.getProgressData().setPoints(newPoint * 2.5);
		MVPApi.updateGoal(token, goal);
	}
	

	
	public void e_init() {
		
		/*
		 * Scenerio:
		 * - Sign up 3 accounts: A, B, C
		 * - Set up yesterday and today' progress:
		 *   Yesterday: 500, 300, 0
		 *   Today: 0, 0, 100
		 * - Sign in A and check leaderboard
		 * - Sign in B
		 * - Input 200 points for B
		 * - Check leaderboard
		 * - Input 300 points for A and pull to refresh
		 * - Check updated leaderboard
		 * - Sign in account C and check leaderboard
		 */
		
		tokenA = MVPApi.signIn(emailA, "qqqqqq").token;
		tokenB = MVPApi.signIn(emailB, "qqqqqq").token;
		tokenC = MVPApi.signIn(emailC, "qqqqqq").token;
		
		Goal yesterdayGoalA = getOrCreateEmptyGoalForYesterday(tokenA);
		Goal yesterdayGoalB = getOrCreateEmptyGoalForYesterday(tokenB);
		getOrCreateEmptyGoalForYesterday(tokenC);
		
		todayGoalA = getOrCreateEmptyGoalForToday(tokenA);
		getOrCreateEmptyGoalForToday(tokenB);
		Goal todayGoalC = getOrCreateEmptyGoalForToday(tokenC);
		
		// update points
		updateGoalWithNewPoint(tokenA, yesterdayGoalA, 500);
		updateGoalWithNewPoint(tokenB, yesterdayGoalB, 300);
		updateGoalWithNewPoint(tokenC, todayGoalC, 100);
	}
	
	public void e_signInAccountA() {
	
		PrometheusHelper.signIn(emailA, "qqqqqq");
	}
	
	public void e_signInAccountB() {

		PrometheusHelper.signIn(emailB, "qqqqqq");
	}

	public void e_signInAccountC() {

		PrometheusHelper.signIn(emailC, "qqqqqq");
	}
	
	public void e_goToLeaderboard() {
		
		HomeScreen.tapLeaderboard();
		LeaderboardView.waitForNoFriendToDissapear();
	}
	
	public void e_inputActivityForB() {
		
		// input 200 points for B
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(new String[] {"12", "10", "AM"}, 20, 2000);
		HomeScreen.tapSave();
	}
	
	public void e_updateActivityForA() {
		
		updateGoalWithNewPoint(tokenA, todayGoalA, 300);
	}
	
	public void e_pullToRefresh() {
		
		LeaderboardView.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingEtcLabel);
	}
	
	public void e_signOut() {

		PrometheusHelper.signOut();
	}
	
	
	
	public void v_LaunchScreen() {
		
		Assert.assertTrue(LaunchScreen.isAtLaunchScreen() || LaunchScreen.isAtInitialScreen(), "Current view is start up view");
	}
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen");
	}
	
	public void v_LeaderboardA() {
		
		LeaderboardView.tapYesterday();
		Assert.assertEquals(LeaderboardView.getPointOfUser(handleA), "500", "A's yesterday points");
		Assert.assertEquals(LeaderboardView.getPointOfUser(handleB), "300", "B's yesterday points");
		Assert.assertEquals(LeaderboardView.getPointOfUser(handleC), "-", "C's yesterday points");
		
		Assert.assertEquals(LeaderboardView.getRankOfUser(handleA), "1", "A's yesterday rank");
		Assert.assertEquals(LeaderboardView.getRankOfUser(handleB), "2", "B's yesterday rank");
		Assert.assertEquals(LeaderboardView.getRankOfUser(handleC), "", "C's yesterday rank");
		
		LeaderboardView.tapToday();
		Assert.assertEquals(LeaderboardView.getPointOfUser(handleA), "-", "A's today points");
		Assert.assertEquals(LeaderboardView.getPointOfUser(handleB), "-", "B's today points");
		Assert.assertEquals(LeaderboardView.getPointOfUser(handleC), "100", "C's today points");
		
		Assert.assertEquals(LeaderboardView.getRankOfUser(handleA), "", "A's today rank");
		Assert.assertEquals(LeaderboardView.getRankOfUser(handleB), "", "B's today rank");
		Assert.assertEquals(LeaderboardView.getRankOfUser(handleC), "1", "C's today rank");
	}
	
	public void v_LeaderboardB() {

		LeaderboardView.tapToday();
		Assert.assertEquals(LeaderboardView.getPointOfUser(handleA), "-", "A's today points");
		Assert.assertEquals(LeaderboardView.getPointOfUser(handleB), "200", "B's today points");
		Assert.assertEquals(LeaderboardView.getPointOfUser(handleC), "100", "C's today points");
		
		Assert.assertEquals(LeaderboardView.getRankOfUser(handleA), "", "A's today rank");
		Assert.assertEquals(LeaderboardView.getRankOfUser(handleB), "1", "B's today rank");
		Assert.assertEquals(LeaderboardView.getRankOfUser(handleC), "2", "C's today rank");
	}

	public void v_LeaderboardBUpdated() {

		LeaderboardView.tapToday();
		Assert.assertEquals(LeaderboardView.getPointOfUser(handleA), "300", "A's today points");
		Assert.assertEquals(LeaderboardView.getPointOfUser(handleB), "200", "B's today points");
		Assert.assertEquals(LeaderboardView.getPointOfUser(handleC), "100", "C's today points");
		
		Assert.assertEquals(LeaderboardView.getRankOfUser(handleA), "1", "A's today rank");
		Assert.assertEquals(LeaderboardView.getRankOfUser(handleB), "2", "B's today rank");
		Assert.assertEquals(LeaderboardView.getRankOfUser(handleC), "3", "C's today rank");
	}
	
}
