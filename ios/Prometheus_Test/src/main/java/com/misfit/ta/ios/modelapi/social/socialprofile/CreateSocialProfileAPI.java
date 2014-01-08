package com.misfit.ta.ios.modelapi.social.socialprofile;

import java.io.File;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.social.LeaderboardView;
import com.misfit.ta.gui.social.SocialProfileView;
import com.misfit.ta.gui.social.WorldFeedView;
import com.misfit.ta.ios.AutomationTest;

public class CreateSocialProfileAPI extends ModelAPI {
	protected static final Logger logger = Util
			.setupLogger(CreateSocialProfileAPI.class);

	public CreateSocialProfileAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private String name = "Social " + System.nanoTime();
	private String handle = TextTool.getRandomString(5, 8).toLowerCase() + System.nanoTime();
	
	
	
	public void e_init() {
		
		PrometheusHelper.signUpDefaultProfile();
	}
	
	public void e_goToLeaderboard() {
		
		HomeScreen.tapLeaderboard();
	}
	
	public void e_goToWorldView() {
		
		HomeScreen.tapWordView();
	}
	
	public void e_tapDontHaveFacebook() {
		
		WorldFeedView.tapDontHaveFacebook();
		PrometheusHelper.waitForView("UILabel", DefaultStrings.ProfilePreviewViewTitle);
	}
	
	public void e_inputEmptyAvatar() {
		
		SocialProfileView.inputName("Valid Name");
		SocialProfileView.inputHandle("validhandle" + System.nanoTime());
		SocialProfileView.tapDone();
		PrometheusHelper.waitForAlert();
	}
	
	public void e_addAvatar() {
		
		SocialProfileView.tapAddPhotoAlert();
		ShortcutsTyper.delayOne();
		SocialProfileView.tapTakePhoto();
		ShortcutsTyper.delayTime(5000);
		SocialProfileView.tapCapturePhoto();
		ShortcutsTyper.delayTime(2000);
		SocialProfileView.tapUsePhoto();
	}
	
	public void e_inputEmptyName() {
		
		SocialProfileView.inputName("");
		SocialProfileView.inputHandle("validhandle" + System.nanoTime());
		SocialProfileView.tapDone();
		PrometheusHelper.waitForAlert();
	}
	
	public void e_inputEmptyHandle() {

		SocialProfileView.inputName("Valid Name");
		SocialProfileView.inputHandle("");
		SocialProfileView.tapDone();
		PrometheusHelper.waitForAlert();
	}

	public void e_inputShortHandle() {

		SocialProfileView.inputName("Valid Name");
		SocialProfileView.inputHandle(TextTool.getRandomString(1, 5));
		SocialProfileView.tapDone();
		PrometheusHelper.waitForAlert();
	}
	
	public void e_inputInvalidHandle() {

		SocialProfileView.inputName("Valid Name");
		SocialProfileView.inputHandle(TextTool.getRandomString(1, 5) + System.nanoTime() + "##");
		SocialProfileView.tapDone();
		PrometheusHelper.waitForAlert();
	}
	
	public void e_inputDuplicatedHandle() {
		
		SocialProfileView.inputName("Valid Name");
		SocialProfileView.inputHandle("sociala");
		SocialProfileView.tapDone();
		PrometheusHelper.waitForAlert();
	}
	
	public void e_confirmAlert() {
		
		SocialProfileView.tapOKAlert();
	}
	
	public void e_submitValidProfile() {
		
		SocialProfileView.inputName(name);
		SocialProfileView.inputHandle(handle);
		SocialProfileView.tapDone();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.ProfilePreviewViewTitle);
	}
	
	public void e_tapGotIt() {
		
		LeaderboardView.tapGotIt();
	}
	
	public void e_goToSocialProfile() {
		
		HomeScreen.tapSocialProfile();
	}
	
	
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isHomeScreen(), "Current view is HomeScreen");
	}
	
	public void v_LeaderBoardWelcome() {
		
		Assert.assertTrue(LeaderboardView.isWelcomeView(), "Current view is Leaderboard - Welcome screen");
	}
	
	public void v_WorldViewWelcome() {
		
		Assert.assertTrue(WorldFeedView.isWelcomeView(), "Current view is WorldFeed - Welcome screen");
	}
	
	public void v_SocialProfileRegister() {
		
		Assert.assertTrue(SocialProfileView.isProfileReviewView(), "Current view is SocialProfile - Preview screen");
	}
	
	public void v_EmptyAvatar() {
		
		Assert.assertTrue(SocialProfileView.hasEmptyAvatarAlert(), "Empty avatar alert is shown");
	}
	
	public void v_EmptyName() {
		
		Assert.assertTrue(SocialProfileView.hasEmptyNameAlert(), "Empty name alert is shown");
	}
	
	public void v_EmptyHandle() {

		Assert.assertTrue(SocialProfileView.hasEmptyHandleAlert(), "Empty handle alert is shown");
	}
	
	public void v_DuplicatedHandle() {

		Assert.assertTrue(SocialProfileView.hasDuplicatedHandleAlert(), "Duplicated handle alert is shown");
	}
	
	public void v_InvalidHandle() {

		Assert.assertTrue(SocialProfileView.hasInvalidHandleAlert(), "Invalid handle alert is shown");
	}
		
	public void v_WorldView() {
	
		Assert.assertTrue(WorldFeedView.isWorldViewDefault(), "Current view is World Feed with records");
		Assert.assertTrue(WorldFeedView.hasOptionPanel(), "Include option is prompted");
	}

	public void v_LeaderboardNoFriend() {
		
		Assert.assertTrue(LeaderboardView.isNoFriendView(), "Current view is Leaderboard - No friend screen");
	}
	
	public void v_Leaderboard() {
		
		Assert.assertTrue(LeaderboardView.isDefaultView(), "Current view is Leaderboard - Default screen");
	}
	
	public void v_SocialProfile() {
		
		Assert.assertTrue(SocialProfileView.isSocialProfileView(), "Current view is SocialProfile - Profile screen");
		
		// check name / handle / joined date
		Calendar cal = Calendar.getInstance();
		String monthString = PrometheusHelper.getMonthString(cal.get(Calendar.MONTH) + 1, false);
		String joinDate = "Joined " + PrometheusHelper.formatBirthday(cal.get(Calendar.YEAR) + "", monthString, cal.get(Calendar.DATE) + "");
		
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", name), "Name is saved correctly");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", handle), "Handle is saved correctly");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", joinDate), "Join date is saved correctly");
	}

}
