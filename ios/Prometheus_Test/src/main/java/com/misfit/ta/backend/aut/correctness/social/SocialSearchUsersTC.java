package com.misfit.ta.backend.aut.correctness.social;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.SocialAutomationBase;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.social.SocialUserWithStatus;
import com.misfit.ta.report.TRS;

public class SocialSearchUsersTC extends SocialAutomationBase {
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SearchUsersAPI" })
	public void SearchUsers_WithoutKeywordParam() {
		
		// query friends
		BaseResult result = SocialAPI.searchSocialUsers(misfitToken, null);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		// no result
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(friends.length, 0, "Number of users found");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SearchUsersAPI" })
	public void SearchUsers_InvalidKeyword() {
		
		// query friends
		BaseResult result = SocialAPI.searchSocialUsers(misfitToken, "qwsfgaakroyxjakfancms");
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		// no result
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(friends.length, 0, "Number of users");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SearchUsersAPI" })
	public void SearchUsers_KeywordEqualsToSelfHandle() {
		
		// query self
		BaseResult result = SocialAPI.searchSocialUsers(misfitToken, "misfit.social.misfit");
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		// can found yourself
		Assert.assertEquals(friends.length, 1, "Number of users");
		Assert.assertEquals(friends[0].getName(), "Misfit Social", "User's name");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SearchUsersAPI" })
	public void SearchUsers_ValidKeyword() {
		
		// query friends
		BaseResult result = SocialAPI.searchSocialUsers(misfitToken, "tung.social.misfit");
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		// found 1
		Assert.assertEquals(friends.length, 1, "Number of users found");

		// check detail of return values
		Assert.assertEquals(friends[0].getHandle(), "tung.social.misfit", "Handle");
		Assert.assertEquals(friends[0].getName(), "Tung Social", "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SearchUsersAPI" })
	public void SearchUsers_AfterChangeRequestStatus() {
		
		TRS.instance().addStep("==================== STATUS: NOT REQUESTED ====================", "subcase");
		
		// 1a) misfit --> tung
		SocialAPI.sendFriendRequest(misfitToken, tungUid);

		// misfit searchs tung
		BaseResult result = SocialAPI.searchSocialUsers(misfitToken, "tung.social.misfit");
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), "Tung Social", "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_REQUESTED_FROM_ME, "Status");

		// tung searchs misfit
		result = SocialAPI.searchSocialUsers(tungToken, "misfit.social.misfit");
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), "Misfit Social", "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_REQUESTED_TO_ME, "Status");


		TRS.instance().addStep("==================== STATUS: IGNORED ====================", "subcase");
		
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

		// cancel request misfit --> tung
		SocialAPI.cancelFriendRequest(misfitToken, tungUid);


		TRS.instance().addStep("==================== STATUS: APPROVED ====================", "subcase");
		
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

		
		TRS.instance().addStep("==================== STATUS: NOT REQUESTED (AFTER DELETE) ====================", "subcase");
		
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
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SearchUsersAPI" })
	public void SearchUsers_AfterTheyChangeTheirProfiles() {
		
		// now tung changes his name
		ProfileData tungProfile = MVPApi.getProfile(tungToken).profile;
		String tungOldName = tungProfile.getName();
		tungProfile.setName("Tung - " + System.nanoTime());
		MVPApi.updateProfile(tungToken, tungProfile, tungProfile.getServerId());

		// misfit searchs tung
		BaseResult result = SocialAPI.searchSocialUsers(misfitToken, "tung.social.misfit");
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

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
