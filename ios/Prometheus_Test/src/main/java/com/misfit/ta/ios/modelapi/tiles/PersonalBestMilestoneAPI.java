package com.misfit.ta.ios.modelapi.tiles;

import java.io.File;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;

public class PersonalBestMilestoneAPI extends ModelAPI {

	public PersonalBestMilestoneAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	

	public void e_init() {
		
		// sign up with goal = 1000 pts
		String email = PrometheusHelper.signUpDefaultProfile();
		String token = MVPApi.signIn(email, "qwerty1").token;
		
		// api: create yesterday's goal
		BackendHelper.completeGoalInPast(token, 1);
		
		// api: update statistics to set best point to 400 pts
		BackendHelper.setPersonalBest(token, 400);
		
		// pull to refresh to make sure local db is latest
		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}
	
	public void e_inputPersonalBest() {
		
		// input 500 pts activity session
		String[] times = new String[] { "7", "00", "AM" };
		
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(times, 50, 5000);
		HomeScreen.tapSave();
		Timeline.dragUpTimelineAndHandleTutorial();
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
