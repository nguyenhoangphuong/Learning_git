package com.misfit.ta.android.aut;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.modelapi.settings.GoalSettingsAPI;
import com.misfit.ta.android.modelapi.settings.ProfileSettingsAPI;
import com.misfit.ta.android.modelapi.settings.ShineSettingsAPI;
import com.misfit.ta.android.modelapi.settings.SleepGoalSettingsAPI;
import com.misfit.ta.android.modelapi.signin.SuccessfulSignInAPI;
import com.misfit.ta.utils.Files;

public class SettingsTest extends AutomationTest {

	@Test(groups = { "android", "Prometheus", "settings", "Included" })
    public void profileSettingsTest() throws InterruptedException, StopConditionException, IOException {
		ModelHandler model = getModelhandler();
       
        model.add("profileSettings", new ProfileSettingsAPI(this, Files.getFile("model/settings/ProfileSettings.graphml"), true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("profileSettings");

        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
	@Test(groups = { "android", "Prometheus", "settings", "Included" })
    public void shineSettingsTest() throws InterruptedException, StopConditionException, IOException {
		ModelHandler model = getModelhandler();
       
        model.add("shineSettings", new ShineSettingsAPI(this, Files.getFile("model/settings/ShineSettings.graphml"), true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("shineSettings");

        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
	
	@Test(groups = { "android", "Prometheus", "settings", "Included" })
    public void goalSettingsTest() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("goalSettings", new GoalSettingsAPI(this, Files.getFile("model/settings/GoalSettings.graphml"), true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("goalSettings");

        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }

	@Test(groups = { "android", "Prometheus", "settings", "Included" })
    public void sleepGoalSettingsTest() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("sleepGoalSettings", new SleepGoalSettingsAPI(this, Files.getFile("model/settings/SleepGoalSettings.graphml"), true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("sleepGoalSettings");

        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
}
