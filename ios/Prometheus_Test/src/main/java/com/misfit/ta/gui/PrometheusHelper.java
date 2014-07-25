package com.misfit.ta.gui;

import java.util.Random;
import java.util.concurrent.Callable;

import com.misfit.ios.AppHelper;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.utils.ShortcutsTyper;

public class PrometheusHelper {

	/* Constants */
	private static int DEFAULT_TIMEOUT = 15;
	private static String[] longMonths = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	private static String[] shortMonths = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	private static String[] shortDaysOfWeek = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	private static String[] longDaysOfWeek = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	// Manual input field tags
	public static final int TAG_MANUAL_ACTIVITY_TYPE = 1;
	public static final int TAG_MANUAL_START_TIME = 2;
	public static final int TAG_MANUAL_START_TIME_EPOCH = 3;
	public static final int TAG_MANUAL_DURATION = 4;
	public static final int TAG_MANUAL_STEPS = 5;
	public static final int TAG_MANUAL_INTENSITY_LEVEL = 6;
	
	/* Input Helper */
	public static void enterEmailPassword(String email, String password) {

		if (ViewUtils.isExistedView("PTEmailVerifyingTextField", 0)) {
			
			Gui.setText("PTEmailVerifyingTextField", 0, email);
			Gui.setText("PTPasswordVerifyingTextField", 0, password);
		} else if (ViewUtils.isExistedView("PTPaddingTextField", 0)) {	
			
			Gui.setText("PTPaddingTextField", 0, email);
			Gui.setText("PTPaddingTextField", 1, password);
		}
		
		Gui.tapNext();
	}

	public static void enterGender(boolean isMale) {
		Gui.touchAVIew("UIButtonLabel", isMale ? DefaultStrings.MaleLabel : DefaultStrings.FemaleLabel);
	}

	public static void enterBirthDay(String year, String month, String day) {
			
		Gui.touchAVIew("PTDatePickerControl", 0);
		Gui.setPicker(0, month);
		Gui.setPicker(1, day);
		Gui.setPicker(2, year);
		Gui.dismissPicker();
	}

	public static void enterBirthDay() {
		
		Gui.touchAVIew("PTDatePickerControl", 0);
		Gui.dismissPicker();
	}

	public static void enterHeight(String digit, String fraction, boolean isUSUnit) {

		Gui.touchAVIew("PTHeightPickerControl", 0);
		Gui.setPicker(2, isUSUnit ? DefaultStrings.InchesLabel : DefaultStrings.MetreLabel);
		if(digit != null) Gui.setPicker(0, digit);
		if(fraction != null) Gui.setPicker(1, fraction);
		Gui.dismissPicker();
	}

	public static void enterHeight() {

		Gui.touchAVIew("PTHeightPickerControl", 0);
		Gui.dismissPicker();
	}

	public static void enterWeight(String digit, String fraction, boolean isUSUnit) {
		
		Gui.touchAVIew("PTWeightPickerControl", 0);
		Gui.setPicker(2, isUSUnit ? DefaultStrings.LbsLabel : DefaultStrings.KgLabel);
		if(digit != null) Gui.setPicker(0, digit);
		if(fraction != null) Gui.setPicker(1, fraction);
		Gui.dismissPicker();
	}

	public static void enterWeight() {
		
		Gui.touchAVIew("PTWeightPickerControl", 0);
		Gui.dismissPicker();
	}

	public static void inputRandomRecord() {
		
		// require current screen is HomeScreen
		HomeScreen.tapOpenManualInput();
		HomeScreen.tapRandom();
		HomeScreen.tapSave();
	}

	public static void inputManualRecord(String[] times, int duration, int steps) {
		
		// it's am in iOS7 instead of AM
		times[2] = times[2].toUpperCase();
	
		// enter time
		manualInputTime(times);

		// enter duration
		Gui.touchATaggedView("UITextField", TAG_MANUAL_DURATION);
		Gui.setTextTagged("UITextField", TAG_MANUAL_DURATION, duration + "");
		Gui.dismissPicker();
		
		// enter steps
		Gui.touchATaggedView("UITextField", TAG_MANUAL_STEPS);
		Gui.setTextTagged("UITextField", TAG_MANUAL_STEPS, steps + "");
		Gui.dismissPicker();
	}

