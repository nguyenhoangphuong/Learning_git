package com.misfit.ta.backend.aut.correctness.backendapi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.summary.ShineHistoryRecord;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.SleepSessionItem;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.common.Verify;
import com.misfit.ta.report.TRS;

public class BackendSummaryTC extends BackendAutomation {

	private String token;
	List<ShineHistoryRecord> weekRecords = new ArrayList<ShineHistoryRecord>();
	List<ShineHistoryRecord> monthRecords = new ArrayList<ShineHistoryRecord>();
	String email = "";
	@BeforeClass(alwaysRun = true)
	public void setUp() {

		/*
		 * set up these data:
		 * 
		 * 1st week: all week in UTC
		 * 2nd week: UTC - change between timezones - UTC
		 * 3rd week: UTC - change between timezones - UTC
		 * 4th week: UTC - any timezones - not UTC
		 * 5th week: not UTC - any timezones - not UTC
		 * 6th week: not UTC - any timezones - UTC
		 * 7th week: only 5 days in UTC
		 * 
		 * note: all the data should start from the beginning of
		 * the 1st Sunday 2 months ago
		 */

		// get the correct time
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTimeInMillis(cal.getTimeInMillis() - 86400 * 60 * 1000l);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		while(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);

		// build the goals
		int[] goalTimezoneOffsets = new int[] {
				0, 0, 0, 0, 0, 0, 0,
				0, -7, -7, -4, -7, -7, 0,
				0, +7, +7, +5, +5, +7, 0,
				0, +12, 0, -12, 0, -1, +1,
				+1, 0, +7, 0, +7, +7, +7,
				+7, 0, 0, 0, +7, +7, 0,
				0, 0, 0, 0, 0
		};

		int numberOfGoal = goalTimezoneOffsets.length;
		Goal[] goals = new Goal[numberOfGoal];
		String[] dates = new String[numberOfGoal];
		double[] points = new double[numberOfGoal];
		int[] sleepSeconds = new int[numberOfGoal];
		List<TimelineItem> items = new ArrayList<TimelineItem>();

		List<Integer> sundayIndex = new ArrayList<Integer>();
		List<Integer> monthIndex = new ArrayList<Integer>();
		monthIndex.add(0);
		int currentMonth = cal.get(Calendar.MONTH);

		for(int i = 0; i < numberOfGoal; i++) {

			// create goal
			long startTime = MVPCommon.getDayStartEpoch(cal.getTimeInMillis() / 1000, TimeZone.getTimeZone("UTC")) - 
					(i == 0 ? 0 : goalTimezoneOffsets[i - 1] * 3600);
			long endTime = MVPCommon.getDayEndEpoch(cal.getTimeInMillis() / 1000, TimeZone.getTimeZone("UTC")) - 
					goalTimezoneOffsets[i] * 3600;
			goals[i] = DataGenerator.generateRandomGoal(0, null);
			goals[i].setStartTime(startTime);
			goals[i].setEndTime(endTime);
			goals[i].setTimeZoneOffsetInSeconds(i == 0 ? 0 : goalTimezoneOffsets[i - 1] * 3600);

			// create sleep data
			List<TimelineItem> sleeps = new ArrayList<TimelineItem>();
			int random = MVPCommon.randInt(0, 100);
			if(random <= 25) {
				// 25% has sleep and nap
				sleeps.add(DataGenerator.generateRandomSleepTimelineItem(startTime - (MVPCommon.randInt(0, 7 * 3600) - 2 * 3600), null));
				sleeps.add(DataGenerator.generateRandomSleepNapTimelineItem(startTime + (MVPCommon.randInt(7 * 3600, 9 * 3600) - 2 * 3600), null));
			}
			else if (random <= 75) {
				// 75% has sleep
				sleeps.add(DataGenerator.generateRandomSleepTimelineItem(startTime - (MVPCommon.randInt(0, 7 * 3600) - 2 * 3600), null));
			}

			if(random < 20 && sleeps.size() > 0) {
				// 20% will edit start time
				SleepSessionItem sleepSession = (SleepSessionItem)sleeps.get(0).getData();
				sleepSession.setEditedStartTime(sleepSession.getRealStartTime() + MVPCommon.randLong(300, 1200));
				sleepSession.setEditedEndTime(sleepSession.getRealEndTime());
				sleeps.get(0).setData(sleepSession);
			}

			if(random > 80 && sleeps.size() > 0) {
				// 20% will edit end time
				SleepSessionItem sleepSession = (SleepSessionItem)sleeps.get(0).getData();
				sleepSession.setEditedStartTime(sleepSession.getRealStartTime());
				sleepSession.setEditedEndTime(sleepSession.getRealEndTime() + MVPCommon.randLong(300, 1200));
				sleeps.get(0).setData(sleepSession);
			}

			int totalSleepSeconds = 0;
			for(TimelineItem sleep : sleeps) {

				SleepSessionItem s = (SleepSessionItem)sleep.getData();
				long sleepStartTime = s.getEditedStartTime() == null ? s.getRealStartTime() : s.getEditedStartTime();
				long sleepEndTime = s.getEditedEndTime() == null ? s.getRealEndTime() : s.getEditedEndTime();

				// only store the sleep if it belongs to this goal
				if(sleepEndTime >= startTime && sleepEndTime <= endTime) {
					totalSleepSeconds += (sleepEndTime - sleepStartTime);
					items.add(sleep);
				}
			}

			// track the index
			if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
				sundayIndex.add(i);
			}
			if(cal.get(Calendar.MONTH) != currentMonth) {
				monthIndex.add(i);
				currentMonth = cal.get(Calendar.MONTH);
			}

			// add to assert data
			dates[i] = MVPCommon.getUTCDateString(cal.getTimeInMillis() / 1000);
			points[i] = goals[i].getProgressData().getPoints();
			sleepSeconds[i] = totalSleepSeconds;

			cal.setTimeInMillis(cal.getTimeInMillis() + 86400 * 1000);
		}
		
