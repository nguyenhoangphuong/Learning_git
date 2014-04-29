package com.misfit.ta.ios.modelapi.tiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.common.Verify;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;

public class PersonalBestMilestoneAPI extends ModelAPI {

	private String email;
	private List<String> errors = new ArrayList<String>();
	
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
		errors.add(Verify.verifyTrue(Timeline.isPersonalBestTileCorrect(time, 500, 400, Timeline.PersonalBestMessages),
				String.format("Personal best tile [%s - 500pts new - 400pts old] displayed correctly", time)));
		Timeline.closeCurrentTile();
		Timeline.dragDownTimeline();
		
	}

	public void v_End() {
		
		if(!Verify.verifyAll(errors))
			Assert.fail("Not all assertions pass");
	}
	
}
