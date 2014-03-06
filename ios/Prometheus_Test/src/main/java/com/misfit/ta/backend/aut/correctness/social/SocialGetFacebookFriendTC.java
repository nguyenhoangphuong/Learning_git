package com.misfit.ta.backend.aut.correctness.social;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.internalapi.social.SocialAPI;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.social.SocialUserWithStatus;

public class SocialGetFacebookFriendTC extends BackendAutomation {
	
	// fields
	protected String misfitToken;
	protected String tungToken;
	protected String thyToken;
	
	protected String misfitUid;
	protected String tungUid;
	protected String thyUid;
	
	protected String misfitHandle;
	protected String tungHandle;
	protected String thyHandle;
	
	protected String misfitName;
	protected String tungName;
	protected String thyName;
	
	
	// set up class
	@BeforeClass(alwaysRun = true)
    public void beforeClass() {
		
		misfitToken = MVPApi.signIn("nhhai16991@gmail.com", "qqqqqq").token;
		tungToken = MVPApi.signIn("tung+1@misfitwearables.com", "qqqqqq").token;
		thyToken = MVPApi.signIn("mfwcqa@gmail.com", "qqqqqq").token;
		
		misfitUid = MVPApi.getUserId(misfitToken);
		tungUid = MVPApi.getUserId(tungToken);
		thyUid = MVPApi.getUserId(thyToken);
		
		misfitHandle = "nhhai";
		tungHandle = "tung_plus1";
		thyHandle = "mfwcqa";
		
		misfitName = "NHHai";
		tungName = "Tung+1";
		thyName = "mfwcqa";
	}
	
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFacebookFriendAPI", "StagingOnly" })
	public void GetFacebookFriends_NormalCase() {
		
		// query friends
		BaseResult result = SocialAPI.getFacebookFriends(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
		SocialTestHelpers.printUsers(friends);
			

		// make sure the result only contains only users who use Shine app
		int count = 0;
		for(SocialUserWithStatus friend : friends) {
			if(friend.getUid().equals(thyUid)) {
				count++;
				Assert.assertEquals(friend.getHandle(), thyHandle, "Handle");
				Assert.assertEquals(friend.getName(), thyName, "Name");
				Assert.assertEquals(friend.getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
			}
		}
		
		for(SocialUserWithStatus friend : friends) {
			if(friend.getUid().equals(tungUid)) {
				count++;
				Assert.assertEquals(friend.getHandle(), tungHandle, "Handle");
				Assert.assertEquals(friend.getName(), tungName, "Name");
				Assert.assertEquals(friend.getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
			}
		}
		
		Assert.assertEquals(count, 2, "Number of friends found");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFacebookFriendAPI", "StagingOnly" })
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
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFacebookFriendAPI", "StagingOnly" })
	public void GetFacebookFriends_AfterFriendsChangeTheirProfiles() {	
		
		// now tung change his name
		ProfileData tungProfile = MVPApi.getProfile(tungToken).profile;
		String tungOldName = tungName;
		tungProfile.setName("Tung - " + System.nanoTime());
		MVPApi.updateProfile(tungToken, tungProfile);

		// query again
		BaseResult result = SocialAPI.getFriends(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

		// now tung change back his name (must do this if you want to run this test again)
		tungProfile.setName(tungOldName);
		MVPApi.updateProfile(tungToken, tungProfile);
				
		// check updated result
		SocialTestHelpers.printUsers(friends);
		for(SocialUserWithStatus friend : friends) {
			if(friend.getUid().equals(tungUid)) {
				Assert.assertEquals(friend.getHandle(), tungHandle, "Handle");
				Assert.assertEquals(friend.getName(), tungProfile.getName(), "Name");
				Assert.assertEquals(friend.getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
				break;
			}
		}

		// query again
		result = SocialAPI.getFriends(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		// check updated result
		SocialTestHelpers.printUsers(friends);
		for(SocialUserWithStatus friend : friends) {
			if(friend.getUid().equals(tungUid)) {
				Assert.assertEquals(friend.getHandle(), tungHandle, "Handle");
				Assert.assertEquals(friend.getName(), tungOldName, "Name");
				Assert.assertEquals(friend.getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
				break;
			}
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFacebookFriendAPI", "StagingOnly" })
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
