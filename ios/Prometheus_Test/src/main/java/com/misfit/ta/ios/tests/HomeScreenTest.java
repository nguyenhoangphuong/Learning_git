package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.ios.modelapi.homescreen.DayInPastAPI;
import com.misfit.ta.ios.modelapi.homescreen.DayProgressAPI;
import com.misfit.ta.ios.modelapi.homescreen.EditActivityFlowByChangeActivityTypeAPI;
import com.misfit.ta.ios.modelapi.homescreen.EditActivityMilestonesAPI;
import com.misfit.ta.ios.modelapi.homescreen.LogActivityAPI;
import com.misfit.ta.ios.modelapi.homescreen.SetAlarmAPI;
import com.misfit.ta.ios.modelapi.homescreen.SetWeightGoalAPI;
import com.misfit.ta.ios.modelapi.homescreen.SleepRemovingAPI;
import com.misfit.ta.ios.modelapi.homescreen.TaggingActivityAPI;
import com.misfit.ta.ios.modelapi.homescreen.TrackTaggingActivityAPI;
import com.misfit.ta.ios.modelapi.homescreen.WeekViewAPI;
import com.misfit.ta.ios.modelapi.signin.SignInWithFacebookAPI;
import com.misfit.ta.utils.Files;

public class HomeScreenTest extends AutomationTest 
{

    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "DayProgress" })
    public void TodayProgress() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("DayProgress", new DayProgressAPI(this, Files.getFile("model/homescreen/DayProgress.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("DayProgress");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "iOS", "Prometheus", "iOSAutomation", "HomeScreen", "DayInPast", "Excluded" })
    public void DayInPast() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("SignInWithFacebook", new SignInWithFacebookAPI(this, Files
                .getFile("model/signin/SignInWithFacebook.graphml"), false, new NonOptimizedShortestPath(
                new EdgeCoverage(1.0)), false));
        model.add("DayInPast", new DayInPastAPI(this, Files.getFile("model/homescreen/DayInPast.graphml"),
                false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("SignInWithFacebook");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }

    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "TaggingActivity" })
    public void TaggingActivity() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("TaggingActivity", new TaggingActivityAPI(this, Files.getFile("model/homescreen/TaggingActivity.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("TaggingActivity");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "WeekView" })
    public void WeekView() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("WeekView", new WeekViewAPI(this, Files.getFile("model/homescreen/WeekView.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("WeekView");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "EditTag" })
    public void EditActivityByChangeActivityTypeTagFlow() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("EditTagFlow", new EditActivityFlowByChangeActivityTypeAPI(this, Files.getFile("model/homescreen/EditActivityFlowByChangeActivityType.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("EditTagFlow");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "EditTag", "KnownIssue" })
    public void EditActivityTagMilestones() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("EditTagToHitMilestones", new EditActivityMilestonesAPI(this, 
        		Files.getFile("model/homescreen/EditActivityMilestones.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("EditTagToHitMilestones");
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "SleepRemoving" })
    public void SleepRemoving() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("SleepRemoving", new SleepRemovingAPI(this, Files.getFile("model/homescreen/SleepRemoving.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("SleepRemoving");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }

    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "SetWeightGoal" })
    public void SetWeightGoal() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("SetWeightGoal", new SetWeightGoalAPI(this, Files.getFile("model/homescreen/SetWeightGoal.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("SetWeightGoal");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "SetAlarm" })
    public void SetAlarm() throws InterruptedException, StopConditionException, IOException
    {
    	ModelHandler model = getModelhandler();
    	model.add("SetAlarm", new SetAlarmAPI(this, Files.getFile("model/homescreen/SetAlarm.graphml"),
    			true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
    	model.execute("SetAlarm");
    	Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
    	String actualResult = getModelhandler().getStatistics();
    	System.out.println(actualResult);
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "TrackTaggingActivity" })
    public void TrackTaggingActivity() throws InterruptedException, StopConditionException, IOException
    {
    	ModelHandler model = getModelhandler();
    	model.add("TrackTaggingActivity", new TrackTaggingActivityAPI(this, Files.getFile("model/homescreen/TrackTaggingActivity.graphml"),
    			true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
    	model.execute("TrackTaggingActivity");
    	Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
    	String actualResult = getModelhandler().getStatistics();
    	System.out.println(actualResult);
    }
    
    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "LogActivity" })
    public void LogActivity() throws InterruptedException, StopConditionException, IOException
    {
    	ModelHandler model = getModelhandler();
    	model.add("LogActivity", new LogActivityAPI(this, Files.getFile("model/homescreen/LogActivity.graphml"),
    			true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
    	model.execute("LogActivity");
    	Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
    	String actualResult = getModelhandler().getStatistics();
    	System.out.println(actualResult);
    }
}
