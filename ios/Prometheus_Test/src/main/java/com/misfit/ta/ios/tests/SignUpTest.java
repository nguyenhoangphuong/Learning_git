package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.*;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.ios.modelapi.signup.InvalidSignUpAPI;
import com.misfit.ta.ios.modelapi.signup.SignUpAPI;
import com.misfit.ta.ios.modelapi.signup.SignUpGoalAPI;
import com.misfit.ta.utils.Files;

public class SignUpTest extends AutomationTest {
    
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "SignUp", "SignUpFlow" })
    public void SignUp() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("SignUp", new SignUpAPI(this, Files.getFile("model/signup/SignUp.graphml"), false,
                new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("SignUp");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }

    @Test(groups = { "iOS", "Prometheus", "iOSAutomation", "SignUp", "SignUpInvalid" })
    public void InvalidSignUp() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("InvalidSignUp", new InvalidSignUpAPI(this, Files
                .getFile("model/signup/InvalidSignUp.graphml"), false, new NonOptimizedShortestPath(
                new EdgeCoverage(1.0)), false));
        model.execute("InvalidSignUp");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "iOS", "Prometheus", "iOSAutomation", "SignUp", "SignUpGoal" })
    public void SignUpGoal() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("SignUpGoal", new SignUpGoalAPI(this, Files
                .getFile("model/signup/SignUpGoal.graphml"), false, new NonOptimizedShortestPath(
                new EdgeCoverage(1.0)), false));
        model.execute("SignUpGoal");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
}
