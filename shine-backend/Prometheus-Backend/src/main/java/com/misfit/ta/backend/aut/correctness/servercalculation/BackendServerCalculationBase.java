package com.misfit.ta.backend.aut.correctness.servercalculation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.metawatch.UserInfo;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.TimestampObject;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalRawData;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsAutoSleepStateChange;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsGoalValueChange;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsTimezoneOffsetChange;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsTracking;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsTripleTapTypeChange;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.DisplayUnit;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.data.sync.sdk.SDKSyncLog;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.ActivitySessionItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.LifetimeDistanceItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.MilestoneItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.MilestoneItemInfo;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimezoneChangeItem;

public class BackendServerCalculationBase extends BackendAutomation {
	
	// fields
	protected static final int timestampDelta = 120;
	protected static final double fullBmrDelta = 10;
	protected static final double totalCalorieDelta = 33;
	protected static final double distanceDelta = 0.001;
	
	
	protected void setDefaultTrackingChanges(Long startDay, UserInfo userInfo) {
		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();

		Integer timezoneOffset = 25200;
		Double goalValue = 2500.152;
		Integer tripleTapType = 0;
		Integer autoSleepState = 1;
		GoalSettingsTimezoneOffsetChange timezone = new GoalSettingsTimezoneOffsetChange(
				startDay, timezoneOffset);
		GoalSettingsGoalValueChange goalValueChange = new GoalSettingsGoalValueChange(
				startDay, goalValue);
		GoalSettingsTripleTapTypeChange tripleTapTypeChange = new GoalSettingsTripleTapTypeChange(
				startDay, tripleTapType);
		GoalSettingsAutoSleepStateChange autoSleepStateChange = new GoalSettingsAutoSleepStateChange(
				startDay, autoSleepState);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		changes.add(timezone);
		changes.add(goalValueChange);
		changes.add(tripleTapTypeChange);
		changes.add(autoSleepStateChange);

		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);
	}
	protected Pedometer setUpNewAccount(String token, Long startDay) {
		ProfileData profile = DataGenerator.generateRandomProfile(startDay,
				null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(startDay,
				null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);
		return pedometer;
	}
	// data helpers
	protected void changeDistanceUnit(String token, int unit) {

		ProfileData profile = new ProfileData();
		profile.setDisplayedUnits(new DisplayUnit());
		profile.getDisplayedUnits().setDistanceUnit(unit);
		MVPApi.updateProfile(token, profile);
	}

	protected GoalRawData generateSessionRawData(int totalSteps, int totalPoints, int duration)  {

		return ServerCalculationTestHelpers.generateSessionRawData(totalSteps, totalPoints, duration);
	}
	
	protected GoalRawData generateSessionRawData(int totalSteps, int totalPoints, int duration, List<int[]> list)  {
		
		return ServerCalculationTestHelpers.generateSessionRawData(totalSteps, totalPoints, duration, list);
	}

	protected GoalRawData generateEmptyRawData(int duration) {
		
		return ServerCalculationTestHelpers.generateEmptyRawData(duration);
	}

	protected GoalRawData generateEmptyRawData(int includeStartOffsetMinute, int excludeEndOffsetMinute) {

		return ServerCalculationTestHelpers.generateEmptyRawData(includeStartOffsetMinute, excludeEndOffsetMinute);
	}

	protected GoalRawData generateGapData(int stepPerMinute, int pointPerMinute, int activeInterval, int idleInterval, int duration) {

		return ServerCalculationTestHelpers.generateGapData(stepPerMinute, pointPerMinute, activeInterval, idleInterval, duration);
	}

	protected void pushSyncData(long timestamp, String email, String serialNumber, List<String> dataStrings) {

		SDKSyncLog syncLog = ServerCalculationTestHelpers.createSDKSyncLogFromDataStrings(timestamp, email, serialNumber, dataStrings);
		MVPApi.pushSDKSyncLog(syncLog);
	}
	

	// test verifying helpers
	protected int getNumberOfTile(List<TimelineItem> items, int itemType) {
		
		int count = 0;
		for(TimelineItem item : items) {

			if(item.getItemType() == itemType)
				count++;
		}

		return count;
	}
	
	protected int getNumberOfSessionTiles(List<TimelineItem> items) {

		int count = 0;
		for(TimelineItem item : items) {

			if(item.getItemType() == TimelineItemDataBase.TYPE_SESSION)
				count++;
		}

		return count;
	}

	
	protected boolean hasSessionTileWithData(List<TimelineItem> items, Goal goal, int offsetMinute, int minutes, int points) {

		long goalStartTime = goal.getStartTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(goalStartTime * 1000 + offsetMinute * 60 * 1000);
		long timestamp = cal.getTimeInMillis() / 1000;
		
		logger.info("Looking for session with: Timestamp: " + timestamp + ", Points: " + points + ", Duration: " + minutes);
		
		for(TimelineItem item : items) {

			if(item.getTimestamp().equals(timestamp) && item.getItemType() == TimelineItemDataBase.TYPE_SESSION) {

				ActivitySessionItem session = (ActivitySessionItem) item.getData();
				double diff = (session.getPoint() - points * 2.5) + (session.getDuration() - minutes * 60);
				return diff == 0;
			}
		}

		return false;
	}

	protected boolean isCorrectTimezoneTimelineItem(TimelineItem item, Integer fromTimezone, Integer toTimezone) {
		TimezoneChangeItem timezoneItemData = (TimezoneChangeItem) item.getData();
		return timezoneItemData.getAfterTimeZoneOffset().equals(toTimezone) && timezoneItemData.getBeforeTimeZoneOffset().equals(fromTimezone);
	}
	
	protected boolean hasDailyGoalMilestone(List<TimelineItem> items, Goal goal, int offsetMinute, int eventType, int points) {

		long goalStartTime = goal.getStartTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(goalStartTime * 1000 + offsetMinute * 60 * 1000);
		long timestamp = cal.getTimeInMillis() / 1000;
		
		logger.info("Looking for daily event with: Timestamp: " + timestamp + ", Points: " + points + ", Event type: " + eventType);
		
		for(TimelineItem item : items) {

			if(Math.abs(item.getTimestamp() - timestamp) <  timestampDelta && item.getItemType() == TimelineItemDataBase.TYPE_MILESTONE) {

				MilestoneItem milestone = (MilestoneItem) item.getData();
				MilestoneItemInfo milestoneInfo = milestone.getInfo();

				// ignore streak tile because it had the same timestamp with 100%
				if(milestone.getEventType() == TimelineItemDataBase.EVENT_TYPE_STREAK)
					continue;

				double diff = milestoneInfo.getPoint() - points * 2.5;
				return milestone.getEventType().equals(eventType) && diff == 0;
			}
		}

		return false;
	}

	protected boolean hasStreakMilestone(List<TimelineItem> items, Goal goal, int offsetMinute, int streakNumber) {

		long goalStartTime = goal.getStartTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(goalStartTime * 1000 + offsetMinute * 60 * 1000);
		long timestamp = cal.getTimeInMillis() / 1000;
		
		logger.info("Looking for streak event with: Timestamp: " + timestamp + ", Streak number: " + streakNumber);
		
		for(TimelineItem item : items) {

			if(Math.abs(item.getTimestamp() - timestamp) <  timestampDelta && item.getItemType() == TimelineItemDataBase.TYPE_MILESTONE) {

				MilestoneItem milestone = (MilestoneItem) item.getData();
				MilestoneItemInfo milestoneInfo = milestone.getInfo();
				if(milestone.getEventType().equals(TimelineItemDataBase.EVENT_TYPE_STREAK))
					return  milestoneInfo.getStreakNumber().equals(streakNumber);
			}
		}

		return false;
	}

	protected boolean hasPersonalBestMilestone(List<TimelineItem> items, Goal goal, int offsetMinute, int points, int extendedAmount) {
		
		long goalStartTime = goal.getStartTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(goalStartTime * 1000 + offsetMinute * 60 * 1000);
		long timestamp = cal.getTimeInMillis() / 1000;
		
		logger.info("Looking for personal best event with: Timestamp: " + timestamp + ", Points: " + points + ", Extended amount: " + extendedAmount);
		
		for(TimelineItem item : items) {

			if(Math.abs(item.getTimestamp() - timestamp) <  timestampDelta && item.getItemType() == TimelineItemDataBase.TYPE_MILESTONE) {

				MilestoneItem milestone = (MilestoneItem) item.getData();
				MilestoneItemInfo milestoneInfo = milestone.getInfo();
				double diff = (milestoneInfo.getPoint() - points * 2.5) + (milestoneInfo.getExceededAmount() - extendedAmount * 2.5); 
				return milestone.getEventType().equals(TimelineItemDataBase.EVENT_TYPE_PERSONAL_BEST) && diff == 0;
			}
		}

		return false;
	}

	protected boolean hasLifeTimeDistanceTile(List<TimelineItem> items, Goal goal, int offsetMinute, int marathonNumber, int unit) {

		int[] marathonNumbers = new int[] {2, 6, 12};
		int milestoneType = 0;
		for(int i = 0; i < marathonNumbers.length; i++) {

			if(marathonNumber == marathonNumbers[i]) {
				milestoneType = i;
				break;
			}
		}

		long goalStartTime = goal.getStartTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(goalStartTime * 1000 + offsetMinute * 60 * 1000);
		long timestamp = cal.getTimeInMillis() / 1000;
		
		logger.info("Looking for distance milestone with: Timestamp: " + timestamp + ", Marathon number: " + marathonNumber + ", Unit: " + unit);
		
		for(TimelineItem item : items) {
			
			if(Math.abs(item.getTimestamp() - timestamp) <  timestampDelta && item.getItemType() == TimelineItemDataBase.TYPE_LIFETIME_DISTANCE) {

				LifetimeDistanceItem distanceitem = (LifetimeDistanceItem) item.getData();
				return distanceitem.getMilestoneType().equals(milestoneType) && distanceitem.getUnitSystem().equals(unit);
			}
		}

		return false;
	}

}
