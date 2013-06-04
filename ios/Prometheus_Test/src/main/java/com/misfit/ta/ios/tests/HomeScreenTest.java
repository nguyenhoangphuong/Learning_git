package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.*;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.ios.modelapi.homescreen.DayInPastAPI;
import com.misfit.ta.ios.modelapi.homescreen.DayProgressAPI;
import com.misfit.ta.ios.modelapi.signin.SignInWithFacebookAPI;
import com.misfit.ta.utils.Files;

public class HomeScreenTest extends AutomationTest 
{

    @Test(groups = { "iOS", "Prometheus", "HomeScreen", "DayProgress" })
    public void TodayProgress() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("DayProgress", new DayProgressAPI(this, Files.getFile("model/homescreen/DayProgress.graphml"),
                false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("DayProgress");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    //@Test(groups = { "iOS", "Prometheus", "HomeScreen", "DayInPast" })
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


}
