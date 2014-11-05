package com.misfit.ta.ios.modelapi.social.friend;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.social.SearchFriendView;
import com.misfit.ta.gui.social.SocialProfileView;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class SearchFriendsAPI extends ModelAPI {

	protected static final Logger logger = Util.setupLogger(SearchFriendsAPI.class);

	private String emailMe = "ios_automation_search_friend@misfitqa.com";
	private String prefix = "ios_automation_1389337763336729000_";
	
	private String hMe = "ios_automation_1389337763336729000_ntgjeue";
	private String hFriend = "ios_automation_1389337763336729000_ffsdqnnj1r19r";
	private String hNew = "ios_automation_1389337763336729000_3rgruxt6l8n";
	private String hRequestedMe = "ios_khoahong";
	private String hRequestedByMe = "ios_automation_1389337763336729000_faakktn";
	private String hIgnoredMe = "ios_automation_1389337763336729000_weaznqwzdwalqj";
	private String hIgnoredByMe = "ios_automation_1389337763336729000_ptsbbni";
	private String currentHandle;

	public SearchFriendsAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	
	private void searchFriends(String keyword) {

		currentHandle = keyword;
		SearchFriendView.tapSearchField();
		SearchFriendView.inputKeyWordAndSearch(keyword);
		SearchFriendView.waitUntilSearchFinish();
	}
	
	

	public void e_init() {
		
		/*
		 * Scenerio:
		 * - Check request from status
		 * - Search invalid keyword
		 * - Search non existed user
		 * - Search yourself
		 * - Search your friend
		 * - Search user that requested you
		 * - Search user that you requested
		 * - Search user that ignored you
		 * - Search user that you ignored
		 */
		
		// log in and go to search friend view
		PrometheusHelper.signIn(emailMe, "qwerty1");
		ShortcutsTyper.delayTime(1000);
		PrometheusHelper.handleUpdateFirmwarePopup();
		ShortcutsTyper.delayTime(1000);
		HomeScreen.tapWordView();
		ShortcutsTyper.delayTime(1000);
		HomeScreen.tapSocialProfile();
		ShortcutsTyper.delayTime(1000);
		SocialProfileView.tapSearchFriend();
		PrometheusHelper.waitForThrobberToDissappear();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.SyncingLabel);
	}

	public void e_searchInvalidKeyword() {

		searchFriends(TextTool.getRandomString(1, 2));
	}

	public void e_searchNonExistedUser() {
	
		searchFriends(prefix + System.nanoTime());
	}
	
	public void e_searchYourself() {

		searchFriends(hMe);
	}

	public void e_searchYourFriend() {

		searchFriends(hFriend);
	}

	public void e_searchNewFriend() {

		searchFriends(hNew);
	}

	public void e_searchFriendWhoRequestedYou() {

		searchFriends(hRequestedMe);
	}

	public void e_searchFriendWhoYouIgnored() {

		searchFriends(hIgnoredByMe);
	}

	public void e_searchFriendWhoYouRequested() {

		searchFriends(hRequestedByMe);
	}

	public void e_searchFriendWhoIgnoredYou() {

		searchFriends(hIgnoredMe);
	}

	public void e_searchMixResult() {

		searchFriends(prefix);
	}

	
	
	public void v_SearchFriendInitial() {
		
		PrometheusHelper.waitForView("UILabel", DefaultStrings.SearchFriendTitle);
		Assert.assertTrue(SearchFriendView.isSearchFriendView(), "Current view is search friend view");
		Assert.assertTrue(SearchFriendView.hasPendingStatus(hRequestedByMe), hRequestedByMe + " status: PENDING");
		Assert.assertTrue(SearchFriendView.hasPendingStatus(hIgnoredMe), hIgnoredMe + " status: PENDING");
	}
	
	public void v_SearchFriend() {
		
		// just a dummy node for graph
	}
	
	public void v_NoResult() {
		
		Assert.assertTrue(SearchFriendView.isNoResult(), "Current view is search friend - no result");
	}
	
	public void v_InvalidParam() {
		
		// TODO: wait for bug fix
		SearchFriendView.tapCancelAlert();
	}
	
	public void v_NewFriendResult() {
		
		Assert.assertTrue(SearchFriendView.hasAddButton(currentHandle), currentHandle + " status: ADD");
	}
	
	public void v_PendingResult() {
		
		Assert.assertTrue(SearchFriendView.hasPendingStatus(currentHandle), currentHandle + " status: PENDING");
	}
	
	public void v_MixResult() {
		
		Assert.assertTrue(SearchFriendView.hasAddButton(hNew), hNew + " status: ADD");
		Assert.assertTrue(SearchFriendView.hasAddButton(hIgnoredByMe), hIgnoredByMe + " status: ADD");
		Assert.assertTrue(SearchFriendView.hasPendingStatus(hRequestedByMe), hRequestedByMe + " status: PENDING");
		Assert.assertTrue(SearchFriendView.hasPendingStatus(hIgnoredMe), hIgnoredMe + " status: PENDING");
	}

}
