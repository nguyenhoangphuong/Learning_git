package com.misfit.ta.android.aut;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.modelapi.homescreen.DayProgressAPI;
import com.misfit.ta.android.modelapi.signin.SuccessfulSignInAPI;
import com.misfit.ta.utils.Files;

public class HomeScreenTest extends AutomationTest {
	@Test(groups = { "android", "Prometheus", "homescreen" })
    public void dayProgressTest() throws InterruptedException, StopConditionException, IOException {
		ModelHandler model = getModelhandler();
       
        model.add("successfulSignin", new SuccessfulSignInAPI(this, Files.getFile("model/signin/SuccessfulSignIn.graphml"), false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.add("dayProgress", new DayProgressAPI(this, Files.getFile("model/homescreen/DayProgress.graphml"), false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("successfulSignin");

        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
}
