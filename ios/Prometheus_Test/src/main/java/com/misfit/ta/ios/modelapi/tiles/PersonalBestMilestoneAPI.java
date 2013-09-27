package com.misfit.ta.ios.modelapi.tiles;

import java.io.File;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class PersonalBestMilestoneAPI extends ModelAPI {

	public PersonalBestMilestoneAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	

	public void e_init() {
		
		// sign up with goal = 1000 pts
		String email = PrometheusHelper.signUp();
		
		// api: create yesterday's goal
		long yesterday = System.currentTimeMillis() / 1000 - 3600 * 24;
		String token = MVPApi.signIn(email, "qwerty1").token;
		Goal goal = DefaultValues.CreateGoal(yesterday);
		MVPApi.createGoal(token, goal);
		
		// api: update statistics to set best point to 400 pts
		Statistics statistics = DefaultValues.RandomStatistic();
		statistics.getPersonalRecords().setPersonalBestRecordsInPoint(1000d);
		statistics.setUpdatedAt(System.currentTimeMillis() / 1000);
		MVPApi.createStatistics(token, statistics);
		
		// pull to refresh to make sure local db is latest
		HomeScreen.pullToRefresh();
		ShortcutsTyper.delayTime(5000);
	}
	
	public void e_inputPersonalBest() {
		
		// input 500 pts activity session
		String[] times = new String[] { "7", "00", "AM" };
		
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(times, 50, 5000);
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(5000);
		Timeline.dragUpTimeline();
	}
	
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isTodayDefault(), "Current screen is HomeScreen");
	}
	
	public void v_PersonalBest() {
		
		String time = "7:50am";
		Timeline.openTile(time);
		Assert.assertTrue(Timeline.isPersonalBestTileCorrect(time, 500, 400, Timeline.PersonalBestMessages),
				"Personal best displayed correctly");
		Timeline.closeCurrentTile();
		
	}

}
