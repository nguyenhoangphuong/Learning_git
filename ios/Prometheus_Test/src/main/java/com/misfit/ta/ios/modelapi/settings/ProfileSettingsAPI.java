package com.misfit.ta.ios.modelapi.settings;

import java.io.File;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.gui.*;
import com.misfit.ta.ios.AutomationTest;

public class ProfileSettingsAPI extends ModelAPI {
	private static final Logger logger = Util.setupLogger(ProfileSettingsAPI.class);
	public ProfileSettingsAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	

	boolean isMale = true;
	boolean isUSUnit = true;
	String h1 = "", h2 = "";
	String w1 = "", w2 = "";
	String year = "", month = "", day = "";

	
	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
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
	}
	
	/**
	 * This method implements the Edge 'e_ChooseProfile'
	 * 
	 */
	public void e_ChooseProfile() {
		HomeSettings.tapYourProfile();
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_ChooseSettings'
	 * 
	 */
	public void e_ChooseSettings() {
		HomeScreen.tapSettings();
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_DoneEditting'
	 * 
	 */
	public void e_DoneEditting() {
		HomeSettings.tapBackAtEditProfile();
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_EditBirthDate'
	 * 
	 */
	public void e_EditBirthDate() {
		this.year = PrometheusHelper.randInt(1970, 2000) + "";
		this.month = PrometheusHelper.getMonthString(PrometheusHelper.randInt(1, 13), true);
		this.day = PrometheusHelper.randInt(1, 29) + "";
		HomeSettings.updateBirthDay(year, month, day);
		
		logger.info("Change birthday to: " + PrometheusHelper.formatBirthday(year, month, day));
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_EditGender'
	 * 
	 */
	public void e_EditGender() {
		this.isMale = PrometheusHelper.coin();
		HomeSettings.updateGender(this.isMale);
		
		logger.info("Change male to: " + (isMale ? "Male" : "Female"));
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_EditHeight'
	 * 
	 */
	public void e_EditHeight() {
		this.isUSUnit = PrometheusHelper.coin();
		this.h1 = this.isUSUnit ? PrometheusHelper.randInt(4, 8) + "'": PrometheusHelper.randInt(1, 3) + "";
		this.h2 = this.isUSUnit ? PrometheusHelper.randInt(0, 12) + "\\\"": "." + String.format("%2d", PrometheusHelper.randInt(50, 100));
		HomeSettings.updateHeight(h1, h2, isUSUnit);
		
		logger.info("Change height to: " + h1 + h2 + (isUSUnit? "" : " m"));
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_EditWeight'
	 * 
	 */
	public void e_EditWeight() {
		this.isUSUnit = PrometheusHelper.coin();
		this.w1 = this.isUSUnit ? PrometheusHelper.randInt(80, 260) + "": PrometheusHelper.randInt(40, 120) + "";
		this.w2 = "." + PrometheusHelper.randInt(0, 10);
		HomeSettings.updateWeight(w1, w2, isUSUnit);
		
		logger.info("Change weight to: " + w1 + w2 + (isUSUnit? " lbs" : " kg"));
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_PressBack'
	 * 
	 */
	public void e_PressBack() {
		HomeSettings.tapBackAtSettings();
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		// check if this is Homescreen
		Assert.assertTrue("Current screen is Homescreen", ViewUtils.isExistedView("UILabel", "TODAY"));
	}

	/**
	 * This method implements the Vertex 'v_ProfileView'
	 * 
	 */
	public void v_ProfileView() {
		// check if this is EditProfile screen
		Assert.assertTrue("Current screen is EditProfile", ViewUtils.isExistedView("UILabel", "USER INFO"));
	}

	/**
	 * This method implements the Vertex 'v_Settings'
	 * 
	 */
	public void v_Settings() {
		// check if this is EditProfile screen
		Assert.assertTrue("Current screen is Settings", ViewUtils.isExistedView("UILabel", "SETTINGS"));
		
		// check the string under your profile cell
		String profilePreview = PrometheusHelper.formatProfile(isMale, isUSUnit, h1, h2, w1, w2);
		Assert.assertTrue("The sort preview of profile is " + profilePreview, ViewUtils.isExistedView("UILabel", profilePreview));
	}

	/**
	 * This method implements the Vertex 'v_UpdatedProfile'
	 * 
	 */
	public void v_UpdatedProfile() {
		// check birthday / height / weight / gender is updated
		String birthday = PrometheusHelper.formatBirthday(year, month, day);
		String weight = w1 + w2 + (isUSUnit ? " lbs" : " kg");
		String height = h1 + h2 + (isUSUnit ? "" : " m");
		
		Assert.assertTrue("Birthday should be " + birthday, ViewUtils.isExistedView("UILabel", birthday));
		Assert.assertTrue("Weight should be " + weight, ViewUtils.isExistedView("UILabel", weight));
		Assert.assertTrue("Height should be " + height, ViewUtils.isExistedView("UILabel", height));
		
		// Note: we cannot check gender because
		// there's nothing that tell us which button is actived
		
	}

}
