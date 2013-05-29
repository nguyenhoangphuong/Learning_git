package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.ios.modelapi.settings.GoalSettingsAPI;
import com.misfit.ta.ios.modelapi.settings.ProfileSettingsAPI;
import com.misfit.ta.ios.modelapi.settings.WearingShineAPI;
import com.misfit.ta.utils.Files;

public class SettingsTest extends AutomationTest {

    @Test(groups = { "iOS", "Prometheus", "Settings", "ProfileSettings" })
    public void ProfileSettings() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("ProfileSettings", new ProfileSettingsAPI(this, Files.getFile("model/settings/ProfileSettings.graphml"),
                false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("ProfileSettings");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }

    @Test(groups = { "iOS", "Prometheus", "Settings", "GoalSettings" })
    public void GoalSettings() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("GoalSettings", new GoalSettingsAPI(this, Files.getFile("model/settings/GoalSettings.graphml"),
                false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("GoalSettings");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "iOS", "Prometheus", "Settings", "WearingShine" })
    public void WearingShine() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("WearingShine", new WearingShineAPI(this, Files.getFile("model/settings/WearingShine.graphml"),
                false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("WearingShine");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
}
