package com.misfit.ta.ios.modelapi.tiles;

import java.io.File;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.goal.Goal;
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
		email = "test1379418406879sjk92z@qa.com";
//		email = PrometheusHelper.signUp();
//		ShortcutsTyper.delayTime(5000);

		// get current goal
		token = MVPApi.signIn(email, "qwerty1").token;
		todayBlankGoal = MVPApi.searchGoal(token, MVPApi.getDayStartEpoch(), Integer.MAX_VALUE, 0).goals[0];
	}

	public void e_createGoalInThePast() {

		// create goal in the past using api
		long timestamp = System.currentTimeMillis() / 1000 - 3600 * 24 * (++dayDiff);
		Goal goal = DefaultValues.CreateGoal(timestamp);
		goal.setValue(1000d);
		goal.getProgressData().setPoints(1200d);
		goal.getProgressData().setDistanceMiles(2d);
		goal.getProgressData().setSteps(4000);
		goal.getProgressData().setSeconds(3600);
		MVPApi.createGoal(token, goal);
	}
	
	public void e_refresh() {
		
		// get new data from server
		
	}

	public void e_finishGoal() {

		// input 1260 pts record (5 mins 1500 steps)
		mins += 10;
		String[] times = new String[] { mins / 60 + "", String.format("%02d", mins % 60), "AM" };

		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(times, 5, 1500);
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(10000);
	}

	public void e_resetGoal() {

		// reset current goal using api
		MVPApi.updateGoal(token, todayBlankGoal);
	}

	public void v_HomeScreen() {

		Assert.assertTrue(HomeScreen.isToday(), "Current screen is HomeScreen");
	}

	public void v_3DaysStreak() {
		
		checkStreakTile(Timeline.Streak3DaysMessages);
	}

	public void v_4DaysStreak() {

		checkStreakTile(Timeline.Streak4DaysMessages);
	}

	public void v_5to6DaysStreak() {

		checkStreakTile(Timeline.Streak5to6DaysMessages);
	}

	public void v_7DaysStreak() {

		checkStreakTile(Timeline.Streak7DaysMessages);
	}

	public void v_8to12DaysStreak() {

		checkStreakTile(Timeline.Streak8to12DaysMessages);
	}
	
	public void checkStreakTile(String[] messages) {
		
		String title = (mins / 60) + ":" + String.format("%02d", mins % 60 + 5) + "am";
		Timeline.dragUpTimeline();
		Timeline.openTile(title);
		Timeline.isStreakTileCorrect(title, dayDiff, messages);
		Timeline.closeCurrentTile();
		Timeline.dragDownTimeline();
	}

}
