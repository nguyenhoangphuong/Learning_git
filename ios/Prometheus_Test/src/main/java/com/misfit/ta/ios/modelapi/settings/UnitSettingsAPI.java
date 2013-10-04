package com.misfit.ta.ios.modelapi.settings;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.gui.DefaultStrings;
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

	private boolean isDistanceUSUnit = true;
	private boolean isWeightUSUnit = true;
	
	private String heightDefaultInUS = "5'8\\\"";
	private String heightDefaultInSI = "1.73 m";
	private String weightDefaultInUS = "120.0 lbs";
	private String weightDefaultInSI = "54.4 kg";
	
	public void e_Init() {
		
		// sign up account with default profile
//		PrometheusHelper.signUp();
//		PrometheusHelper.inputRandomRecord();
//		HomeScreen.tapProgressCircle();
	}
	
	public void e_ChooseSettings() {
		
		HomeScreen.tapOpenSettingsTray();
		HomeScreen.tapSettings();
	}
	
	public void e_ChangeProfileDistanceUnit() {
		
		Gui.touchAVIew("UIButton", isDistanceUSUnit ? DefaultStrings.KmLabel : DefaultStrings.MileLabel);
		isDistanceUSUnit = !isDistanceUSUnit;
		
		System.out.println("Change Profile Distance Unit to: " + isDistanceUSUnit);
		HomeSettings.tapYourProfile();
	}

	public void e_ChangeProfileWeightUnit() {
		
		Gui.touchAVIew("UIButton", isWeightUSUnit ? DefaultStrings.KgLabel : DefaultStrings.LbsLabel);
		isWeightUSUnit = !isWeightUSUnit;
		
		System.out.println("Change Profile Weight Unit to: " + isWeightUSUnit);
		HomeSettings.tapYourProfile();
	}

	public void e_ChangeSettingsDistanceUnit() {
		
		Gui.touchAVIew("PTHeightPickerControl", 0);
		Gui.setPicker(2, !isDistanceUSUnit ? DefaultStrings.InchesLabel : DefaultStrings.MetreLabel);
		isDistanceUSUnit = !isDistanceUSUnit;
		
		System.out.println("Change Settings Distance Unit to: " + isDistanceUSUnit);
		HomeSettings.tapBack();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.EditProfileTitle);
	}

	public void e_ChangeSettingsWeightUnit() {
		
		Gui.touchAVIew("PTWeightPickerControl", 0);
		Gui.setPicker(2, !isWeightUSUnit ? DefaultStrings.LbsLabel : DefaultStrings.KgLabel);
		isWeightUSUnit = !isWeightUSUnit;
		
		System.out.println("Change Settings Weight Unit to: " + isWeightUSUnit);
		HomeSettings.tapBack();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.EditProfileTitle);
	}

	public void e_PressBack() {
		
		HomeSettings.tapBack();
	}


	
	public void v_HomeScreen() {
			
		String distanceSummary = Gui.getProperty("PTRichTextLabel", 2, "text");
		Assert.assertTrue(HomeScreen.isToday(), "Current screen is Homescreen");
		Assert.assertTrue(distanceSummary.contains(isDistanceUSUnit ? "miles" : "km"),
				"Distance summary display in correct unit");
	}

	public void v_ProfileView() {
		
		String weight = isWeightUSUnit ? weightDefaultInUS : weightDefaultInSI;
		String height = isDistanceUSUnit ? heightDefaultInUS : heightDefaultInSI;

		Assert.assertTrue(ViewUtils.isExistedView("UILabel", height),
				"Height should be " + height);
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", weight),
				"Weight should be " + weight);
	}

	public void v_SettingsView() {
		
		boolean currentDistanceUSUnit = Gui.getProperty("UIButton", DefaultStrings.MileLabel,
				"isSelected").equals("1") ? true : false;
		boolean currentWeightUSUnit = Gui.getProperty("UIButton", DefaultStrings.LbsLabel,
				"isSelected").equals("1") ? true : false;
		
		Assert.assertTrue(isDistanceUSUnit == currentDistanceUSUnit,
				"Distance Unit is correct");
		Assert.assertTrue(isWeightUSUnit == currentWeightUSUnit,
				"Weight Unit is correct");
	}

}