	public static void manualInputTime(String[] times) {
		Gui.touchATaggedView("UITextField", TAG_MANUAL_START_TIME);
		Gui.setPicker(0, times[0]);
		Gui.setPicker(1, times[1]);
		Gui.setPicker(2, times[2]);
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
	
	public static boolean hasAlert(String message, String title) {
		
		return (message == null || Gui.getPopupContent().equals(message)) && (title == null || Gui.getPopupTitle().equals(title));
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
		return isLongMonthString ? longMonths[monthNumber - 1] : shortMonths[monthNumber - 1];
	}

	public static String getDayOfWeek(int dayNumber, boolean isLongString) {
		return isLongString ? longDaysOfWeek[dayNumber - 1] : shortDaysOfWeek[dayNumber - 1];
	}

	public static String formatBirthday(String year, String monthString, String day) {
		return monthString.substring(0, 3) + " " + String.format("%02d", Integer.parseInt(day)) + ", " + year;
	}

	/* Quick navigation */
	public static void handleTutorial() {
		PrometheusHelper.waitForView("PTRichTextLabel", DefaultStrings.TutorialFirstPageLabel);
			
		for (int i = 0; i < 2; i++) {
			Gui.swipeLeft(500);
		}
		
		if (ViewUtils.isExistedView("UIButtonLabel",
				DefaultStrings.EndTutorialButton)) {
			Gui.touchAVIew("UIButtonLabel", DefaultStrings.EndTutorialButton);
		}
	}
	
	public static void handleTagEditingTutorial() {
		if (ViewUtils.isExistedView("UIButtonLabel", DefaultStrings.EndTutorialButton)) {
			Gui.touchAVIew("UIButtonLabel", DefaultStrings.EndTutorialButton);
		}
	}

	public static void handleBatteryLowPopup() {
		if (ViewUtils.isExistedView("UILabel", DefaultStrings.BatteryLowTitle) && ViewUtils.isExistedView("UILabel", DefaultStrings.BatteryLowMessage)) {
			Gui.touchAVIew("UIButtonLabel", DefaultStrings.DismissButton);
		}
	}
	
	public static void handleUpdateFirmwarePopup() {
		if (ViewUtils.isExistedView("UILabel", DefaultStrings.UpdateFirmwareMessage)) {
			Gui.touchAVIew("UIButtonLabel", DefaultStrings.FirmwareMessageTurnOffButtonTag);
		}
	}

	public static void signUp(String email, String password, boolean isMale, int date, int month, int year, boolean isUSUnit, String h1, String h2, String w1, String w2, int goalLevel) {
		
		LaunchScreen.launch();

		// email and password
		SignUp.tapSignUp();
		SignUp.enterEmailPassword(email, password);
		waitForView("UILabel", DefaultStrings.SignUpProfileTitle);

		// profile
		SignUp.enterGender(isMale);
		SignUp.enterBirthDay(String.valueOf(year), PrometheusHelper.getMonthString(month, true), String.valueOf(date));
		SignUp.enterHeight(h1, h2, isUSUnit);
		SignUp.enterWeight(w1, w2, isUSUnit);
		SignUp.tapShine();
		SignUp.tapNext();
		
		// Select Shine
		SignUp.tapSelectDevice(SignUp.SELECT_SHINE);

		// goal
		SignUp.setGoal(goalLevel);
		SignUp.tapNext();
		waitForView("UILabel", DefaultStrings.SignUpLinkShineTitle);

		// linking shine
		SignUp.sync();

		// Backed to "Device select" view. Click next
		SignUp.tapNext();
		
		// tutorial
		waitForView("PTRichTextLabel", DefaultStrings.TutorialFirstPageLabel);
		PrometheusHelper.handleTutorial();
		
		// firmware popup
		PrometheusHelper.handleUpdateFirmwarePopup();
		
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

	public static String signUpDefaultProfile() {
		String email = MVPApi.generateUniqueEmail();
		String password = "qwerty1";
		
		return signUpDefaultProfile(email, password);
	}

	public static String signUpDefaultProfile(String email, String password) {
		LaunchScreen.launch();
		ShortcutsTyper.delayOne();

		// email and password
		SignUp.tapSignUp();
		SignUp.enterEmailPassword(email, password);
		waitForView("UILabel", DefaultStrings.SignUpProfileTitle);

		// profile
		SignUp.enterGender(true);
		SignUp.enterBirthDay();
		SignUp.enterHeight();
		SignUp.enterWeight();
		SignUp.tapShine();
		SignUp.tapNext();
		
		// Select Shine
		SignUp.tapSelectDevice(SignUp.SELECT_SHINE);

		// goal
		SignUp.setGoal(1);
		SignUp.tapNext();
		waitForView("UILabel", DefaultStrings.SignUpLinkShineTitle);

		// linking shine
		SignUp.sync();
		
		// Backed to "Device select" view. Click next
		SignUp.tapNext();
		
		// tutorial
		waitForView("PTRichTextLabel", DefaultStrings.TutorialFirstPageLabel);
		PrometheusHelper.handleTutorial();
		
		return email;
	}
	
	public static void signIn(String email, String password) {
		
		LaunchScreen.launch();
		SignIn.tapLogIn();
		ShortcutsTyper.delayOne();
		SignIn.enterEmailPassword(email, password);
		waitForViewToDissappear("UILabel", DefaultStrings.SignInTitle);
	}
	
	public static void signOut() {
		PrometheusHelper.handleUpdateFirmwarePopup();
		HomeScreen.tapOpenSettingsTray();
		HomeScreen.tapSettings();
		HomeSettings.tapSignOut();
		HomeSettings.chooseSignOut();
	}
	
	public static void sync() {
		Gui.sync();
	}
	
	/* Time control */
	public static void waitForAlert() {
		
		waitForAlert(DEFAULT_TIMEOUT);
	}
	
	public static void waitForAlert(int timeout) {
		
		Callable<Boolean> condition = new Callable<Boolean>() {

			public Boolean call() {
				return Gui.hasAlert();
			}
		};
		
		waitForCondition(condition, timeout);
	}
	
	public static void waitForView(final String viewType, final int index) {
		
		waitForView(viewType, index, DEFAULT_TIMEOUT);
	}
	
	public static void waitForView(final String viewType, final int index, int timeout) {
		
		Callable<Boolean> condition = new Callable<Boolean>() {

			public Boolean call() {
				return ViewUtils.isExistedView(viewType, index);
			}
		};
		
		waitForCondition(condition, timeout);
	}
	
	public static void waitForView(String viewType, String title) {
		
		waitForView(viewType, title, DEFAULT_TIMEOUT);
	}
	
	public static void waitForView(final String viewType, final String title, int timeout) {
		
		Callable<Boolean> condition = new Callable<Boolean>() {

			public Boolean call() {
				return ViewUtils.isExistedView(viewType, title);
			}
		};
		
		waitForCondition(condition, timeout);
	}
	
	public static void waitForViewToDissappear(String viewType, String title) {
		
		waitForViewToDissappear(viewType, title, DEFAULT_TIMEOUT);
	}
	
	public static void waitForViewToDissappear(final String viewType, final String title, int timeout) {
		
		Callable<Boolean> condition = new Callable<Boolean>() {

			public Boolean call() {
				return !ViewUtils.isExistedView(viewType, title);
			}
		};
		
		waitForCondition(condition);
	}
	
	public static void waitForThrobberToDissappear() {
		
		waitForThrobberToDissappear(DEFAULT_TIMEOUT);
	}
	
	public static void waitForThrobberToDissappear(int timeout) {
		
		Callable<Boolean> condition = new Callable<Boolean>() {

			public Boolean call() {
				return !ViewUtils.isExistedView("UIActivityIndicatorView", 0);
			}
		};
		
		waitForCondition(condition);
	}
	
	public static void waitForCondition(Callable<Boolean> condition) {
		
		waitForCondition(condition, DEFAULT_TIMEOUT);
	}
	
	public static void waitForCondition(Callable<Boolean> condition, int timeout) {
		
		try {
			int total = 0;
			while(!condition.call()) {
				total += 1;

				if(total > timeout)
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* Debug */	
	public void startApp() {
		(new Thread() {
			public void run() {
				AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), "script/automation/loop.js");
			}
		}).start();
		ShortcutsTyper.delayTime(20000);
	}
	
	public static String convertNearestTimeInMinuteToString(int minutes) {
		// use for iOS only
		if (minutes < 60)
			return minutes + " mins";
		else if (minutes == 60)
			return "1 hour";

		float hours = minutes / 60f;
		if(minutes % 60 == 0)
			return String.format("%.0f hours", hours);
		
		return String.format("%.1f hours", hours);
	}
	
	public static void main(String[] args) {
		Gui.init("192.168.1.144");
		Gui.shutdown();
	}

}