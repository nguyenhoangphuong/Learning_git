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
    
    @Test(groups = { "android", "Prometheus", "signup", "step1" })
    public void SignUpStep1() throws InterruptedException, StopConditionException, IOException 
    {
        ModelHandler model = getModelhandler();
        model.add("SignUpStep1", new SignUpStep1API(this, Files.getFile("model/signup/SignUpStep1.graphml"), 
        		false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("SignUpStep1");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "android", "Prometheus", "signup", "step2" })
    public void SignUpStep2() throws InterruptedException, StopConditionException, IOException 
    {
        ModelHandler model = getModelhandler();
        model.add("SignUpStep2", new SignUpStep2API(this, Files.getFile("model/signup/SignUpStep2.graphml"), 
        		false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("SignUpStep2");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    

}