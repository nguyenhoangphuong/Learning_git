package com.misfit.ta.backend.aut.correctness.social;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.common.Verify;
import com.misfit.ta.utils.ShortcutsTyper;

public class ShineBackendSocialSmokeTestWithoutCreateUsers extends BackendAutomation {

	private List<String> errors = new ArrayList<String>();
	private long DelayTime = 2000;
	
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "shineapi_light_smoke_test" })
	public void ShineBackendSocialAPIsSmokeTest() {
		
		errors.clear();
		
		// clean up
		String myToken = MVPApi.signIn("shine_backend_smoke_test_me@qa.com", "qqqqqq").token;
		String friendToken = MVPApi.signIn("shine_backend_smoke_test_you@qa.com", "qqqqqq").token;
		String myUid = MVPApi.getUserId(myToken);
		String friendUid = MVPApi.getUserId(friendToken);
		
		SocialAPI.deleteFriend(myToken, friendUid);
		SocialAPI.cancelFriendRequest(myToken, friendUid);	
		
		// run test
		runFriendRequestTest(myToken, myUid, friendToken, friendUid);
		runDashboardTest(myToken);
		runSocialMiscTest(myToken);
		
		if(!Verify.verifyAll(errors))
			Assert.fail("Smoke test fails, some routes don't work as expected");
	}

	
	// test helpers	
	public void runFriendRequestTest(String myToken, String myUid, String friendToken, String friendUid) {
		
		List<String> friendUids = new ArrayList<String>();
		friendUids.add(friendUid);
		
		// friend request: send (multiple) / accept / ignore / delete / cancel / list
		//                 get request from / to
		// ----------------------------------------------
		BaseResult r = SocialAPI.sendFriendRequest(myToken, friendUid);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Send friend request OK"));
		ShortcutsTyper.delayTime(DelayTime);
		
		r = SocialAPI.acceptFriendRequest(friendToken, myUid);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Accept friend request OK"));
		ShortcutsTyper.delayTime(DelayTime);
		
		r = SocialAPI.getFriends(myToken);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Get friends list OK"));
		
		r = SocialAPI.getFriendsWithAverageUsers(myToken);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Get friends list with average users OK"));
		
		r = SocialAPI.deleteFriend(myToken, friendUid);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Delete friend OK"));
		ShortcutsTyper.delayTime(DelayTime);
		
		
		r = SocialAPI.sendFriendRequests(myToken, friendUids);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Send friend requests (multiple) OK"));
		ShortcutsTyper.delayTime(DelayTime);
		
		r = SocialAPI.getFriendRequestsFromMe(myToken);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Get friend requests from me OK"));
		
		r = SocialAPI.getFriendRequestsToMe(friendToken);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Get friend requests to me OK"));
	
		r = SocialAPI.ignoreFriendRequest(friendToken, myUid);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Ignore friend request OK"));
		ShortcutsTyper.delayTime(DelayTime);
		
		r = SocialAPI.cancelFriendRequest(myToken, friendUid);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Cancel friend request OK"));
		ShortcutsTyper.delayTime(DelayTime);
	}
	
	public void runDashboardTest(String myToken) {
		
		// dashboard: leaderboard / world_feed
		// ----------------------------------------------
		BaseResult r = SocialAPI.getLeaderboardInfo(myToken);
		errors.add(Verify.verifyTrue(r.isOK(), "[dashboard] Get leaderboard OK"));
		
		r = SocialAPI.getLeaderboardInfoWithAverageUsers(myToken);
		errors.add(Verify.verifyTrue(r.isOK(), "[dashboard] Get leaderboard with average users OK"));
		
		r = SocialAPI.getWorldInfo(myToken);
		errors.add(Verify.verifyTrue(r.isOK(), "[dashboard] Get world feed OK"));
	}
	
	public void runSocialMiscTest(String myToken) {

		List<String> emails = new ArrayList<String>();
		emails.add("shine_backend_smoke_test_me@qa.com");
		emails.add("shine_backend_smoke_test_you@qa.com");
		
		
		// misc: match contacts / search
		// ----------------------------------------------
		BaseResult r = SocialAPI.searchSocialUsers(myToken, "shine_backend");
		errors.add(Verify.verifyTrue(r.isOK(), "[social_misc] Search social users OK"));
		
		r = SocialAPI.matchContacts(myToken, emails);
		errors.add(Verify.verifyTrue(r.isOK(), "[social_misc] Match contacts OK"));
	}

}
