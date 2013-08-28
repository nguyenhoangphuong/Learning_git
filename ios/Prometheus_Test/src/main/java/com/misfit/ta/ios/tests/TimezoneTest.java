package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.ios.modelapi.timezone.ChangeTimezoneBackwardDifferentDaysAPI;
import com.misfit.ta.ios.modelapi.timezone.ChangeTimezoneBackwardSameDayAPI;
import com.misfit.ta.ios.modelapi.timezone.ChangeTimezoneForwardSameDayAPI;
import com.misfit.ta.utils.Files;

public class TimezoneTest extends AutomationTest {
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "TimeZone" })
	public void ChangeTimezoneBackwardSameday() throws InterruptedException,
			StopConditionException, IOException {
		ModelHandler model = getModelhandler();
		model.add(
				"ChangeTimezoneBackwardSameday",
				new ChangeTimezoneBackwardSameDayAPI(this, Files
						.getFile("model/timezone/ChangeTimezone.graphml"), false,
						new NonOptimizedShortestPath(new EdgeCoverage(1.0)),
						false));
		model.execute("ChangeTimezoneBackwardSameday");
		Assert.assertTrue(getModelhandler().isAllModelsDone(),
				"Not all models are done");
		String actualResult = getModelhandler().getStatistics();
		System.out.println(actualResult);
	}
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "TimeZone" })
	public void ChangeTimezoneForwardSameday() throws InterruptedException,
			StopConditionException, IOException {
		ModelHandler model = getModelhandler();
		model.add(
				"ChangeTimezoneForwardSameday",
				new ChangeTimezoneForwardSameDayAPI(this, Files
						.getFile("model/timezone/ChangeTimezone.graphml"), false,
						new NonOptimizedShortestPath(new EdgeCoverage(1.0)),
						false));
		model.execute("ChangeTimezoneForwardSameday");
		Assert.assertTrue(getModelhandler().isAllModelsDone(),
				"Not all models are done");
		String actualResult = getModelhandler().getStatistics();
		System.out.println(actualResult);
	}
	@Test(groups = { "iOS", "Prometheus", "iOSAutomation", "TimeZone" })
	public void ChangeTimezoneBackwardDifferentDays() throws InterruptedException,
			StopConditionException, IOException {
		ModelHandler model = getModelhandler();
		model.add(
				"ChangeTimezoneBackwardDifferentDays",
				new ChangeTimezoneBackwardDifferentDaysAPI(this, Files
						.getFile("model/timezone/ChangeTimezoneDifferentDays.graphml"), false,
						new NonOptimizedShortestPath(new EdgeCoverage(1.0)),
						false));
		model.execute("ChangeTimezoneBackwardDifferentDays");
		Assert.assertTrue(getModelhandler().isAllModelsDone(),
				"Not all models are done");
		String actualResult = getModelhandler().getStatistics();
		System.out.println(actualResult);
	}
}
