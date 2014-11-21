package com.misfit.ta.backend.aut.correctness.social;


import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.social.SocialUserWithStatus;

public class SocialSendFriendRequestTC extends SocialTestAutomationBase {
	
	// fields
	protected String nonSocialUserUid;
	
	@BeforeClass(alwaysRun = true)
    public void beforeClass() {
		
		super.beforeClass();
		
		String nonSocialEmail = MVPApi.generateUniqueEmail();
		String token = MVPApi.signUp(nonSocialEmail, "qqqqqq").token;
		ProfileData profile = DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null);
		profile.setHandle(null);
		
		MVPApi.createProfile(token, profile);
		nonSocialUserUid = MVPApi.getUserId(token);
	}
	
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
				
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorCode, DefaultValues.UserNotUseSocialCode, "Error code");
		Assert.assertEquals(result.errorMessage, DefaultValues.UserNotUseSocialMessage, "Error message");
		
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

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
		
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorCode, DefaultValues.CannotSendRequestToYourSelfCode, "Error code");
		Assert.assertEquals(result.errorMessage, DefaultValues.CannotSendRequestToYourSelfMessage, "Error message");

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
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest", "KnownIssue" })
	public void SendFriendRequests_WithEmptyList() throws JSONException {
	
		List<String> friendIds = new ArrayList<String>();
		BaseResult result = SocialAPI.sendFriendRequests(misfitToken, friendIds);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(result.rawData, "{}", "Data");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest", "KnownIssue" })
	public void SendFriendRequests_ToUserWithInvalidUid() throws JSONException {
		
		// to invalid uid user
		List<String> friendIds = new ArrayList<String>();
		friendIds.add("invalid_uid");

		BaseResult result = SocialAPI.sendFriendRequests(misfitToken, friendIds);
		Assert.assertEquals(result.statusCode, 200, "Status code");

		JSONArray jsonarr = new JSONArray(result.rawData);
		Assert.assertEquals(getErrorCode(jsonarr.getJSONObject(0)), DefaultValues.UserNotFoundCode, "Error code for invalid user");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequests_ToInvalidUsers() throws JSONException {
		
		// misfit --> tung, tung -v- misfit
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		
		// to invalid user / non social user / yourself / your friend / valid friend
		List<String> friendIds = new ArrayList<String>();
		friendIds.add(nonSocialUserUid);
		friendIds.add(misfitUid);
		friendIds.add(tungUid);
		friendIds.add(thyUid);
		
		// expect first 3 requests are failed
		BaseResult result = SocialAPI.sendFriendRequests(misfitToken, friendIds);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		JSONArray jsonarr = new JSONArray(result.rawData);
		Assert.assertEquals(jsonarr.length(), 3, "Number of failed requests");
		Assert.assertEquals(getErrorCode(jsonarr.getJSONObject(0)), DefaultValues.UserNotUseSocialCode, "Error code for non-social user");
		Assert.assertEquals(getErrorCode(jsonarr.getJSONObject(1)), DefaultValues.CannotSendRequestToYourSelfCode, "Error code for yourself");
		Assert.assertEquals(getErrorCode(jsonarr.getJSONObject(2)), DefaultValues.AlreadyAreFriendsCode, "Error code for invalid user");
		
		// last one passed (to thy)
		result = SocialAPI.getFriendRequestsFromMe(misfitToken);
		SocialUserWithStatus[] users = SocialUserWithStatus.usersFromResponse(result.response);
		
		Assert.assertEquals(users.length, 1, "Number of request from me");
		Assert.assertEquals(users[0].getUid(), thyUid, "User id");
		Assert.assertEquals(users[0].getStatus(), SocialAPI.STATUS_REQUESTED_FROM_ME, "Friend status");
		
		result = SocialAPI.getFriendRequestsToMe(thyToken);
		users = SocialUserWithStatus.usersFromResponse(result.response);
		
		Assert.assertEquals(users.length, 1, "Number of request to thy");
		Assert.assertEquals(users[0].getUid(), misfitUid, "User id");
		Assert.assertEquals(users[0].getStatus(), SocialAPI.STATUS_REQUESTED_TO_ME, "Friend status");
		
		// now send to requested friend
		friendIds.clear();
		friendIds.add(thyUid);
		
		result = SocialAPI.sendFriendRequests(misfitToken, friendIds);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		jsonarr = new JSONArray(result.rawData);
		Assert.assertEquals(jsonarr.length(), 1, "Number of failed requests");
		Assert.assertEquals(getErrorCode(jsonarr.getJSONObject(0)), DefaultValues.AlreadyRequestedCode, "Error code for already requested user");
		
		// now send to who requested you
		friendIds.clear();
		friendIds.add(misfitUid);
		
		result = SocialAPI.sendFriendRequests(thyToken, friendIds);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		result = SocialAPI.getFriends(thyToken);
		users = SocialUserWithStatus.usersFromResponse(result.response);
		Assert.assertEquals(users.length, 1, "Number of thy's friends");
		Assert.assertEquals(users[0].getUid(), misfitUid, "Friend's uid");
		
		// clean up test: delete friend / cancel request
		SocialAPI.deleteFriend(misfitToken, thyUid);
		SocialAPI.deleteFriend(misfitToken, tungUid);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SendFriendRequest" })
	public void SendFriendRequests_ToMultipleVvalidUsers() throws JSONException {
		
		// to 2 valid non friend users
		List<String> friendIds = new ArrayList<String>();
		friendIds.add(tungUid);
		friendIds.add(thyUid);
		
		// expect first 3 requests are failed
		BaseResult result = SocialAPI.sendFriendRequests(misfitToken, friendIds);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(result.rawData, "{}", "Response's body");
		
		// clean up test: delete friend / cancel request
		SocialAPI.cancelFriendRequest(misfitToken, thyUid);
		SocialAPI.cancelFriendRequest(misfitToken, tungUid);
	}
	
	private int getErrorCode(JSONObject json) {
		
		try {
			return Integer.parseInt(json.getJSONObject("error").getString("code"));
		} catch (Exception e) {
			return -1;
		}
	}

}