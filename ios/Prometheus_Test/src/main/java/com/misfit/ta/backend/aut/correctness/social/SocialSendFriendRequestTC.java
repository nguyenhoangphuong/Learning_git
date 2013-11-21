package com.misfit.ta.backend.aut.correctness.social;


import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.SocialAutomationBase;
import com.misfit.ta.backend.aut.SocialTestHelpers;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.social.SocialUserWithStatus;

public class SocialSendFriendRequestTC extends SocialAutomationBase {
	
	// fields
	protected String nonSocialEmail = "qa001@a.a";
	protected String nonSocialUserUid = "51b1e051513810703f000054";
	
	
	// test methods	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequest_WithInvalidUid() {

		BaseResult result = SocialAPI.sendFriendRequest(misfitToken, "invalid_uid");
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.UserNotFoundMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.UserNotFoundCode, "Error code");
		
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
		SocialTestHelpers.printUsers(friends);
		
		Assert.assertEquals(friends.length, 0, "Number of friend requests");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequest_ToUserWhoDontUseSocial() {
		
		BaseResult result = SocialAPI.sendFriendRequest(misfitToken, nonSocialUserUid);
		
		// TODO: remove these 2 lines when api is fixed
		SocialAPI.acceptFriendRequest(MVPApi.signIn(nonSocialEmail, "qwerty1").token, misfitUid);
		SocialAPI.deleteFriend(misfitToken, nonSocialUserUid);
				
		Assert.assertEquals(result.statusCode, 400, "Status code");
		
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
		SocialTestHelpers.printUsers(friends);
		
		Assert.assertEquals(friends.length, 0, "Number of friend requests");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequest_ToWhoAlreadyIsYourFriend() {

		// misfit --> tung, tung -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		
		// now misfit and tung is friend, misit --> tung again
		BaseResult result = SocialAPI.sendFriendRequest(misfitToken, tungUid);
		
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.AlreadyAreFriendsMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.AlreadyAreFriendsCode, "Error code");
		
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
		
		Assert.assertEquals(friends.length, 0, "Number of friend requests from me");
		
		// delete friend
		SocialAPI.deleteFriend(misfitToken, tungUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequest_ToWhoYouRequested() {

		// misfit --> tung, 1st time
		BaseResult result = SocialAPI.sendFriendRequest(misfitToken, tungUid);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
		SocialTestHelpers.printUsers(friends);
		
		Assert.assertEquals(friends.length, 1, "Number of friend requests");
		
		// misfit --> tung, 2nd time
		result = SocialAPI.sendFriendRequest(misfitToken, tungUid);
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.AlreadyRequestedMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.AlreadyRequestedCode, "Error message");

		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		SocialTestHelpers.printUsers(friends);

		Assert.assertEquals(friends.length, 1, "Number of friend requests");
		
		// delete the request misfit --> tung
		SocialAPI.cancelFriendRequest(misfitToken, tungUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequest_ToWhoRequestedYou() {

		// tung --> misfit
		SocialAPI.sendFriendRequest(tungToken, misfitUid);

		// now misfit --> tung
		BaseResult result = SocialAPI.sendFriendRequest(misfitToken, tungUid);

		// now tung and misfit are friends, fuck
		// TODO: may change in the future, when we have cancel friend requests
		Assert.assertEquals(result.statusCode, 200, "Status code");

		// check friend request list from tung
		result = SocialAPI.getFriendRequestsFromMe(tungToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 0, "Number of friend requests from tung");
		
		// check friend request list to misfit
		result = SocialAPI.getFriendRequestsToMe(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 0, "Number of friend requests to misfit");
		
		// check friend list
		result = SocialAPI.getFriends(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of friends of misfit");
		
		// delete friend
		SocialAPI.deleteFriend(misfitToken, tungUid);
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequest_ToYourself() {

		// misfit --> misfit
		BaseResult result = SocialAPI.sendFriendRequest(misfitToken, misfitUid);
		
		// TODO: api sucks, work around
		SocialAPI.cancelFriendRequest(misfitToken, misfitUid);
		SocialAPI.cancelFriendRequest(misfitToken, misfitUid);
		
		Assert.assertEquals(result.statusCode, 400, "Status code");

		// check friend request list
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 0, "Number of friend requests from me");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequest_ToValidNonFriendUser() {

		// misfit --> tung
		BaseResult result = SocialAPI.sendFriendRequest(misfitToken, tungUid);
		Assert.assertEquals(result.statusCode, 200, "Status code");

		// check friend request from misfit
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of friend requests from misfit");
		
		// check friend request to tung
		result = SocialAPI.getFriendRequestsToMe(tungToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of friend requests to tung");
		
		// cancel friend request misfit --> tung
		SocialAPI.cancelFriendRequest(misfitToken, tungUid);
	}

}