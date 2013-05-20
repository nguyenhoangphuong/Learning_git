package com.misfit.ta.gui;

import java.util.Random;
import org.testng.annotations.Test;

import com.misfit.ios.AppHelper;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class PrometheusHelper {

	/* Views Helper */
	public static void enterGender(boolean isMale) {
		Gui.touchAVIew("UIButtonLabel", isMale ? "Male" : "Female");
	}

	public static void enterBirthDay(String year, String month, String day) {
		Gui.touchAVIew("PTDatePickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		Gui.setPicker(0, month);
		Gui.setPicker(1, day);
		Gui.setPicker(2, year);
		Gui.dismissPicker();
		ShortcutsTyper.delayTime(1000);
	}

	public static void enterHeight(String digit, String fraction,
			boolean isUSUnit) {
		/* Example for ft in Gui.setPicker(1, "9\\\""); */
		Gui.touchAVIew("PTHeightPickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		Gui.setPicker(2, isUSUnit ? "ft in" : "m");
		Gui.setPicker(0, digit);
		Gui.setPicker(1, fraction);
		Gui.dismissPicker();
		ShortcutsTyper.delayTime(1000);
	}

	public static void enterWeight(String digit, String fraction,
			boolean isUSUnit) {
		Gui.touchAVIew("PTWeightPickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		Gui.setPicker(2, isUSUnit ? "lbs" : "kg");
		Gui.setPicker(0, digit);
		Gui.setPicker(1, fraction);
		Gui.dismissPicker();
		ShortcutsTyper.delayTime(1000);
	}

	public static boolean hasInvalidEmailMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.InvalidEmailMessage);
	}

	public static boolean hasInvalidPasswordMessage() {
		return Gui.getPopupContent().equals(
				DefaultStrings.InvalidPasswordMessage);
	}

	public static void sync() {
		Gui.sync();
	}

	public static void enterEmailPassword(String email, String password) {

		if (ViewUtils.isExistedView("PTEmailVerifyingTextField", 0)) {
			Gui.touchAVIew("PTEmailVerifyingTextField", 0);
			String txtEmail = Gui.getProperty("PTEmailVerifyingTextField", 0,
					"text");

			System.out.println("Deleting: " + txtEmail);
			Gui.moveCursorInCurrentTextViewTo(-1);
			for (int i = 0; i < txtEmail.length(); i++)
				Gui.pressDelete();
		} else if (ViewUtils.isExistedView("PTPaddingTextField", 0))
			;
		{
			Gui.touchAVIew("PTPaddingTextField", 0);
			String txtEmail = Gui.getProperty("PTPaddingTextField", 0, "text");

			System.out.println("Deleting: " + txtEmail);
			Gui.moveCursorInCurrentTextViewTo(-1);
			for (int i = 0; i < txtEmail.length(); i++)
				Gui.pressDelete();
		}
		ShortcutsTyper.delayTime(800);
		Gui.type(email);
		ShortcutsTyper.delayTime(300);
		Gui.pressNext();
		ShortcutsTyper.delayTime(300);
		Gui.pressDelete();
		Gui.type(password);
		ShortcutsTyper.delayTime(300);
		Gui.pressDone();
	}

	/* Utilities */
	private static String[] longMonths = { "January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December" };
	private static String[] sortMonths = { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	public static int randInt(int includeFrom, int excludeTo) {
		Random r = new Random();
		return r.nextInt(excludeTo - includeFrom) + includeFrom;
	}

	public static boolean coin() {
		Random r = new Random();
		return r.nextBoolean();
	}

	public static float calculatePoint(int steps, int minutes) {
		return (steps
				* (0.25f + 0.01f * (Math.max(115f, steps * 1f / minutes) - 115))
				+ 0.0001f) / 2.5f;
	}

	public static float calculateMiles(int steps, int heightInInches) {
		return steps * 0.414f * heightInInches / 63360;
	}

	public static float calculateCalories(float points, float weightInLbs) {
		float weightInKg = (float) (weightInLbs * 0.453592);
		weightInKg = Math.round(weightInKg * 10) / 10;
		return points * weightInKg / 600f;
	}

	public static String getMonthString(int monthNumber,
			boolean isLongMonthString) {
		return isLongMonthString ? longMonths[monthNumber - 1]
				: sortMonths[monthNumber - 1];
	}

	public static String formatBirthday(String year, String monthString,
			String day) {
		return monthString.substring(0, 3) + " "
				+ String.format("%02d", Integer.parseInt(day)) + ", " + year;
	}

	public static String formatProfile(boolean isMale, boolean isUSUnit,
			String h1, String h2, String w1, String w2) {
		return (isMale ? "Male" : "Female") + " - " + h1 + h2
				+ (isUSUnit ? "" : " m") + " - " + w1 + w2
				+ (isUSUnit ? " lbs" : " kg");
	}

	/* Quick navigation */
	public static void signUp(String email, String password, boolean isMale,
			int date, int month, int year, boolean isUSUnit, String h1,
			String h2, String w1, String w2, int goalLevel) {
		LaunchScreen.launch();

		SignUp.tapSignUp();
		ShortcutsTyper.delayTime(1000);
		SignUp.enterEmailPassword(email, password);
		ShortcutsTyper.delayTime(5000);
		SignUp.enterGender(isMale);
		SignUp.enterBirthDay(year + "",
				PrometheusHelper.getMonthString(month, true), date + "");
		SignUp.enterHeight(h1, h2, isUSUnit);
		SignUp.enterWeight(w1, w2, isUSUnit);
		SignUp.tapNext();
		ShortcutsTyper.delayTime(1000);
		SignUp.setGoal(goalLevel);
		SignUp.tapNext();
		ShortcutsTyper.delayTime(1000);
		SignUp.sync();
		ShortcutsTyper.delayTime(10000);
		SignUp.tapFinishSetup();
		ShortcutsTyper.delayTime(3000);
	}

	public static void setInputModeToManual() {
		// require current view is HomeScreen
		HomeScreen.tapSettings();
		ShortcutsTyper.delayTime(1000);

		Gui.swipeUp(1000);
		ShortcutsTyper.delayTime(1000);

		HomeSettings.tapDebug();
		ShortcutsTyper.delayTime(1000);

		HomeSettings.chooseManual();
		HomeSettings.tapDoneAtDebug();
		ShortcutsTyper.delayTime(1000);

		HomeSettings.tapBackAtSettings();
		ShortcutsTyper.delayTime(1000);
	}

	public static void inputRandomRecord() {
		// require current screen is HomeScreen
		HomeScreen.tapOpenManualInput();
		ShortcutsTyper.delayTime(1000);
		HomeScreen.tapRandom();
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(1000);
	}

	public static void inputManualRecord(String[] times, int duration, int steps) {
		// enter activity
		Gui.touchAVIew("UITextField", 0);
		ShortcutsTyper.delayTime(500);
		Gui.setPicker(0, "Running");
		Gui.dismissPicker();
		ShortcutsTyper.delayTime(500);

		// enter time
		Gui.touchAVIew("UITextField", 1);
		ShortcutsTyper.delayTime(500);
		Gui.setPicker(0, times[0]);
		Gui.setPicker(1, times[1]);
		Gui.setPicker(2, times[2]);
		Gui.dismissPicker();
		ShortcutsTyper.delayTime(500);

		// enter duration
		Gui.touchAVIew("UITextField", 2);
		Gui.type(String.valueOf(duration));
		ShortcutsTyper.delayTime(500);
		Gui.dismissPicker();

		// enter steps
		Gui.touchAVIew("UITextField", 3);
		Gui.type(String.valueOf(steps));
		ShortcutsTyper.delayTime(500);
		Gui.dismissPicker();
	}

	// public static void main(String[] args)

	public void startApp() {
		(new Thread() {
			public void run() {
				AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
						AppHelper.getAppPath(),
						"script/automation/alertsupport.js");
			}
		}).start();
		ShortcutsTyper.delayTime(20000);
	}

	public static void handleTutorial() {
		boolean hasTutorialView = ViewUtils.isExistedView("PTRichTextLabel",
				"This \\_circle\\_ represents\\nyour \\_activity\\_ each day.");
		if (hasTutorialView) {
			for (int i = 0; i < 4; i++) {
				Gui.swipeLeft(500);
				ShortcutsTyper.delayTime(200); 
			}
			if (ViewUtils.isExistedView("UIButtonLabel", "OK, I GOT IT")) {
				Gui.touchAVIew("UIButtonLabel", "OK, I GOT IT");
			} else if (ViewUtils.isExistedView("UIButtonLabel", "DISMISS IT")) {
				Gui.touchAVIew("UIButtonLabel", "DISMISS IT");
			}
		}
	}

	@Test
	public void test() {
		// startApp();
		Gui.init("192.168.1.247");

		// for(int i = 0; i < 1000; i++)
		// {
		// SignIn.tapLogInWithFacebook();
		// ShortcutsTyper.delayTime(5000);
		// HomeScreen.tapSettings();
		// ShortcutsTyper.delayTime(500);
		// Gui.swipeUp(1000);
		// ShortcutsTyper.delayTime(500);
		//
		// HomeSettings.tapSignOut();
		// ShortcutsTyper.delayTime(1000);
		//
		// if(!ViewUtils.isExistedView("UIButtonLabel", " SIGN UP"))
		// Gui.touchAVIew("UIButtonLabel", "Sign out");
		// }

		HomeSettings.setSpinnerGoal(4000);
		ShortcutsTyper.delayTime(2000);
		HomeSettings.tapDoneAtNewGoal();

		// Gui.swipeUp(1000);
		// Gui.printView();

		// HomeScreen.tapSyncTray();
		// HomeScreen.tapSettings();

		// PrometheusHelper.signUp(MVPApi.generateUniqueEmail(), "qwerty1",
		// true, 16, 9, 1991, true, "5'", "8\\\"", "120", ".0", 1);
	}

}
