package com.misfit.ta.gui;

import java.util.Random;

import com.misfit.ios.AppHelper;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.utils.ShortcutsTyper;

public class PrometheusHelper {

	/* Constants */
	private static String[] longMonths = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	private static String[] sortMonths = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	private static String[] sortDaysOfWeek = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	private static String[] longDaysOfWeek = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	/* Input Helper */
	public static void enterEmailPassword(String email, String password) {

		if (ViewUtils.isExistedView("PTEmailVerifyingTextField", 0)) {
			Gui.touchAVIew("PTEmailVerifyingTextField", 0);
			String txtEmail = Gui.getProperty("PTEmailVerifyingTextField", 0, "text");

			Gui.moveCursorInCurrentTextViewTo(-1);
			for (int i = 0; i < txtEmail.length(); i++)
				Gui.pressDelete();
		} else if (ViewUtils.isExistedView("PTPaddingTextField", 0)) {
			Gui.touchAVIew("PTPaddingTextField", 0);
			String txtEmail = Gui.getProperty("PTPaddingTextField", 0, "text");

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
		Gui.touchAVIew("UIButtonLabel", isMale ? DefaultStrings.MaleLabel : DefaultStrings.FemaleLabel);
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

	public static void enterHeight(String digit, String fraction, boolean isUSUnit) {

		Gui.touchAVIew("PTHeightPickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		Gui.setPicker(2, isUSUnit ? DefaultStrings.InchesLabel : DefaultStrings.MetreLabel);
		Gui.setPicker(0, digit);
		Gui.setPicker(1, fraction);
		Gui.dismissPicker();
		ShortcutsTyper.delayTime(1000);
	}

	public static void enterHeight() {

		Gui.touchAVIew("PTHeightPickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		Gui.dismissPicker();
		ShortcutsTyper.delayTime(1000);
	}

	public static void enterWeight(String digit, String fraction, boolean isUSUnit) {
		Gui.touchAVIew("PTWeightPickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		Gui.setPicker(2, isUSUnit ? DefaultStrings.LbsLabel : DefaultStrings.KgLabel);
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

	/* Alerts */
	public static boolean hasSignUpInvalidEmailMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.SignUpInvalidEmailMessage) && Gui.getPopupTitle().equals(DefaultStrings.Title);
	}

	public static boolean hasSignInInvalidEmailMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.SignInInvalidEmailMessage) && Gui.getPopupTitle().equals(DefaultStrings.Title);
	}

	public static boolean hasForgotPasswordInvalidEmailMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.ForgotPasswordInvalidEmailMessage);
	}

	public static boolean hasInvalidPasswordMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.InvalidPasswordMessage) && Gui.getPopupTitle().equals(DefaultStrings.Title);
	}

	public static boolean hasIncorrectLoginMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.SignInWrongAccountMessage) && Gui.getPopupTitle().equals(DefaultStrings.Title);
	}

	public static boolean hasExistedEmailMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.SignUpDuplicatedEmailMessage) && Gui.getPopupTitle().equals(DefaultStrings.Title);
	}

	public static boolean hasNoShineAvailableMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.NoShineAvailableMessage) && Gui.getPopupTitle().equals(DefaultStrings.NoShineAvailableTitle);
	}

	public static boolean hasUnableToLinkMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.UnableToLinkMessage) && Gui.getPopupTitle().equals(DefaultStrings.UnableToLinkTitle);
	}

	public static boolean hasSyncFailedMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.SyncFailedMessage) && Gui.getPopupTitle().equals(DefaultStrings.Title);
	}

	/* Utilities */
	public static int randInt(int includeFrom, int excludeTo) {
		Random r = new Random();
		return r.nextInt(excludeTo - includeFrom) + includeFrom;
	}

	public static boolean coin() {
		Random r = new Random();
		return r.nextBoolean();
	}

	/* Calculation */
	public static float calculatePoint(int steps, int minutes, int activityType) {

		return (float)MVPCalculator.calculatePoint(steps, minutes, activityType);
	}

	public static float calculateMiles(int steps, int mins, float heightInInches) {
		
		return (float)MVPCalculator.calculateMiles(steps, mins, heightInInches);
	}

	public static float calculateCalories(float points, float weightInLbs, float fullBMR, int currentMinute) {
		
		return (float)MVPCalculator.calculateCalories(points, weightInLbs, fullBMR, currentMinute);
	}

	public static float calculateFullBMR(float weightInLbs, float heightInInches, int age, boolean isMale) {
		
		return (float)MVPCalculator.calculateFullBMR(weightInLbs, heightInInches, age, isMale);
	}

	/* String utilities */
	public static String getMonthString(int monthNumber, boolean isLongMonthString) {
		return isLongMonthString ? longMonths[monthNumber - 1] : sortMonths[monthNumber - 1];
	}

	public static String getDayOfWeek(int dayNumber, boolean isLongString) {
		return isLongString ? longDaysOfWeek[dayNumber - 1] : sortDaysOfWeek[dayNumber - 1];
	}

	public static String formatBirthday(String year, String monthString, String day) {
		return monthString.substring(0, 3) + " " + String.format("%02d", Integer.parseInt(day)) + ", " + year;
	}

	/* Quick navigation */
	public static void handleTutorial() {
		boolean hasTutorialView = ViewUtils.isExistedView("PTRichTextLabel", DefaultStrings.TutorialFirstPageLabel);

		if (hasTutorialView) {
			for (int i = 0; i < 5; i++) {
				Gui.swipeLeft(500);
				ShortcutsTyper.delayTime(200);
			}
			if (ViewUtils.isExistedView("UIButtonLabel", DefaultStrings.EndTutorialButton)) {
				Gui.touchAVIew("UIButtonLabel", DefaultStrings.EndTutorialButton);
			}
		}
	}

	public static void handleUpdateFirmwarePopup() {
		if (DefaultStrings.UpdateFirmwareTitle.equals(Gui.getPopupTitle()) && DefaultStrings.UpdateFirmwareMessage.equals(Gui.getPopupContent())) {
			Gui.touchPopupButton(DefaultStrings.UpdateFirmwareLaterButton);
		}
	}

	public static void signUp(String email, String password, boolean isMale, int date, int month, int year, boolean isUSUnit, String h1, String h2, String w1, String w2, int goalLevel) {
		LaunchScreen.launch();

		SignUp.tapSignUp();
		ShortcutsTyper.delayTime(1000);
		SignUp.enterEmailPassword(email, password);
		ShortcutsTyper.delayTime(10000);

		SignUp.enterGender(isMale);
		SignUp.enterBirthDay(String.valueOf(year), PrometheusHelper.getMonthString(month, true), String.valueOf(date));
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
		PrometheusHelper.signUp(email, "qwerty1", true, 16, 9, 1991, true, h1, h2, w1, w2, 1);

		return email;
	}

	public static void sync() {
		Gui.sync();
	}
	
	public void startApp() {
		(new Thread() {
			public void run() {
				AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), "script/automation/alertsupport.js");
			}
		}).start();
		ShortcutsTyper.delayTime(20000);
	}

	
	public static void main(String[] args) {
		Gui.init("192.168.1.206");
		Gui.shutdown();
	}

}
