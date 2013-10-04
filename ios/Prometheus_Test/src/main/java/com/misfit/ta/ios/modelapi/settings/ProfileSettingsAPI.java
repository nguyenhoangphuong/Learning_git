package com.misfit.ta.ios.modelapi.settings;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.gui.*;
import com.misfit.ta.ios.AutomationTest;

public class ProfileSettingsAPI extends ModelAPI {
	private static final Logger logger = Util.setupLogger(ProfileSettingsAPI.class);
	public ProfileSettingsAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	

	private boolean isMale = true;
	private boolean isUSUnit = true;
	private String h1 = ""; 
	private String h2 = "";
	private String w1 = ""; 
	private String w2 = "";
	private String year = "";
	private String month = "";
	private String day = "";
	
	private boolean heightUseUSUnit = true;
	private boolean weightUseUSUnit = true;


	public void e_Init() {
		
		// THIS MODEL REQUIRE SIGN UP A NEW ACCOUNT
		// THAT HAVE THE FOLLOWING PROFILE
		this.isMale = true;
		this.isUSUnit = true;
		this.h1 = "5'";
		this.h2 = "8\\\"";
		this.w1 = "120";
		this.w2 = ".0";
		this.year = "1991";
		this.month = PrometheusHelper.getMonthString(9, true);
		this.day = "16";
		
		// sign up account with require information
		PrometheusHelper.signUp(MVPApi.generateUniqueEmail(), "qwerty1", isMale, 16, 9, 1991, isUSUnit, h1, h2, w1, w2, 1);
	}

	public void e_ToProfile() {
		
		HomeScreen.tapOpenSettingsTray();
		HomeScreen.tapSettings();
		HomeSettings.tapYourProfile();
	}

	public void e_DoneEditting() {
		
		HomeSettings.tapBack();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.EditProfileTitle);
		HomeSettings.tapBack();
	}

	public void e_EditBirthDate() {
		
		this.year = PrometheusHelper.randInt(1970, 2000) + "";
		this.month = PrometheusHelper.getMonthString(PrometheusHelper.randInt(1, 13), true);
		this.day = PrometheusHelper.randInt(1, 29) + "";
		HomeSettings.updateBirthDay(year, month, day);
		
		logger.info("Change birthday to: " + PrometheusHelper.formatBirthday(year, month, day));
	}

	public void e_EditGender() {
		
		this.isMale = PrometheusHelper.coin();
		HomeSettings.updateGender(this.isMale);
		
		logger.info("Change male to: " + (isMale ? DefaultStrings.MaleLabel : DefaultStrings.FemaleLabel));
	}

	public void e_EditHeight() {
		
		this.heightUseUSUnit = PrometheusHelper.coin();
		this.h1 = this.heightUseUSUnit ? PrometheusHelper.randInt(4, 8) + "'": "1";
		this.h2 = this.heightUseUSUnit ? PrometheusHelper.randInt(0, 12) + "\\\"": "." + String.format("%2d", PrometheusHelper.randInt(50, 100));
		HomeSettings.updateHeight(h1, h2, heightUseUSUnit);
		
		logger.info("Change height to: " + h1 + h2 + (heightUseUSUnit? "" : " m"));
	}

	public void e_EditWeight() {
		
		this.weightUseUSUnit = PrometheusHelper.coin();
		this.w1 = this.weightUseUSUnit ? PrometheusHelper.randInt(80, 260) + "": PrometheusHelper.randInt(40, 120) + "";
		this.w2 = "." + PrometheusHelper.randInt(0, 10);
		HomeSettings.updateWeight(w1, w2, weightUseUSUnit);
		
		logger.info("Change weight to: " + w1 + w2 + (weightUseUSUnit? " lbs" : " kg"));
	}
		
	

	public void v_HomeScreen() {

		Assert.assertTrue(HomeScreen.isToday(), "Current screen is Homescreen");
	}

	public void v_ProfileView() {

		Assert.assertTrue(HomeSettings.isAtEditProfile(), "Current screen is EditProfile");
	}

	public void v_Settings() {

		Assert.assertTrue(HomeSettings.isAtSettings(), "Current screen is Settings");
	}

	public void v_UpdatedProfile() {
		
		// check gender / birthday / height / weight / gender is updated
		boolean male = Gui.getProperty("UIButton", DefaultStrings.MaleLabel, "isSelected").equals("1") ? true : false;
		String birthday = PrometheusHelper.formatBirthday(year, month, day);
		String weight = w1 + w2 + (weightUseUSUnit ? " lbs" : " kg");
		String height = h1 + h2 + (heightUseUSUnit ? "" : " m");
		
		Assert.assertTrue(isMale == male, "Gender should be " + (isMale ? DefaultStrings.MaleLabel : DefaultStrings.FemaleLabel));
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", birthday), "Birthday should be " + birthday);
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", weight), "Weight should be " + weight);
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", height), "Height should be " + height);
		
	}

}
