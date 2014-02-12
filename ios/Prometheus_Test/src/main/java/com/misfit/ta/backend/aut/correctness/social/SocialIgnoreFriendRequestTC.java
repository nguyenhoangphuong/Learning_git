package com.misfit.ta.backend.aut.correctness.social;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.social.SocialAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.social.SocialUserBase;

public class SocialIgnoreFriendRequestTC extends SocialTestAutomationBase {

	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "IgnoreFriend" })
	public void IgnoreFriend_WithInvalidUid() {

		BaseResult result = SocialAPI.ignoreFriendRequest(misfitToken, "invalid_uid");
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.UserNotFoundMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.UserNotFoundCode, "Error code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "IgnoreFriend" })
	public void IgnoreFriend_NonExistRequest() {

		BaseResult result = SocialAPI.ignoreFriendRequest(misfitToken, tungUid);
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.FriendRequestNotExistMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.FriendRequestNotExistCode, "Error code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "IgnoreFriend" })
	public void IgnoreFriend_WhoYouAlreadyIgnored() {

		// misfit --> tung, tung -x- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.ignoreFriendRequest(tungToken, misfitUid);
		
		// tung ignores again
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		BaseResult result = SocialAPI.ignoreFriendRequest(tungToken, misfitUid);

		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.FriendRequestNotExistMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.FriendRequestNotExistCode, "Error code");
		
		// delete friend
		SocialAPI.deleteFriend(misfitToken, tungUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "IgnoreFriend" })
	public void IgnoreFriend_WhoYouAlreadyIsYourFriend() {

		// misfit --> tung, tung -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);

		// tung ignores his friend misfit
		BaseResult result = SocialAPI.ignoreFriendRequest(tungToken, misfitUid);
		Assert.assertEquals(result.statusCode, 400, "Status code");
		
		// TODO: return message is correct.. but not very clear, may ask backend team for an improvement
		Assert.assertEquals(result.errorMessage, DefaultValues.FriendRequestNotExistMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.FriendRequestNotExistCode, "Error code");
		
		// delete friend
		SocialAPI.deleteFriend(misfitToken, tungUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "IgnoreFriend" })
	public void IgnoreFriend_WithValidUid() {

		// delete friend (set up)
		SocialAPI.deleteFriend(misfitToken, tungUid);
		
		// misfit --> tung, tung -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		BaseResult result = SocialAPI.ignoreFriendRequest(tungToken, misfitUid);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		// check friendship
		result = SocialAPI.getFriends(misfitToken);
		SocialUserBase[] friends = SocialUserBase.usersFromResponse(result.response);
		Assert.assertEquals(friends.length, 0, "Number of friends");
		
		// check friend request from misfit
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		friends = SocialUserBase.usersFromResponse(result.response);
		Assert.assertEquals(friends.length, 1, "Number of friends");
		
		// check friend request to tung
		result = SocialAPI.getFriendRequestsToMe(tungToken);
		friends = SocialUserBase.usersFromResponse(result.response);
		Assert.assertEquals(friends.length, 0, "Number of friends");
		
		// cancel friend request misfit --> tung
		SocialAPI.cancelFriendRequest(misfitToken, tungUid);
	}
	
}
