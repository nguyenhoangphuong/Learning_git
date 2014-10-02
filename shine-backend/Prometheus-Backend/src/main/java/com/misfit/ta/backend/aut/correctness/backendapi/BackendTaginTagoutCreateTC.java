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
import com.misfit.ta.common.MVPEnums;
import com.misfit.ta.common.Verify;
import com.misfit.ta.utils.ShortcutsTyper;

public class BackendTaginTagoutCreateTC extends BackendServerCalculationBase {

	protected int delayTime = 60000;
	protected long timestamp = System.currentTimeMillis() / 1000;
	String token = "";
	// Calendar instance
	Calendar cal = Calendar.getInstance();

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

	@Test(groups = { "MVPBackend", "servercalculation", "TaginTagout" })
	public void TagInTagOutWithNoData() {
		boolean testPassed = true;

		// Create Goal for today
		Goal goal = Goal.getDefaultGoal();

		// Timestamp : tripple tap at 6:59AM on current day for changing to
		// first activity : tennis
		cal.set(Calendar.HOUR_OF_DAY, 6);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 0);

		List<TripleTapData> tripleTapTypeChanges = new ArrayList<TripleTapData>();
		TripleTapData tripleTapDefault = new TripleTapData();
		tripleTapDefault.setActivityType(MVPEnums.ACTIVITY_TENNIS);
		tripleTapDefault.setTimestamp(cal.getTimeInMillis() / 1000);
		tripleTapTypeChanges.add(tripleTapDefault);
		goal.setTripleTapTypeChanges(tripleTapTypeChanges);
		
		GoalsResult result = MVPApi.createGoal(token, goal);
		goal.setServerId(result.goals[0].getServerId());
		goal.setUpdatedAt(result.goals[0].getUpdatedAt());

		// story
		GoalRawData data = new GoalRawData();
		data.appendGoalRawData(generateEmptyRawData(0, 7 * 60));
		// timestamp at 7:00AM
		cal.set(Calendar.HOUR_OF_DAY, 7);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long timestampTennisSession = cal.getTimeInMillis() / 1000;
		// points : 0, steps : 0, duration : 60, tag_in_out = [7*60, 8*60] :
		// 7AM-8AM
		int[] arrDataTaginTagout = new int[] { 7 * 60, 8 * 60 };
		List<int[]> listDataTaginTagout = new ArrayList<int[]>();
		listDataTaginTagout.add(arrDataTaginTagout);
		data.appendGoalRawData(generateSessionRawData(0, 0, 60,
				listDataTaginTagout));

		// push to server
		MVPApi.pushRawData(token, goal.getServerId(), data, 0);
		ShortcutsTyper.delayTime(delayTime);

		List<TimelineItem> timelineitems = MVPApi.getTimelineItems(token,
				goal.getStartTime(), goal.getEndTime(), 0l);
		goal = MVPApi.getGoal(token, goal.getServerId()).goals[0];

		testPassed &= Verify.verifyEquals(
				getNumberOfSessionTiles(timelineitems), 1,
				"Goal has 1 session tiles") == null;

		// session tiles are correct
		testPassed &= Verify.verifyTrue(
				hasSessionTileWithData(timelineitems, goal, 7 * 60, 60, 0),
				"Goal has session tile at 7:00 - 0 pts") == null;

		for (int i = 0; i < timelineitems.size(); i++) {
			int type = timelineitems.get(i).getItemType();
			if (type == 2) {
				TimelineItem item = timelineitems.get(i);
				ActivitySessionItem sessionData = (ActivitySessionItem) item
						.getData();
				long timestampTemp = item.getTimestamp();

				if (timestampTemp == timestampTennisSession) {
					testPassed &= Verify.verifyEquals(
							sessionData.getActivityType(),
							MVPEnums.ACTIVITY_TENNIS,
							"Not the tennis activity") == null;
				}
			}
		}

