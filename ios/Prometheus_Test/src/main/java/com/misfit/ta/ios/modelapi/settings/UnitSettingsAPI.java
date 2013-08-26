package com.misfit.ta.ios.modelapi.settings;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.ios.AutomationTest;

public class UnitSettingsAPI extends ModelAPI {
	public UnitSettingsAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private boolean isMale = true;
	private boolean isDistanceUSUnit = true;
	private boolean isWeightUSUnit = true;

	private String metricsH1 = "";
	private String metricsH2 = "";
	private String metricsW1 = "";
	private String metricsW2 = "";

	private String h1 = "";
	private String h2 = "";
	private String w1 = "";
	private String w2 = "";

	/**
	 * This method implements the Edge 'e_ChangeProfileDistanceUnit'
	 * 
	 */
	public void e_ChangeProfileDistanceUnit() {
		if (isDistanceUSUnit) {
			Gui.touchAVIew("UIButton", "km");
			isDistanceUSUnit = false;
		} else {
			Gui.touchAVIew("UIButton", "mi");
			isDistanceUSUnit = true;
		}
		System.out.println("Change Profile Distance Unit");
		System.out.println(isDistanceUSUnit);
		HomeSettings.tapYourProfile();
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_ChangeProfileWeightUnit'
	 * 
	 */
	public void e_ChangeProfileWeightUnit() {
		if (isWeightUSUnit) {
			Gui.touchAVIew("UIButton", "kg");
			isWeightUSUnit = false;
		} else {
			Gui.touchAVIew("UIButton", "lbs");
			isWeightUSUnit = true;
		}
		System.out.println("Change Profile Weight Unit");
		System.out.println(isWeightUSUnit);
		HomeSettings.tapYourProfile();
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_ChangeSettingsDistanceUnit'
	 * 
	 */
	public void e_ChangeSettingsDistanceUnit() {
		Gui.touchAVIew("PTHeightPickerControl", 0);
		ShortcutsTyper.delayTime(200);
		Gui.setPicker(2, !isDistanceUSUnit ? "ft in" : "m");
		isDistanceUSUnit = !isDistanceUSUnit;
		System.out.println("Change Settings Distance Unit");
		System.out.println(isDistanceUSUnit);
		HomeSettings.tapBack();
		ShortcutsTyper.delayTime(2000);
	}

	/**
	 * This method implements the Edge 'e_ChangeSettingsWeightUnit'
	 * 
	 */
	public void e_ChangeSettingsWeightUnit() {
		Gui.touchAVIew("PTWeightPickerControl", 0);
		ShortcutsTyper.delayTime(200);
		Gui.setPicker(2, !isWeightUSUnit ? "lbs" : "kg");
		isWeightUSUnit = !isWeightUSUnit;
		System.out.println("Change Settings Weight Unit");
		System.out.println(isWeightUSUnit);
		HomeSettings.tapBack();
		ShortcutsTyper.delayTime(2000);
	}

	/**
	 * This method implements the Edge 'e_ChooseSettings'
	 * 
	 */
	public void e_ChooseSettings() {
		HomeScreen.tapOpenSettingsTray();
		ShortcutsTyper.delayTime(500);
		HomeScreen.tapSettings();
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		// THIS MODEL REQUIRE SIGN UP A NEW ACCOUNT
		// THAT HAVE THE FOLLOWING PROFILE

		this.isMale = true;
		this.h1 = "5'";
		this.h2 = "8\\\"";
		this.w1 = "120";
		this.w2 = ".0";

		this.metricsH1 = "1";
		this.metricsH2 = ".73";
		this.metricsW1 = "54";
		this.metricsW2 = ".4";

		this.year = "1991";
		this.month = PrometheusHelper.getMonthString(9, true);
		this.day = "16";

		// sign up account with require information
		PrometheusHelper.signUp(MVPApi.generateUniqueEmail(), "qwerty1",
				isMale, 16, 9, 1991, true, h1, h2, w1, w2, 1);
		ShortcutsTyper.delayTime(5000);
	}

	/**
	 * This method implements the Edge 'e_PressBack'
	 * 
	 */
	public void e_PressBack() {
		HomeSettings.tapBack();
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		Assert.assertTrue(HomeScreen.isToday(), "Current screen is Homescreen");
	}

	/**
	 * This method implements the Vertex 'v_ProfileView'
	 * 
	 */
	public void v_ProfileView() {
		String weight = "";
		String height = "";
		if (isDistanceUSUnit) {
			height = h1 + h2;
		} else {
			height = metricsH1 + metricsH2 + " m";
		}

		if (isWeightUSUnit) {
			weight = w1 + w2 + " lbs";
		} else {
			weight = metricsW1 + metricsW2 + " kg";
		}
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", height),
				"Height should be " + height);
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", weight),
				"Weight should be " + weight);
	}

	/**
	 * This method implements the Vertex 'v_SettingsView'
	 * 
	 */
	public void v_SettingsView() {
		boolean currentDistanceUSUnit = Gui.getProperty("UIButton", "mi",
				"isSelected").equals("1") ? true : false;
		boolean currentWeightUSUnit = Gui.getProperty("UIButton", "lbs",
				"isSelected").equals("1") ? true : false;
		Assert.assertTrue(isDistanceUSUnit == currentDistanceUSUnit,
				"Distance Unit is correct");
		Assert.assertTrue(isWeightUSUnit == currentWeightUSUnit,
				"Weight Unit is correct");
	}

}
