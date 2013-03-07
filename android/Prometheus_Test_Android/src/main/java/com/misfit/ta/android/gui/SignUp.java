package com.misfit.ta.android.gui;

import org.testng.annotations.Test;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.chimpchat.core.TouchPressType;
import com.misfit.ta.android.gui.Helper.Helper;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;

public class SignUp {
	public static void chooseSignUp() {
		Gui.touchAView("Button", "mID", "id/buttonNotHaveAccount");
	}

	public static void pressBack() {
		Gui.touchAView("ImageButton", "mID", "id/buttonPrevious");
	}

	public static void pressNext() {
		Gui.touchAView("ImageButton", "mID", "id/buttonNext");
	}

	public static boolean isSignUpVisible() {
		return ViewUtils.findView("EditText", "mID",
				"id/userinfo_passwordEdit", 0) != null;
	}

	public static boolean isAtStep1() {
		return ViewUtils.findView("EditText", "mID",
				"id/userinfo_passwordEdit", 0) != null;
	}

	public static boolean isAtStep2() {
		return true;
	}

	/**
	 * Step 1: fill register profile
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * @param isMale
	 * @param isMetric
	 */

	public static boolean isInvalidEmailMessageShown() {
		System.out.print("2222");
		
		 return ViewUtils.isVisible(
				 ViewUtils.findView("TextView", "mID", "id/messageaaa", 0));
	}

	public static void fillRegisterProfile(String name, String email,
			String password, Boolean isMale, Boolean isMetric) {
		if (name != null) {
			Gui.touchAView("EditText", "mID", "id/userinfo_nameEdit");
			Gui.type(name);
		}

		if (email != null) {
			Gui.touchAView("EditText", "mID", "id/userinfo_emailEdit");
			Gui.type(email);
		}

		if (password != null) {
			Gui.touchAView("EditText", "mID", "id/userinfo_passwordEdit");
			Gui.type(password);
		}

		if (isMale != null) {
			ViewNode maleRadioButton = ViewUtils.findView("RadioButton", "mID",
					"id/userinfo_maleButton", 0);
			if (isMale && !maleRadioButton.isChecked) {
				Gui.touchAView("RadioButton", "mID", "id/userinfo_maleButton");
			} else if (maleRadioButton.isChecked) {
				Gui.touchAView("RadioButton", "mID", "id/userinfo_femaleButton");
			}
		}

		if (isMetric != null) {
			ViewNode metricRadioButton = ViewUtils.findView("RadioButton",
					"mID", "id/userinfo_metricButton", 0);
			if (isMetric && !metricRadioButton.isChecked) {
				Gui.touchAView("RadioButton", "mID", "id/userinfo_metricButton");
			} else if (metricRadioButton.isChecked) {
				Gui.touchAView("RadioButton", "mID", "id/userinfo_usButton");
			}
		}

		// TODO fill info for birthdate, weight, height

	}

	public static String getName() {
		return ViewUtils.findView("EditText", "mID", "id/userinfo_nameEdit", 0).text;
	}

	public static String getEmail() {
		return ViewUtils
				.findView("EditText", "mID", "id/userinfo_emailEdit", 0).text;
	}

	public static boolean getSex() {
		return ViewUtils.findView("RadioButton", "mID",
				"id/userinfo_maleButton", 0).isChecked;
	}

	public static boolean getUnit() {
		return ViewUtils.findView("RadioButton", "mID", "id/userinfo_usButton",
				0).isChecked;
	}

	public static String getBirthday() {
		return null;
	}

	public static String getWeight() {
		return null;
	}

	public static String getHeight() {
		return null;
	}

	public static void inputName(String name) {
		SignUp.fillRegisterProfile(name, null, null, null, null);
	}

	public static void inputEmail(String email) {
		SignUp.fillRegisterProfile(null, email, null, null, null);
	}

	public static void inputPassword(String password) {
		SignUp.fillRegisterProfile(null, null, password, null, null);
	}

	public static void inputSex(Boolean sex) {
		SignUp.fillRegisterProfile(null, null, null, sex, null);
	}

	public static void inputUnit(Boolean unit) {
		SignUp.fillRegisterProfile(null, null, null, null, unit);
	}

	public static void inputBirthday(int date, int month, int year) {

	}

	public static void inputWeight(String weight) {

	}

	public static void inputHeight(String height) {

	}

	/**
	 * TODO Step 2: set average activities
	 */

	/**
	 * Step 3: set goal
	 * 
	 * @param steps
	 */
	public static void setGoalUp(int steps) {
		for (int i = 0; i < steps; i++) {
			Gui.touchAView("Button", 1);
		}
	}

	public static void setGoalDown(int steps) {
		for (int i = 0; i < steps; i++) {
			Gui.touchAView("Button", 0);
		}
	}

	public static void tapLevelMild() {
		Helper.tapLevelMild();
	}

	public static void tapLevelDormant() {
		Helper.tapLevelDormant();
	}

	public static void tapLevelModerate() {
		Helper.tapLevelModerate();
	}

	public static void tapLevelActive() {
		Helper.tapLevelActive();
	}

	/**
	 * Step 4: set up device
	 */
	public static void setupDevice() {
		Gui.touch(Gui.getCoordinators("ImageButton", "mID", "id/buttonSetup"),
				TouchPressType.DOWN);
	}

	public static void tapToBuyShine() {
		Gui.touchAView("Button", "mID", "id/buttonDeviceSetupAskForBuyingShine");
	}

	/**
	 * Don't have a shine GET YOURS NOW
	 */
	public static boolean hasBuyingInstruction() {
		return ViewUtils.findView("Button", "mID",
				"id/buttonDeviceSetupAskForBuyingShine", 0) != null
				&& ViewUtils.findView("TextView", "mID",
						"id/textViewDeviceSetupAskForBuyingShine", 0) != null;
	}

	/**
	 * GO. ALWAYS WEAR YOUR SHINE To sync just place Shine on the logo below
	 */
	public static boolean hasSyncInstruction() {
		return ViewUtils.findView("Button", "mID",
				"id/textViewDeviceSetupTitle", 0) != null
				&& ViewUtils.findView("TextView", "mID",
						"id/textViewDeviceSetupSyncInstruction", 0) != null;
	}

	public static boolean hasMagicHappening() {
		return ViewUtils.findView("Button", "mText", "MAGIC HAPPENING NOW", 0) != null;
	}

	/**
	 * Sign up complete
	 */
	public static void goToHomeScreen() {
		Gui.touchAView("ImageButton", "mID", "id/buttonNext");
	}

}
