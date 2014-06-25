package com.misfit.ta.backend.aut.correctness.servercalculation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.google.resting.json.JSONException;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.performance.newservercalculation.NewServerCalculationScenario;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.TimestampObject;
import com.misfit.ta.backend.data.goal.GoalRawData;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsGoalValueChange;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsTimezoneOffsetChange;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsTracking;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsTripleTapTypeChange;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.common.MVPCommon;


public class BackendNewServerCalculationActivityGoalSettingsTracking extends BackendServerCalculationBase{
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "NewServerCalculationGoalCreation", "NewServercalculation", "GoalCreation" })
	public void NewServerCalculation_GoalCreation() throws IOException, JSONException {
		NewServerCalculationScenario scenarioTest = new NewServerCalculationScenario();
		String email = MVPApi.generateUniqueEmail();
		System.out.println(email);
		scenarioTest.runNewServerCalculationGoalCreationTest(email);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "NewServerCalculationGoalCreation", "NewServercalculation", "GoalCreation" })
	public void NewServerCalculation_GoalCreation_SimpleCase() throws IOException, JSONException {
		String email = MVPApi.generateUniqueEmail();
		long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signUp(email, "qqqqqq").token;
		String userId = MVPApi.getUserId(token);


		// create profile / pedometer / statistics
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);
		
		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();
		
		GoalSettingsTimezoneOffsetChange timezone = new GoalSettingsTimezoneOffsetChange(timestamp, 25200);
		GoalSettingsGoalValueChange goalValue = new GoalSettingsGoalValueChange(timestamp, 190.98);
		GoalSettingsTripleTapTypeChange tripleTapTypeChange = new GoalSettingsTripleTapTypeChange(timestamp, 3);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		changes.add(timezone);
		changes.add(goalValue);
		changes.add(tripleTapTypeChange);
		
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(token, goalSettingsTracking);
		
		GoalRawData data = new GoalRawData();
		data.appendGoalRawData(generateEmptyRawData(0 * 60 + 40, 24 * 60));
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(MVPCommon.getDayStartEpoch(timestamp), 25200 / 60, "0104", "18", data).rawData);
		pushSyncData(timestamp, userId, pedometer.getSerialNumberString(), dataStrings);
		System.out.println("****************Email: " + email);
	}
}
