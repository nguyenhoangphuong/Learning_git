package com.misfit.ta.ios.modelapi.social.socialprofile;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.social.LeaderboardView;
import com.misfit.ta.gui.social.SocialProfileView;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class EditSocialProfileAPI extends ModelAPI {
	protected static final Logger logger = Util
			.setupLogger(EditSocialProfileAPI.class);

	public EditSocialProfileAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private String name;
	private String handle = "wjop4j1388482447561228000";
	
	
	public void e_init() {
		
		// sign up
		String email = PrometheusHelper.signUpDefaultProfile();
		
		// create social account use api
		String token = MVPApi.signIn(email, "qwerty1").token;
		
		ProfileData profile = new ProfileData();
		handle = TextTool.getRandomString(4, 5).toLowerCase() + System.nanoTime();
		profile.setHandle(handle);
		profile.setName(TextTool.getRandomString(5, 10));
		
		MVPApi.updateProfile(token, profile);
		
		// sign out and sign in again
		PrometheusHelper.signOut();
		PrometheusHelper.signIn(email, "qwerty1");
		ShortcutsTyper.delayTime(2000);
		// set up gui
		PrometheusHelper.handleUpdateFirmwarePopup();
		HomeScreen.tapMenuSocial();
		PrometheusHelper.waitForView("UIButtonLabel", DefaultStrings.OKGotItButton);
		LeaderboardView.tapGotIt();
		PrometheusHelper.handleUpdateFirmwarePopup();
		HomeScreen.tapProfileSocial();
	}
	
	public void e_tapEdit() {
		
		SocialProfileView.tapEdit();
		ShortcutsTyper.delayOne();
	}
	
	public void e_cancelEdit() {
		
		SocialProfileView.tapCancel();
	}
	
	public void e_inputEmptyName() {
		
		SocialProfileView.inputName("");
		SocialProfileView.tapDone();
		PrometheusHelper.waitForAlert();
	}
	
	public void e_confirmAlert() {
		
		SocialProfileView.tapOKAlert();
	}
	
	public void e_updateNewName() {
		
		// input new name
		name = TextTool.getRandomString(5, 10);
		SocialProfileView.inputName(name);
		
		// submit
		SocialProfileView.tapDone();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.DoneButton);
	}
	
	public void e_updateNewAvatarByTakingPhoto() {
		
		// input new avatar
		SocialProfileView.tapEditAvatarButton();
		SocialProfileView.tapTakePhoto();
		ShortcutsTyper.delayTime(5000);
		
		if(Gui.hasAlert()) {
			Gui.touchPopupButton("OK");
		}
		
		SocialProfileView.tapCapturePhoto();
		ShortcutsTyper.delayTime(2000);
		SocialProfileView.tapUsePhoto();

		// submit
		SocialProfileView.tapDone();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.DoneButton);
	}
	
	public void e_updateNewAvatarByChoosingCameraPhoto() {
				
		// input new avatar by choosing from camera roll
		SocialProfileView.tapEditAvatarButton();
		SocialProfileView.tapChooseFromLibrary();
		
		ShortcutsTyper.delayTime(3000);
		if(Gui.hasAlert()) {
			Gui.touchPopupButton("OK");
		}
		
		SocialProfileView.tapCameraRoll(); 
		ShortcutsTyper.delayOne();
		Gui.swipeDown(500);
    	SocialProfileView.tapAlbumImage(0);
		ShortcutsTyper.delayOne();
		SocialProfileView.tapChoosePhoto(); 
		ShortcutsTyper.delayOne();

		// submit
		SocialProfileView.tapDone();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.DoneButton);
	}
	
	public void e_goToLeaderboard() {
		
		SocialProfileView.tapBack();
		PrometheusHelper.handleUpdateFirmwarePopup();
		HomeScreen.goToTodayViewOfLeaderboard();
	}
	
	public void e_goToSocialProfile() {
		
		HomeScreen.tapSocialProfile();
	}
	
	
	
	public void v_SocialProfile() {
		
		Assert.assertTrue(SocialProfileView.isSocialProfileView(), "Current view is SocialProfile");
	}
	
	public void v_SocialProfileEditMode() {
		
		Assert.assertTrue(SocialProfileView.isInEditMode(), "Current view is SocialProfile - Edit mode");
	}
	
	public void v_EmptyNameAlert() {
		
		Assert.assertTrue(SocialProfileView.hasEmptyNameAlert(), "Empty name alert is shown");
	}
	
	public void v_NewSocialProfile() {
		
		Assert.assertTrue(!SocialProfileView.isInEditMode(), "Current view is SocialProfile - None edit mode");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", name), "New name is saved correctly");
	}
	
	public void v_Leaderboard() {
		
		Assert.assertTrue(LeaderboardView.isDefaultView(), "Current view is leaderboard");
		
		LeaderboardView.tapUserWithHandle(handle);
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", name), "New name is updated correctly on leaderboard");
		LeaderboardView.tapToCloseCurrentUser();
	}
}