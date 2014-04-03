package com.misfit.ta.backend.aut.correctness.social;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.social.SocialAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.social.SocialUserBase;

public class SocialDeleteFriendTC extends SocialTestAutomationBase {
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "DeleteFriend" })
	public void DeleteFriend_WithInvalidUid() {

		BaseResult result = SocialAPI.deleteFriend(misfitToken, "invalid_uid");
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.UserNotFoundMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.UserNotFoundCode, "Error code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "DeleteFriend" })
	public void DeleteFriend_NonFriendUser() {

		BaseResult result = SocialAPI.deleteFriend(misfitToken, tungUid);
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.NotFriendYetMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.NotFriendYetCode, "Error code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "DeleteFriend" })
	public void DeleteFriend_Yourself() {
		
		BaseResult result = SocialAPI.deleteFriend(misfitToken, misfitUid);
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.NotFriendYetMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.NotFriendYetCode, "Error code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "DeleteFriend" })
	public void DeleteFriend_NonFriendUserWithPendingRequest() {

		// misfit --> tung
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		
		// now delete friend
		BaseResult result = SocialAPI.deleteFriend(misfitToken, misfitUid);
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.NotFriendYetMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.NotFriendYetCode, "Error code");
		
		// cancel friend request
		SocialAPI.cancelFriendRequest(misfitToken, tungUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "DeleteFriend" })
	public void DeleteFriend_ValidFriendUser() {

		// misfit --> tung, tung -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		
		// delete friend
		BaseResult result = SocialAPI.deleteFriend(misfitToken, tungUid);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		// get friends of misfit
		result = SocialAPI.getFriends(misfitToken);
		SocialUserBase[] friends = SocialUserBase.usersFromResponse(result.response);
		Assert.assertEquals(friends.length, 0, "Number of friends of misfit");
		
		// get friends of tung
		result = SocialAPI.getFriends(tungToken);
		friends = SocialUserBase.usersFromResponse(result.response);
		Assert.assertEquals(friends.length, 0, "Number of friends of tung");
		
	}

}
