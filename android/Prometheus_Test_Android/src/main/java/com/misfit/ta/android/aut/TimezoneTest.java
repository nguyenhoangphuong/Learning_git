package com.misfit.ta.android.aut;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.modelapi.timezone.ChangeTimezoneBackwardSameDayAPI;
import com.misfit.ta.android.modelapi.timezone.ChangeTimezoneForwardSameDayAPI;
import com.misfit.ta.utils.Files;

public class TimezoneTest extends AutomationTest{

	@Test(groups = { "android", "TimeZone", "Excluded" })
	public void ChangeTimezoneBackwardSameday() throws InterruptedException, StopConditionException, IOException 
	{
		ModelHandler model = getModelhandler();
		model.add("ChangeTimezoneBackwardSameday", new ChangeTimezoneBackwardSameDayAPI(this, 
				Files.getFile("model/timezone/ChangeTimezone.graphml"), true, 
				new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
		model.execute("ChangeTimezoneBackwardSameday");
		Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
		String actualResult = getModelhandler().getStatistics();
		System.out.println(actualResult);
	}
	

	@Test(groups = { "android", "TimeZone", "Excluded" })
	public void ChangeTimezoneForwardSameday() throws InterruptedException, StopConditionException, IOException 
	{
		ModelHandler model = getModelhandler();
		model.add("ChangeTimezoneForwardSameday", new ChangeTimezoneForwardSameDayAPI(this, 
				Files.getFile("model/timezone/ChangeTimezone.graphml"), true, 
				new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
		model.execute("ChangeTimezoneForwardSameday");
		Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
		String actualResult = getModelhandler().getStatistics();
		System.out.println(actualResult);
	}
}
