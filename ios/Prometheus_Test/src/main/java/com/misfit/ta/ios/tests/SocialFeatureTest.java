package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.ios.modelapi.social.socialprofile.CreateSocialProfileAPI;
import com.misfit.ta.ios.modelapi.social.socialprofile.EditSocialProfileAPI;
import com.misfit.ta.utils.Files;

public class SocialFeatureTest extends AutomationTest 
{

	@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "SocialFeature", "CreateSocialProfile" })
    public void CreateSocialProfile() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("CreateSocialProfile", new CreateSocialProfileAPI(this, Files.getFile("model/social/socialprofile/CreateSocialProfile.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("CreateSocialProfile");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
	@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "SocialFeature", "EditSocialProfile" })
    public void EditSocialProfile() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("EditSocialProfile", new EditSocialProfileAPI(this, Files.getFile("model/social/socialprofile/EditSocialProfile.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("EditSocialProfile");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }

}
