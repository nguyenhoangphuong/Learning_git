package com.misfit.ta.backend.aut.correctness.social;


import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.SocialAutomationBase;
import com.misfit.ta.backend.aut.SocialTestHelpers;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.social.SocialUserWithStatus;

public class SocialSendFriendRequestTC extends SocialAutomationBase {
	
	// fields
	protected String nonSocialUserUid = "51b1e051513810703f000054"; // qa001@a.a
	
	
	// test methods	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequest_WithInvalidUid() {

		BaseResult result = SocialAPI.sendFriendRequest(misfitToken, "invalid_uid");
		Assert.assertEquals(result.statusCode, 400, "Status code");
		
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
		SocialTestHelpers.printUsers(friends);
		
		Assert.assertEquals(friends.length, 0, "Number of friend requests");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequest_ToUserWhoDontUseSocial() {

		BaseResult result = SocialAPI.sendFriendRequest(misfitToken, nonSocialUserUid);
		Assert.assertEquals(result.statusCode, 400, "Status code");
		
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
		SocialTestHelpers.printUsers(friends);
		
		Assert.assertEquals(friends.length, 0, "Number of friend requests");
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
		Assert.assertEquals(result.errorMessage, DefaultValues.FriendRequestExisted, "Error message");

		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		SocialTestHelpers.printUsers(friends);

		Assert.assertEquals(friends.length, 1, "Number of friend requests");
		
		// delete the request misfit --> tung
		SocialTestHelpers.deleteFriendRequest(tungToken, misfitUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequest_ToWhoAlreadyYourFriend() {

		// misfit --> tung, tung -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		
		// now misfit and tung is friend, misit --> tung again
		BaseResult result = SocialAPI.sendFriendRequest(misfitToken, tungUid);
		
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.AlreadyAreFriends, "Error message");
		
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
		
		Assert.assertEquals(friends.length, 0, "Number of friend requests from me");
		
		// delete friend
		SocialAPI.deleteFriend(misfitToken, tungUid);
		
		// TODO: remove the request, should not be here, but the api sucks
		SocialTestHelpers.deleteFriendRequest(tungToken, misfitUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequest_ToWhoYouIgnored() {

		// tung --> misfit, misfit -x- tung
		SocialAPI.sendFriendRequest(tungToken, misfitUid);
		SocialAPI.ignoreFriendRequest(misfitToken, tungUid);

		// now misfit --> tung
		BaseResult result = SocialAPI.sendFriendRequest(misfitToken, tungUid);

		//				Assert.assertEquals(result.statusCode, 400, "Status code");
		//				Assert.assertEquals(result.errorMessage, "", "Error message");

//		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
//		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
//
//		Assert.assertEquals(friends.length, 0, "Number of friend requests from me");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequest_ToWhoRequestedYou() {

	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequest_ToWhoIgnoredYou() {

	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequest_ToYourself() {

	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequest_ToValidNonFriendUser() {

	}

}
