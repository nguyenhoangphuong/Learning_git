package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.ios.modelapi.signin.ForgotPasswordAPI;
import com.misfit.ta.ios.modelapi.signin.InvalidSignInAPI;
import com.misfit.ta.ios.modelapi.signin.SuccessfulSignInAPI;
import com.misfit.ta.utils.Files;

public class SignInTest extends AutomationTest {

    @Test(groups = { "iOS", "Prometheus", "iOSAutomation", "SignIn", "InvalidSignIn" })
    public void InvalidSignIn() throws InterruptedException, StopConditionException, IOException {
    	Assert.assertTrue(false, "Debug");
        ModelHandler model = getModelhandler();
        model.add("InvalidSignIn", new InvalidSignInAPI(this, Files.getFile("model/signin/InvalidSignIn.graphml"),
                false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.add("ForgotPassword", new ForgotPasswordAPI(this, Files.getFile("model/signin/ForgotPassword.graphml"),
                false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("InvalidSignIn");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "SignIn", "SuccessfulSignIn"})
    public void SuccessfulSignIn() throws InterruptedException, StopConditionException, IOException 
    {
        ModelHandler model = getModelhandler();
        model.add("SuccessfulSignIn", new SuccessfulSignInAPI(this, Files
                .getFile("model/signin/SuccessfulSignIn.graphml"), false, new NonOptimizedShortestPath(
                new EdgeCoverage(1.0)), false));
        model.execute("SuccessfulSignIn");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }

}
