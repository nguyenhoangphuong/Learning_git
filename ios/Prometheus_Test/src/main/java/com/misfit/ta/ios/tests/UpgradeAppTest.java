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
import com.misfit.ta.report.TRS;
import com.misfit.ta.utils.Files;

public class UpgradeAppTest extends AutomationTest 
{

    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "Upgrade", "ProductionOnly" })
    public void UpgradeFromMVP16_1() throws InterruptedException, StopConditionException, IOException
    {
    	int mvp = UpgradeAppAPI.MVP_16_1;
    	TRS.instance().addStep("================== Upgrade without sign out =======================", "subtest");
    	testUpgradeFromApp("UpradeAppFromMVP16.1WithoutSignOut", mvp, false);
    	TRS.instance().addStep("=================== Upgrade after sign out ========================", "subtest");
        testUpgradeFromApp("UpradeAppFromMVP16.1AfterSignOut", mvp, true);
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "Upgrade", "ProductionOnly" })
    public void UpgradeFromMVP17_1() throws InterruptedException, StopConditionException, IOException
    {
    	int mvp = UpgradeAppAPI.MVP_17_1;
    	TRS.instance().addStep("================== Upgrade without sign out =======================", "subtest");
    	testUpgradeFromApp("UpradeAppFromMVP17.1WithoutSignOut", mvp, false);
    	TRS.instance().addStep("=================== Upgrade after sign out ========================", "subtest");
        testUpgradeFromApp("UpradeAppFromMVP17.1AfterSignOut", mvp, true);
    }
        
    //@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "Upgrade", "ProductionOnly" })
    public void UpgradeFromMVP18_1() throws InterruptedException, StopConditionException, IOException
    {
    	int mvp = UpgradeAppAPI.MVP_18_1;
    	TRS.instance().addStep("================== Upgrade without sign out =======================", "subtest");
    	testUpgradeFromApp("UpradeAppFromMVP18.1WithoutSignOut", mvp, false);
    	TRS.instance().addStep("=================== Upgrade after sign out ========================", "subtest");
        testUpgradeFromApp("UpradeAppFromMVP18.1AfterSignOut", mvp, true);
    }

    // test helpers
    public void testUpgradeFromApp(String modelName, int fromMVP, boolean willSignOutBeforeUpgrade)
    	throws FileNotFoundException, StopConditionException, InterruptedException {
    
    	ModelHandler model = getModelhandler();
    	String modelFile = willSignOutBeforeUpgrade ? "UpgradeAppAfterSignOut.graphml" :
    							"UpgradeAppWithoutSignOut.graphml";
        UpgradeAppAPI api = new UpgradeAppAPI(this, 
        		Files.getFile("model/upgrade/" + modelFile),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false);
        
        api.fromMVP = fromMVP;
        
        model.add(modelName, api);
        model.execute(modelName);
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
}
