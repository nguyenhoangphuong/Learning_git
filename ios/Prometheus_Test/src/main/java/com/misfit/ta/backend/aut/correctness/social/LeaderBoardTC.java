package com.misfit.ta.backend.aut.correctness.social;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.SocialAutomationBase;
import com.misfit.ta.backend.aut.SocialTestHelpers;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.social.Leaderboard;
import com.misfit.ta.backend.data.social.SocialUserLeaderBoardEvent;

public class LeaderBoardTC extends SocialAutomationBase {


	// fields
	protected Goal misfitTodayGoal = null;
	protected Goal tungTodayGoal = null;
	protected Goal thyTodayGoal = null;
	
	protected Goal misfitYesterdayGoal = null;
	protected Goal tungYesterdayGoal = null;
	protected Goal thyYesterdayGoal = null;
	
	
	// test helpers
	protected Goal getOrCreateEmptyGoal(String token, long timestamp) {
		
		long startDayEpoch = MVPApi.getDayStartEpoch(timestamp);
		Goal[] goals = MVPApi.searchGoal(token, startDayEpoch, Integer.MAX_VALUE, 0).goals;
		
		// if no result, create one with progress == 0
		if(goals == null || goals.length == 0) {
			
			Goal goal = DefaultValues.DefaultGoal();
			goal.setValue(1000 * 2.5d);
			goal.getProgressData().setPoints(0d);
			goal.getProgressData().setCalorie(0d);
			goal.getProgressData().setDistanceMiles(0d);
			goal.getProgressData().setFullBmrCalorie(0);
			goal.getProgressData().setSeconds(0);
			goal.getProgressData().setSteps(0);
			
			Goal goal_meta = MVPApi.createGoal(token, goal).goals[0];
			goal.setServerId(goal_meta.getServerId());
			
			return goal;
		}
		
		// else update current goal with progress == 0
		Goal goal = goals[0];
		goal.setValue(1000 * 2.5d);
		goal.getProgressData().setPoints(0d);
		goal.getProgressData().setCalorie(0d);
		goal.getProgressData().setDistanceMiles(0d);
		goal.getProgressData().setFullBmrCalorie(0);
		goal.getProgressData().setSeconds(0);
		goal.getProgressData().setSteps(0);
		
		Goal goal_meta = MVPApi.createGoal(token, goal).goals[0];
		goal.setServerId(goal_meta.getServerId());
		
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
		
		goal.getProgressData().setPoints(newPoint);
		MVPApi.updateGoal(token, goal);
	}
	
	
	// set up / clean up test
	@Override
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {

		super.beforeClass();
		
		// create goals with progress = 0
		misfitTodayGoal = getOrCreateEmptyGoalForToday(misfitToken);
		tungTodayGoal = getOrCreateEmptyGoalForToday(tungToken);
		thyTodayGoal = getOrCreateEmptyGoalForToday(thyToken);
		
		misfitYesterdayGoal = getOrCreateEmptyGoalForYesterday(misfitToken);
		tungYesterdayGoal = getOrCreateEmptyGoalForYesterday(tungToken);
		thyYesterdayGoal = getOrCreateEmptyGoalForYesterday(thyToken);
		
		// add friends
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.sendFriendRequest(misfitToken, thyUid);
		
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		SocialAPI.acceptFriendRequest(thyToken, misfitUid);
	}
	
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		
		// delete friends
		SocialAPI.deleteFriend(misfitToken, tungUid);
		SocialAPI.deleteFriend(misfitToken, thyUid);
	}
	
	
    // test methods
    @Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "Leaderboard", "TodayEvents" })
    public void LeaderBoardTodayTest() {

        // set point to misfit (500), tung (300) and thy (100)
    	updateGoalWithNewPoint(misfitToken, misfitTodayGoal, 500);
    	updateGoalWithNewPoint(tungToken, tungTodayGoal, 300);
    	updateGoalWithNewPoint(thyToken, thyTodayGoal, 100);

        // query leaderboard
        BaseResult result = SocialAPI.getLeaderboardInfo(misfitToken);
        Leaderboard board = Leaderboard.fromResponse(result.response);

        List<SocialUserLeaderBoardEvent> today = board.getToday();
        SocialUserLeaderBoardEvent[] records = today.toArray(new SocialUserLeaderBoardEvent[today.size()]);
        SocialTestHelpers.printUsers(records);
        
        // check leaderboard detail
        Assert.assertEquals(records[0].getName(), "Misfit Social", "1st user's name");
        Assert.assertEquals(records[0].getUid(), misfitUid, "1st user's uid");
        Assert.assertEquals(records[0].getPoints(), (Integer)500, "1st user's point");
        
        Assert.assertEquals(records[1].getName(), "Tung Social", "2nd user's name");
        Assert.assertEquals(records[1].getUid(), tungUid, "2nd user's uid");
        Assert.assertEquals(records[1].getPoints(), (Integer)300, "2nd user's point");
        
        Assert.assertEquals(records[2].getName(), "Thy Social", "3rd user's name");
        Assert.assertEquals(records[2].getUid(), thyUid, "3rd user's uid");
        Assert.assertEquals(records[2].getPoints(), (Integer)100, "3rd user's point");
        
        // now thy jumps to 600 and tung jumps to 601
        updateGoalWithNewPoint(thyToken, thyTodayGoal, 600);
        updateGoalWithNewPoint(tungToken, tungTodayGoal, 601);
    	
        // query leaderboard
        result = SocialAPI.getLeaderboardInfo(misfitToken);
        board = Leaderboard.fromResponse(result.response);

        today = board.getToday();
        records = today.toArray(new SocialUserLeaderBoardEvent[today.size()]);
        SocialTestHelpers.printUsers(records);
        
        // check leaderboard detail
        Assert.assertEquals(records[0].getName(), "Tung Social", "1st user's name");
        Assert.assertEquals(records[0].getUid(), tungUid, "1st user's uid");
        Assert.assertEquals(records[0].getPoints(), (Integer)601, "1st user's point");
        
        Assert.assertEquals(records[1].getName(), "Thy Social", "2nd user's name");
        Assert.assertEquals(records[1].getUid(), thyUid, "2nd user's uid");
        Assert.assertEquals(records[1].getPoints(), (Integer)600, "2nd user's point");
        
        Assert.assertEquals(records[2].getName(), "Misfit Social", "3rd user's name");
        Assert.assertEquals(records[2].getUid(), misfitUid, "3rd user's uid");
        Assert.assertEquals(records[2].getPoints(), (Integer)500, "3rd user's point");
    }
    
    @Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "Leaderboard", "YesterdayEvents" })
    public void LeaderBoardYesterdayTest() {

        // set point to misfit (500), tung (300) and thy (100)
    	updateGoalWithNewPoint(misfitToken, misfitYesterdayGoal, 500);
    	updateGoalWithNewPoint(tungToken, tungYesterdayGoal, 300);
    	updateGoalWithNewPoint(thyToken, thyYesterdayGoal, 100);

        // query leaderboard
        BaseResult result = SocialAPI.getLeaderboardInfo(misfitToken);
        Leaderboard board = Leaderboard.fromResponse(result.response);

        List<SocialUserLeaderBoardEvent> yesterday = board.getYesterday();
        SocialUserLeaderBoardEvent[] records = yesterday.toArray(new SocialUserLeaderBoardEvent[yesterday.size()]);
        SocialTestHelpers.printUsers(records);
        
        // check leaderboard detail
        Assert.assertEquals(records[0].getName(), "Misfit Social", "1st user's name");
        Assert.assertEquals(records[0].getUid(), misfitUid, "1st user's uid");
        Assert.assertEquals(records[0].getPoints(), (Integer)500, "1st user's point");
        
        Assert.assertEquals(records[1].getName(), "Tung Social", "2nd user's name");
        Assert.assertEquals(records[1].getUid(), tungUid, "2nd user's uid");
        Assert.assertEquals(records[1].getPoints(), (Integer)300, "2nd user's point");
        
        Assert.assertEquals(records[2].getName(), "Thy Social", "3rd user's name");
        Assert.assertEquals(records[2].getUid(), thyUid, "3rd user's uid");
        Assert.assertEquals(records[2].getPoints(), (Integer)100, "3rd user's point");
    }

}
