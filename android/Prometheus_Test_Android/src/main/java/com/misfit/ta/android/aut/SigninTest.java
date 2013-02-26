package com.misfit.ta.android.aut;

import java.io.IOException;

import org.graphwalker.conditions.ReachedVertex;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.A_StarPathGenerator;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.modelapi.SignInAPI;
import com.misfit.ta.utils.Files;

public class SigninTest extends AutomationTest {

    @Test(groups = { "android", "Prometheus", "signin" })
    public void signInTest() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("signin", new SignInAPI(this, Files.getFile("model/signin/SignIn.graphml"), false, new A_StarPathGenerator(new ReachedVertex("v_InitialView")), false));
        model.execute("Login");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }

}