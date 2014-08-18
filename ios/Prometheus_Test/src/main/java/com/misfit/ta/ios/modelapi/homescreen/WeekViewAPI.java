package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.common.Verify;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
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
	private Calendar now = Calendar.getInstance();
	private String token;
	private List<String> errors = new ArrayList<String>();
	
	
	public void e_Init() {
		
		String email = "automation_weekly@qa.com";
		String password = "qqqqqq";
		
		token = MVPApi.signIn(email, password).token;
		for(int i = 14; i >=0; i--) {
			MVPApi.createGoal(token, Goal.getDefaultGoal(System.currentTimeMillis() / 1000 - 3600 * 24 * i));
		}
		
		Goal goal = MVPApi.searchGoal(token, getTimeStampOfPreviousDay(0), null, null).goals[0];
		goal.getProgressData().setPoints(MVPCommon.randInt(10, 15) * 100 * 2.5);
		MVPApi.updateGoal(token, goal);
		
		PrometheusHelper.signIn(email,password);
		HomeScreen.pullToRefresh();
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
				
		int oldGoal = currentGoal;
		do {
			currentGoal = PrometheusHelper.randInt(10, 25) * 100;
		} while (oldGoal == currentGoal);
		
		// change goal
		HomeSettings.setActivityGoal(currentGoal);
		HomeSettings.tapDoneAtNewGoal();
		PrometheusHelper.waitForAlert();
		
		// dismiss alert
		HomeSettings.tapOKAtNewGoalPopup();
		HomeScreen.tapOpenSettingsTray();
		HomeScreen.changeToWeekView();
	}
	
	public void e_GoToLastWeek() {
		HomeScreen.tapPreviousDayButton(1);
	}

	public void e_GoToThisWeek() {
		HomeScreen.tapNextDayButton(1);
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
		
		// day -- > Monday: 2; Tuesday: 3; Wednesday: 4; Thursday: 5; Friday: 6;
		// Sunday: 1
		int day = now.get(Calendar.DAY_OF_WEEK);
		int index = day != 1 ? day - 2 : 6;
		int totalGoal = currentGoal * (7 - index);
		
		if (index > 0) {
			
			long startTimeStamp = getTimeStampOfPreviousDay(index);
			long endTimeStamp = getTimeStampOfPreviousDay(1);
			Goal[] goals = MVPApi.searchGoal(token, startTimeStamp, endTimeStamp, null).goals;
			
			for (int i = 0; i < goals.length; i++) {
				System.out.println("Goal " + goals[i].getStartTime());
				System.out.println("Goal " + goals[i].getGoalValue());
				totalGoal += (int) (goals[i].getGoalValue() / 2.5);
			}
		}
		
		System.out.println("DEBUG: Total goal " + totalGoal);
		errors.add(Verify.verifyTrue(ViewUtils.isExistedView("UILabel", 
				String.format(DefaultStrings.PointsDisplay, totalGoal)), 
				String.format("Week goal [%d] is correct", totalGoal)));
	}
	
	public void v_ThisWeekImprovement() {
		
		Timeline.dragUpTimelineAndHandleTutorial();
		Gui.captureScreen("weekview" + System.nanoTime());
		Integer improvement = calculateImprovement();
		boolean pass = false;
		
		if (improvement != null) {
			for(int i = -1; i <= 1; i++) {
				String message = improvement > 0 ? DefaultStrings.MoreImprovmentMessage : DefaultStrings.LessImprovmentMessage;
				if (ViewUtils.isExistedView("UILabel", String.format("%d%%", Math.abs(improvement + i))) && 
					ViewUtils.isExistedView("UILabel", message)) {
					pass = true;
					break;
				}
			}
		}
		
		errors.add(Verify.verifyTrue(pass, String.format("Improvement [%d%%] displays correctly", improvement)));
		Timeline.dragDownTimeline();
		
		// verify all
		if(!Verify.verifyAll(errors))
			Assert.fail("Not all assertions pass");
	}
	
	
	
	private long getTimeStampOfPreviousDay(int index) {
		
		return MVPCommon.getDayStartEpoch(System.currentTimeMillis() / 1000 - 3600 * 24 * index);
	}
	
	private Integer calculateImprovement() {
		
		// search goals from monday to current day of this week and last week
		int day = now.get(Calendar.DAY_OF_WEEK);
		int index = day != 1 ? day - 2 : 6;
		long startTimeStamp = index > 0 ? getTimeStampOfPreviousDay(index) : MVPCommon.getDayStartEpoch();
		
		Goal[] thisWeekGoals = MVPApi.searchGoal(token, startTimeStamp, MVPCommon.getDayStartEpoch(), null).goals;
		Goal[] lastWeekGoals = MVPApi.searchGoal(token, startTimeStamp - SevenDaysDiff, MVPCommon.getDayStartEpoch() - SevenDaysDiff, null).goals;
		
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
