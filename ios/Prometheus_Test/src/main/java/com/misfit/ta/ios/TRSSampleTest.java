package com.misfit.ta.ios;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.utils.Files;

public class TRSSampleTest extends com.misfit.ta.aut.AutomationTest
{
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "TRSTest" })
    public void TRSTestOne() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("TRSTest", new TRSTestAPI(this, Files.getFile("model/TRSTest.graphml"),
                false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("TRSTest");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }

}
