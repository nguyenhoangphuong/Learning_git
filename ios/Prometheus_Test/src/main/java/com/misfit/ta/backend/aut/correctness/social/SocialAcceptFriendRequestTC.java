package com.misfit.ta.backend.aut.correctness.social;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.SocialAutomationBase;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.social.SocialUserBase;

public class SocialAcceptFriendRequestTC extends SocialAutomationBase {
	
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
		Assert.assertEquals(result.errorMessage, DefaultValues.FriendReqeustNotExistMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.FriendRequestNotExistCode, "Error code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "AcceptFriend" })
	public void AcceptFriend_WhoYouIgnored() {

		// misfit --> tung, tung -x- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.ignoreFriendRequest(tungToken, misfitUid);
		
		// now tung changes his mind and accept misfit
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		BaseResult result = SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.FriendReqeustNotExistMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.FriendRequestNotExistCode, "Error code");

		// cancel friend request
		SocialAPI.cancelFriendRequest(misfitToken, tungUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "AcceptFriend" })
	public void AcceptFriend_WhoYouAlreadyIsYourFriend() {

		// misfit --> tung, tung -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);

		// now tung accept again
		BaseResult result = SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		Assert.assertEquals(result.statusCode, 400, "Status code");
		
		// TODO: return message is correct.. but not very clear, may ask backend team for an improvement
		Assert.assertEquals(result.errorMessage, DefaultValues.FriendReqeustNotExistMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.FriendReqeustNotExistMessage, "Error code");
		
		// delete friend
		SocialAPI.deleteFriend(misfitToken, tungUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "AcceptFriend" })
	public void AcceptFriend_WithValidUid() {

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
