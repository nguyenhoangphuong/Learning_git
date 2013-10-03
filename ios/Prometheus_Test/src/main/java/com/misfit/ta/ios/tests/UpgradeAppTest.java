package com.misfit.ta.ios.tests;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.*;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.aut.AutomationTest;
import com.misfit.ta.ios.modelapi.upgrade.UpgradeAppAPI;
import com.misfit.ta.utils.Files;

public class UpgradeAppTest extends AutomationTest 
{

    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "Upgrade", "ProductionOnly" })
    public void UpgradeFromMVP16_1() throws InterruptedException, StopConditionException, IOException
    {
        testUpgradeFromApp("apps/mvp16.1/Prometheus.ipa", true);
        testUpgradeFromApp("apps/mvp16.1/Prometheus.ipa", false);
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "DayProgress", "ProductionOnly" })
    public void UpgradeFromMVP17_1() throws InterruptedException, StopConditionException, IOException
    {
    	testUpgradeFromApp("apps/mvp17.1/Prometheus.ipa", true);
    	testUpgradeFromApp("apps/mvp17.1/Prometheus.ipa", false);
    }
        
    //@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "DayProgress", "ProductionOnly" })
    public void UpgradeFromMVP18_1() throws InterruptedException, StopConditionException, IOException
    {
    	testUpgradeFromApp("apps/mvp18.1/Prometheus.ipa", true);
    	testUpgradeFromApp("apps/mvp18.1/Prometheus.ipa", false);
    }

    // test helpers
    public void testUpgradeFromApp(String pathToApp, boolean willSignOutBeforeUpgrade)
    	throws FileNotFoundException, StopConditionException, InterruptedException {
    
    	ModelHandler model = getModelhandler();
        UpgradeAppAPI api = new UpgradeAppAPI(this, 
        		Files.getFile("model/upgrade/UpgradeApp.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false);
        
        api.pathToOldApp = pathToApp;
        api.willSignOutBeforeUpgrade = willSignOutBeforeUpgrade;
        
        model.add("UpgradeAfterSignout", api);
        model.execute("UpgradeAfterSignout");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
}
