package com.misfit.ta.backend.aut.correctness.backendapi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.correctness.servercalculation.BackendServerCalculationBase;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalRawData;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.goal.TripleTapData;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.ActivitySessionItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.common.MVPEnums;
import com.misfit.ta.common.Verify;
import com.misfit.ta.utils.ShortcutsTyper;

public class BackendTripleTapForShine extends BackendServerCalculationBase{

	protected int delayTime = 60000;
	protected long timestamp = System.currentTimeMillis() / 1000;
	String token = "";
	// Calendar instance
	Calendar cal = Calendar.getInstance();
	boolean testPassed = true;

	@BeforeClass(alwaysRun = true)
	public void setUp() {

		// sign up and create items
		String email = MVPApi.generateUniqueEmail();
		token = MVPApi.signUp(email, "qqqqqq").token;
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp,
				null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(token, profile);
		MVPApi.createStatistics(token, statistics);
	}
	
	@Test(groups = { "MVPBackend", "servercalculation", "TripleTap" })
	public void ChangTrippleTapMultipleTimes(){
		testPassed = true;
		// Create goal for today
		Goal goal = Goal.getDefaultGoal();
		
		// Timestamp tripple tap at 8:59AM to change to tennis in goal
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 0);
		
		List<TripleTapData> tripleTapTypeChange = new ArrayList<TripleTapData>();
		TripleTapData tripleTapDefault = new TripleTapData();
		tripleTapDefault.setActivityType(MVPEnums.ACTIVITY_TENNIS);
		tripleTapDefault.setTimestamp(cal.getTimeInMillis() / 1000);
		tripleTapTypeChange.add(tripleTapDefault);
		
		// Timestamp tripple tap at 9:34AM to change to swimming in goal
		cal.set(Calendar.HOUR_OF_DAY, 9);
		cal.set(Calendar.MINUTE, 34);
		cal.set(Calendar.SECOND, 0);
		
		tripleTapDefault = new TripleTapData();
		tripleTapDefault.setActivityType(MVPEnums.ACTIVITY_SWIMMING);
		tripleTapDefault.setTimestamp(cal.getTimeInMillis() / 1000);
		tripleTapTypeChange.add(tripleTapDefault);

		// Timestamp tripple tap at 2:44PM to change to basketball in goal
		cal.set(Calendar.HOUR_OF_DAY, 14);
		cal.set(Calendar.MINUTE, 44);
		cal.set(Calendar.SECOND, 0);
		
		tripleTapDefault = new TripleTapData();
		tripleTapDefault.setActivityType(MVPEnums.ACTIVITY_BASKETBALL);
		tripleTapDefault.setTimestamp(cal.getTimeInMillis() / 1000);
		tripleTapTypeChange.add(tripleTapDefault);
		
		// Timestamp tripple tap at 4:44PM to change to soccer again in goal
		cal.set(Calendar.HOUR_OF_DAY, 16);
		cal.set(Calendar.MINUTE, 44);
		cal.set(Calendar.SECOND, 0);
		
		tripleTapDefault = new TripleTapData();
		tripleTapDefault.setActivityType(MVPEnums.ACTIVITY_SOCCER);
		tripleTapDefault.setTimestamp(cal.getTimeInMillis() / 1000);
		tripleTapTypeChange.add(tripleTapDefault);
		
		goal.setTripleTapTypeChanges(tripleTapTypeChange);
		
		GoalsResult goalResult = MVPApi.createGoal(token, goal);
		goal.setServerId(goalResult.goals[0].getServerId());
		goal.setUpdatedAt(goalResult.goals[0].getUpdatedAt());
		
		GoalRawData data = new GoalRawData();
		int[] triple_tap_minutes = new int[] {9 * 60, 9 * 60 + 35, 14 * 60 + 45, 16 * 60 + 45};
		data.setTriple_tap_minutes(triple_tap_minutes);
		// empty raw data from 0 - 7AM 
		data.appendGoalRawData(generateEmptyRawData(0, 7 * 60));
		// running from 7 - 7:30AM
		// timestamp at 7AM
		cal.set(Calendar.HOUR_OF_DAY, 7);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long timeStampRunning = cal.getTimeInMillis() / 1000;
		data.appendGoalRawData(generateSessionRawData(3030, 300, 30));
		// empty raw data from 7:30 - 9AM
		data.appendGoalRawData(generateEmptyRawData(7 * 60 + 30, 9 * 60));
		// tennis from 9 - 9:15AM
		// timestamp at 9AM
		cal.set(Calendar.HOUR_OF_DAY, 9);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long timestampTennis = cal.getTimeInMillis() / 1000;
		data.appendGoalRawData(generateSessionRawData(1650, 500, 15));
		// empty raw data  from 9:15 - 9:35AM
		data.appendGoalRawData(generateEmptyRawData(9 * 60 + 15, 9 * 60 + 35));
		// swimming from 9:35 - 9:45AM
		cal.set(Calendar.HOUR_OF_DAY, 9);
		cal.set(Calendar.MINUTE, 35);
		cal.set(Calendar.SECOND, 0);
		long timestampSwimming = cal.getTimeInMillis() / 1000;
		data.appendGoalRawData(generateSessionRawData(1110, 500, 10));
		// empty raw data from 9:45 - 13PM
		data.appendGoalRawData(generateEmptyRawData(9 * 60 + 45, 13 * 60));
		// running from 13 - 13:30PM
		cal.set(Calendar.HOUR_OF_DAY, 13);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long timestampRunning1 = cal.getTimeInMillis() / 1000;
		data.appendGoalRawData(generateSessionRawData(3000, 300, 30));
		// empty raw data from 13:30 - 14:45PM
		data.appendGoalRawData(generateEmptyRawData(13 * 60 + 30, 14 * 60 + 45));
		// basketball from 14:45 - 15PM
		cal.set(Calendar.HOUR_OF_DAY, 14);
		cal.set(Calendar.MINUTE, 45);
		cal.set(Calendar.SECOND, 0);
		long timestampBasketball = cal.getTimeInMillis() / 1000;
		data.appendGoalRawData(generateSessionRawData(1800, 600, 15));
		// empty raw data from 15 - 16:45PM
		data.appendGoalRawData(generateEmptyRawData(15 * 60, 16 * 60 + 45));
		// soccer from 16:45 - 17PM
		cal.set(Calendar.HOUR_OF_DAY, 16);
		cal.set(Calendar.MINUTE, 45);
		cal.set(Calendar.SECOND, 0);
		long timestampSoccer = cal.getTimeInMillis() / 1000;
		data.appendGoalRawData(generateSessionRawData(1950, 800, 15));
		
