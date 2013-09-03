package com.misfit.ta.gui;

import java.util.Random;

import com.misfit.ios.AppHelper;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.utils.ShortcutsTyper;

public class PrometheusHelper {

	/* Views Helper */
	public static void enterEmailPassword(String email, String password) {

		if (ViewUtils.isExistedView("PTEmailVerifyingTextField", 0)) {
			Gui.touchAVIew("PTEmailVerifyingTextField", 0);
			String txtEmail = Gui.getProperty("PTEmailVerifyingTextField", 0,
					"text");

			System.out.println("Deleting: " + txtEmail);
			Gui.moveCursorInCurrentTextViewTo(-1);
			for (int i = 0; i < txtEmail.length(); i++)
				Gui.pressDelete();
		} else if (ViewUtils.isExistedView("PTPaddingTextField", 0)) {
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

	public static void enterGender(boolean isMale) {
		Gui.touchAVIew("UIButtonLabel", isMale ? DefaultStrings.MaleLabel
				: DefaultStrings.FemaleLabel);
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

	public static void enterBirthDay() {
		Gui.touchAVIew("PTDatePickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		Gui.dismissPicker();
		ShortcutsTyper.delayTime(1000);
	}

	public static void enterHeight(String digit, String fraction,
			boolean isUSUnit) {
		/* Example for ft in Gui.setPicker(1, "9\\\""); */
		Gui.touchAVIew("PTHeightPickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		Gui.setPicker(2, isUSUnit ? DefaultStrings.InchesLabel
				: DefaultStrings.MetreLabel);
		Gui.setPicker(0, digit);
		Gui.setPicker(1, fraction);
		Gui.dismissPicker();
		ShortcutsTyper.delayTime(1000);
	}

	public static void enterHeight() {
		/* Example for ft in Gui.setPicker(1, "9\\\""); */
		Gui.touchAVIew("PTHeightPickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		Gui.dismissPicker();
		ShortcutsTyper.delayTime(1000);
	}

	public static void enterWeight(String digit, String fraction,
			boolean isUSUnit) {
		Gui.touchAVIew("PTWeightPickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		Gui.setPicker(2, isUSUnit ? DefaultStrings.LbsLabel
				: DefaultStrings.KgLabel);
		Gui.setPicker(0, digit);
		Gui.setPicker(1, fraction);
		Gui.dismissPicker();
		ShortcutsTyper.delayTime(1000);
	}

	public static void enterWeight() {
		Gui.touchAVIew("PTWeightPickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		Gui.dismissPicker();
		ShortcutsTyper.delayTime(1000);
	}

	/* Alerts */
	public static boolean hasSignUpInvalidEmailMessage() {
		return Gui.getPopupContent().equals(
				DefaultStrings.SignUpInvalidEmailMessage)
				&& Gui.getPopupTitle().equals(DefaultStrings.Title);
	}

	public static boolean hasSignInInvalidEmailMessage() {
		return Gui.getPopupContent().equals(
				DefaultStrings.SignInInvalidEmailMessage)
				&& Gui.getPopupTitle().equals(DefaultStrings.Title);
	}

	public static boolean hasForgotPasswordInvalidEmailMessage() {
		return Gui.getPopupContent().equals(
				DefaultStrings.ForgotPasswordInvalidEmailMessage);
	}

	public static boolean hasInvalidPasswordMessage() {
		return Gui.getPopupContent().equals(
				DefaultStrings.InvalidPasswordMessage)
				&& Gui.getPopupTitle().equals(DefaultStrings.Title);
	}

	public static boolean hasIncorrectLoginMessage() {
		return Gui.getPopupContent().equals(
				DefaultStrings.SignInWrongAccountMessage)
				&& Gui.getPopupTitle().equals(DefaultStrings.Title);
	}

	public static boolean hasExistedEmailMessage() {
		return Gui.getPopupContent().equals(
				DefaultStrings.SignUpDuplicatedEmailMessage)
				&& Gui.getPopupTitle().equals(DefaultStrings.Title);
	}

	public static boolean hasNoShineAvailableMessage() {
		return Gui.getPopupContent().equals(
				DefaultStrings.NoShineAvailableMessage)
				&& Gui.getPopupTitle().equals(
						DefaultStrings.NoShineAvailableTitle);
	}

	public static boolean hasUnableToLinkMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.UnableToLinkMessage)
				&& Gui.getPopupTitle().equals(DefaultStrings.UnableToLinkTitle);
	}

	public static boolean hasSyncFailedMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.SyncFailedMessage)
				&& Gui.getPopupTitle().equals(DefaultStrings.Title);
	}

	public static void sync() {
		Gui.sync();
	}

	/* Utilities */
	private static String[] longMonths = { "January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December" };
	private static String[] sortMonths = { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	private static String[] sortDaysOfWeek = { "Sun", "Mon", "Tue", "Wed",
			"Thu", "Fri", "Sat" };
	private static String[] longDaysOfWeek = { "Sunday", "Monday", "Tuesday",
			"Wednesday", "Thursday", "Friday", "Saturday" };

	public static int randInt(int includeFrom, int excludeTo) {
		Random r = new Random();
		return r.nextInt(excludeTo - includeFrom) + includeFrom;
	}

	public static boolean coin() {
		Random r = new Random();
		return r.nextBoolean();
	}

	public static float calculatePoint(int steps, int minutes) {
		// Manual input: real activity points should be floor down before
		// deviding by 2.5
		float stepsPerMin = (float) Math.floor(steps * 1f /minutes);
		float realPointsPerMin = (stepsPerMin
				* (0.25f + 0.01f * (Math.max(115f, stepsPerMin) - 115)) + 0.0001f);
		return (float) ((Math.floor(realPointsPerMin) * minutes) / 2.5f);
	}

	public static float calculateMiles(int steps, float heightInInches) {
		// TODO: check new distance calculation
		return (steps * 0.414f * heightInInches) / 63360f;
	}

	public static float calculateCalories(float points, float weightInLbs,
			float fullBMR, int currentMinute) {
		// TODO: update new calculation
		// E = alpha * activity_points * [weight_in_kg / 60] + beta *
		// BMR_elapsed
		// Calories = min(E, 0.5*E + 0.925*BMR_24hrs)
		// alpha = 0.15; beta = 1.3
		float weightInKg = weightInLbs * 0.45359237f;
		float alpha = 0.15f;
		float beta = 1.3f;
		float E = alpha * points * 2.5f * (weightInKg / 60f) + beta
				* (fullBMR * (currentMinute / 1440f));
		return (float) Math.min(E, 0.5f * E + 0.925f * fullBMR);
	}

	public static float calculateFullBMR(float weightInLbs,
			float heightInInches, int age, boolean isMale) {
		float weightInKg = weightInLbs * 0.453592f;
		float heightInCm = heightInInches * 2.54f;
		float genderFactor = isMale ? 5f : -161f;
		float fullBMR = (10f * weightInKg) + (6.25f * heightInCm) - (5f * age)
				+ genderFactor;
		return fullBMR;
	}

	public static String getMonthString(int monthNumber,
			boolean isLongMonthString) {
		return isLongMonthString ? longMonths[monthNumber - 1]
				: sortMonths[monthNumber - 1];
	}

	public static String getDayOfWeek(int dayNumber, boolean isLongString) {
		return isLongString ? longDaysOfWeek[dayNumber - 1]
				: sortDaysOfWeek[dayNumber - 1];
	}

	public static String formatBirthday(String year, String monthString,
			String day) {
		return monthString.substring(0, 3) + " "
				+ String.format("%02d", Integer.parseInt(day)) + ", " + year;
	}

	/* Quick navigation */
	public static void signUp(String email, String password, boolean isMale,
			int date, int month, int year, boolean isUSUnit, String h1,
			String h2, String w1, String w2, int goalLevel) {
		LaunchScreen.launch();

		SignUp.tapSignUp();
		ShortcutsTyper.delayTime(1000);
		SignUp.enterEmailPassword(email, password);
		ShortcutsTyper.delayTime(10000);

		SignUp.enterGender(isMale);
		SignUp.enterBirthDay(String.valueOf(year),
				PrometheusHelper.getMonthString(month, true),
				String.valueOf(date));
		SignUp.enterHeight(h1, h2, isUSUnit);
		SignUp.enterWeight(w1, w2, isUSUnit);
		SignUp.tapNext();

		ShortcutsTyper.delayTime(1000);
		SignUp.setGoal(goalLevel);
		SignUp.tapNext();

		ShortcutsTyper.delayTime(1000);
		SignUp.sync();
		ShortcutsTyper.delayTime(1000);
		SignUp.tapFinishSetup();
		ShortcutsTyper.delayTime(1000);
		PrometheusHelper.handleUpdateFirmwarePopup();
		ShortcutsTyper.delayTime(1000);
		PrometheusHelper.handleTutorial();
		ShortcutsTyper.delayTime(1000);
	}

	public static String signUp() {
		String email = MVPApi.generateUniqueEmail();
		String h1 = "5'";
		String h2 = "8\\\"";
		String w1 = "120";
		String w2 = ".0";
		PrometheusHelper.signUp(email, "qwerty1", true, 16, 9, 1991, true, h1,
				h2, w1, w2, 1);

		return email;
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
		Gui.touchAVIew("UITextField", 3);
		Gui.pressDelete();
		Gui.type(String.valueOf(duration));
		ShortcutsTyper.delayTime(500);
		Gui.dismissPicker();

		// enter steps
		Gui.touchAVIew("UITextField", 4);
		Gui.pressDelete();
		Gui.type(String.valueOf(steps));
		ShortcutsTyper.delayTime(500);
		Gui.dismissPicker();
	}

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
				DefaultStrings.TutorialFirstPageLabel);

		if (hasTutorialView) {
			for (int i = 0; i < 5; i++) {
				Gui.swipeLeft(500);
				ShortcutsTyper.delayTime(200);
			}
			if (ViewUtils.isExistedView("UIButtonLabel",
					DefaultStrings.EndTutorialButton)) {
				Gui.touchAVIew("UIButtonLabel",
						DefaultStrings.EndTutorialButton);
			}
		}
	}

	public static void handleUpdateFirmwarePopup() {
		if (DefaultStrings.UpdateFirmwareTitle.equals(Gui.getPopupTitle())
				&& DefaultStrings.UpdateFirmwareMessage.equals(Gui
						.getPopupContent())) {
			Gui.touchPopupButton(DefaultStrings.UpdateFirmwareLaterButton);
		}
	}

	public static void main(String[] args) {
		Gui.init("192.168.1.206");
		Gui.shutdown();
	}

}
