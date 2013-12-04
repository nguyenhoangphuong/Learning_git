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

public class SocialGetFacebookFriendTC extends SocialAutomationBase {
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFacebookFriendAPI", "Excluded" })
	public void GetFacebookFriends_NormalCase() {
		
		// query friends
		BaseResult result = SocialAPI.getFacebookFriends(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
		SocialTestHelpers.printUsers(friends);
		
		int count = -1;
		for(String key : mapNames) {
			
			boolean isUsingApp = (Boolean) mapNameData.get(key).get("isUsingApp");
			if(isUsingApp)
				count++;
		}
		
		Assert.assertEquals(friends.length, count, "Number of facebook users");

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
				Assert.assertEquals(friend.getHandle(), "tung_social_misfit", "Handle");
				Assert.assertEquals(friend.getName(), "Tung Social", "Name");
				Assert.assertEquals(friend.getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
				break;
			}
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFacebookFriendAPI", "Excluded" })
	public void GetFacebookFriends_ChangeRequestStatus() {
		
		// set up
		SocialAPI.deleteFriend(misfitToken, tungUid);
		SocialAPI.deleteFriend(thyToken, misfitUid);
		
		// misfit--> tung, thy --> misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.sendFriendRequest(thyToken, misfitUid);

		// query
		BaseResult result = SocialAPI.getFacebookFriends(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
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
		
		// tung -x- misfit, misfit -v- thy
		SocialAPI.ignoreFriendRequest(tungToken, misfitUid);
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

		// cancel request misfit --> tung
		SocialAPI.cancelFriendRequest(misfitToken, tungUid);

		// now tung --> misfit, misfit -x- tung, check status from misfit's side
		SocialAPI.sendFriendRequest(tungToken, misfitUid);
		SocialAPI.ignoreFriendRequest(misfitToken, tungUid);

		result = SocialAPI.getFacebookFriends(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		SocialTestHelpers.printUsers(friends);

		for(SocialUserWithStatus friend : friends) {

			if(friend.getUid().equals(tungUid))
				tungStatusIsCorrect = friend.getStatus().equals(SocialAPI.STATUS_IGNORED);
		}

		// cancel request tung --> misfit
		SocialAPI.cancelFriendRequest(tungToken, misfitUid);
		
		// delete friend (thy)
		SocialAPI.deleteFriend(misfitToken, thyUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFacebookFriendAPI", "Excluded" })
	public void GetFacebookFriends_AfterFriendsChangeTheirProfiles() {	
		
		// now tung change his name
		ProfileData tungProfile = MVPApi.getProfile(tungToken).profile;
		String tungOldName = "Tung Social";
		tungProfile.setName("Tung - " + System.nanoTime());
		MVPApi.updateProfile(tungToken, tungProfile);

		// query again
		BaseResult result = SocialAPI.getFriends(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		// check updated result
		SocialTestHelpers.printUsers(friends);
		for(SocialUserWithStatus friend : friends) {
			if(friend.getUid().equals(tungUid)) {
				Assert.assertEquals(friend.getHandle(), "tung_social_misfit", "Handle");
				Assert.assertEquals(friend.getName(), tungProfile.getName(), "Name");
				Assert.assertEquals(friend.getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
				break;
			}
		}

		// now tung change back his name (must do this if you want to run this test again)
		tungProfile.setName(tungOldName);
		MVPApi.updateProfile(tungToken, tungProfile);

		// query again
		result = SocialAPI.getFriends(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		// check updated result
		SocialTestHelpers.printUsers(friends);
		for(SocialUserWithStatus friend : friends) {
			if(friend.getUid().equals(tungUid)) {
				Assert.assertEquals(friend.getHandle(), "tung_social_misfit", "Handle");
				Assert.assertEquals(friend.getName(), tungOldName, "Name");
				Assert.assertEquals(friend.getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
				break;
			}
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFacebookFriendAPI", "Excluded" })
	public void GetFacebookFriends_AfterDeleteFriends() {		
		

		// misfit --> thy, thy -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, thyUid);
		SocialAPI.acceptFriendRequest(thyToken, misfitUid);
		
		// now thy deletes misfit
		SocialAPI.deleteFriend(thyToken, misfitUid);
		
		// query
		BaseResult result = SocialAPI.getFacebookFriends(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		// check status of tung and thy
		boolean tungStatusIsCorrect = false;
		boolean thyStatusIsCorrect = false;

		for(SocialUserWithStatus friend : friends) {

			if(friend.getUid().equals(tungUid))
				tungStatusIsCorrect = friend.getStatus().equals(SocialAPI.STATUS_NOT_REQUESTED);

			if(friend.getUid().equals(thyUid))
				thyStatusIsCorrect = friend.getStatus().equals(SocialAPI.STATUS_NOT_REQUESTED);
		}

		Assert.assertTrue(tungStatusIsCorrect && thyStatusIsCorrect, "Both 'tung' and 'thy' statuses are correct");

	}

}
