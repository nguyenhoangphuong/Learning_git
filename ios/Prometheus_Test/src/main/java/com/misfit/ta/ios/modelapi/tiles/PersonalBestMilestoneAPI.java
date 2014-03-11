package com.misfit.ta.ios.modelapi.tiles;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;

public class PersonalBestMilestoneAPI extends ModelAPI {

	private String email;
	
	public PersonalBestMilestoneAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	

	public void e_init() {
		
		// sign up with goal = 1000 pts
		email = PrometheusHelper.signUpDefaultProfile();
		String token = MVPApi.signIn(email, "qwerty1").token;
		
		// api: create yesterday's goal
		BackendHelper.createGoalInPast(token, 1);
		
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
	}
	
	public void e_signOutAndSignInAgain() {
		
		PrometheusHelper.signOut();
		PrometheusHelper.signIn(email, "qwerty1");
	}
	
	
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current screen is HomeScreen");
	}
	
	public void v_PersonalBest() {
		
		Timeline.dragUpTimelineAndHandleTutorial();
		String time = "7:50am";
		Timeline.openTile(time);
		Assert.assertTrue(Timeline.isPersonalBestTileCorrect(time, 500, 400, Timeline.PersonalBestMessages),
				"Personal best displayed correctly");
		Timeline.closeCurrentTile();
		Timeline.dragDownTimeline();
		
	}

}
