package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.ios.modelapi.social.averageuser.AverageUserAutoFriendAPI;
import com.misfit.ta.ios.modelapi.social.averageuser.AverageUserChangeGenderAPI;
import com.misfit.ta.ios.modelapi.social.averageuser.AverageUserDeleteAndAddAgainAPI;
import com.misfit.ta.ios.modelapi.social.board.LeaderboardAPI;
import com.misfit.ta.ios.modelapi.social.board.WorldFeedAPI;
import com.misfit.ta.ios.modelapi.social.friend.AddFriendsAPI;
import com.misfit.ta.ios.modelapi.social.friend.DeleteFriendsAPI;
import com.misfit.ta.ios.modelapi.social.friend.IgnoreFriendsAPI;
import com.misfit.ta.ios.modelapi.social.friend.SearchFriendsAPI;
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

	
	@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "SocialFeature", "AddFriends" })
    public void AddFriends() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("AddFriends", new AddFriendsAPI(this, Files.getFile("model/social/friend/AddFriends.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("AddFriends");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
	@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "SocialFeature", "SearchFriends" })
    public void SearchFriends() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("SearchFriends", new SearchFriendsAPI(this, Files.getFile("model/social/friend/SearchFriends.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("SearchFriends");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
	@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "SocialFeature", "IgnoreFriends" })
    public void IgnoreFriends() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("IgnoreFriends", new IgnoreFriendsAPI(this, Files.getFile("model/social/friend/IgnoreFriends.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("IgnoreFriends");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
	@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "SocialFeature", "DeleteFriends" })
    public void DeleteFriends() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("DeleteFriends", new DeleteFriendsAPI(this, Files.getFile("model/social/friend/DeleteFriends.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("DeleteFriends");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }

	@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "SocialFeature", "Leaderboard" })
    public void LeaderboardTest() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("Leaderboard", new LeaderboardAPI(this, Files.getFile("model/social/board/Leaderboard.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("Leaderboard");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
	@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "SocialFeature", "WorldFeed" })
    public void WorldFeedTest() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("WorldFeed", new WorldFeedAPI(this, Files.getFile("model/social/board/WorldFeed.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("WorldFeed");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
	
	@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "SocialFeature", "AverageUserAutoFriend" })
    public void AverageUserAutoFriend() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("AverageUserAutoFriend", new AverageUserAutoFriendAPI(this, 
        		Files.getFile("model/social/averageuser/AverageUserAutoFriend.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("AverageUserAutoFriend");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
	@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "SocialFeature", "AverageUserChangeGender" })
    public void AverageUserChangeGender() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("AverageUserChangeGender", new AverageUserChangeGenderAPI(this, 
        		Files.getFile("model/social/averageuser/AverageUserChangeGender.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("AverageUserChangeGender");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
	@Test(groups = { "iOS", "Prometheus", "HomeScreen", "iOSAutomation", "SocialFeature", "AverageUserDeleteAndAddAgain" })
    public void AverageUserDeleteAndAddAgain() throws InterruptedException, StopConditionException, IOException
    {
        ModelHandler model = getModelhandler();
        model.add("AverageUserDeleteAndAddAgain", new AverageUserDeleteAndAddAgainAPI(this, 
        		Files.getFile("model/social/averageuser/AverageUserDeleteAndAddAgain.graphml"),
                true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("AverageUserDeleteAndAddAgain");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
	
}
