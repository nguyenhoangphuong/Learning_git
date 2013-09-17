package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.ios.modelapi.tiles.DailyGoalMilestonesAPI;
import com.misfit.ta.ios.modelapi.tiles.PersonalBestMilestoneAPI;
import com.misfit.ta.utils.Files;

public class TilesTest extends AutomationTest {

	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Tile", "Milestone" })
    public void PersonalBestMilestone() throws InterruptedException, StopConditionException, IOException 
    {    	
        ModelHandler model = getModelhandler();
        model.add("PersonalBestMilestone", new PersonalBestMilestoneAPI(this, 
        		Files.getFile("model/tiles/PersonalBestMilestone.graphml"),
                false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("PersonalBestMilestone");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Tile", "Milestone" })
    public void DailyGoalMilestones() throws InterruptedException, StopConditionException, IOException 
    {    	
        ModelHandler model = getModelhandler();
        model.add("DailyGoalMilestones", new DailyGoalMilestonesAPI(this, 
        		Files.getFile("model/tiles/DailyGoalMilestone.graphml"),
                false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("DailyGoalMilestones");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
}
