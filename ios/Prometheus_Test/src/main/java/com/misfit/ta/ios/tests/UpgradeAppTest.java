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
import com.misfit.ta.ios.modelapi.upgrade.UpgradeAfterSignoutAPI;
import com.misfit.ta.utils.Files;

public class UpgradeAppTest extends AutomationTest 
{

    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "Upgrade", "ProductionOnly" })
    public void UpgradeWithoutSignOutFromMVP16_1() throws InterruptedException, StopConditionException, IOException
    {
        testUpgradeWithoutSignOutFromApp("apps/mvp16.1/Prometheus.ipa");
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "DayProgress", "ProductionOnly" })
    public void UpgradeWithoutSignOutFromMVP17_1() throws InterruptedException, StopConditionException, IOException
    {
    	testUpgradeWithoutSignOutFromApp("apps/mvp17.1/Prometheus.ipa");
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "DayProgress", "ProductionOnly" })
    public void UpgradeWithoutSignOutFromMVP18() throws InterruptedException, StopConditionException, IOException
    {
    	testUpgradeWithoutSignOutFromApp("apps/mvp18/Prometheus.ipa");
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "DayProgress", "ProductionOnly" })
    public void UpgradeWithoutSignOutFromMVP18_1() throws InterruptedException, StopConditionException, IOException
    {
    	testUpgradeWithoutSignOutFromApp("apps/mvp18.1/Prometheus.ipa");
    }

    // test helpers
    public void testUpgradeWithoutSignOutFromApp(String pathToApp) 
    	throws FileNotFoundException, StopConditionException, InterruptedException {
    
    	ModelHandler model = getModelhandler();
        UpgradeAfterSignoutAPI api = new UpgradeAfterSignoutAPI(this, 
        		Files.getFile("model/upgrade/UpgradeAfterSignout.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false);
        api.pathToOldApp = pathToApp;
        
        model.add("UpgradeAfterSignout", api);
        model.execute("UpgradeAfterSignout");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
}
