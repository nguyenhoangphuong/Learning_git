package com.misfit.ta.backend.aut.correctness.social;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.social.SocialUserBase;
import com.misfit.ta.backend.data.social.SocialUserWithStatus;
import com.misfit.ta.report.TRS;

public class SocialFriendsAPIsTC extends BackendAutomation {

	protected static Logger logger = Util.setupLogger(SocialFriendsAPIsTC.class);
	
	protected String misfitToken;
	protected String tungToken;
	protected String thyToken;
	
	protected String misfitUid;
	protected String tungUid;
	protected String thyUid;
	
	protected Map<String, HashMap<String, Object>> mapNameData;
	protected List<String> mapNames;
	
	
	// set up and clean up
	@BeforeClass(alwaysRun = true)
	public void setUp() {
		
		// get social test data
		mapNameData = SocialTestHelpers.getSocialInitialTestData();
		
		misfitToken = (String) mapNameData.get("misfit").get("token");
		tungToken = (String) mapNameData.get("tung").get("token");
		thyToken = (String) mapNameData.get("thy").get("token");
		
		misfitUid = (String) mapNameData.get("misfit").get("fuid");
		tungUid = (String) mapNameData.get("tung").get("fuid");
		thyUid = (String) mapNameData.get("thy").get("fuid");
		
		
		// map key set
		Iterator<String> iterator = mapNameData.keySet().iterator();
		mapNames = new ArrayList<String>();
		while(iterator.hasNext())
			mapNames.add(iterator.next());
	}
	
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFriendAPI" })
	public void GetFriendsTest() {
		
		TRS.instance().addStep("==================== NO FRIEND ====================", null);
		
		// query friends
		BaseResult result = SocialAPI.getFriends(misfitToken);
		SocialUserBase[] friends = SocialUserBase.usersFromResponse(result.response);
				
		// total number of friend is 0
		Assert.assertEquals(friends.length, 0, "Number of friends");
		
		
		
		TRS.instance().addStep("==================== ADD NEW FRIENDS ====================", null);
		
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
				Assert.assertEquals(friend.getAvatar(), "http://graph.facebook.com/100007032820465/picture", "Avatar");
				Assert.assertEquals(friend.getHandle(), "tung.social.misfit", "Handle");
				Assert.assertEquals(friend.getName(), "Tung Social", "Name");
				break;
			}
		}
		
		
		TRS.instance().addStep("==================== FRIENDS CHANGE PROFILE ====================", null);
		
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
				Assert.assertEquals(friend.getAvatar(), "http://graph.facebook.com/100007032820465/picture", "Avatar");
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
				Assert.assertEquals(friend.getAvatar(), "http://graph.facebook.com/100007032820465/picture", "Avatar");
				Assert.assertEquals(friend.getHandle(), "tung.social.misfit", "Handle");
				Assert.assertEquals(friend.getName(), tungOldName, "Name");
				break;
			}
		}
		
		
		TRS.instance().addStep("==================== REMOVE FRIENDS ====================", null);
		
		// now misfit delete tung
		SocialAPI.deleteFriend(misfitToken, tungUid);
		
		// query again
		result = SocialAPI.getFriends(misfitToken);
		friends = SocialUserBase.usersFromResponse(result.response);
		
		// total number of friend should be 1
		Assert.assertEquals(friends.length, 1, "Number of friends");
		
		// make sure the result only contains only thy
		Assert.assertEquals(friends[0].getUid(), thyUid);
		
		
		TRS.instance().addStep("==================== BE REMOVED BY FRIENDS ====================", null);
		
		// now thy delete misfit
		SocialAPI.deleteFriend(thyToken, misfitUid);
		
		// query again
		result = SocialAPI.getFriends(misfitToken);
		friends = SocialUserBase.usersFromResponse(result.response);
		
		// total number of friend should be 0
		Assert.assertEquals(friends.length, 0, "Number of friends");
		
	}
	
	public void GetFacebookFriendsTest() {
		
		TRS.instance().addStep("==================== GET FRIENDS ====================", null);
		
		// query friends
		BaseResult result = SocialAPI.getFacebookFriends(misfitToken);
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);

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
		SocialTestHelpers.printUsers(friends);
		for(SocialUserWithStatus friend : friends) {
			if(friend.getUid().equals(tungUid)) {
				Assert.assertEquals(friend.getAvatar(), "http://graph.facebook.com/100007032820465/picture", "Avatar");
				Assert.assertEquals(friend.getHandle(), "tung.social.misfit", "Handle");
				Assert.assertEquals(friend.getName(), "Tung Social", "Name");
				Assert.assertEquals(friend.getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status");
				break;
			}
		}
		
		
		TRS.instance().addStep("==================== SEND AND RECIEVE FRIEND REQUESTS ====================", null);
		
		// misfit send friend request to tung
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		
		// thy send friend request to misfit
		SocialAPI.sendFriendRequest(thyToken, misfitUid);
		
		// query
		result = SocialAPI.getFacebookFriends(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		
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
		
		
		TRS.instance().addStep("==================== ACCEPT/IGNORE FRIEND REQUESTS ====================", null);
		
		// tung ignores misfit
		SocialAPI.ignoreFriendRequest(tungToken, misfitUid);
		
		// misfit accepts thy
		SocialAPI.acceptFriendRequest(misfitToken, thyUid);
		
		// query
		result = SocialAPI.getFacebookFriends(misfitToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);

		// check status of tung and thy
		tungStatusIsCorrect = false;
		thyStatusIsCorrect = false;

		for(SocialUserWithStatus friend : friends) {

			// TODO: A - B, if B ignore, status of A is still from_me, may change in the future
			if(friend.getUid().equals(tungUid))
				tungStatusIsCorrect = friend.getStatus().equals(SocialAPI.STATUS_REQUESTED_FROM_ME);

			if(friend.getUid().equals(thyUid))
				thyStatusIsCorrect = friend.getStatus().equals(SocialAPI.STATUS_APPROVED);
		}

		Assert.assertTrue(tungStatusIsCorrect && thyStatusIsCorrect, "Both 'tung' and 'thy' statuses are correct");
		
		// check status from tung's side
		result = SocialAPI.getFacebookFriends(tungToken);
		friends = SocialUserWithStatus.usersFromResponse(result.response);
		
		Assert.assertTrue(friends[0].getStatus().equals(SocialAPI.STATUS_IGNORED), "Status from 'tung' to 'misfit'");
		
		// TODO: work around to remove ignored request between misfit - tung
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		SocialAPI.deleteFriend(tungToken, misfitUid);
		

		TRS.instance().addStep("==================== DELETE FRIENDS ====================", null);
		
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
	
}
