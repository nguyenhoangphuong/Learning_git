package com.misfit.ta.backend.aut.correctness.social;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.social.SocialUserWithStatus;

public class SocialGetFriendRequestTC extends SocialTestAutomationBase {
	
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFriendRequest" })
	public void GetFriendRequest_Empty() {
		
		// get requests from misfit
		BaseResult result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
		Assert.assertEquals(friends.length, 0, "Number of friend requests from misfit");
		
		// get requests to misfit
		result = SocialAPI.getFriendRequestsToMe(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		Assert.assertEquals(friends.length, 0, "Number of friend requests to misfit");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFriendRequest" })
	public void GetFriendRequest_Pending() {
		
		// misfit --> tung
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		
		// get requests from misfit
		BaseResult result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
		
		Assert.assertEquals(friends.length, 1, "Number of requests from misfit");
		Assert.assertEquals(friends[0].getName(), tungName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_REQUESTED_FROM_ME, "Status");
		Assert.assertEquals(friends[0].getUid(), tungUid, "Uid");
		
		// get requests to tung
		result = SocialAPI.getFriendRequestsToMe(tungToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		
		Assert.assertEquals(friends.length, 1, "Number of requests to tung");
		Assert.assertEquals(friends[0].getName(), misfitName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_REQUESTED_TO_ME, "Status");
		Assert.assertEquals(friends[0].getUid(), misfitUid, "Uid");
		
		// cancel the request
		SocialAPI.cancelFriendRequest(misfitToken, tungUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFriendRequest" })
	public void GetFriendRequest_Accepted() {
		
		// misfit --> tung, tung -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);

		// get requests from misfit
		BaseResult result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 0, "Number of requests from misfit");

		// get requests to tung
		result = SocialAPI.getFriendRequestsToMe(tungToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 0, "Number of requests to tung");

		// cancel friend
		SocialAPI.deleteFriend(misfitToken, tungUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFriendRequest" })
	public void GetFriendRequest_Ignored() {
		
		// misfit --> tung, tung -x- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.ignoreFriendRequest(tungToken, misfitUid);

		// get requests from misfit
		BaseResult result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of requests from misfit");
		Assert.assertEquals(friends[0].getName(), tungName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_REQUESTED_FROM_ME, "Status");
		Assert.assertEquals(friends[0].getUid(), tungUid, "Uid");

		// get requests to tung
		result = SocialAPI.getFriendRequestsToMe(tungToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 0, "Number of requests to tung");

		// cancel the request
		SocialAPI.cancelFriendRequest(misfitToken, tungUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFriendRequest" })
	public void GetFriendRequest_ProfileChanged() {
		
		// misfit --> tung
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		
		// get requests from misfit
		BaseResult result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
		
		Assert.assertEquals(friends.length, 1, "Number of requests from misfit");
		Assert.assertEquals(friends[0].getName(), tungName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_REQUESTED_FROM_ME, "Status");
		Assert.assertEquals(friends[0].getUid(), tungUid, "Uid");
		
		// get requests to tung
		result = SocialAPI.getFriendRequestsToMe(tungToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		
		Assert.assertEquals(friends.length, 1, "Number of requests to tung");
		Assert.assertEquals(friends[0].getName(), misfitName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_REQUESTED_TO_ME, "Status");
		Assert.assertEquals(friends[0].getUid(), misfitUid, "Uid");
		
		
		
		// tung and misfit change their profile
		ProfileData misfitProfile = MVPApi.getProfile(misfitToken).profile;
		String misfitOldName = misfitName;
		misfitProfile.setName("Misfit - " + System.nanoTime());
		MVPApi.updateProfile(misfitToken, misfitProfile);
		
		ProfileData tungProfile = MVPApi.getProfile(tungToken).profile;
		String tungOldName = tungName;
		tungProfile.setName("Tung - " + System.nanoTime());
		MVPApi.updateProfile(tungToken, tungProfile);
		
		// get requests from misfit
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		Assert.assertEquals(friends[0].getName(), tungProfile.getName(), "Name");
		
		// get requests to tung
		result = SocialAPI.getFriendRequestsToMe(tungToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		Assert.assertEquals(friends[0].getName(), misfitProfile.getName(), "Name");
		
		
		
		// change profile back to original version
		misfitProfile.setName(misfitOldName);
		MVPApi.updateProfile(misfitToken, misfitProfile);
		
		tungProfile.setName(tungOldName);
		MVPApi.updateProfile(tungToken, tungProfile);
		
		// get requests from misfit
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		Assert.assertEquals(friends[0].getName(), tungOldName, "Name");
		
		// get requests to tung
		result = SocialAPI.getFriendRequestsToMe(tungToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		Assert.assertEquals(friends[0].getName(), misfitOldName, "Name");
		
		
		
		// cancel the request
		SocialAPI.cancelFriendRequest(misfitToken, tungUid);
	}
	
}
