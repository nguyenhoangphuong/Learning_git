package com.misfit.ta.ios.tests;

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

    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "DayProgress" })
    public void UpgradeFromMVP171() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        UpgradeAfterSignoutAPI api = new UpgradeAfterSignoutAPI(this, 
        		Files.getFile("model/upgrade/UpgradeAfterSignout.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false);
        api.pathToOldApp = "apps/mvp17.1/Prometheus.ipa";
        
        model.add("UpgradeAfterSignout", api);
        model.execute("UpgradeAfterSignout");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }

}
