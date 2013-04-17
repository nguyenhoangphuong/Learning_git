package com.misfit.ta.ios;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.ios.modelapi.signin.InvalidSignInAPI;
import com.misfit.ta.ios.modelapi.signup.SignUpAPI;
import com.misfit.ta.utils.Files;

public class SignInTest extends AutomationTest  {
	@Test(groups = { "iOS", "Prometheus", "signin"})
    public void InvalidSignIn() throws InterruptedException, StopConditionException, IOException 
    {
        ModelHandler model = getModelhandler();
        model.add("InvalidSignIn", new InvalidSignInAPI(this, Files.getFile("model/signin/InvalidSignIn.graphml"), 
        		false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("InvalidSignIn");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
}
