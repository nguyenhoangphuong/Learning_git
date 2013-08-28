package com.misfit.ta.backend.aut;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.ProgressData;
import com.misfit.ta.backend.data.goal.TrippleTapData;
import com.misfit.ta.backend.data.graph.GraphItem;
import com.misfit.ta.backend.data.profile.DisplayUnit;
import com.misfit.ta.backend.data.profile.PersonalRecord;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.data.timeline.TimelineData;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.TimelineItemBase;

public class DefaultValues {

	static public String ArbitraryToken = "134654678931452236-arbitrarytokenasdasfjk";

	// messages
	static public String InvalidEmail = "Sorry, this email is invalid";
	static public String DuplicateEmail = "Sorry, someone else has used this before";
	static public String InvalidPassword = "Sorry, this password is too short";
	static public String WrongAccountMsg = "Incorrect email or password";
	static public String InvalidAuthToken = "Invalid auth token";

	// profile
	static public ProfileData DefaultProfile() {
		ProfileData p = new ProfileData();

		p.setLocalId(System.currentTimeMillis() + "-" + System.nanoTime());
		p.setUpdatedAt((long) (System.currentTimeMillis() / 1000));

		p.setWeight(144.4);
		p.setHeight(66.0);
		p.setGender(0);
		p.setDateOfBirth((long) 684954000);
		p.setName("Tears");

		p.setGoalLevel(4);
		p.setLatestVersion("8");
		p.setWearingPosition("Wrist");
		p.setPersonalRecords(null);
		p.setDisplayedUnits(new DisplayUnit());

		return p;
	}

	// goal
	static public Goal DefaultGoal() {

		ProgressData progressData = new ProgressData();
		progressData.setFullBmrCalorie(1500);
		Goal g = new Goal();

		g.setLocalId(System.currentTimeMillis() + "-" + System.nanoTime());
		g.setUpdatedAt((long) (System.currentTimeMillis() / 1000));

		g.setValue(1000.0);
		g.setStartTime(MVPApi.getDayStartEpoch(System.currentTimeMillis() / 1000));
		g.setEndTime(MVPApi.getDayEndEpoch(System.currentTimeMillis() / 1000));
		g.setTimeZoneOffsetInSeconds(7 * 3600);
		g.setProgressData(progressData);
		g.setTripleTapTypeChanges(new ArrayList<TrippleTapData>());

		return g;
	}

	static public Goal GoalForDate(int date, int month, int year) {

		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);

		ProgressData progressData = new ProgressData();
		progressData.setFullBmrCalorie(1500);
		Goal g = new Goal();

		g.setLocalId(cal.getTimeInMillis() + "-" + System.nanoTime());
		g.setUpdatedAt((long) (System.currentTimeMillis() / 1000));

		g.setValue(1000.0);
		g.setStartTime(MVPApi.getDayStartEpoch(cal.getTimeInMillis() / 1000));
		g.setEndTime(MVPApi.getDayEndEpoch(cal.getTimeInMillis() / 1000));
		g.setTimeZoneOffsetInSeconds(7 * 3600);
		g.setProgressData(progressData);
		g.setTripleTapTypeChanges(new ArrayList<TrippleTapData>());

		return g;
	}

	// graphitem
	static public GraphItem CreateGraphItem(long timestamp) {

		GraphItem item = new GraphItem();
		Random r = new Random();
		item.setLocalId("graphitem-" + System.currentTimeMillis() + "-" + System.nanoTime());
		item.setUpdatedAt(timestamp);
		item.setTimestamp(timestamp);
		item.setTotalValue(0d);
		item.setAverageValue((r.nextInt() % 70 + 10) / 60d);

		return item;
	}

	static public GraphItem RandomGraphItem() {

		return CreateGraphItem(System.currentTimeMillis() / 1000);
	}

	static public GraphItem RandomGraphItem(int secondsOfDay) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.HOUR_OF_DAY, secondsOfDay / 3600);
		cal.set(Calendar.MINUTE, (secondsOfDay % 3600) / 60);
		cal.set(Calendar.SECOND, (secondsOfDay % 3600) % 60);

		return CreateGraphItem(cal.getTimeInMillis() / 1000);
	}

	// timelineitem
	static public TimelineItem CreateTimelineItem(long timestamp, int timelineType) {

		Random r = new Random();

		TimelineData data = new TimelineData();
		data.setTimestamp(timestamp);
		data.setType(timelineType);
		data.addValue("Random", r.nextInt());

		TimelineItem item = new TimelineItem();
		item.setLocalId("timelineitem-" + System.currentTimeMillis() + "-" + System.nanoTime());
		item.setUpdatedAt(timestamp);
		item.setTimestamp(timestamp);
		item.setItemType(timelineType);
		item.setData(data);

		return item;
	}
	
	static public TimelineItem CreateTimelineItem(long timestamp) {
		
		Random r = new Random();
		int itemType = r.nextInt() % (TimelineItemBase.TYPE_END - TimelineItemBase.TYPE_START + 1) 
				+ TimelineItemBase.TYPE_START;
		
		return CreateTimelineItem(timestamp, itemType);
	}

	static public TimelineItem RandomTimelineItem() {

		return CreateTimelineItem(System.currentTimeMillis() / 1000);
	}
	
	static public TimelineItem RandomTimelineItem(int secondsOfDay) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.HOUR_OF_DAY, secondsOfDay / 3600);
		cal.set(Calendar.MINUTE, (secondsOfDay % 3600) / 60);
		cal.set(Calendar.SECOND, (secondsOfDay % 3600) % 60);

		return CreateTimelineItem(cal.getTimeInMillis() / 1000);
	}
	
	static public TimelineItem RandomTimelineItem(int secondsOfDay, int timelineType) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.HOUR_OF_DAY, secondsOfDay / 3600);
		cal.set(Calendar.MINUTE, (secondsOfDay % 3600) / 60);
		cal.set(Calendar.SECOND, (secondsOfDay % 3600) % 60);
		
		return CreateTimelineItem(cal.getTimeInMillis() / 1000, timelineType);
	}

	// statistics
	static public Statistics RandomStatistic() {
		
		Random r = new Random();
		long timestamp = System.currentTimeMillis() / 1000;
		
		PersonalRecord personalRecords = new PersonalRecord();
		personalRecords.setPersonalBestRecordsInPoint(r.nextInt() % 1500 + 500d);
		
		Statistics item = new Statistics();
		item.setLocalId("statistics-" + System.currentTimeMillis() + "-" + System.nanoTime());
		item.setUpdatedAt(timestamp);
		item.setPersonalRecords(personalRecords);
		
		return item;
	}

}
