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
import com.misfit.ta.backend.data.social.SocialUserWithStatus;
import com.misfit.ta.report.TRS;

public class SocialGetFriendTC extends SocialAutomationBase {
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFriendAPI" })
	public void GetFriendsTest() {
		
		TRS.instance().addStep("==================== NO FRIEND ====================", "subcase");
		
		// query friends
		BaseResult result = SocialAPI.getFriends(misfitToken);
		SocialUserBase[] friends = SocialUserBase.usersFromResponse(result.response);
				
		// total number of friend is 0
		Assert.assertEquals(friends.length, 0, "Number of friends");
		
		
		
		TRS.instance().addStep("==================== ADD NEW FRIENDS ====================", "subcase");
		
		// misfit sends friend request to tung and thy
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.sendFriendRequest(misfitToken, thyUid);
		
		// query again
		result = SocialAPI.getFriends(misfitToken);
		friends = SocialUserBase.usersFromResponse(result.response);
				
		// total number of friend is still 0
		Assert.assertEquals(friends.length, 0, "Number of friends");
		
		// tung and thy accept friend requests
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
				Assert.assertEquals(friend.getHandle(), "tung.social.misfit", "Handle");
				Assert.assertEquals(friend.getName(), "Tung Social", "Name");
				break;
			}
		}
		
		
		TRS.instance().addStep("==================== FRIENDS CHANGE PROFILE ====================", "subcase");
		
		// now tung change his name
		ProfileData tungProfile = MVPApi.getProfile(tungToken).profile;
		String tungOldName = tungProfile.getName();
		tungProfile.setName("Tung - " + System.nanoTime());
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
				Assert.assertEquals(friend.getHandle(), "tung.social.misfit", "Handle");
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
				Assert.assertEquals(friend.getHandle(), "tung.social.misfit", "Handle");
				Assert.assertEquals(friend.getName(), tungOldName, "Name");
				break;
			}
		}
		
		
		TRS.instance().addStep("==================== REMOVE FRIENDS ====================", "subcase");
		
		// now misfit delete tung
		SocialAPI.deleteFriend(misfitToken, tungUid);
		
		// query again
		result = SocialAPI.getFriends(misfitToken);
		friends = SocialUserBase.usersFromResponse(result.response);
		
		// total number of friend should be 1
		Assert.assertEquals(friends.length, 1, "Number of friends");
		
		// make sure the result only contains only thy
		Assert.assertEquals(friends[0].getUid(), thyUid);
		
		
		TRS.instance().addStep("==================== BE REMOVED BY FRIENDS ====================", "subcase");
		
		// now thy delete misfit
		SocialAPI.deleteFriend(thyToken, misfitUid);
		
		// query again
		result = SocialAPI.getFriends(misfitToken);
		friends = SocialUserBase.usersFromResponse(result.response);
		
		// total number of friend should be 0
		Assert.assertEquals(friends.length, 0, "Number of friends");
		
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFacebookFriendAPI" })
	public void GetFacebookFriendsTest() {
		
		TRS.instance().addStep("==================== GET FRIENDS ====================", "subcase");
		
		// query friends
		BaseResult result = SocialAPI.getFacebookFriends(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
		SocialTestHelpers.printUsers(friends);

		// make sure the result only contains only users who use Shine app
		for(SocialUserBase friend : friends) {
			for(String key : mapNames) {
			
				String uid = (String) mapNameData.get(key).get("fuid");
				boolean isUsingApp = (Boolean) mapNameData.get(key).get("isUsingApp");
				
				if(friend.getUid().equals(uid)) {
					Assert.assertTrue(isUsingApp, "User '" + key + "' is using app");
					break;
				}
				
			}
		}

		// check detail of return values
		for(SocialUserWithStatus friend : friends) {
			if(friend.getUid().equals(tungUid)) {
				Assert.assertEquals(friend.getHandle(), "tung.social.misfit", "Handle");
				Assert.assertEquals(friend.getName(), "Tung Social", "Name");
				Assert.assertEquals(friend.getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
				break;
			}
		}
		
		
		TRS.instance().addStep("==================== SEND AND RECIEVE FRIEND REQUESTS ====================", "subcase");
		
		// misfit send friend request to tung
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		
		// thy send friend request to misfit
		SocialAPI.sendFriendRequest(thyToken, misfitUid);
		
		// query
		result = SocialAPI.getFacebookFriends(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		SocialTestHelpers.printUsers(friends);
		
		// check status of tung and thy
		boolean tungStatusIsCorrect = false;
		boolean thyStatusIsCorrect = false;
		
		for(SocialUserWithStatus friend : friends) {
			
			if(friend.getUid().equals(tungUid))
				tungStatusIsCorrect = friend.getStatus().equals(SocialAPI.STATUS_REQUESTED_FROM_ME);
			
			if(friend.getUid().equals(thyUid))
				thyStatusIsCorrect = friend.getStatus().equals(SocialAPI.STATUS_REQUESTED_TO_ME);
		}
		
		Assert.assertTrue(tungStatusIsCorrect && thyStatusIsCorrect, "Both 'tung' and 'thy' statuses are correct");
		
		
		TRS.instance().addStep("==================== ACCEPT/IGNORE FRIEND REQUESTS ====================", "subcase");
		
		// tung ignores misfit
		SocialAPI.ignoreFriendRequest(tungToken, misfitUid);
		
		// misfit accepts thy
		SocialAPI.acceptFriendRequest(misfitToken, thyUid);
		
		// query
		result = SocialAPI.getFacebookFriends(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		SocialTestHelpers.printUsers(friends);

		// check status of tung and thy
		tungStatusIsCorrect = false;
		thyStatusIsCorrect = false;

		for(SocialUserWithStatus friend : friends) {

			if(friend.getUid().equals(tungUid))
				tungStatusIsCorrect = friend.getStatus().equals(SocialAPI.STATUS_REQUESTED_FROM_ME);

			if(friend.getUid().equals(thyUid))
				thyStatusIsCorrect = friend.getStatus().equals(SocialAPI.STATUS_APPROVED);
		}

		Assert.assertTrue(tungStatusIsCorrect && thyStatusIsCorrect, "Both 'tung' and 'thy' statuses are correct");
		
		// remove ignored request between misfit - tung
		SocialAPI.cancelFriendRequest(misfitToken, tungUid);
		
		// now tung send request and misfit ignore, check status from misfit's side
		SocialAPI.sendFriendRequest(tungToken, misfitUid);
		SocialAPI.ignoreFriendRequest(misfitToken, tungUid);
		
		result = SocialAPI.getFacebookFriends(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		SocialTestHelpers.printUsers(friends);

		for(SocialUserWithStatus friend : friends) {
			
			if(friend.getUid().equals(tungUid))
				tungStatusIsCorrect = friend.getStatus().equals(SocialAPI.STATUS_IGNORED);
		}
		
		// remove ignored request between misfit - tung
		SocialAPI.cancelFriendRequest(tungToken, misfitUid);
		
		
		TRS.instance().addStep("==================== FRIENDS CHANGE PROFILE ====================", "subcase");
		
		// now tung change his name
		ProfileData tungProfile = MVPApi.getProfile(tungToken).profile;
		String tungOldName = tungProfile.getName();
		tungProfile.setName("Tung - " + System.nanoTime());
		MVPApi.updateProfile(tungToken, tungProfile, tungProfile.getServerId());
		
		// query again
		result = SocialAPI.getFriends(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		
		// check updated result
		SocialTestHelpers.printUsers(friends);
		for(SocialUserWithStatus friend : friends) {
			if(friend.getUid().equals(tungUid)) {
				Assert.assertEquals(friend.getHandle(), "tung.social.misfit", "Handle");
				Assert.assertEquals(friend.getName(), tungProfile.getName(), "Name");
				Assert.assertEquals(friend.getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
				break;
			}
		}
		
		// now tung change back his name (must do this if you want to run this test again)
		tungProfile.setName(tungOldName);
		MVPApi.updateProfile(tungToken, tungProfile, tungProfile.getServerId());
		
		// query again
		result = SocialAPI.getFriends(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		
		// check updated result
		SocialTestHelpers.printUsers(friends);
		for(SocialUserWithStatus friend : friends) {
			if(friend.getUid().equals(tungUid)) {
				Assert.assertEquals(friend.getHandle(), "tung.social.misfit", "Handle");
				Assert.assertEquals(friend.getName(), tungOldName, "Name");
				Assert.assertEquals(friend.getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
				break;
			}
		}

		

		TRS.instance().addStep("==================== DELETE FRIENDS ====================", "subcase");
		
		// now thy delete misfit
		SocialAPI.deleteFriend(thyToken, misfitUid);
		
		// query
		result = SocialAPI.getFacebookFriends(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		// check status of tung and thy
		tungStatusIsCorrect = false;
		thyStatusIsCorrect = false;

		for(SocialUserWithStatus friend : friends) {

			if(friend.getUid().equals(tungUid))
				tungStatusIsCorrect = friend.getStatus().equals(SocialAPI.STATUS_NOT_REQUESTED);

			if(friend.getUid().equals(thyUid))
				thyStatusIsCorrect = friend.getStatus().equals(SocialAPI.STATUS_NOT_REQUESTED);
		}

		Assert.assertTrue(tungStatusIsCorrect && thyStatusIsCorrect, "Both 'tung' and 'thy' statuses are correct");

	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SearchUsersAPI" })
	public void SearchUsersTest() {
		
		TRS.instance().addStep("==================== INVALID KEYWORD ====================", "subcase");
		
		// query friends
		BaseResult result = SocialAPI.searchSocialUsers(misfitToken, "qwsfgaakroyxjakfancms");
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		// no result
		Assert.assertEquals(friends.length, 0, "Number of users");

		
		TRS.instance().addStep("==================== KEYWORD EQUALS TO SELF'S HANDLE ====================", "subcase");
		
		// query self
		result = SocialAPI.searchSocialUsers(misfitToken, "misfit.social.misfit");
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		
		// can found yourself
		Assert.assertEquals(friends.length, 1, "Number of users");
		Assert.assertEquals(friends[0].getName(), "Misfit Socia", "User's name");
		
		
		TRS.instance().addStep("==================== VALID KEYWORD ====================", "subcase");
		
		// query friends
		result = SocialAPI.searchSocialUsers(misfitToken, "tung.social.misfit");
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		
		// found 1
		Assert.assertEquals(friends.length, 1, "Number of users found");
		
		// check detail of return values
		Assert.assertEquals(friends[0].getHandle(), "tung.social.misfit", "Handle");
		Assert.assertEquals(friends[0].getName(), "Tung Social", "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
		
		
		TRS.instance().addStep("==================== CHANGING STATUS THROUGH REQEUSTS ====================", "subcase");
		
		// 1a) misfit --> tung
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		
		// misfit searchs tung
		result = SocialAPI.searchSocialUsers(misfitToken, "tung.social.misfit");
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		
		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), "Tung Social", "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_REQUESTED_FROM_ME, "Status");
		
		// tung searchs misfit
		result = SocialAPI.searchSocialUsers(tungToken, "misfit.social.misfit");
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		
		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), "Misfit Social", "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_REQUESTED_TO_ME, "Status");
		
		
		// 1b) tung -x- misfit
		SocialAPI.ignoreFriendRequest(tungToken, misfitUid);
		
		// misfit searchs tung
		result = SocialAPI.searchSocialUsers(misfitToken, "tung.social.misfit");
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), "Tung Social", "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_REQUESTED_FROM_ME, "Status");

		// tung searchs misfit
		result = SocialAPI.searchSocialUsers(tungToken, "misfit.social.misfit");
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), "Misfit Social", "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_IGNORED, "Status");
		
		// remove ignored request from misfit to tung
		SocialAPI.deleteFriend(tungToken, misfitUid);
		
		
		// 2a) misfit --> thy, thy -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, thyUid);
		SocialAPI.acceptFriendRequest(thyToken, misfitUid);
		
		// misfit searchs thy
		result = SocialAPI.searchSocialUsers(misfitToken, "thy.social.misfit");
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), "Thy Social", "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_APPROVED, "Status");
		
		// thy searchs misfit
		result = SocialAPI.searchSocialUsers(thyToken, "misfit.social.misfit");
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), "Misfit Social", "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_APPROVED, "Status");
		
		// 2b) thy deletes misfit
		SocialAPI.deleteFriend(thyToken, misfitUid);
	
		// misfit searchs thy
		result = SocialAPI.searchSocialUsers(misfitToken, "thy.social.misfit");
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), "Thy Social", "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
		
		// thy searchs misfit
		result = SocialAPI.searchSocialUsers(thyToken, "misfit.social.misfit");
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), "Misfit Social", "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
		
		
		TRS.instance().addStep("==================== USERS CHANGE PROFILES ====================", "subcase");
		
		// now tung changes his name
		ProfileData tungProfile = MVPApi.getProfile(tungToken).profile;
		String tungOldName = tungProfile.getName();
		tungProfile.setName("Tung - " + System.nanoTime());
		MVPApi.updateProfile(tungToken, tungProfile, tungProfile.getServerId());

		// misfit searchs tung
		result = SocialAPI.searchSocialUsers(misfitToken, "tung.social.misfit");
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), tungProfile.getName(), "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
		
		// now tung change back his name (must do this if you want to run this test again)
		tungProfile.setName(tungOldName);
		MVPApi.updateProfile(tungToken, tungProfile, tungProfile.getServerId());
		
		// misfit searchs tung
		result = SocialAPI.searchSocialUsers(misfitToken, "tung.social.misfit");
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), tungOldName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
	}

}
