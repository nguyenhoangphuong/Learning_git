package com.misfit.ta.backend.aut.correctness.social;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.SocialAutomationBase;
import com.misfit.ta.backend.aut.SocialTestHelpers;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.social.SocialUserBase;

public class SocialGetFriendTC extends SocialAutomationBase {
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFriendAPI" })
	public void GetFriends_EmptyFriendList() {
		
		// get misfit's friends
		BaseResult result = SocialAPI.getFriends(misfitToken);
		SocialUserBase[] friends = SocialUserBase.usersFromResponse(result.response);

		// total number of friend is 0
		Assert.assertEquals(friends.length, 0, "Number of friends");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFriendAPI" })
	public void GetFriends_AfterAddNewFriends() {
				
		// misfit --> thy, misfit --> tung
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.sendFriendRequest(misfitToken, thyUid);
		
		// query again
		BaseResult result = SocialAPI.getFriends(misfitToken);
		SocialUserBase[] friends = SocialUserBase.usersFromResponse(result.response);
				
		// total number of friend is still 0
		Assert.assertEquals(friends.length, 0, "Number of friends");
		
		// tung -v- misfit, thy -v- misfit
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		SocialAPI.acceptFriendRequest(thyToken, misfitUid);
		
		// query again
		result = SocialAPI.getFriends(misfitToken);
		friends = SocialUserBase.usersFromResponse(result.response);
		
		// total number of friend should be 2
		Assert.assertEquals(friends.length, 2, "Number of friends");
		
		// make sure the result only contains only tung and thy
		boolean hasTung = false;
		boolean hasThy = false;
		
		for(SocialUserBase friend : friends) {
			
			if(friend.getUid().equals(tungUid))
				hasTung = true;
			
			if(friend.getUid().equals(thyUid))
				hasThy = true;
		}
		
		Assert.assertTrue(hasTung && hasThy, "Has both tung and thy in result");
		
		
		// check detail of return values
		SocialTestHelpers.printUsers(friends);
		for(SocialUserBase friend : friends) {
			if(friend.getUid().equals(tungUid)) {
				Assert.assertEquals(friend.getHandle(), "tung_social_misfit", "Handle");
				Assert.assertEquals(friend.getName(), "Tung Social", "Name");
				break;
			}
		}
		
		// delete friends
		SocialAPI.deleteFriend(misfitToken, tungUid);
		SocialAPI.deleteFriend(misfitToken, thyUid);
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFriendAPI" })
	public void GetFriends_AfterFriendsChangeProfile() {
		
		// misfit --> tung, misfit--> thy
		// tung -v- misfit, thy -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.sendFriendRequest(misfitToken, thyUid);
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		SocialAPI.acceptFriendRequest(thyToken, misfitUid);
		
		// now tung changes his name
		ProfileData tungProfile = MVPApi.getProfile(tungToken).profile;
		String tungOldName = "Tung Social";
		tungProfile.setName("Tung - " + System.nanoTime());
		MVPApi.updateProfile(tungToken, tungProfile, tungProfile.getServerId());
		
		// query again
		BaseResult result = SocialAPI.getFriends(misfitToken);
		SocialUserBase[] friends = SocialUserBase.usersFromResponse(result.response);

		// total number of friend should be 2
		Assert.assertEquals(friends.length, 2, "Number of friends");
		
		// check updated result
		SocialTestHelpers.printUsers(friends);
		for(SocialUserBase friend : friends) {
			if(friend.getUid().equals(tungUid)) {
				Assert.assertEquals(friend.getHandle(), "tung_social_misfit", "Handle");
				Assert.assertEquals(friend.getName(), tungProfile.getName(), "Name");
				break;
			}
		}
		
		// now tung change back his name (must do this if you want to run this test again)
		tungProfile.setName(tungOldName);
		MVPApi.updateProfile(tungToken, tungProfile, tungProfile.getServerId());
		
		// query again
		result = SocialAPI.getFriends(misfitToken);
		friends = SocialUserBase.usersFromResponse(result.response);

		// total number of friend should be 2
		Assert.assertEquals(friends.length, 2, "Number of friends");
		
		// check updated result
		SocialTestHelpers.printUsers(friends);
		for(SocialUserBase friend : friends) {
			if(friend.getUid().equals(tungUid)) {
				Assert.assertEquals(friend.getHandle(), "tung_social_misfit", "Handle");
				Assert.assertEquals(friend.getName(), tungOldName, "Name");
				break;
			}
		}
		
		// delete friends
		SocialAPI.deleteFriend(misfitToken, tungUid);
		SocialAPI.deleteFriend(misfitToken, thyUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFriendAPI" })
	public void GetFriends_AfterRemoveFriends() {
		
		// misfit --> tung, misfit--> thy
		// tung -v- misfit, thy -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.sendFriendRequest(misfitToken, thyUid);
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		SocialAPI.acceptFriendRequest(thyToken, misfitUid);

		// now misfit delete tung
		SocialAPI.deleteFriend(misfitToken, tungUid);

		// query again
		BaseResult result = SocialAPI.getFriends(misfitToken);
		SocialUserBase[] friends = SocialUserBase.usersFromResponse(result.response);

		// total number of friend should be 1
		Assert.assertEquals(friends.length, 1, "Number of friends");

		// make sure the result only contains only thy
		Assert.assertEquals(friends[0].getUid(), thyUid);

		// delete friends
		SocialAPI.deleteFriend(misfitToken, thyUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFriendAPI" })
	public void GetFriends_AfterBeingRemovedByFriends() {
		
		// misfit --> tung, misfit--> thy
		// tung -v- misfit, thy -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.sendFriendRequest(misfitToken, thyUid);
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		SocialAPI.acceptFriendRequest(thyToken, misfitUid);

		// now thy delete misfit
		SocialAPI.deleteFriend(thyToken, misfitUid);

		// query again
		BaseResult result = SocialAPI.getFriends(misfitToken);
		SocialUserBase[] friends = SocialUserBase.usersFromResponse(result.response);

		// total number of friend should be 1
		Assert.assertEquals(friends.length, 1, "Number of friends");
		
		// make sure the result only contains only tung
		Assert.assertEquals(friends[0].getUid(), tungUid);
				
		// delete friends
		SocialAPI.deleteFriend(misfitToken, tungUid);
	}

}
