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

import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.social.SocialUserBase;
import com.misfit.ta.backend.data.social.SocialUserWithStatus;
import com.misfit.ta.report.TRS;

public class SocialGetFriendsAPIsTC extends BackendAutomation {

	protected static Logger logger = Util.setupLogger(SocialGetFriendsAPIsTC.class);
	
	protected String socialToken;
	protected Map<String, HashMap<String, Object>> mapNameData;
	protected List<String> mapNames;
	
	
	// set up and clean up
	@BeforeClass(alwaysRun = true)
	public void setUp() {
		
		// get social test data
		mapNameData = SocialTestHelpers.getSocialInitialTestData();
		socialToken = (String) mapNameData.get("social").get("token");
		
		
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
		BaseResult result = SocialAPI.getFriends(socialToken);
		SocialUserBase[] friends = SocialUserBase.usersFromResponse(result.response);
				
		// total number of friend is 0
		Assert.assertEquals(friends.length, 0, "Number of friends");
		
		
		
		TRS.instance().addStep("==================== ADD NEW FRIENDS ====================", null);
		
		// send friend request to tung and thy
		SocialAPI.sendFriendRequest(socialToken, (String) mapNameData.get("tung").get("uid"));
		SocialAPI.sendFriendRequest(socialToken, (String) mapNameData.get("thy").get("uid"));
		
		// query again
		result = SocialAPI.getFriends(socialToken);
		friends = SocialUserBase.usersFromResponse(result.response);
				
		// total number of friend is still 0
		Assert.assertEquals(friends.length, 0, "Number of friends");
		
		// accept both requests
		
		// make sure the result only contain 'misfit friends' not both 'misfit friends' and 'fb friends'
		for(String key : mapNames) {
			
			String uid = (String) mapNameData.get(key).get("uid");
			boolean isFriend = (Boolean) mapNameData.get(key).get("isFriend");
			
			for(SocialUserBase friend : friends) {
				
				if(friend.getUid().equals(uid)) {
					Assert.assertTrue(isFriend, "'" + key + "' is a friend");
					break;
				}
			}
		}
		
		
		// check detail of return values
		String qaUid = (String) mapNameData.get("qa").get("uid");
		for(SocialUserBase friend : friends) {
			if(friend.getUid().equals(qaUid)) {
				Assert.assertEquals(friend.getAvatar(), "http://graph.facebook.com/100003739311514/picture", "Avatar of qa");
				Assert.assertEquals(friend.getHandle(), "mfwc.qa", "Handle of qa");
				Assert.assertEquals(friend.getName(), "Mfwc Qa", "Name of qa");
				break;
			}
		}
		
	}
		
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "GetFacebookFriendAPI" })
	public void GetFacebookFriendsTest() {
		
		BaseResult result = SocialAPI.getFacebookFriends(socialToken);
		SocialUserBase[] friends = SocialUserBase.usersFromResponse(result.response);
		
		
		// check total number of facebook friends
		Assert.assertEquals(friends.length, mapNames.size(), "Number of facebook friends");
		
		
		// make sure only facebook users who use app are in the result
		for(String key : mapNames) {
			
			String uid = (String) mapNameData.get(key).get("uid");
			boolean found = false;
			
			// not using facebook
			if(uid == null || uid.isEmpty())
				continue;
			
			for(SocialUserBase friend : friends) {
				
				if(friend.getUid().equals(uid)) {
					found = true;
					break;
				}
			}
			
			Assert.assertTrue(found, "Found facebook friend '" + key + "'");
		}
		
		
		// check detail of return values
		String qaUid = (String) mapNameData.get("qa").get("uid");
		for(SocialUserBase friend : friends) {
			if(friend.getUid().equals(qaUid)) {
				Assert.assertEquals(friend.getAvatar(), "http://graph.facebook.com/100003739311514/picture", "Avatar of qa");
				Assert.assertEquals(friend.getHandle(), "mfwc.qa", "Handle of qa");
				Assert.assertEquals(friend.getName(), "Mfwc Qa", "Name of qa");
				break;
			}
		}
		
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SearchUserAPI" })
	public void SearchUserValidKeywordTest() {
		
		BaseResult result = SocialAPI.searchSocialUsers(socialToken, "mfwc.qa");
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
		
		
		// check total number of facebook friends
		Assert.assertEquals(friends.length, 1, "Number of friends in search result");
	
		
		// check detail of return values
		String qaUid = (String) mapNameData.get("qa").get("uid");
		for(SocialUserWithStatus friend : friends) {
			
			if(friend.getUid().equals(qaUid)) {
				Assert.assertEquals(friend.getAvatar(), "http://graph.facebook.com/100003739311514/picture", "Avatar of qa");
				Assert.assertEquals(friend.getHandle(), "mfwc.qa", "Handle of qa");
				Assert.assertEquals(friend.getName(), "Mfwc Qa", "Name of qa");
				Assert.assertEquals(friend.getStatus(), SocialAPI.STATUS_APPROVED, "Friend status (aprroved) to qa");
				break;
			}
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "SearchUserAPI" })
	public void SearchUserInvalidKeywordTest() {
		
		BaseResult result = SocialAPI.searchSocialUsers(socialToken, "zsgleisrelruirxvnm");
		SocialUserWithStatus[] friends = SocialUserWithStatus.usersFromResponse(result.response);
		
		
		// check total number of facebook friends
		Assert.assertEquals(friends.length, 0, "Number of friends in search result");
	}

	
	
}
