package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.ios.modelapi.launch.LaunchAppAPI;
import com.misfit.ta.utils.Files;

public class LaunchTest extends AutomationTest {
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Launch" })
	public void LaunchApp() throws InterruptedException,
			StopConditionException, IOException {
		ModelHandler model = getModelhandler();
		model.add(
				"LaunchApp",
				new LaunchAppAPI(this, Files
						.getFile("model/launch/LaunchApp.graphml"),
						false, new NonOptimizedShortestPath(new EdgeCoverage(
								1.0)), false));
		model.execute("LaunchApp");
		Assert.assertTrue(getModelhandler().isAllModelsDone(),
				"Not all models are done");
		String actualResult = getModelhandler().getStatistics();
		System.out.println(actualResult);
	}
}
