package com.misfit.ta.ios.modelapi.tiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class StreakMilestoneAPI extends ModelAPI {

	public StreakMilestoneAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private int mins = 60;
	private int dayDiff = 0;

	private String email = "";
	private String token = "";
	private Goal todayBlankGoal;

	public void e_init() {

		// sign up with goal = 1000 pts
		email = PrometheusHelper.signUpDefaultProfile();

		// get current goal
		token = MVPApi.signIn(email, "qwerty1").token;
		todayBlankGoal = MVPApi.searchGoal(token, MVPApi.getDayStartEpoch(), Integer.MAX_VALUE, 0).goals[0];
		
		// update current day goal's value to 100 pts
		todayBlankGoal.setValue(100 * 2.5d);
		MVPApi.updateGoal(token, todayBlankGoal);
		
		// create 1 goal in the past
		BackendHelper.completeGoalInPast(token, ++dayDiff);
		
		// Now we have 1 complete goal and
		// today goal with value = 100
	}

	public void e_createGoalInThePast() {

		// create goal in the past using api
		BackendHelper.completeGoalInPast(token, ++dayDiff);

		// reset current goal using api
		MVPApi.updateGoal(token, todayBlankGoal);
				
		// get new data from server
		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}

	public void e_finishGoal() {

		// input 100 pts (10 mins - 1000 steps)
		mins += 20;
		String[] times = new String[] { mins / 60 + "", String.format("%02d", mins % 60), "AM" };

		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(times, 10, 1000);
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(5000);
	}

	
	public void v_HomeScreen() {
		PrometheusHelper.handleUpdateFirmwarePopup();
		Assert.assertTrue(HomeScreen.isToday(), "Current screen is HomeScreen");
	}

	public void v_DaysStreak() {

		int totalGoal = dayDiff + 1;
		String[] messages = {};
		
		if(totalGoal == 3)
			messages = Timeline.Streak3DaysMessages;
		if(totalGoal == 4)
			messages = Timeline.Streak4DaysMessages;
		if(totalGoal >= 5 && totalGoal <= 6)
			messages = Timeline.Streak5to6DaysMessages;
		if(totalGoal == 7)
			messages = Timeline.Streak7DaysMessages;
		if(totalGoal >= 8 && totalGoal <= 11)
			messages = Timeline.Streak8to11DaysMessages;
		if(totalGoal >= 12 && totalGoal <= 13) {
			List<String> list = new ArrayList<String>();
			list.addAll(Arrays.asList(Timeline.Streak8to11DaysMessages));
			list.addAll(Arrays.asList(Timeline.Streak12to13DaysOnMessages));

			messages = list.toArray(new String[list.size()]);
		}
		if(totalGoal == 14)
			messages = Timeline.Streak14DaysMessages;
		if(totalGoal >= 15)
			messages = Timeline.Streak15DaysOnMessages;
		
		if(messages.length != 0)
			checkStreakTile(messages);
	}

	public void v_End() {
		
	}
	
	public void checkStreakTile(String[] messages) {

		Timeline.dragUpTimelineAndHandleTutorial();
		for(int i = 0; i < dayDiff; i++)
			Gui.swipeUp(1000);

		//String title = (mins / 60) + ":" + String.format("%02d", mins % 60 + 10) + "am";
		String title = (dayDiff + 1) + "";
		boolean pass = false;
		for (int i = 0; i < 3; i++) {
			Timeline.openTile(title);
			Gui.captureScreen("streaktile-" + System.nanoTime());
			if (Timeline.isStreakTileCorrect(title, dayDiff + 1, messages)) {
				pass = true;
				Timeline.closeCurrentTile();
				break;
			}
			Timeline.closeCurrentTile();
		}

		Timeline.dragDownTimeline();

		Assert.assertTrue(pass, "At least one tile has correct content");
	}

}