		Assert.assertTrue(testPassed, "All asserts are passed");

	}

	@Test(groups = { "MVPBackend", "servercalculation", "TaginTagout" })
	public void TagOutLessThanTagIn() {
		boolean testPassed = true;
		
		cal.set(Calendar.HOUR_OF_DAY, 5);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 0);

		// Create Goal for today
		Goal goal = Goal.getDefaultGoal();
		List<TripleTapData> tripleTapTypeChanges = new ArrayList<TripleTapData>();
		TripleTapData tripleTapDefault = new TripleTapData();
		tripleTapDefault.setActivityType(MVPEnums.ACTIVITY_SWIMMING);
		tripleTapDefault.setTimestamp(cal.getTimeInMillis() / 1000);
		tripleTapTypeChanges.add(tripleTapDefault);
		goal.setTripleTapTypeChanges(tripleTapTypeChanges);
		
		GoalsResult result = MVPApi.createGoal(token, goal);
		goal.setServerId(result.goals[0].getServerId());
		goal.setUpdatedAt(result.goals[0].getUpdatedAt());

		GoalRawData data = new GoalRawData();
		// empty raw data : 0-6AM
		data.appendGoalRawData(generateEmptyRawData(0, 6 * 60));
		// points : 0, steps : 0, duration : 20, tag_in_out = [6 * 60, 5 *
		// 60 + 45] : tag out < tag in
		// 7AM-7:30AM
		int[] arrDataTaginTagout = new int[] { 6 * 60, 5 * 60 + 45 };
		List<int[]> listDataTaginTagout = new ArrayList<int[]>();
		listDataTaginTagout.add(arrDataTaginTagout);
		data.appendGoalRawData(generateSessionRawData(200, 20, 20,
				listDataTaginTagout));

		// push to server
		MVPApi.pushRawData(token, goal.getServerId(), data, 0);
		ShortcutsTyper.delayTime(delayTime);

		List<TimelineItem> timelineitems = MVPApi.getTimelineItems(token,
				goal.getStartTime(), goal.getEndTime(), 0l);
		goal = MVPApi.getGoal(token, goal.getServerId()).goals[0];

		testPassed &= Verify.verifyEquals(
				getNumberOfSessionTiles(timelineitems), 0,
				"Goal has 0 session tiles") == null;

		Assert.assertTrue(testPassed, "All asserts are passed");
	}

	@Test(groups = { "MVPBackend", "servercalculation", "TaginTagout" })
	public void TaginNoTagout() {
		boolean testPassed = true;

		// Timestamp : tripple tap at 6:59AM on current day for changing to
		// first activity : swimming 
		cal.set(Calendar.HOUR_OF_DAY, 6);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 0);
		
		// Create Goal for today
		Goal goal = Goal.getDefaultGoal();
		List<TripleTapData> tripleTapTypeChanges = new ArrayList<TripleTapData>();
		TripleTapData tripleTapDefault = new TripleTapData();
		tripleTapDefault.setActivityType(MVPEnums.ACTIVITY_SWIMMING);
		tripleTapDefault.setTimestamp(cal.getTimeInMillis() / 1000);
		tripleTapTypeChanges.add(tripleTapDefault);
		goal.setTripleTapTypeChanges(tripleTapTypeChanges);
		
		GoalsResult result = MVPApi.createGoal(token, goal);
		goal.setServerId(result.goals[0].getServerId());
		goal.setUpdatedAt(result.goals[0].getUpdatedAt());

		GoalRawData data = new GoalRawData();
		// empty raw data : 0-7AM
		data.appendGoalRawData(generateEmptyRawData(0, 7 * 60));

		// points : 0, steps : 0, duration : 30, tag_in_out = [7 * 60]
		// 7AM-7:30AM
		int[] arrDataTaginTagout = new int[] { 7 * 60 };
		List<int[]> listDataTaginTagout = new ArrayList<int[]>();
		listDataTaginTagout.add(arrDataTaginTagout);
		data.appendGoalRawData(generateSessionRawData(0, 0, 30,
				listDataTaginTagout));

		// push to server
		MVPApi.pushRawData(token, goal.getServerId(), data, 0);
		ShortcutsTyper.delayTime(delayTime);

		List<TimelineItem> timelineitems = MVPApi.getTimelineItems(token,
				goal.getStartTime(), goal.getEndTime(), 0l);
		goal = MVPApi.getGoal(token, goal.getServerId()).goals[0];

		testPassed &= Verify.verifyEquals(
				getNumberOfSessionTiles(timelineitems), 0,
				"Goal has 0 session tiles") == null;
		
		testPassed &= Verify.verifyTrue(
				!hasSessionTileWithData(timelineitems, goal, 7 * 60 , 30, 0),
				"Having timeline item at 7:00AM in 30 minutes") == null;

		Assert.assertTrue(testPassed, "All asserts are passed");
	}

	@Test(groups = { "MVPBackend", "servercalculation", "TaginTagout" })
	public void CreateActivitySessionSuccessfully() {
		boolean testPassed = true;

		// Timestamp tripple tap first activity
		cal.set(Calendar.HOUR_OF_DAY, 6);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 0);
		// Create Goal for today
		Goal goal = Goal.getDefaultGoal();
		List<TripleTapData> tripleTapTypeChanges = new ArrayList<TripleTapData>();
		TripleTapData tripleTapDefault = new TripleTapData();
		tripleTapDefault.setActivityType(MVPEnums.ACTIVITY_SOCCER);
		tripleTapDefault.setTimestamp(cal.getTimeInMillis() / 1000);
		tripleTapTypeChanges.add(tripleTapDefault);
		goal.setTripleTapTypeChanges(tripleTapTypeChanges);
		
		GoalsResult result = MVPApi.createGoal(token, goal);
		goal.setServerId(result.goals[0].getServerId());
		goal.setUpdatedAt(result.goals[0].getUpdatedAt());

		//timestamp at 7:00AM for soccer session
		cal.set(Calendar.HOUR_OF_DAY, 7);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long timestampSoccer = cal.getTimeInMillis() / 1000;
		GoalRawData data = new GoalRawData();
		// empty raw data : 0-7AM
		data.appendGoalRawData(generateEmptyRawData(0, 7 * 60));

		// points : 0, steps : 0, duration : 20, tag_in_out = [7 * 60, 7 *
		// 60 + 20]
		int[] arrDataTaginTagout = new int[] { 7 * 60, 7 * 60 + 20 };
		List<int[]> listDataTaginTagout = new ArrayList<int[]>();
		listDataTaginTagout.add(arrDataTaginTagout);
		data.appendGoalRawData(generateSessionRawData(0, 0, 20,
				listDataTaginTagout));

		// push to server
		MVPApi.pushRawData(token, goal.getServerId(), data, 0);
		ShortcutsTyper.delayTime(delayTime);

		List<TimelineItem> timelineitems = MVPApi.getTimelineItems(token,
				goal.getStartTime(), goal.getEndTime(), 0l);
		goal = MVPApi.getGoal(token, goal.getServerId()).goals[0];

		testPassed &= Verify.verifyTrue(
				hasSessionTileWithData(timelineitems, goal, 7 * 60 , 20, 0),
				"Goal doesn't have session tile at 7:00 - 7:20") == null;
		
		for (int i = 0; i < timelineitems.size(); i++) {
			int type = timelineitems.get(i).getItemType();
			if (type == 2) {
				TimelineItem item = timelineitems.get(i);
				ActivitySessionItem sessionData = (ActivitySessionItem) item
						.getData();
				long timestampTemp = item.getTimestamp();

				if (timestampTemp == timestampSoccer) {
					testPassed &= Verify
							.verifyEquals(sessionData.getActivityType(),
									MVPEnums.ACTIVITY_SOCCER,
									"Not the soccer activity") == null;
				}
			}
		}

		Assert.assertTrue(testPassed, "All asserts are passed");
	}

	@Test(groups = { "MVPBackend", "servercalculation", "TaginTagout" })
	public void ChangeActivityType() {
		boolean testPassed = true;

		// Timestamp first activity
		cal.set(Calendar.HOUR_OF_DAY, 7);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long timestampBasketball = cal.getTimeInMillis() / 1000;
		// Timestamp second activity
		cal.set(Calendar.HOUR_OF_DAY, 7);
		cal.set(Calendar.MINUTE, 30);
		cal.set(Calendar.SECOND, 0);
		long timestampSoccer = cal.getTimeInMillis() / 1000;
		// Timestamp : tripple tap at 6:59AM on current day for changing to
		// first activity
		cal.set(Calendar.HOUR_OF_DAY, 6);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 0);
		List<TripleTapData> tripleTapTypeChanges = new ArrayList<TripleTapData>();
		TripleTapData tripleTapDefault = new TripleTapData();
		tripleTapDefault.setActivityType(MVPEnums.ACTIVITY_BASKETBALL);
		tripleTapDefault.setTimestamp(cal.getTimeInMillis() / 1000);
		tripleTapTypeChanges.add(tripleTapDefault);
		// Timestamp : tripple tap at 7:29AM on current day for changing to
		// second activity
		cal.set(Calendar.HOUR_OF_DAY, 7);
		cal.set(Calendar.MINUTE, 29);
		cal.set(Calendar.SECOND, 0);
		tripleTapDefault = new TripleTapData();
		tripleTapDefault.setActivityType(MVPEnums.ACTIVITY_SOCCER);
		tripleTapDefault.setTimestamp(cal.getTimeInMillis() / 1000);
		tripleTapTypeChanges.add(tripleTapDefault);
		// Create Goal for today
		Goal goal = Goal.getDefaultGoal();
		goal.setTripleTapTypeChanges(tripleTapTypeChanges);
		GoalsResult result = MVPApi.createGoal(token, goal);
		goal.setServerId(result.goals[0].getServerId());
		goal.setUpdatedAt(result.goals[0].getUpdatedAt());

		GoalRawData data = new GoalRawData();
		// 0 - 7AM empty raw data
		data.appendGoalRawData(generateEmptyRawData(0, 7 * 60));
		// 7:00 - 7:20AM basketball
		int[] arrDataTaginTagout = new int[] { 7 * 60, 7 * 60 + 20 };
		List<int[]> listDataTaginTagout = new ArrayList<int[]>();
		listDataTaginTagout.add(arrDataTaginTagout);
		data.appendGoalRawData(generateSessionRawData(4000, 400, 20,
				listDataTaginTagout));
		// 7:20 - 7:30AM empty raw data
		data.appendGoalRawData(generateEmptyRawData(7 * 60 + 20, 7 * 60 + 30));
		// 7:30 - 7:45AM soccer
		arrDataTaginTagout = new int[] { 7 * 60 + 30, 7 * 60 + 45 };
		listDataTaginTagout = new ArrayList<int[]>();
		listDataTaginTagout.add(arrDataTaginTagout);
		data.appendGoalRawData(generateSessionRawData(3000, 300, 15,
				listDataTaginTagout));

		// push to server
		MVPApi.pushRawData(token, goal.getServerId(), data, 0);
		ShortcutsTyper.delayTime(delayTime);

		List<TimelineItem> timelineitems = MVPApi.getTimelineItems(token,
				goal.getStartTime(), goal.getEndTime(), 0l);
		goal = MVPApi.getGoal(token, goal.getServerId()).goals[0];

		testPassed &= Verify.verifyEquals(
				getNumberOfSessionTiles(timelineitems), 2,
				"Goal has 2 session tiles") == null;

		for (int i = 0; i < timelineitems.size(); i++) {
			int type = timelineitems.get(i).getItemType();
			if (type == 2) {
				TimelineItem item = timelineitems.get(i);
				ActivitySessionItem sessionData = (ActivitySessionItem) item
						.getData();
				long timestampTemp = item.getTimestamp();

				if (timestampTemp == timestampBasketball) {
					testPassed &= Verify.verifyEquals(
							sessionData.getActivityType(),
							MVPEnums.ACTIVITY_BASKETBALL,
							"Not the basketball activity") == null;
				}
				if (timestampTemp == timestampSoccer) {
					testPassed &= Verify
							.verifyEquals(sessionData.getActivityType(),
									MVPEnums.ACTIVITY_SOCCER,
									"Not the soccer activity") == null;
				}
			}
		}

		Assert.assertTrue(testPassed, "All asserts are passed");
	}

	@Test(groups = { "MVPBackend", "servercalculation", "TaginTagout" })
	public void OverlapSession() {
		boolean testPassed = true;

		// Create Goal for today
		Goal goal = Goal.getDefaultGoal();
		// Timestamp : tripple tap at 6:59AM on current day for changing to
		// first activity
		cal.set(Calendar.HOUR_OF_DAY, 6);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 0);
		List<TripleTapData> tripleTapTypeChanges = new ArrayList<TripleTapData>();
		TripleTapData tripleTapDefault = new TripleTapData();
		tripleTapDefault.setActivityType(MVPEnums.ACTIVITY_BASKETBALL);
		tripleTapDefault.setTimestamp(cal.getTimeInMillis() / 1000);
		tripleTapTypeChanges.add(tripleTapDefault);
		// Timestamp : tripple tap at 7:29AM on current day for changing to
		// second activity
		cal.set(Calendar.HOUR_OF_DAY, 7);
		cal.set(Calendar.MINUTE, 29);
		cal.set(Calendar.SECOND, 0);
		tripleTapDefault = new TripleTapData();
		tripleTapDefault.setActivityType(MVPEnums.ACTIVITY_SOCCER);
		tripleTapDefault.setTimestamp(cal.getTimeInMillis() / 1000);
		tripleTapTypeChanges.add(tripleTapDefault);
		goal.setTripleTapTypeChanges(tripleTapTypeChanges);
		
		GoalsResult result = MVPApi.createGoal(token, goal);
		goal.setServerId(result.goals[0].getServerId());
		goal.setUpdatedAt(result.goals[0].getUpdatedAt());

		GoalRawData data = new GoalRawData();
		// 0 - 7AM empty raw data
		data.appendGoalRawData(generateEmptyRawData(0, 7 * 60));
		// 7:00 - 7:20AM 
		int[] arrDataTaginTagout = new int[] { 7 * 60 , 7 * 60 + 20 };
		List<int[]> listDataTaginTagout = new ArrayList<int[]>();
		listDataTaginTagout.add(arrDataTaginTagout);
		data.appendGoalRawData(generateSessionRawData(1000, 30, 20,
				listDataTaginTagout));
		// 7:20 - 7:30AM empty raw data
		data.appendGoalRawData(generateEmptyRawData(7 * 60 + 20, 7 * 60 + 30));
		// 7:30 - 7:45AM : tag_in tag_out : 7:05 - 7:10
		arrDataTaginTagout = new int[] { 7 * 60 + 5 , 7 * 60 + 10};
		listDataTaginTagout = new ArrayList<int[]>();
		listDataTaginTagout.add(arrDataTaginTagout);
		data.appendGoalRawData(generateSessionRawData(1000, 30, 15,
				listDataTaginTagout));

		// push to server
		MVPApi.pushRawData(token, goal.getServerId(), data, 0);
		ShortcutsTyper.delayTime(delayTime);

		List<TimelineItem> timelineitems = MVPApi.getTimelineItems(token,
				goal.getStartTime(), goal.getEndTime(), 0l);
		goal = MVPApi.getGoal(token, goal.getServerId()).goals[0];

		 testPassed &= Verify.verifyEquals(
				 getNumberOfSessionTiles(timelineitems), 1,
				 		"Goal has 2 session tiles") == null;
		
		 Assert.assertTrue(testPassed, "All asserts are passed");
	}

	@Test(groups = { "MVPBackend", "servercalculation", "TaginTagout" })
	public void TagInEqualTagOut() {
		boolean testPassed = true;
		// Timestamp tripple tap first activity
		cal.set(Calendar.HOUR_OF_DAY, 6);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 0);
		
		// Create goal for today
		Goal goal = Goal.getDefaultGoal();
		List<TripleTapData> tripleTapTypeChanges = new ArrayList<TripleTapData>();
		TripleTapData tripleTapDefault = new TripleTapData();
		tripleTapDefault.setActivityType(MVPEnums.ACTIVITY_RUNNING);
		tripleTapDefault.setTimestamp(cal.getTimeInMillis() / 1000);
		tripleTapTypeChanges.add(tripleTapDefault);
		goal.setTripleTapTypeChanges(tripleTapTypeChanges);
		
		GoalsResult result = MVPApi.createGoal(token, goal);
		goal.setServerId(result.goals[0].getServerId());
		goal.setUpdatedAt(result.goals[0].getUpdatedAt());

		GoalRawData data = new GoalRawData();
		// empty raw data : 0-7AM
		data.appendGoalRawData(generateEmptyRawData(0, 7 * 60));
		
		// points : 10, steps : 20, duration : 30, tag_in_out = [7 * 60, 7 * 60] : tag_in = tag_out
		// 7AM-7:30AM
		int[] arrDataTaginTagout = new int[] { 7 * 60, 7 * 60 };
		List<int[]> listDataTaginTagout = new ArrayList<int[]>();
		listDataTaginTagout.add(arrDataTaginTagout);
		data.appendGoalRawData(generateSessionRawData(200, 10, 30,
				listDataTaginTagout));

		// push to server
		MVPApi.pushRawData(token, goal.getServerId(), data, 0);
		ShortcutsTyper.delayTime(delayTime);

		List<TimelineItem> timelineitems = MVPApi.getTimelineItems(token,
				goal.getStartTime(), goal.getEndTime(), 0l);
		goal = MVPApi.getGoal(token, goal.getServerId()).goals[0];

		testPassed &= Verify.verifyEquals(
				getNumberOfSessionTiles(timelineitems), 0,
				"Goal has 2 session tiles") == null;
		
		Assert.assertTrue(testPassed, "All asserts are passed");
	}
	
	@Test(groups = { "MVPBackend", "servercalculation", "TaginTagout" })
	public void SequentActivitySession(){
		boolean testPassed = true;
		// Timestamp tripple tap first activity
		cal.set(Calendar.HOUR_OF_DAY, 6);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 0);
		
		// Create goal for today
		Goal goal = Goal.getDefaultGoal();
		GoalsResult result = MVPApi.createGoal(token, goal);
		goal.setServerId(result.goals[0].getServerId());
		goal.setUpdatedAt(result.goals[0].getUpdatedAt());

		GoalRawData data = new GoalRawData();
		// empty raw data : 0-7AM
		data.appendGoalRawData(generateEmptyRawData(0, 7 * 60));
		// 7:00 - 7:20 AM 
		int[] arrDataTaginTagout = new int[] { 7 * 60, 7 * 60 + 20 };
		List<int[]> listDataTaginTagout = new ArrayList<int[]>();
		listDataTaginTagout.add(arrDataTaginTagout);
		data.appendGoalRawData(generateSessionRawData(1000, 30, 20, listDataTaginTagout));
		// 7:20 - 7:40 AM
		arrDataTaginTagout = new int[]{7 * 60 + 20, 7 * 60 + 40};
		listDataTaginTagout = new ArrayList<int[]>();
		listDataTaginTagout.add(arrDataTaginTagout);
		data.appendGoalRawData(generateSessionRawData(1000, 30, 20, listDataTaginTagout));

		// push to server
		MVPApi.pushRawData(token, goal.getServerId(), data, 0);
		ShortcutsTyper.delayTime(delayTime);
		
		List<TimelineItem> timelineitems = MVPApi.getTimelineItems(token,
				goal.getStartTime(), goal.getEndTime(), 0l);
		goal = MVPApi.getGoal(token, goal.getServerId()).goals[0];

		testPassed &= Verify.verifyEquals(
				getNumberOfSessionTiles(timelineitems), 2,
				"Goal has 2 session tiles") == null;
		
		Assert.assertTrue(testPassed, "All asserts are passed");
	}
	
}
