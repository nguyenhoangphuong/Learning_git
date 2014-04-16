package com.misfit.ta.backend.aut.correctness.servercalculation;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalRawData;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.servercalculation.ServerCalculationCursor;
import com.misfit.ta.common.Verify;
import com.misfit.ta.utils.ShortcutsTyper;

public class BackendNewServerCalculationCursor extends BackendServerCalculationBase {
	
	List<String> assertionMessages = new ArrayList<String>();
	
	@BeforeClass(alwaysRun = true)
	public void setUp() {
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "NewServercalculation", "NewServercalculationCursor" })
	public void ServerCalculationCursor() {
		
		assertionMessages.clear();

		// create account, profile and goal
		long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		String userId = MVPApi.getUserId(token);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		
		MVPApi.createProfile(token, DataGenerator.generateRandomProfile(timestamp, null));
		MVPApi.createPedometer(token, pedometer);
		Goal[] goals = new Goal[4];
		for(int i = 0; i < goals.length; i++) {

			long goalTimestamp = timestamp - i * 3600 * 24;
			Goal goal = Goal.getDefaultGoal(goalTimestamp);
			GoalsResult result = MVPApi.createGoal(token, goal);

			goal.setServerId(result.goals[0].getServerId());
			goal.setUpdatedAt(result.goals[0].getUpdatedAt());

			goals[i] = goal;
		} 
		
		// get initial cursors
		BaseResult result = MVPApi.getCursors(token);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		ServerCalculationCursor cursor = ServerCalculationCursor.fromResponse(result.response);
		Assert.assertEquals((long)cursor.getActivitySessionCursor(), 0l, "Activity session cursor");
		Assert.assertEquals((long)cursor.getGraphItemCursor(), 0l, "Graph item cursor");
		Assert.assertEquals((long)cursor.getMilestoneCursor(), 0l, "Milestone cursor");
		Assert.assertEquals((long)cursor.getNotableEventCursor(), 0l, "Notable events cursor");
		Assert.assertEquals((long)cursor.getProgressCursor(), 0l, "Goal progress cursor");
		Assert.assertEquals((long)cursor.getSleepSessionCursor(), 0l, "Sleep session cursor");
		Assert.assertEquals((long)cursor.getStatisticCursor(), 0l, "Statistics cursor");

		// now calculate somethings
		GoalRawData[] data = createTestData();
		int delayTime = 20000;
		
		// push 4 days ago data
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(goals[3].getStartTime(), goals[3].getTimeZoneOffsetInSeconds() / 60, "0101", "18", data[3]).rawData);
		pushSyncData(timestamp, userId, pedometer.getSerialNumberString(), dataStrings);
		
		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);
		
		result = MVPApi.getCursors(token);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		ServerCalculationCursor newCursor = ServerCalculationCursor.fromResponse(result.response);
		assertCursor(newCursor, cursor, "gic, pc, sc");
		cursor = newCursor;

		// push 3 days ago data
		dataStrings.clear();
		dataStrings.add(MVPApi.getRawDataAsString(goals[2].getStartTime(), goals[2].getTimeZoneOffsetInSeconds() / 60, "0101", "18", data[2]).rawData);
		pushSyncData(timestamp, userId, pedometer.getSerialNumberString(), dataStrings);
		
		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);
		
		result = MVPApi.getCursors(token);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		newCursor = ServerCalculationCursor.fromResponse(result.response);
		assertCursor(newCursor, cursor, "gic, ssc, nec, pc, sc");
		cursor = newCursor;
		
		// push yesterday data
		dataStrings.clear();
		dataStrings.add(MVPApi.getRawDataAsString(goals[1].getStartTime(), goals[1].getTimeZoneOffsetInSeconds() / 60, "0101", "18", data[1]).rawData);
		pushSyncData(timestamp, userId, pedometer.getSerialNumberString(), dataStrings);
		
		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);
		
		result = MVPApi.getCursors(token);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		newCursor = ServerCalculationCursor.fromResponse(result.response);
		assertCursor(newCursor, cursor, "gic, ssc, nec, pc, sc, mc");
		cursor = newCursor;
		
		// push today data