		sundayIndex.add(numberOfGoal);
		monthIndex.add(numberOfGoal);


		// create test assert data
		for(int i = 0; i < sundayIndex.size() - 1; i++) {

			int totalSleepSeconds = 0;
			double totalPoints = 0d;

			for(int j = sundayIndex.get(i); j < sundayIndex.get(i + 1); j++) {

				totalSleepSeconds += sleepSeconds[j];
				totalPoints += points[j];
			}

			ShineHistoryRecord weekRecord = new ShineHistoryRecord();
			weekRecord.activityPoints = totalPoints;
			weekRecord.sleepSeconds = totalSleepSeconds;
			weekRecord.date = dates[sundayIndex.get(i)];
			weekRecords.add(weekRecord);
		}

		for(int i = 0; i < monthIndex.size() - 1; i++) {

			int totalSleepSeconds = 0;
			double totalPoints = 0d;

			for(int j = monthIndex.get(i); j < monthIndex.get(i + 1); j++) {

				totalSleepSeconds += sleepSeconds[j];
				totalPoints += points[j];
			}

			String[] parts = dates[monthIndex.get(i)].split("-");
			Calendar cal2 = Calendar.getInstance();
			cal2.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) - 1, 1);
			
			ShineHistoryRecord monthRecord = new ShineHistoryRecord();
			monthRecord.activityPoints = totalPoints;
			monthRecord.sleepSeconds = totalSleepSeconds;
			monthRecord.date = MVPCommon.getDateString(cal2.getTimeInMillis() / 1000);
			
			monthRecords.add(monthRecord);
		}

		// ensure test data is correct
		for(int i = 0; i < numberOfGoal - 1; i++) {
			if(!goals[i].getEndTime().equals(goals[i + 1].getStartTime() - 1))
				System.out.println("Error: " + i);
		}

		// create data
		email = MVPApi.generateUniqueEmail();
		token = MVPApi.signUp(email, "qqqqqq").token;
		for(Goal goal : goals)
			MVPApi.createGoal(token, goal);
		MVPApi.createTimelineItems(token, items);
		
		// print test data
		TRS.getCurrent().addStep("Test data", "testdata");

		System.out.println("The goals:");
		TRS.getCurrent().addCode("The goals: ", null);
		
		for(int i = 0; i < numberOfGoal; i++) {
			String message = String.format("%02d | %s: %d | %d | %02d | %d\t| %.2f | %d",
					i + 1,
					dates[i],
					goals[i].getStartTime(),
					goals[i].getEndTime(),
					((goals[i].getEndTime() - goals[i].getStartTime() + 1) / 3600),
					goals[i].getTimeZoneOffsetInSeconds() / 3600,
					points[i],
					sleepSeconds[i]);
			System.out.println(message);
			TRS.getCurrent().addCode(message, null);
		}

		System.out.println("\nExpected week records: " + weekRecords.size());
		TRS.getCurrent().addCode("\nExpected week records: " + weekRecords.size(), null);
		
		for(ShineHistoryRecord r : weekRecords) {
			System.out.println(String.format("%s | %.2f | %d", r.date, r.activityPoints, r.sleepSeconds));
			TRS.getCurrent().addCode(String.format("%s | %.2f | %d", r.date, r.activityPoints, r.sleepSeconds), null);
		}
		
		System.out.println("\nExpected month records: " + monthRecords.size());
		TRS.getCurrent().addCode("\nExpected month records: " + monthRecords.size(), null);
		
		for(ShineHistoryRecord r : monthRecords) {
			System.out.println(String.format("%s | %.2f | %d", r.date, r.activityPoints, r.sleepSeconds));
			TRS.getCurrent().addCode(String.format("%s | %.2f | %d", r.date, r.activityPoints, r.sleepSeconds), null);
		}
		
		System.out.println();
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "HistorySummary", "HistorySummaryWeekly", "Excluded" })
	public void GetSummaryByWeek_GetAll() {

		BaseResult result = MVPApi.getSummaryByWeek(token, null);
		List<ShineHistoryRecord> records = ShineHistoryRecord.getSummaryFromResponse(result.response);

		Assert.assertEquals(records.size(), weekRecords.size(), "Number of week records");
		
		boolean pass = true;
		for(int i = 0; i < records.size(); i++) {
			pass &= Verify.verifyEquals(records.get(i).date, weekRecords.get(i).date, "Date field") == null;
			pass &= Verify.verifyEquals(records.get(i).sleepSeconds, weekRecords.get(i).sleepSeconds, "Sleep seconds field") == null;
			pass &= Verify.verifyEquals(Math.floor(records.get(i).activityPoints), Math.floor(weekRecords.get(i).activityPoints), "Points field") == null;
		}
		
		Assert.assertTrue(pass, "All records are correct");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "HistorySummary", "HistorySummaryWeekly", "Excluded" })
	public void GetSummaryByMonth_GetAll() {

		BaseResult result = MVPApi.getSummaryByMonth(token, null);
		List<ShineHistoryRecord> records = ShineHistoryRecord.getSummaryFromResponse(result.response);

		Assert.assertEquals(records.size(), monthRecords.size(), "Number of month records");
		
		boolean pass = true;
		for(int i = 0; i < records.size(); i++) {
			pass &= Verify.verifyEquals(records.get(i).date, monthRecords.get(i).date, "Date field") == null;
			pass &= Verify.verifyEquals(records.get(i).sleepSeconds, monthRecords.get(i).sleepSeconds, "Sleep seconds field") == null;
			pass &= Verify.verifyEquals(Math.floor(records.get(i).activityPoints), Math.floor(monthRecords.get(i).activityPoints), "Points field") == null;
		}
		
		Assert.assertTrue(pass, "All records are correct");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "HistorySummary", "HistorySummaryWeekly", "Excluded" })
	public void GetSummaryByWeek_SpecificRange() {

		// from the 1st week => everything
		BaseResult result = MVPApi.getSummaryByWeek(token, weekRecords.get(0).date);
		List<ShineHistoryRecord> records = ShineHistoryRecord.getSummaryFromResponse(result.response);

		Assert.assertEquals(records.size(), weekRecords.size(), "Number of week records");
		
		boolean pass = true;
		for(int i = 0; i < records.size(); i++) {
			pass &= Verify.verifyEquals(records.get(i).date, weekRecords.get(i).date, "Date field") == null;
			pass &= Verify.verifyEquals(records.get(i).sleepSeconds, weekRecords.get(i).sleepSeconds, "Sleep seconds field") == null;
			pass &= Verify.verifyEquals(Math.floor(records.get(i).activityPoints), Math.floor(weekRecords.get(i).activityPoints), "Points field") == null;
		}
		
		Assert.assertTrue(pass, "All records are correct");
		
		// from 3rd week
		result = MVPApi.getSummaryByWeek(token, weekRecords.get(2).date);
		records = ShineHistoryRecord.getSummaryFromResponse(result.response);

		Assert.assertEquals(records.size(), weekRecords.size() - 2, "Number of week records");
		
		pass = true;
		for(int i = 0; i < records.size(); i++) {
			pass &= Verify.verifyEquals(records.get(i).date, weekRecords.get(i + 2).date, "Date field") == null;
			pass &= Verify.verifyEquals(records.get(i).sleepSeconds, weekRecords.get(i + 2).sleepSeconds, "Sleep seconds field") == null;
			pass &= Verify.verifyEquals(Math.floor(records.get(i).activityPoints), Math.floor(weekRecords.get(i + 2).activityPoints), "Points field") == null;
		}
		
		Assert.assertTrue(pass, "All records are correct");
		
		// from the last week => only last record
		result = MVPApi.getSummaryByWeek(token, weekRecords.get(weekRecords.size() - 1).date);
		records = ShineHistoryRecord.getSummaryFromResponse(result.response);

		Assert.assertEquals(records.size(), 1, "Number of week records");
		
		ShineHistoryRecord lastWeekRecord = weekRecords.get(weekRecords.size() - 1);
		pass = true;
		pass &= Verify.verifyEquals(records.get(0).date, lastWeekRecord.date, "Date field") == null;
		pass &= Verify.verifyEquals(records.get(0).sleepSeconds, lastWeekRecord.sleepSeconds, "Sleep seconds field") == null;
		pass &= Verify.verifyEquals(Math.floor(records.get(0).activityPoints), Math.floor(lastWeekRecord.activityPoints), "Points field") == null;

		
		Assert.assertTrue(pass, "All records are correct");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "HistorySummary", "HistorySummaryWeekly", "Excluded" })
	public void GetSummaryByMonth_SpecificRange() {
		
		// from the 3rd week => everything
		BaseResult result = MVPApi.getSummaryByMonth(token, weekRecords.get(3).date);
		List<ShineHistoryRecord> records = ShineHistoryRecord.getSummaryFromResponse(result.response);

		Assert.assertEquals(records.size(), monthRecords.size(), "Number of month records");
		
		boolean pass = true;
		for(int i = 0; i < records.size(); i++) {
			pass &= Verify.verifyEquals(records.get(i).date, monthRecords.get(i).date, "Date field") == null;
			pass &= Verify.verifyEquals(records.get(i).sleepSeconds, monthRecords.get(i).sleepSeconds, "Sleep seconds field") == null;
			pass &= Verify.verifyEquals(Math.floor(records.get(i).activityPoints), Math.floor(monthRecords.get(i).activityPoints), "Points field") == null;
		}
		
		Assert.assertTrue(pass, "All records are correct");
		
		// from the last week => only last month
		result = MVPApi.getSummaryByMonth(token, weekRecords.get(weekRecords.size() - 1).date);
		records = ShineHistoryRecord.getSummaryFromResponse(result.response);

		Assert.assertEquals(records.size(), 1, "Number of month records");
		
		ShineHistoryRecord lastMonthRecord = monthRecords.get(monthRecords.size() - 1);
		pass = true;
		pass &= Verify.verifyEquals(records.get(0).date, lastMonthRecord.date, "Date field") == null;
		pass &= Verify.verifyEquals(records.get(0).sleepSeconds, lastMonthRecord.sleepSeconds, "Sleep seconds field") == null;
		pass &= Verify.verifyEquals(Math.floor(records.get(0).activityPoints), Math.floor(lastMonthRecord.activityPoints), "Points field") == null;
		
		Assert.assertTrue(pass, "All records are correct");
	}

}