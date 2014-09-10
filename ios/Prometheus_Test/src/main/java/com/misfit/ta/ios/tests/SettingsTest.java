package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.ios.modelapi.settings.DeviceSettingsAPI;
import com.misfit.ta.ios.modelapi.settings.GoalSettingsAPI;
import com.misfit.ta.ios.modelapi.settings.ProfileSettingsAPI;
import com.misfit.ta.ios.modelapi.settings.UnitSettingsAPI;
import com.misfit.ta.ios.modelapi.settings.WearingShineAPI;
import com.misfit.ta.utils.Files;

public class SettingsTest extends AutomationTest {

    @Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Settings", "ProfileSettings" })
    public void ProfileSettings() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("ProfileSettings", new ProfileSettingsAPI(this, Files.getFile("model/settings/ProfileSettings.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("ProfileSettings");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Settings", "GoalSettings" })
    public void GoalSettings_Activity() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("GoalSettingsActivity", new GoalSettingsAPI(this, Files.getFile("model/settings/GoalSettingsActivity.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("GoalSettingsActivity");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Settings", "GoalSettings" })
    public void GoalSettings_Sleep() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("GoalSettingsSleep", new GoalSettingsAPI(this, Files.getFile("model/settings/GoalSettingsSleep.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("GoalSettingsSleep");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Settings", "GoalSettings" })
    public void GoalSettings_Weight() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("GoalSettingsWeight", new GoalSettingsAPI(this, Files.getFile("model/settings/GoalSettingsWeight.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("GoalSettingsWeight");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Settings", "UnitSettings" })
    public void UnitSettings() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("UnitSettings", new UnitSettingsAPI(this, Files.getFile("model/settings/UnitSettings.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("UnitSettings");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Settings", "WearingShine" })
    public void WearingShine() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("WearingShine", new WearingShineAPI(this, Files.getFile("model/settings/WearingShine.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("WearingShine");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Settings", "DevicesSettings" })
    public void DevicesSettings() throws InterruptedException, StopConditionException, IOException {
    	ModelHandler model = getModelhandler();
    	model.add("DevicesSettings", new DeviceSettingsAPI(this, Files.getFile("model/settings/DevicesSettings.graphml"),
    			true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
    	model.execute("DevicesSettings");
    	Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
    	String actualResult = getModelhandler().getStatistics();
    	System.out.println(actualResult);
    }

}
