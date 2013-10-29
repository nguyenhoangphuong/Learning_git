package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Sync;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class WeekViewAPI extends ModelAPI {
	private static final int SevenDaysDiff = 604800;

	public WeekViewAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private int currentGoal;
	private int diff;
	private Calendar now = Calendar.getInstance();
	private String token;

	/**
	 * This method implements the Edge 'e_ChangeGoal'
	 * 
	 */
	public void e_ChangeGoal() {
		HomeScreen.tapOpenSettingsTray();
		ShortcutsTyper.delayTime(200);
		HomeScreen.tapAdjustGoal();
		ShortcutsTyper.delayTime(300);
		currentGoal = HomeSettings.getSpinnerGoal();
		int oldGoal = currentGoal;
		diff = PrometheusHelper.randInt(1, 5) * 100;
		int day = now.get(Calendar.DAY_OF_WEEK);
		if (day % 2 == 0) {
			diff *= -1;
		}
		currentGoal = currentGoal + diff;
		HomeSettings.setSpinnerGoal(currentGoal, oldGoal);
		System.out.println("DEBUG: current goal " + currentGoal);
		ShortcutsTyper.delayTime(300);
		HomeSettings.tapDoneAtNewGoal();
		ShortcutsTyper.delayTime(5000);
		HomeSettings.tapOKAtNewGoalPopup();
		ShortcutsTyper.delayTime(200);
	}

	/**
	 * This method implements the Edge 'e_CheckGoal'
	 * 
	 */
	public void e_CheckGoal() {
		Timeline.dragUpTimelineAndHandleTutorial();
		Integer improvement = calculateImprovement();
		if (improvement != null) {
			String message = improvement > 0 ? DefaultStrings.MoreImprovmentMessage
					: DefaultStrings.LessImprovmentMessage;
			Assert.assertTrue(ViewUtils.isExistedView("UILabel",
					String.format("%d%%", Math.abs(improvement)))
					&& ViewUtils.isExistedView("UILabel", message), "Improvement is displayed OK");
		}
		Timeline.dragDownTimeline();
	}

	/**
	 * This method implements the Edge 'e_GoToLastWeek'
	 * 
	 */
	public void e_GoToLastWeek() {
		HomeScreen.goToLastWeek();
	}

	/**
	 * This method implements the Edge 'e_GoToThisWeek'
	 * 
	 */
	public void e_GoToThisWeek() {
		HomeScreen.goToThisWeek();
	}

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		String email = "thy@misfitwearables.com";
		String password = "test12";
		Sync.signIn(email, password);
		ShortcutsTyper.delayTime(2000);
		token = MVPApi.signIn(email, password).token;
	}

	/**
	 * This method implements the Edge 'e_Stay'
	 * 
	 */
	public void e_Stay() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_EndInput'
	 * 
	 */
	public void v_EndInput() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_LastWeekView'
	 * 
	 */
	public void v_LastWeek() {
		Assert.assertTrue(HomeScreen.isLastWeek(), "This is last week view");
	}

	/**
	 * This method implements the Vertex 'v_ThisWeek'
	 * 
	 */
	public void v_ThisWeek() {
		Assert.assertTrue(HomeScreen.isThisWeek(), "This is this week view");
	}

	public void v_Today() {
		PrometheusHelper.handleBatteryLowPopup();
		PrometheusHelper.handleUpdateFirmwarePopup();
	}

	public void e_ChangeDayView() {
		HomeScreen.changeToDayView();
	}

	public void e_ChangeWeekView() {
		HomeScreen.changeToWeekView();
	}

	/**
	 * This method implements the Vertex 'v_UpdatedWeekGoal'
	 * 
	 */
	public void v_UpdatedWeekGoal() {
		int day = now.get(Calendar.DAY_OF_WEEK);
		// day -- > Monday: 2; Tuesday: 3; Wednesday: 4; Thursday: 5; Friday: 6;
		// Sunday: 1
		int index = day != 1 ? day - 2 : 6;
		int totalGoal = currentGoal * (7 - index);
		if (index > 0) {
			long startTimeStamp = getTimeStampOfPreviousDay(index);
			long endTimeStamp = getTimeStampOfPreviousDay(1);
			Goal[] goals = MVPApi.searchGoal(token, startTimeStamp,
					endTimeStamp, 0).goals;
			System.out.println("DEBUG: start timestamp " + startTimeStamp);
			System.out.println("DEBUG: end timestamp " + endTimeStamp);
			for (int i = 0; i < goals.length; i++) {
				System.out.println("Goal " + goals[i].getStartTime());
				System.out.println("Goal " + goals[i].getValue());
				totalGoal += (int) (goals[i].getValue() / 2.5);
			}
		}
		System.out.println("DEBUG: Total goal " + totalGoal);
		Assert.assertTrue(
				ViewUtils.isExistedView("UILabel",
						String.format(DefaultStrings.PointsDisplay, totalGoal)),
				"Week goal is correct");
	}

	private long getTimeStampOfPreviousDay(int index) {
		Calendar dayBefore = Calendar.getInstance();
		dayBefore.add(Calendar.DATE, -(index));
		long timeStamp = MVPApi.getDayStartEpoch(dayBefore.get(Calendar.DATE),
				dayBefore.get(Calendar.MONTH), dayBefore.get(Calendar.YEAR));
		return timeStamp;
	}

	public void v_UpdatedTodayGoal() {

	}

	public Integer calculateImprovement() {
		int day = now.get(Calendar.DAY_OF_WEEK);
		int index = day != 1 ? day - 2 : 6;
		Goal[] thisWeekGoals = {};
		Goal[] lastWeekGoals = {};
		long startTimeStamp = index > 0 ? getTimeStampOfPreviousDay(index)
				: MVPApi.getDayStartEpoch();
		// get this week goals
		// if today is Thursday, we only search goal from Monday to Thursday
		thisWeekGoals = MVPApi.searchGoal(token, startTimeStamp,
				MVPApi.getDayStartEpoch(), 0).goals;
		// get last week goals
		// only search goal from last Monday to last Thursday
		lastWeekGoals = MVPApi.searchGoal(token, startTimeStamp - SevenDaysDiff,
				MVPApi.getDayStartEpoch() - SevenDaysDiff, 0).goals;
		// create a map with timestamp and last week points
		Map<Long, Double> lastWeekPointsWithTimestampMap = new HashMap<Long, Double>(
				(int) (lastWeekGoals.length / 0.75) + 1);
		for (int i = 0; i < lastWeekGoals.length; i++) {
			lastWeekPointsWithTimestampMap.put(lastWeekGoals[i].getStartTime(),
					lastWeekGoals[i].getProgressData().getPoints());
		}

		int deltaProgress = 0;
		int lastWeekProgress = 0;
		for (int i = 0; i < thisWeekGoals.length; i++) {
			// find the corresponding day in last week, 7 days ago
			long lastWeekDayTimestamp = thisWeekGoals[i].getStartTime() - SevenDaysDiff;
			if (lastWeekPointsWithTimestampMap
					.containsKey(lastWeekDayTimestamp)) {
				int lastWeekDayPoints = (int) Math
						.floor(lastWeekPointsWithTimestampMap
								.get(lastWeekDayTimestamp) / 2.5);

				deltaProgress += (int) Math.floor(thisWeekGoals[i]
						.getProgressData().getPoints() / 2.5)
						- lastWeekDayPoints;
				lastWeekProgress += lastWeekDayPoints;
			}
		}
		System.out.println("DEBUG: deltaProgress" + deltaProgress);
		System.out.println("DEBUG: lastWeekProgress" + lastWeekProgress);
		Integer improvement = null;
		if (lastWeekProgress != 0) {
			improvement = Integer.valueOf((int) Math
					.round((deltaProgress * 1d / lastWeekProgress) * 100d));
		}
		return improvement;
	}
}
