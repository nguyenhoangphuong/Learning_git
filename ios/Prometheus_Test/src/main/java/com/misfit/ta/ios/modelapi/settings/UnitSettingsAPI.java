package com.misfit.ta.ios.modelapi.settings;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
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

	private String heightDefaultInUS = "5'4\\\"";
	private String heightDefaultInSI = "1.63 m";
	private String weightDefaultInUS = "140.0 lbs";
	private String weightDefaultInSI = "63.5 kg";

	public void e_Init() {

		// sign up account with default profile
		PrometheusHelper.signUpDefaultProfile();

		// input a record
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(new String[] { "1", "00", "am" }, 5,
				500);
		HomeScreen.tapSave();
		ShortcutsTyper.delayOne();
		HomeScreen.tapProgressCircle();
	}

	public void e_ChooseSettings() {

		HomeScreen.tapOpenSettingsTray();
		HomeScreen.tapSettings();
		ShortcutsTyper.delayOne();
	}

	public void e_ChangeProfileDistanceUnit() {

		isDistanceUSUnit = !isDistanceUSUnit;
		if (isDistanceUSUnit)
			HomeSettings.tapMile();
		else
			HomeSettings.tapKm();

		HomeSettings.tapYourProfile();
	}

	public void e_ChangeProfileWeightUnit() {

		isWeightUSUnit = !isWeightUSUnit;
		if (isWeightUSUnit)
			HomeSettings.tapLbs();
		else
			HomeSettings.tapKg();

		HomeSettings.tapYourProfile();
	}

	public void e_ChangeSettingsDistanceUnit() {

		isDistanceUSUnit = !isDistanceUSUnit;
		HomeSettings.updateHeight(null, null, isDistanceUSUnit);

		HomeSettings.tapBack();
		PrometheusHelper.waitForViewToDissappear("UILabel",
				DefaultStrings.EditProfileTitle);
	}

	public void e_ChangeSettingsWeightUnit() {

		isWeightUSUnit = !isWeightUSUnit;
		HomeSettings.updateWeight(null, null, isWeightUSUnit);

		HomeSettings.tapBack();
		PrometheusHelper.waitForViewToDissappear("UILabel",
				DefaultStrings.EditProfileTitle);
	}

	public void e_PressBack() {

		HomeSettings.tapBack();
		HomeScreen.tapOpenSettingsTray();
	}

	public void v_HomeScreen() {

		String distanceSummary = Gui.getProperty("PTRichTextLabel", 2, "text");
		Assert.assertTrue(HomeScreen.isToday(), "Current screen is Homescreen");
		Assert.assertTrue(
				distanceSummary.contains(isDistanceUSUnit ? "miles" : "km"),
				"Distance summary display in correct unit");
	}

	public void v_ProfileView() {
		ShortcutsTyper.delayTime(3000);
		String weight = isWeightUSUnit ? weightDefaultInUS : weightDefaultInSI;
		String height = isDistanceUSUnit ? heightDefaultInUS
				: heightDefaultInSI;

		Assert.assertTrue(ViewUtils.isExistedView("UILabel", height),
				"Height should be " + height);
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", weight),
				"Weight should be " + weight);
	}

	public void v_SettingsView() {
		ShortcutsTyper.delayTime(3000);
		boolean currentDistanceUSUnit = Gui.getProperty("UIButton",
				DefaultStrings.MileLabel, "isSelected").equals("1") ? true
				: false;
		boolean currentWeightUSUnit = Gui.getProperty("UIButton",
				DefaultStrings.LbsLabel, "isSelected").equals("1") ? true
				: false;

		Assert.assertTrue(isDistanceUSUnit == currentDistanceUSUnit,
				"Distance Unit is correct");
		Assert.assertTrue(isWeightUSUnit == currentWeightUSUnit,
				"Weight Unit is correct");
	}

}