		// push rawData
		MVPApi.pushRawData(token, goal.getServerId(), data, 0);
		ShortcutsTyper.delayTime(delayTime);
		
		List<TimelineItem> timelineItemList = MVPApi.getTimelineItems(token, goal.getStartTime(), goal.getEndTime(), 0l);
		goal = MVPApi.getGoal(token, goal.getServerId()).goals[0];
		
		testPassed &= Verify.verifyEquals(
				getNumberOfSessionTiles(timelineItemList), 6,
				"Goal has only 1 session tile") == null;

		for(int i = 0; i < timelineItemList.size(); i++){
			int itemType = timelineItemList.get(i).getItemType();
			if(itemType == TimelineItemDataBase.TYPE_SESSION){
				ActivitySessionItem item = (ActivitySessionItem)timelineItemList.get(i).getData();
				long timeStamp = timelineItemList.get(i).getTimestamp();
				if(timeStamp == timestampSoccer){
					testPassed &= Verify.verifyEquals(item.getActivityType(), MVPEnums.ACTIVITY_SOCCER, "Not the soccer session!") == null;
				}else if(timeStamp == timeStampRunning || timeStamp == timestampRunning1){
					testPassed &= Verify.verifyEquals(item.getActivityType(), MVPEnums.ACTIVITY_RUNNING, "Not the running session!") == null;
				}else if(timeStamp == timestampTennis){
					testPassed &= Verify.verifyEquals(item.getActivityType(), MVPEnums.ACTIVITY_TENNIS, "Not the tennis session!") == null;
				}else if(timeStamp == timestampBasketball){
					testPassed &= Verify.verifyEquals(item.getActivityType(), MVPEnums.ACTIVITY_BASKETBALL, "Not the basketball session!") == null;
				}else if(timeStamp == timestampSwimming){
					testPassed &= Verify.verifyEquals(item.getActivityType(), MVPEnums.ACTIVITY_SWIMMING, "Not the swimming session!") == null;
				}
			}
		}
		
		Assert.assertTrue(testPassed, "All asserts are passed");
	}
	
	@Test(groups = { "MVPBackend", "servercalculation", "TripleTap" })
	public void ChangeTrippleTapOneTimeInSettings(){
		testPassed = true;
		
		// Create goal for today
		Goal goal = Goal.getDefaultGoal();
		
		// Timestamp tripple tap at 5:59AM current day to change to tennis in goal
		cal.set(Calendar.HOUR_OF_DAY, 5);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 0);
		
		List<TripleTapData> tripleTapTypeChange = new ArrayList<TripleTapData>();
		TripleTapData tripleTapDefault = new TripleTapData();
		tripleTapDefault.setActivityType(MVPEnums.ACTIVITY_TENNIS);
		tripleTapDefault.setTimestamp(cal.getTimeInMillis() / 1000);
		tripleTapTypeChange.add(tripleTapDefault);
		goal.setTripleTapTypeChanges(tripleTapTypeChange);
		
		GoalsResult goalResult = MVPApi.createGoal(token, goal);
		goal.setServerId(goalResult.goals[0].getServerId());
		goal.setUpdatedAt(goalResult.goals[0].getUpdatedAt());
		
		GoalRawData data = new GoalRawData();
		int[] triple_tap_minutes = new int[] {6 * 60, 6 * 60 + 5};
		data.setTriple_tap_minutes(triple_tap_minutes);
		// empty raw data from 0 - 6AM
		data.appendGoalRawData(generateEmptyRawData(0, 6 * 60));
		// steps : 500, points : 50, duration : 5 mins
		data.appendGoalRawData(generateSessionRawData(500, 50, 5));
		// steps : 1100, points : 100, duration : 10 mins
		data.appendGoalRawData(generateSessionRawData(1100, 100, 10));
		
		MVPApi.pushRawData(token, goal.getServerId(), data, 0);
		ShortcutsTyper.delayTime(delayTime);
		
		List<TimelineItem> timelineItemList = MVPApi.getTimelineItems(token, goal.getStartTime(), goal.getEndTime(), 0l);
		goal = MVPApi.getGoal(token, goal.getServerId()).goals[0];
		
		testPassed &= Verify.verifyEquals(
				getNumberOfSessionTiles(timelineItemList), 1,
				"Goal has only 1 session tile") == null;

		for(int i = 0; i < timelineItemList.size(); i++){
			int itemType = timelineItemList.get(i).getItemType();
			if(itemType == TimelineItemDataBase.TYPE_SESSION){
				ActivitySessionItem item = (ActivitySessionItem)timelineItemList.get(i).getData();
				testPassed &= Verify.verifyEquals(item.getActivityType(), MVPEnums.ACTIVITY_TENNIS, "Not the tennis session!") == null;
			}
		}
		Assert.assertTrue(testPassed, "All asserts are passed");
	}
}
