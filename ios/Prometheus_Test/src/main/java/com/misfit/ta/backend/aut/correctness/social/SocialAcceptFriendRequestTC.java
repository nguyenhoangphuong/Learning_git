package com.misfit.ta.backend.aut.correctness.social;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.social.SocialAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.social.SocialUserBase;

public class SocialAcceptFriendRequestTC extends SocialTestAutomationBase {
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "AcceptFriend" })
	public void AcceptFriend_WithInvalidUid() {
		
		BaseResult result = SocialAPI.acceptFriendRequest(misfitToken, "invalid_uid");
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.UserNotFoundMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.UserNotFoundCode, "Error code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "AcceptFriend" })
	public void AcceptFriend_NonExistRequest() {

		BaseResult result = SocialAPI.acceptFriendRequest(misfitToken, tungUid);
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.FriendRequestNotExistMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.FriendRequestNotExistCode, "Error code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "AcceptFriend" })
	public void AcceptFriend_WhoYouIgnored() {

		// delete friends
		SocialAPI.deleteFriend(misfitToken, tungUid);
				
		// misfit --> tung, tung -x- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.ignoreFriendRequest(tungToken, misfitUid);
		
		// now tung changes his mind and accept misfit
		BaseResult result = SocialAPI.acceptFriendRequest(tungToken, misfitUid);		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		// tung and misfit should be friends now
		result = SocialAPI.getFriends(misfitToken);
		SocialUserBase[] friends = SocialUserBase.usersFromResponse(result.response);
		
		Assert.assertEquals(friends.length, 1, "Number of friends of misfit");
		Assert.assertEquals(friends[0].getUid(), tungUid, "Uid of tung");
		
		result = SocialAPI.getFriends(tungToken);
		friends = SocialUserBase.usersFromResponse(result.response);
		
		Assert.assertEquals(friends.length, 1, "Number of friends of tung");
		Assert.assertEquals(friends[0].getUid(), misfitUid, "Uid of misfit");

		// delete friends
		SocialAPI.deleteFriend(misfitToken, tungUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "AcceptFriend" })
	public void AcceptFriend_WhoAlreadyIsYourFriend() {

		// delete friends
		SocialAPI.deleteFriend(misfitToken, tungUid);
				
		// misfit --> tung, tung -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);

		// now tung accept again
		BaseResult result = SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		Assert.assertEquals(result.statusCode, 400, "Status code");
		
		// TODO: return message is correct.. but not very clear, may ask backend team for an improvement
		Assert.assertEquals(result.errorMessage, DefaultValues.FriendRequestNotExistMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.FriendRequestNotExistCode, "Error code");
		
		// delete friend
		SocialAPI.deleteFriend(misfitToken, tungUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "AcceptFriend" })
	public void AcceptFriend_WithValidUid() {

		// delete friend (set up)
		SocialAPI.deleteFriend(misfitToken, tungUid);
				
		// misfit --> tung, tung -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		BaseResult result = SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		// check friendship
		result = SocialAPI.getFriends(misfitToken);
		SocialUserBase[] friends = SocialUserBase.usersFromResponse(result.response);
		Assert.assertEquals(friends.length, 1, "Number of friends");
		Assert.assertEquals(friends[0].getUid(), tungUid, "Tung's uid");
		
		// delete friend
		SocialAPI.deleteFriend(misfitToken, tungUid);
	}

}
