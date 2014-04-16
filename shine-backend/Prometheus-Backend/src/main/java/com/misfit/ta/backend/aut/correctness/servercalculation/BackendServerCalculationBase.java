package com.misfit.ta.backend.aut.correctness.servercalculation;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalRawData;
import com.misfit.ta.backend.data.profile.DisplayUnit;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.ActivitySessionItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.LifetimeDistanceItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.MilestoneItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.MilestoneItemInfo;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;

public class BackendServerCalculationBase extends BackendAutomation {
	
	// fields
	protected static final int timestampDelta = 120;
	protected static final double fullBmrDelta = 10;
	protected static final double totalCalorieDelta = 30;
	protected static final double distanceDelta = 0.001;
	
	
	// data helpers
	protected void changeDistanceUnit(String token, int unit) {

		ProfileData profile = new ProfileData();
		profile.setDisplayedUnits(new DisplayUnit());
		profile.getDisplayedUnits().setDistanceUnit(unit);
		MVPApi.updateProfile(token, profile);
	}

	protected GoalRawData generateSessionRawData(int totalSteps, int totalPoints, int duration)  {

		int stepPerMinute = totalSteps / duration;
		int pointPerMinute = totalPoints / duration;

		int[] steps = new int[duration];
		int[] points = new int[duration];
		int[] variances = new int[duration];

		Arrays.fill(steps, stepPerMinute);
		Arrays.fill(points, (int)(pointPerMinute * 2.5));
		Arrays.fill(variances, 10000);

		// if steps/point is not devidable
		if(totalSteps % duration != 0)
			steps[0] = totalSteps % duration;

		if(totalPoints % duration != 0)
			points[0] = totalPoints % duration;

		GoalRawData rawdata = new GoalRawData();
		rawdata.setPoints(points);
		rawdata.setSteps(steps);

		rawdata.setVariances(variances);

		return rawdata;
	}

	protected GoalRawData generateEmptyRawData(int duration) {

		int[] steps = new int[duration];
		int[] points = new int[duration];
		int[] variances = new int[duration];

		Arrays.fill(steps, 0);
		Arrays.fill(points, 0);
		Arrays.fill(variances, 10000);

		GoalRawData rawdata = new GoalRawData();
		rawdata.setPoints(points);
		rawdata.setSteps(steps);
		rawdata.setVariances(variances);

		return rawdata;
	}

	protected GoalRawData generateEmptyRawData(int includeStartOffsetMinute, int excludeEndOffsetMinute) {

		int duration = excludeEndOffsetMinute - includeStartOffsetMinute;
		return generateEmptyRawData(duration);
	}

	protected GoalRawData generateGapData(int stepPerMinute, int pointPerMinute, int activeInterval, int idleInterval, int duration) {

		int gapCount = duration / (idleInterval + activeInterval);

		int[] steps = new int[duration];
		int[] points = new int[duration];
		int[] variances = new int[duration];

		Arrays.fill(steps, 0);
		Arrays.fill(points, 0);
		Arrays.fill(variances, 10000);

		int index = 0;
		for(int i = 0; i < gapCount; i++) {

			for(int j = 0; j < activeInterval; j++) {
				steps[index] = stepPerMinute;
				points[index] = (int)(pointPerMinute * 2.5);
				index++;
			}

			for(int k = 0; k < idleInterval; k++) {
				steps[index] = 0;
				points[index] = 0;
				index++;
			}

		}

		GoalRawData rawdata = new GoalRawData();
		rawdata.setPoints(points);
		rawdata.setSteps(steps);
		rawdata.setVariances(variances);

		return rawdata;
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
