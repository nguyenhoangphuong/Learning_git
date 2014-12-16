package com.misfit.ta.backend.aut.correctness.social;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.BackendTestEnvironment;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.social.Leaderboard;
import com.misfit.ta.backend.data.social.SocialUserBase;
import com.misfit.ta.backend.data.social.SocialUserWithStatus;
import com.misfit.ta.backend.data.statistics.StatisticsOfAverageUser;
import com.misfit.ta.utils.TextTool;

public class SocialAverageUsersTC extends BackendAutomation {
	
	private static String AverageFemaleUserUid = BackendTestEnvironment.isStaging() ? "52bc053551381073fb000006" : "52c69f2c33a23766da000001";
	private static String AverageMaleUserUid = BackendTestEnvironment.isStaging() ? "52bc053451381073fb000003" : "52c69f2c33a23766da000005";
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "AverageUsers"})
	public void AverageUsers_AutoFriendWithAverageFemaleUser() {
		
		// sign up and create profile
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		MVPApi.createProfile(token, DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null));
		
		// set female and join social
		ProfileData profile = new ProfileData();
		profile.setHandle(TextTool.getRandomString(10, 20) + System.nanoTime());
		profile.setGender(1);
		MVPApi.updateProfile(token, profile);
		
		// get friend list
		BaseResult	result = SocialAPI.getFriendsWithAverageUsers(token);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of friends of misfit");
		Assert.assertEquals(friends[0].getUid(), AverageFemaleUserUid, "Uid of average user");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "AverageUsers"})
	public void AverageUsers_AutoFriendWithAverageMaleUser() {
		
		// sign up and create profile
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		MVPApi.createProfile(token, DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null));
		
		// set female and join social
		ProfileData profile = new ProfileData();
		profile.setHandle(TextTool.getRandomString(10, 20) + System.nanoTime());
		profile.setGender(0);
		MVPApi.updateProfile(token, profile);
		
		// get friend list
		BaseResult	result = SocialAPI.getFriendsWithAverageUsers(token);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of friends of misfit");
		Assert.assertEquals(friends[0].getUid(), AverageMaleUserUid, "Uid of average user");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "AverageUsers"})
	public void AverageUsers_ReAddAverageFemaleUser() {
		
		// create account and profile
		String misfitToken = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		MVPApi.createProfile(misfitToken, DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null));

		// set female and join social
		ProfileData profile = new ProfileData();
		profile.setHandle(TextTool.getRandomString(10, 20) + System.nanoTime());
		profile.setGender(1);
		MVPApi.updateProfile(misfitToken, profile);

		// delete avgUser
		SocialAPI.deleteFriend(misfitToken, AverageFemaleUserUid);
		BaseResult result = SocialAPI.getFriendsWithAverageUsers(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 0, "Number of friends of misfit");

		// misfit --> avgUser 
		result = SocialAPI.sendFriendRequest(misfitToken, AverageFemaleUserUid);
		Assert.assertEquals(result.statusCode, 200, "Status code");

		// check friend request from misfit
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 0, "Number of friend requests from misfit");

		// check friend list if misfit
		result = SocialAPI.getFriendsWithAverageUsers(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of friends of misfit");
		Assert.assertEquals(friends[0].getUid(), AverageFemaleUserUid, "Uid of average user");

		// delete avgUser
		SocialAPI.deleteFriend(misfitToken, AverageFemaleUserUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "AverageUsers"})
	public void AverageUsers_ReAddAverageMaleUser() {

		// create account and profile
		String misfitToken = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		MVPApi.createProfile(misfitToken, DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null));

		// set female and join social
		ProfileData profile = new ProfileData();
		profile.setHandle(TextTool.getRandomString(10, 20) + System.nanoTime());
		profile.setGender(0);
		MVPApi.updateProfile(misfitToken, profile);

		// delete avgUser
		SocialAPI.deleteFriend(misfitToken, AverageMaleUserUid);
		BaseResult result = SocialAPI.getFriendsWithAverageUsers(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 0, "Number of friends of misfit");

		// misfit --> avgUser 
		result = SocialAPI.sendFriendRequest(misfitToken, AverageMaleUserUid);
		Assert.assertEquals(result.statusCode, 200, "Status code");

		// check friend request from misfit
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 0, "Number of friend requests from misfit");

		// check friend list if misfit
		result = SocialAPI.getFriendsWithAverageUsers(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of friends of misfit");
		Assert.assertEquals(friends[0].getUid(), AverageMaleUserUid, "Uid of average user");

		// delete avgUser
		SocialAPI.deleteFriend(misfitToken, AverageMaleUserUid);
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "AverageUsers"})
	public void AverageUsers_GetProfileOfAverageUsers() {

		// create account
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;

		// get profile of male user
		BaseResult result = MVPApi.getProfileOfUserId(token, AverageMaleUserUid);
		StatisticsOfAverageUser statistics = StatisticsOfAverageUser.fromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertNotNull(statistics.getTodayAverage(), "Today average is not null");
		Assert.assertNotNull(statistics.getTodayRecord(), "Today record is not null");
		Assert.assertNotNull(statistics.getYesterdayAverage(), "Yesterday average is not null");
		Assert.assertNotNull(statistics.getYesterdayRecord(), "Yesterday record is not null");
		Assert.assertNotNull(statistics.getWeekRecord(), "Week record is not null");
		
		// get profile of female user
		result = MVPApi.getProfileOfUserId(token, AverageFemaleUserUid);
		statistics = StatisticsOfAverageUser.fromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertNotNull(statistics.getTodayAverage(), "Today average is not null");
		Assert.assertNotNull(statistics.getTodayRecord(), "Today record is not null");
		Assert.assertNotNull(statistics.getYesterdayAverage(), "Yesterday average is not null");
		Assert.assertNotNull(statistics.getYesterdayRecord(), "Yesterday record is not null");
		Assert.assertNotNull(statistics.getWeekRecord(), "Week record is not null");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "AverageUsers"})
	public void AverageUsers_GetLeaderboardWithAverageUser() {
		
		// sign up and create profile
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		MVPApi.createProfile(token, DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null));
		
		// set female and join social
		ProfileData profile = new ProfileData();
		profile.setHandle(TextTool.getRandomString(10, 20) + System.nanoTime());
		profile.setGender(1);
		MVPApi.updateProfile(token, profile);
		
		// get leaderboard
		BaseResult	result = SocialAPI.getLeaderboardInfoWithAverageUsers(token);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		Leaderboard leaderboard = Leaderboard.fromResponse(result.response);
		
		// check today
		boolean pass = false;
		for(SocialUserBase user : leaderboard.getToday()) {
			if(user.getUid().equals(AverageFemaleUserUid)) {
				pass = true;
				break;
			}
		}

		Assert.assertTrue(pass, "Average user is in today leaderboard");

		// check yesterday
		pass = false;
		for(SocialUserBase user : leaderboard.getYesterday()) {
			if(user.getUid().equals(AverageFemaleUserUid)) {
				pass = true;
				break;
			}
		}

		Assert.assertTrue(pass, "Average user is in yesterday leaderboard");
	}

}
