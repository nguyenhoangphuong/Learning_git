package com.misfit.ta.android.modelapi.settings;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.Gui;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.gui.HomeScreen;
import com.misfit.ta.android.gui.PrometheusHelper;
import com.misfit.ta.android.gui.Settings;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class ProfileSettingsAPI extends ModelAPI {
	public ProfileSettingsAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private Field latestUpdatedField;
	private boolean checkAll = false;
	private int fullScreenHeight;
	private int fullScreenWidth;

	private enum Field {
		EMAIL, BIRTHDATE, HEIGHT, WEIGHT, GENDER // ; is optional
	}

	private boolean isMale = true;
	private boolean isUSUnit = true;
	private int h1 = 0; 
	private int h2 = 0;
	private int w1 = 0; 
	private int w2 = 0;
	private String year = "";
	private String month = "";
	private String day = "";
	/**
	 * This method implements the Edge 'e_ChooseSettings'
	 * 
	 */
	public void e_ChooseMyProfile() {
		fullScreenHeight = Gui.getScreenHeight();
		fullScreenWidth = Gui.getScreenWidth();
		System.out.println(fullScreenHeight);
		System.out.println(fullScreenWidth);
		HomeScreen.openDashboardMenu(fullScreenHeight, fullScreenWidth);
		Settings.tapMyProfile();
		ShortcutsTyper.delayTime(2000);
	}

	/**
	 * This method implements the Edge 'e_EditBirthDate'
	 * 
	 */
	public void e_EditBirthDate() {
		Gui.touchAView("TextView", "mID", DefaultStrings.BirthdayTextViewId);
		
		this.year = String.valueOf(PrometheusHelper.randInt(1970, 2000));
		this.month = PrometheusHelper.getMonthString(PrometheusHelper.randInt(1, 13), false);
		this.day = String.valueOf(PrometheusHelper.randInt(1, 29));
		PrometheusHelper.editBirthDate(day, month, year, fullScreenHeight, fullScreenWidth);
		PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth,
				DefaultStrings.SetText);
		System.out.println("Change birthday to day : " + day + " month: " + month + "year: " + year);
		latestUpdatedField = Field.BIRTHDATE;
	}

	/**
	 * This method implements the Edge 'e_EditGender'
	 * 
	 */
	public void e_EditGender() {
		this.isMale = PrometheusHelper.coin();
		PrometheusHelper.editGender(isMale);
		latestUpdatedField = Field.GENDER;
	}

	/**
	 * This method implements the Edge 'e_EditHeight'
	 * 
	 */
	public void e_EditHeight() {
		this.h1 = PrometheusHelper.randInt(4, 8);
		this.h2 = PrometheusHelper.randInt(0, 12);
		Gui.touchAView("TextView", "mID", DefaultStrings.HeightTextViewId);
		System.out.println("Change height to digit: " + h1 + " fraction: " + h2);
		PrometheusHelper.editHeightInInches(h1, h2, fullScreenHeight, fullScreenWidth);
		PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth,
				DefaultStrings.SetText);
		latestUpdatedField = Field.HEIGHT;
	}

	/**
	 * This method implements the Edge 'e_EditWeight'
	 * 
	 */
	public void e_EditWeight() {
		this.w1 = PrometheusHelper.randInt(80, 260);
		this.w2 = PrometheusHelper.randInt(0, 10);
		Gui.touchAView("TextView", "mID", DefaultStrings.WeightTextViewId);
		System.out.println("Change weight to digit: " + w1 + " fraction: " + w2);
		PrometheusHelper.editWeightInLbs(w1, w2, fullScreenHeight, fullScreenWidth);
		PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth,
				DefaultStrings.SetText);
		latestUpdatedField = Field.WEIGHT;
	}

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		ShortcutsTyper.delayOne();
		PrometheusHelper.signUp();
		ShortcutsTyper.delayTime(2000);
	}

	/**
	 * This method implements the Edge 'e_Keep'
	 * 
	 */
	public void e_Keep() {
	}

	/**
	 * This method implements the Edge 'e_PressBack'
	 * 
	 */
	public void e_PressBack() {
		Gui.touchAView("TextView", "mText", DefaultStrings.MyProfileText);
		checkAll = true;
		ShortcutsTyper.delayTime(2000);
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_ProfileView'
	 * 
	 */
	public void v_ProfileView() {
		if (checkAll) {
			System.out.println("Check all!!!");
			assertGenderValue();
			assertHeightValue();
			assertBirthdateValue();
		}
	}

	/**
	 * This method implements the Vertex 'v_UpdateProfile'
	 * 
	 */
	public void v_UpdateProfile() {
		ShortcutsTyper.delayTime(2000);
		switch (latestUpdatedField) {
			case GENDER:
				System.out.println("Gender is updated!!!");
				assertGenderValue();
				break; 
			case HEIGHT:
				System.out.println("Height is updated!!!");
				assertHeightValue();
				break;
			case WEIGHT:
				System.out.println("Weight is updated!!!");
				assertWeightValue();
				break;
			case BIRTHDATE:
				System.out.println("Birthdate is updated!!!");
				assertBirthdateValue();
				break;
		}
	}

	private void assertHeightValue() {
		String heightText = Settings.getCurrentHeight();
		String expectedHeightText = String.valueOf(h1) + "\' " + String.valueOf(h2) + "\"";
		Assert.assertTrue(expectedHeightText.equals(heightText), "This is not a correct height value");
	}
	
	private void assertWeightValue() {
		String weightText = Settings.getCurrentWeight();
		String expectedWeightText = String.valueOf(w1);
		if (w2 != 0) {
			expectedWeightText +=  "." + String.valueOf(w2);
		}
		expectedWeightText += " lbs";
		Assert.assertTrue(expectedWeightText.equals(weightText), "This is not a correct weight value");
	}
	
	private void assertBirthdateValue() {
		String birthdateText = Settings.getCurrentBirthDate();
		String expectedBirthdateText = Integer.valueOf(day) <= 9 ? "0" + day : day;
		expectedBirthdateText += " " + month + ", " + year;
		Assert.assertTrue(expectedBirthdateText.equals(birthdateText), "This is not a correct birthdate value");
	}
	
	private void assertGenderValue() {
		boolean expectedIsMale = Settings.hasMaleGender();
		Assert.assertTrue(expectedIsMale == isMale, "This is not a correct gender value");
	}
}
