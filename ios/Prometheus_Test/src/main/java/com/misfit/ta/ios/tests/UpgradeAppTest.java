package com.misfit.ta.ios.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.aut.AutomationTest;
import com.misfit.ta.gui.AppInstaller;
import com.misfit.ta.ios.modelapi.upgrade.UpgradeAppAPI;
import com.misfit.ta.utils.Files;

public class UpgradeAppTest extends AutomationTest 
{   
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "Upgrade", "UpgradeMVP17.1", "ProductionOnly" })
    public void UpgradeFromMVP17_1WithoutSignOut() throws InterruptedException, StopConditionException, IOException
    {
    	int mvp = AppInstaller.MVP_17_1;
    	testUpgradeFromApp("UpradeAppFromMVP17.1WithoutSignOut", mvp, false);
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "Upgrade", "UpgradeMVP17.1", "ProductionOnly" })
    public void UpgradeFromMVP17_1AfterSignOut() throws InterruptedException, StopConditionException, IOException
    {
    	int mvp = AppInstaller.MVP_17_1;
        testUpgradeFromApp("UpradeAppFromMVP17.1AfterSignOut", mvp, true);
    }

    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "Upgrade", "UpgradeMVP20", "ProductionOnly" })
    public void UpgradeFromMVP20WithoutSignOut() throws InterruptedException, StopConditionException, IOException
    {
    	int mvp = AppInstaller.MVP_20;
    	testUpgradeFromApp("UpradeAppFromMVP20WithoutSignOut", mvp, false);
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "Upgrade", "UpgradeMVP20", "ProductionOnly" })
    public void UpgradeFromMVP20AfterSignOut() throws InterruptedException, StopConditionException, IOException
    {
    	int mvp = AppInstaller.MVP_20;
        testUpgradeFromApp("UpradeAppFromMVP20AfterSignOut", mvp, true);
    }

    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "Upgrade", "UpgradeMVP20", "ProductionOnly" })
    public void UpgradeFromMVP21WithoutSignOut() throws InterruptedException, StopConditionException, IOException
    {
    	int mvp = AppInstaller.MVP_21;
    	testUpgradeFromApp("UpradeAppFromMVP21WithoutSignOut", mvp, false);
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "Upgrade", "UpgradeMVP20", "ProductionOnly" })
    public void UpgradeFromMVP21AfterSignOut() throws InterruptedException, StopConditionException, IOException
    {
    	int mvp = AppInstaller.MVP_21;
        testUpgradeFromApp("UpradeAppFromMVP21AfterSignOut", mvp, true);
    }
    
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "Upgrade", "UpgradeMVP20", "ProductionOnly" })
    public void UpgradeFromMVP22WithoutSignOut() throws InterruptedException, StopConditionException, IOException
    {
    	int mvp = AppInstaller.MVP_22;
    	testUpgradeFromApp("UpradeAppFromMVP22WithoutSignOut", mvp, false);
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "Upgrade", "UpgradeMVP20", "ProductionOnly" })
    public void UpgradeFromMVP22AfterSignOut() throws InterruptedException, StopConditionException, IOException
    {
    	int mvp = AppInstaller.MVP_22;
        testUpgradeFromApp("UpradeAppFromMVP22AfterSignOut", mvp, true);
    }

   
    // test helpers
    public void testUpgradeFromApp(String modelName, int fromMVP, boolean willSignOutBeforeUpgrade)
    	throws FileNotFoundException, StopConditionException, InterruptedException {
    
    	ModelHandler model = getModelhandler();
    	String modelFile = willSignOutBeforeUpgrade ? "UpgradeAppAfterSignOut.graphml" :
    							"UpgradeAppWithoutSignOut.graphml";
    	FileUtils.deleteQuietly(new File("apps"));
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
