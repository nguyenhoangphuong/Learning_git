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
import com.misfit.ta.ios.modelapi.tiles.LifetimeDistanceAchievementAPI;
import com.misfit.ta.ios.modelapi.tiles.LifetimeDistanceAchievementInSIMetricAPI;
import com.misfit.ta.ios.modelapi.tiles.LifetimeDistanceAchievementInUSMetricAPI;
import com.misfit.ta.ios.modelapi.tiles.PersonalBestMilestoneAPI;
import com.misfit.ta.ios.modelapi.tiles.StreakMilestoneAPI;
import com.misfit.ta.utils.Files;

public class TilesTest extends AutomationTest {

	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Tile", "Milestone", "PersonalBestTile" })
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
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Tile", "Milestone", "DailyGoalTile" })
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
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Tile", "Milestone", "StreakTile" })
    public void StreakMilestone() throws InterruptedException, StopConditionException, IOException 
    {    	
        ModelHandler model = getModelhandler();
        model.add("StreakMilestone", new StreakMilestoneAPI(this, 
        		Files.getFile("model/tiles/StreakMilestone.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), true));
        model.execute("StreakMilestone");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Tile", "Milestone", "LifetimeDistanceTile" })
    public void LifetimeDistanceAchievement() throws InterruptedException, StopConditionException, IOException 
    {    	
        ModelHandler model = getModelhandler();
        model.add("LifetimeDistanceAchievement", new LifetimeDistanceAchievementAPI(this, 
        		Files.getFile("model/tiles/LifetimeDistanceAchievement.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), true));
        model.execute("LifetimeDistanceAchievement");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Tile", "Milestone", "LifetimeDistanceTile" })
	public void LifetimeDistanceAchievementInUSMetric() throws InterruptedException, StopConditionException, IOException 
	{    	
		ModelHandler model = getModelhandler();
		model.add("LifetimeDistanceAchievementInUSMetric", new LifetimeDistanceAchievementInUSMetricAPI(this, 
				Files.getFile("model/tiles/LifetimeDistanceAchievementInUSMetrics.graphml"),
				true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), true));
		model.execute("LifetimeDistanceAchievementInUSMetric");
		Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
		String actualResult = getModelhandler().getStatistics();
		System.out.println(actualResult);
	}
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Tile", "Milestone", "LifetimeDistanceTile" })
	public void LifetimeDistanceAchievementInSIMetric() throws InterruptedException, StopConditionException, IOException 
	{    	
		ModelHandler model = getModelhandler();
		model.add("LifetimeDistanceAchievementInSIMetric", new LifetimeDistanceAchievementInSIMetricAPI(this, 
				Files.getFile("model/tiles/LifetimeDistanceAchievementInSIMetric.graphml"),
				true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), true));
		model.execute("LifetimeDistanceAchievementInSIMetric");
		Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
		String actualResult = getModelhandler().getStatistics();
		System.out.println(actualResult);
	}
	
}
