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
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class WeekViewAPI extends ModelAPI {
	private static final int SevenDaysDiff = 604800;

	public WeekViewAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private int currentGoal;
	private int diff;
	private Calendar now = Calendar.getInstance();
	private String token;

	
	
	public void e_Init() {
		
		String email = "thy@misfitwearables.com";
		String password = "test12";
		token = MVPApi.signIn(email, password).token;
		PrometheusHelper.signIn(email, password);
	}
	
	public void e_ChangeDayView() {
		
		ShortcutsTyper.delayOne();
		HomeScreen.changeToDayView();
	}

	public void e_ChangeWeekView() {
		
		ShortcutsTyper.delayOne();
		HomeScreen.changeToWeekView();
	}
	
	public void e_ChangeGoal() {
		
		HomeScreen.tapOpenSettingsTray();
		ShortcutsTyper.delayTime(1000);
		HomeScreen.tapAdjustGoal();
		ShortcutsTyper.delayTime(3000);
				
		int oldGoal = HomeSettings.getSpinnerGoal();
		diff = 100;
		currentGoal = oldGoal + diff * (System.nanoTime() % 2 == 0 ? 1 : -1);
		if(currentGoal == oldGoal)
			currentGoal = oldGoal + 100;
		
		// change goal
		HomeSettings.setSpinnerGoal(currentGoal, oldGoal);
		HomeSettings.tapDoneAtNewGoal();
		PrometheusHelper.waitForAlert();
		
		// dismiss alert
		HomeSettings.tapOKAtNewGoalPopup();
		HomeScreen.changeToWeekView();
	}
	
	public void e_GoToLastWeek() {
		HomeScreen.goToLastWeek();
	}

	public void e_GoToThisWeek() {
		HomeScreen.goToThisWeek();
	}

	

	public void v_Today() {
		
		PrometheusHelper.handleBatteryLowPopup();
		PrometheusHelper.handleUpdateFirmwarePopup();
	}

	public void v_ThisWeek() {
		
		Assert.assertTrue(HomeScreen.isThisWeek(), "This is this week view");
	}
	
	public void v_LastWeek() {
		
		Assert.assertTrue(HomeScreen.isLastWeek(), "This is last week view");
	}
	
	public void v_UpdatedWeekGoal() {
		int day = now.get(Calendar.DAY_OF_WEEK);
		// day -- > Monday: 2; Tuesday: 3; Wednesday: 4; Thursday: 5; Friday: 6;
		// Sunday: 1
		int index = day != 1 ? day - 2 : 6;
		int totalGoal = currentGoal * (7 - index);
		if (index > 0) {
			long startTimeStamp = getTimeStampOfPreviousDay(index);
			long endTimeStamp = getTimeStampOfPreviousDay(1);
			Goal[] goals = MVPApi.searchGoal(token, startTimeStamp, endTimeStamp, 0).goals;
			System.out.println("DEBUG: start timestamp " + startTimeStamp);
			System.out.println("DEBUG: end timestamp " + endTimeStamp);
			for (int i = 0; i < goals.length; i++) {
				System.out.println("Goal " + goals[i].getStartTime());
				System.out.println("Goal " + goals[i].getValue());
				totalGoal += (int) (goals[i].getValue() / 2.5);
			}
		}
		System.out.println("DEBUG: Total goal " + totalGoal);
		ShortcutsTyper.delayOne();
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format(DefaultStrings.PointsDisplay, totalGoal)), "Week goal is correct");
	}
	
	public void v_ThisWeekImprovement() {
		
		Timeline.dragUpTimelineAndHandleTutorial();
		Integer improvement = calculateImprovement();
		if (improvement != null) {
			for(int i = -1; i <= 1; i++) {
				String message = improvement > 0 ? DefaultStrings.MoreImprovmentMessage : DefaultStrings.LessImprovmentMessage;
				Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%d%%", Math.abs(improvement + i))) && ViewUtils.isExistedView("UILabel", message), "Improvement is displayed OK");
			}
		}
		Timeline.dragDownTimeline();
	}
	
	
	
	private long getTimeStampOfPreviousDay(int index) {
		Calendar dayBefore = Calendar.getInstance();
		dayBefore.add(Calendar.DATE, -(index));
		long timeStamp = MVPApi.getDayStartEpoch(dayBefore.get(Calendar.DATE), dayBefore.get(Calendar.MONTH), dayBefore.get(Calendar.YEAR));
		return timeStamp;
	}
	
	private Integer calculateImprovement() {
		
		// search goals from monday to current day of this week and last week
		int day = now.get(Calendar.DAY_OF_WEEK);
		int index = day != 1 ? day - 2 : 6;
		long startTimeStamp = index > 0 ? getTimeStampOfPreviousDay(index) : MVPApi.getDayStartEpoch();
		
		Goal[] thisWeekGoals = MVPApi.searchGoal(token, startTimeStamp, MVPApi.getDayStartEpoch(), 0).goals;
		Goal[] lastWeekGoals = MVPApi.searchGoal(token, startTimeStamp - SevenDaysDiff, MVPApi.getDayStartEpoch() - SevenDaysDiff, 0).goals;
		
		// calculate the difference in points between each day
		int deltaProgress = 0;
		int lastWeekProgress = 0;
		for (int i = 0; i < thisWeekGoals.length; i++) {

			Double lastWeekDayRawPoints = lastWeekGoals[i].getProgressData().getPoints();
			Double thisWeekDayRawPoints = thisWeekGoals[i].getProgressData().getPoints();
			int lastWeekDayPoints = (int) Math.floor( (lastWeekDayRawPoints == null ? 0 : lastWeekDayRawPoints) / 2.5);
			int thisWeekDayPoints = (int) Math.floor( (thisWeekDayRawPoints == null ? 0 : thisWeekDayRawPoints) / 2.5);
			
			deltaProgress += (thisWeekDayPoints - lastWeekDayPoints);
			lastWeekProgress += lastWeekDayPoints;
		}
		
		System.out.println("DEBUG: deltaProgress" + deltaProgress);
		System.out.println("DEBUG: lastWeekProgress" + lastWeekProgress);
		
		Integer improvement = null;
		if (lastWeekProgress != 0) {
			improvement = Integer.valueOf((int) Math.round((deltaProgress * 1d / lastWeekProgress) * 100d));
		}

		return improvement;
	}

}
