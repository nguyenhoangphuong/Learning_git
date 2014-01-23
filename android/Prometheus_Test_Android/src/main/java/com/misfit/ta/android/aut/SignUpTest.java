package com.misfit.ta.android.aut;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.modelapi.*;
import com.misfit.ta.utils.Files;

public class SignUpTest extends AutomationTest {
    
    @Test(groups = { "Android", "Prometheus", "signup" })
    public void SignUp() throws InterruptedException, StopConditionException, IOException 
    {
        ModelHandler model = getModelhandler();
        model.add("SignUp", new SignUpAPI(this, Files.getFile("model/signup/SignUp.graphml"), 
        		true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("SignUp");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
}