package com.misfit.ta.backend.aut;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.ProgressData;
import com.misfit.ta.backend.data.goal.TripleTapData;
import com.misfit.ta.backend.data.graph.GraphItem;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.DisplayUnit;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.statistics.PersonalRecord;
import com.misfit.ta.backend.data.statistics.Record;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.CustomTimelineItemData;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.common.MVPEnums;
import com.misfit.ta.utils.TextTool;

public class DefaultValues {

	// values
	static public String ArbitraryToken = "134654678931452236-arbitrarytokenasdasfjk";

	// messages
	static public String InvalidEmail = "Sorry, this email is invalid";
	static public String DuplicateEmail = "Sorry, someone else has used this before";
	static public String InvalidPassword = "Sorry, this password is too short";
	static public String WrongAccountMsg = "Incorrect email or password";
	static public String InvalidAuthToken = "Invalid auth token";
	static public String DeviceLinkedToYourAccount = "Device already linked to your account";
	static public String DeviceLinkedToAnotherAccount = "Device already linked to another account";
	static public String DeviceUsedToLinkToYourAccount = "Device is used to be linked to your account";
	static public String DeviceUsedToLinkToOtherAccount = "Device is used to be linked to other account";
	static public String DeviceNotLinkToAnyAccount = "Device is not linked to any account yet";
	static public String DeviceNotLinkToThisAccount = "This device doesn't link to any device";
	static public String DeviceUnlinkedSuccessfully = "Unlink device successfully";
	
	// social error messages
	static public int UserNotFoundCode = 2;
	static public String UserNotFoundMessage = "User not found";
	
	static public int AlreadyAreFriendsCode = 303;
	static public String AlreadyAreFriendsMessage = "Already friended";
	
	static public int AlreadyRequestedCode = 302;
	static public String AlreadyRequestedMessage = "Already requested";
	
	static public int FriendRequestNotExistCode = 305;
	static public String FriendRequestNotExistMessage = "Friend request not existed";
	
	static public int NotFriendYetCode = 306;
	static public String NotFriendYetMessage = "Not friend yet";
	
	static public int InvalidParameterCode = 1;
	static public String InvalidParameterMessage = "Invalid parameters";
	

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
	static public Goal CreateGoal(long timestamp) {
		
		ProgressData progressData = new ProgressData();
		progressData.setFullBmrCalorie(1500);
		List<TripleTapData> tripleTaps = new ArrayList<TripleTapData>();
		tripleTaps.add(new TripleTapData(timestamp, MVPEnums.ACTIVITY_SLEEPING));
		Goal g = new Goal();

		g.setLocalId("goal-" + MVPApi.generateLocalId());
		g.setUpdatedAt(timestamp);

		g.setValue(1000.0);
		g.setStartTime(MVPApi.getDayStartEpoch(timestamp));
		g.setEndTime(MVPApi.getDayEndEpoch(timestamp));
		g.setTimeZoneOffsetInSeconds(7 * 3600);
		g.setProgressData(progressData);
		g.setTripleTapTypeChanges(tripleTaps);

		return g;
	}
	
	static public Goal DefaultGoal() {

		return CreateGoal(System.currentTimeMillis() / 1000);
	}
	
	static public Goal GoalForDate(int date, int month, int year) {

		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);

		return CreateGoal(cal.getTimeInMillis() / 1000);
	}

	// pedomeer
	static public Pedometer RandomPedometer() {
		
		Long now = System.currentTimeMillis() / 1000;
		
		Pedometer item = new Pedometer();
		item.setLocalId("pedometer-" + MVPApi.generateLocalId());
		item.setBatteryLevel(100);
		item.setBookmarkState(0);
		item.setClockState(0);
		item.setFirmwareRevisionString(MVPApi.LATEST_FIRMWARE_VERSION_STRING);
		item.setLastSyncedTime(now);
		item.setLinkedTime(now);
		item.setSerialNumberString(TextTool.getRandomString(10, 10));
		item.setUnlinkedTime(null);
		item.setUpdatedAt(now);
		
		return item;
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

		CustomTimelineItemData data = new CustomTimelineItemData();
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
		int itemType = r.nextInt() % (TimelineItemDataBase.TYPE_END - TimelineItemDataBase.TYPE_START + 1) + TimelineItemDataBase.TYPE_START;

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
		personalRecords.setPersonalBestRecordsInPoint(new Record());
		personalRecords.getPersonalBestRecordsInPoint().setPoint(r.nextInt() % 1500 + 500d);
		personalRecords.getPersonalBestRecordsInPoint().setTimestamp(timestamp);

		Statistics item = new Statistics();
		item.setLocalId("statistics-" + System.currentTimeMillis() + "-" + System.nanoTime());
		item.setUpdatedAt(timestamp);
		item.setPersonalRecords(personalRecords);

		return item;
	}

}
