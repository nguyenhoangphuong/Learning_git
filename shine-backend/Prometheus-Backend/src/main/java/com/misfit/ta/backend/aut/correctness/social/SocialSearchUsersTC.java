package com.misfit.ta.backend.aut.correctness.social;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.social.SocialUserWithStatus;
import com.misfit.ta.report.TRS;
import com.misfit.ta.utils.TextTool;

public class SocialSearchUsersTC extends SocialTestAutomationBase {
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SearchUsersAPI" })
	public void SearchUsers_WithoutKeywordParam() {
		
		// query friends non existed id
		BaseResult result = SocialAPI.searchSocialUsers(misfitToken, null);

		// no result
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.InvalidParameterMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.InvalidParameterCode, "Error code");
		
		
		// query friends short keywords
		result = SocialAPI.searchSocialUsers(misfitToken, TextTool.getRandomString(1, 2));

		// no result
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.InvalidParameterMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.InvalidParameterCode, "Error code");
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
		BaseResult result = SocialAPI.searchSocialUsers(misfitToken, misfitHandle);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		// cannot found yourself
		Assert.assertEquals(friends.length, 0, "Number of users");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SearchUsersAPI" })
	public void SearchUsers_ValidKeyword() {
		
		// query friends
		BaseResult result = SocialAPI.searchSocialUsers(misfitToken, tungHandle);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		// found 1
		Assert.assertEquals(friends.length, 1, "Number of users found");

		// check detail of return values
		Assert.assertEquals(friends[0].getHandle(), tungHandle, "Handle");
		Assert.assertEquals(friends[0].getName(), tungName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SearchUsersAPI" })
	public void SearchUsers_AfterChangeRequestStatus() {
		
		TRS.instance().addStep("==================== STATUS: REQUESTED ====================", "subcase");
		
		// 1a) misfit --> tung
		SocialAPI.sendFriendRequest(misfitToken, tungUid);

		// misfit searchs tung
		BaseResult result = SocialAPI.searchSocialUsers(misfitToken, tungHandle);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), tungName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_REQUESTED_FROM_ME, "Status");

		// tung searchs misfit
		result = SocialAPI.searchSocialUsers(tungToken, misfitHandle);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), misfitName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_REQUESTED_TO_ME, "Status");


		TRS.instance().addStep("==================== STATUS: IGNORED ====================", "subcase");
		
		// 1b) tung -x- misfit
		SocialAPI.ignoreFriendRequest(tungToken, misfitUid);

		// misfit searchs tung
		result = SocialAPI.searchSocialUsers(misfitToken, tungHandle);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), tungName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_REQUESTED_FROM_ME, "Status");

		// tung searchs misfit
		result = SocialAPI.searchSocialUsers(tungToken, misfitHandle);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), misfitName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_IGNORED, "Status");

		// cancel request misfit --> tung
		SocialAPI.cancelFriendRequest(misfitToken, tungUid);


		TRS.instance().addStep("==================== STATUS: APPROVED ====================", "subcase");
		
		// 2a) misfit --> thy, thy -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, thyUid);
		SocialAPI.acceptFriendRequest(thyToken, misfitUid);

		// misfit searchs thy
		result = SocialAPI.searchSocialUsers(misfitToken, thyHandle);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), thyName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_APPROVED, "Status");

		// thy searchs misfit
		result = SocialAPI.searchSocialUsers(thyToken, misfitHandle);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), misfitName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_APPROVED, "Status");

		
		TRS.instance().addStep("==================== STATUS: NOT REQUESTED (AFTER DELETE) ====================", "subcase");
		
		// 2b) thy deletes misfit
		SocialAPI.deleteFriend(thyToken, misfitUid);

		// misfit searchs thy
		result = SocialAPI.searchSocialUsers(misfitToken, thyHandle);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), thyName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");

		// thy searchs misfit
		result = SocialAPI.searchSocialUsers(thyToken, misfitHandle);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), misfitName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SearchUsersAPI" })
	public void SearchUsers_AfterTheyChangeTheirProfiles() {
		
		// now tung changes his name
		ProfileData tungProfile = MVPApi.getProfile(tungToken).profile;
		String tungOldName = tungName;
		tungProfile.setName("Tung - " + System.nanoTime());
		MVPApi.updateProfile(tungToken, tungProfile);

		// misfit searchs tung
		BaseResult result = SocialAPI.searchSocialUsers(misfitToken, tungHandle);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), tungProfile.getName(), "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
		
		// now tung change back his name (must do this if you want to run this test again)
		tungProfile.setName(tungOldName);
		MVPApi.updateProfile(tungToken, tungProfile);
		
		// misfit searchs tung
		result = SocialAPI.searchSocialUsers(misfitToken, tungHandle);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(friends.length, 1, "Number of users found");
		Assert.assertEquals(friends[0].getName(), tungOldName, "Name");
		Assert.assertEquals(friends[0].getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
	}

}
