package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.ios.modelapi.sync.LinkNoShineAvailableAPI;
import com.misfit.ta.ios.modelapi.sync.LinkOneShineAvailableAPI;
import com.misfit.ta.utils.Files;

public class SyncTest extends AutomationTest {
	
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Syncing", "Linking", "Excluded" })
	public void LinkNoShineAvailable() throws InterruptedException, StopConditionException, IOException {
		ModelHandler model = getModelhandler();
		model.add("LinkNoShineAvailable", new LinkNoShineAvailableAPI(this, Files.getFile("model/sync/LinkNoShineAvailable.graphml"), false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
		model.execute("LinkNoShineAvailable");
		Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
		String actualResult = getModelhandler().getStatistics();
		System.out.println(actualResult);
	}

	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "Syncing", "Linking", "Excluded" })
	public void LinkOneShineAvailable() throws InterruptedException, StopConditionException, IOException {
		ModelHandler model = getModelhandler();
		model.add("LinkOneShineAvailable", new LinkOneShineAvailableAPI(this, Files.getFile("model/sync/LinkOneShineAvaiable.graphml"), false, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
		model.execute("LinkOneShineAvailable");
		Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
		String actualResult = getModelhandler().getStatistics();
		System.out.println(actualResult);
	}

}