//		dataStrings.clear();
//		dataStrings.add(MVPApi.getRawDataAsString(goals[0].getStartTime(), goals[0].getTimeZoneOffsetInSeconds() / 60, "0101", "18", data[0]).rawData);
//		pushSyncData(timestamp, userId, pedometer.getSerialNumberString(), dataStrings);
//		
//		logger.info("Waiting " + delayTime + " miliseconds");
//		ShortcutsTyper.delayTime(delayTime);

		assertTest();
	}
	
	private GoalRawData[] createTestData() {
		
		// story on 4th day (3 days ago):
		// - gap: 2 active + 4 idle / 60 minutes - 2000 steps - 200 points from 7:00 to 8:00
		// expect:
		// - pc, gic, sc
		GoalRawData data3 = new GoalRawData();
		data3.appendGoalRawData(generateEmptyRawData(0, 7 * 60));

		data3.appendGoalRawData(generateGapData(100, 10, 2, 4, 60));
		data3.appendGoalRawData(generateEmptyRawData(7 * 60 + 60, 24 * 60));

		// story on 3th day (2 days ago):
		// - session: 60 minutes - 6000 steps - 600 points at 7:00
		// - session: 50 minutes - 5000 steps - 500 points at 10:00
		// - session: 30 minutes - 3000 steps - 300 points at 13:00
		// - session: 40 minutes - 4000 steps - 400 points at 17:00
		// - session: 40 minutes - 4000 steps - 400 points at 20:00
		// expect:
		// - pc, gic, sc, ssc, nec
		GoalRawData data2 = new GoalRawData();
		data2.appendGoalRawData(generateEmptyRawData(0, 7 * 60));

		data2.appendGoalRawData(generateSessionRawData(6000, 600, 60));
		data2.appendGoalRawData(generateEmptyRawData(7 * 60 + 60, 10 * 60));

		data2.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		data2.appendGoalRawData(generateEmptyRawData(10 * 60 + 50, 13 * 60));

		data2.appendGoalRawData(generateSessionRawData(3000, 300, 30));
		data2.appendGoalRawData(generateEmptyRawData(13 * 60 + 30, 17 * 60));

		data2.appendGoalRawData(generateSessionRawData(4000, 400, 40));
		data2.appendGoalRawData(generateEmptyRawData(17 * 60 + 40, 20 * 60));

		data2.appendGoalRawData(generateSessionRawData(4000, 400, 40));
		data2.appendGoalRawData(generateEmptyRawData(20 * 60 + 40, 24 * 60));

		// story on 2nd day (yesterday):
		// - 63720 steps - 360 minute (49.21 miles)
		// expect:
		// - pc, gic, sc, ssc, nec, mc
		GoalRawData data1 = new GoalRawData();
		data1.appendGoalRawData(generateSessionRawData(63720, 3600, 360));
		data1.appendGoalRawData(generateEmptyRawData(0 * 60 + 360, 24 * 60));

		// story on today:
		// TODO: should be updated this with sleep
		// - session: 50 minutes - 5000 steps - 600 points at 8:00
		// - session: 50 minutes - 5000 steps - 500 points at 9:00
		// - session: 50 minutes - 5000 steps - 500 points at 10:00
		// - session: 50 minutes - 5000 steps - 500 points at 11:00
		// - session: 50 minutes - 5000 steps - 500 points at 12:00
		// - session: 50 minutes - 5000 steps - 500 points at 13:00
		// expect:
		// - 26 graph items
		// - 6 acitivity session tiles
		// - 100% tile
		// - 150% tile
		// - 200% tile
		// - streak tile
		// - personal best
		// - statistics updated
		GoalRawData data0 = new GoalRawData();
		data0.appendGoalRawData(generateEmptyRawData(0, 8 * 60));

		data0.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		data0.appendGoalRawData(generateEmptyRawData(8 * 60 + 50, 9 * 60));

		data0.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		data0.appendGoalRawData(generateEmptyRawData(9 * 60 + 50, 10 * 60));

		data0.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		data0.appendGoalRawData(generateEmptyRawData(10 * 60 + 50, 11 * 60));

		data0.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		data0.appendGoalRawData(generateEmptyRawData(11 * 60 + 50, 12 * 60));

		data0.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		data0.appendGoalRawData(generateEmptyRawData(12 * 60 + 50, 13 * 60));

		data0.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		
		return new GoalRawData[] { data0, data1, data2, data3 };
	}
	

	private void assertCursor(ServerCalculationCursor newCursor, ServerCalculationCursor cursor, String fieldsToCheck) {
		
		// activity session
		if(fieldsToCheck.contains("asc")) {
			assertionMessages.add(Verify.verifyNotEquals(newCursor.getActivitySessionCursor(), cursor.getActivitySessionCursor(), "Activity session cursor"));
			assertionMessages.add(Verify.verifyTrue(newCursor.getActivitySessionCursor() > cursor.getActivitySessionCursor(), "New activity session cursor is bigger than current one"));
		}
		else {
			assertionMessages.add(Verify.verifyEquals(newCursor.getActivitySessionCursor(), cursor.getActivitySessionCursor(), "Activity session cursor"));
		}

		// sleep session
		if(fieldsToCheck.contains("ssc")) {
			assertionMessages.add(Verify.verifyNotEquals(newCursor.getSleepSessionCursor(), cursor.getSleepSessionCursor(), "Sleep session cursor"));
			assertionMessages.add(Verify.verifyTrue(newCursor.getSleepSessionCursor() > cursor.getSleepSessionCursor(), "New sleep session cursor is bigger than current one"));
		}
		else {
			assertionMessages.add(Verify.verifyEquals(newCursor.getSleepSessionCursor(), cursor.getSleepSessionCursor(), "Sleep session cursor"));
		}

		// progress
		if(fieldsToCheck.contains("pc")) {
			assertionMessages.add(Verify.verifyNotEquals(newCursor.getProgressCursor(), cursor.getProgressCursor(), "Progress cursor"));
			assertionMessages.add(Verify.verifyTrue(newCursor.getProgressCursor() > cursor.getProgressCursor(), "New progress cursor is bigger than current one"));
		}
		else {
			assertionMessages.add(Verify.verifyEquals(newCursor.getProgressCursor(), cursor.getProgressCursor(), "Progress cursor"));
		}

		// graph items
		if(fieldsToCheck.contains("gic")) {
			assertionMessages.add(Verify.verifyNotEquals(newCursor.getGraphItemCursor(), cursor.getGraphItemCursor(), "Graph items cursor"));
			assertionMessages.add(Verify.verifyTrue(newCursor.getGraphItemCursor() > cursor.getGraphItemCursor(), "New graph items cursor is bigger than current one"));
		}
		else {
			assertionMessages.add(Verify.verifyEquals(newCursor.getGraphItemCursor(), cursor.getGraphItemCursor(), "Graph items cursor"));
		}

		// milestone
		if(fieldsToCheck.contains("mc")) {
			assertionMessages.add(Verify.verifyNotEquals(newCursor.getMilestoneCursor(), cursor.getMilestoneCursor(), "Milestone cursor"));
			assertionMessages.add(Verify.verifyTrue(newCursor.getMilestoneCursor() > cursor.getMilestoneCursor(), "New milestone cursor is bigger than current one"));
		}
		else {
			assertionMessages.add(Verify.verifyEquals(newCursor.getMilestoneCursor(), cursor.getMilestoneCursor(), "Milestone cursor"));
		}

		// events
		if(fieldsToCheck.contains("nec")) {
			assertionMessages.add(Verify.verifyNotEquals(newCursor.getNotableEventCursor(), cursor.getNotableEventCursor(), "Notable events cursor"));
			assertionMessages.add(Verify.verifyTrue(newCursor.getNotableEventCursor() > cursor.getNotableEventCursor(), "New notable events cursor is bigger than current one"));
		}
		else {
			assertionMessages.add(Verify.verifyEquals(newCursor.getNotableEventCursor(), cursor.getNotableEventCursor(), "Notable events cursor"));
		}

		// statistics
		if(fieldsToCheck.contains("sc")) {
			assertionMessages.add(Verify.verifyNotEquals(newCursor.getStatisticCursor(), cursor.getStatisticCursor(), "Statistics cursor"));
			assertionMessages.add(Verify.verifyTrue(newCursor.getStatisticCursor() > cursor.getStatisticCursor(), "New statistics cursor is bigger than current one"));
		}
		else {
			assertionMessages.add(Verify.verifyEquals(newCursor.getStatisticCursor(), cursor.getStatisticCursor(), "Statistics cursor"));
		}
		
	}
	
	private void assertTest() {
		
		boolean pass = false;
		for(String message : assertionMessages) {
			if(message == null) {
				pass = false;
				continue;
			}
			
			logger.info(message);
		}
		
		Assert.assertTrue(pass, "All tests are passed");
	}
	
}
