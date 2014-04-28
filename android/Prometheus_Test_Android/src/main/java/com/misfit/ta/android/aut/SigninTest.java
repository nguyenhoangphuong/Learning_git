package com.misfit.ta.android.aut;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.modelapi.signin.InvalidSignInAPI;
import com.misfit.ta.android.modelapi.signin.SuccessfulSignInAPI;
import com.misfit.ta.utils.Files;

public class SigninTest extends AutomationTest {

	@Test(groups = { "android", "Prometheus", "signin", "invalidSignIn", "Included" })
    public void invalidSignInTest() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("invalidSignin", new InvalidSignInAPI(this, Files.getFile("model/signin/InvalidSignIn.graphml"), true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("invalidSignin");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "android", "Prometheus", "signin", "succesfulSignIn", "Included" })
    public void successfulSignInTest() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("successfulSignin", new SuccessfulSignInAPI(this, Files.getFile("model/signin/SuccessfulSignIn.graphml"), true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("successfulSignin");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    

}