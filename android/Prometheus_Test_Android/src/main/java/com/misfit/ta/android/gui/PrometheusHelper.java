package com.misfit.ta.android.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.chimpchat.core.TouchPressType;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class PrometheusHelper {

	private static String[] longMonths = { "January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December" };
	private static String[] shortMonths = { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	public static String generateUniqueEmail() {
		return "test" + System.currentTimeMillis()
				+ TextTool.getRandomString(6, 6) + "@misfitqa.com";
	}

	public static int randInt(int includeFrom, int excludeTo) {
		Random r = new Random();
		return r.nextInt(excludeTo - includeFrom) + includeFrom;
	}

	public static void signUp(String email) {
		SignUp.chooseSignUp();
		int fullScreenHeight = Gui.getScreenHeight();
		int fullScreenWidth = Gui.getScreenWidth();
		String password = "test12";
		SignUp.fillSignUpForm(email, password);
		SignUp.pressNext();
		// input profile

		ShortcutsTyper.delayTime(6000);
		PrometheusHelper.editGender(true);
		ShortcutsTyper.delayTime(500);
		SignUp.inputUnits(true);
		ShortcutsTyper.delayTime(500);
		SignUp.chooseDefaultHeightValue(fullScreenHeight, fullScreenWidth);
		ShortcutsTyper.delayTime(500);
		SignUp.chooseDefaultWeightValue(fullScreenHeight, fullScreenWidth);
		ShortcutsTyper.delayTime(500);
		SignUp.chooseDefaultBirthdate(fullScreenHeight, fullScreenWidth);
		ShortcutsTyper.delayOne();
		SignUp.pressNext();
		ShortcutsTyper.delayTime(2000);
		SignUp.linkShine(fullScreenHeight, fullScreenWidth);
		ShortcutsTyper.delayTime(2000);
		SignUp.handleSignUpTutorial();
	}

	public static void signUp() {
		String email = generateUniqueEmail();
		signUp(email);
	}
	
	public static void signOut(int fullScreenHeight, int fullScreenWidth) {
		HomeScreen.signOutAtHomeScreen(fullScreenHeight, fullScreenWidth);
	}

	public static float calculatePoint(int lastSteps, int lastDuration,
			int activityType) {
		return (float) MVPCalculator.calculatePoint(lastSteps, lastDuration,
				activityType);
	}

	public static float calculateMiles(int lastSteps, int lastDuration,
			float heightInInches) {
		return (float) MVPCalculator.calculateMiles(lastSteps, lastDuration,
				heightInInches);
	}

	public static String convertNearestTimeInMinuteToString(int minutes) {
		// use for Android only
		if (minutes < 60) {
			return String.format(
					DefaultStrings.RemainWalkingTimeInMinutesStringFormat,
					minutes);
		} else if (minutes == 60) {
			return DefaultStrings.RemainWalkingTimeOneHourText;
		} else {
			float hours = minutes / 60f;
			if (minutes % 60 == 0) {
				return String.format(
						DefaultStrings.RemainWalkingTimeInHrsStringFormat,
						hours);
			} else {
				return String
						.format(DefaultStrings.RemainWalkingTimeInHrsWithDecimalStringFormat,
								hours);
			}
		}
	}

	public static String convertNearestTimeInMinuteToStringNumber(int minutes) {
		// use for Android only
		if (minutes < 60) {
			return String.valueOf(minutes);
		} else {
			if (minutes % 60 == 0) {
				return String.valueOf((int) minutes / 60);
			} else {
				return String.valueOf(minutes / 60f);
			}
		}
	}

	public static void dismissPopup(int fullScreenHeight, int fullScreenWidth,
			String buttonText) {
		int popupHeight = Gui.getHeight();
		int popupWidth = Gui.getWidth();
		ViewNode okButton = ViewUtils.findView("TextView", "mText", buttonText,
				0);
		Gui.touchViewOnPopup(fullScreenHeight, fullScreenWidth, popupHeight,
				popupWidth, okButton);
		// Magic line which makes ViewServer reload views after we dismiss popup
		ShortcutsTyper.delayTime(50);
	}

	public static String getCurrentValueInPicker(int index) {
		ViewNode currentGoalView = ViewUtils.findView("ShineCustomEditText",
				"mID", DefaultStrings.ShineCustomEditTextInPickerId, index);
		return currentGoalView.text;
	}

	public static void editBirthDate(String day, String month, String year,
			int fullScreenHeight, int fullScreenWidth) {
		List<String> months = new ArrayList<String>(Arrays.asList(shortMonths));
		String currentMonth = PrometheusHelper.getCurrentValueInPicker(0);
		int currentMonthIndex = months.indexOf(currentMonth);
		int monthIndex = months.indexOf(month);
		monthIndex = monthIndex == -1 ? 0 : monthIndex;
		editValueOnPicker(currentMonthIndex, monthIndex, 0, fullScreenHeight,
				fullScreenWidth);

		String currentDay = PrometheusHelper.getCurrentValueInPicker(1);
		int currentDayIndex = Integer.valueOf(currentDay);
		int dayIndex = Integer.valueOf(day);
		editValueOnPicker(currentDayIndex, dayIndex, 1, fullScreenHeight,
				fullScreenWidth);

		String currentYear = PrometheusHelper.getCurrentValueInPicker(2);
		int currentYearNumber = Integer.valueOf(currentYear);
		int yearNumber = Integer.valueOf(year);
		int maxYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
		yearNumber = yearNumber < 1900 ? 1900 : yearNumber > maxYear ? maxYear
				: yearNumber;
		editValueOnPicker(currentYearNumber, yearNumber, 2, fullScreenHeight,
				fullScreenWidth);
	}

	public static void editWeightInLbs(int digit, int fraction,
			int fullScreenHeight, int fullScreenWidth) {
		// in lbs
		int maxDigit = 1399;
		int minDigit = 22;
		int maxFraction = 9;
		int minFraction = 0;
		System.out.println("Start to edit weight value");
		String currentDigit = PrometheusHelper.getCurrentValueInPicker(0);
		int currentDigitNumber = Integer.valueOf(currentDigit);
		digit = digit < minDigit ? minDigit : digit > maxDigit ? maxDigit
				: digit;
		System.out.println("Weight digit is changed from: "
				+ currentDigitNumber + " to " + digit);
		editValueOnPicker(currentDigitNumber, digit, 0, fullScreenHeight,
				fullScreenWidth);

		String currentFraction = PrometheusHelper.getCurrentValueInPicker(1);
		int currentFractionNumber = Integer.valueOf(currentFraction
				.substring(1));
		fraction = fraction < minFraction ? minFraction
				: fraction > maxFraction ? maxFraction : fraction;
		System.out.println("Weight fraction is changed from: "
				+ currentFractionNumber + " to " + fraction);
		editValueOnPicker(currentFractionNumber, fraction, 1, fullScreenHeight,
				fullScreenWidth);

	}

	public static void editGender(Boolean isMale) {
		if (isMale) {
			Gui.touchAView("RadioButton", "mID", DefaultStrings.MaleButtonId);
		} else {
			Gui.touchAView("RadioButton", "mID", DefaultStrings.FemaleButtonId);
		}
	}

	public static void editHeightInInches(int digit, int fraction,
			int fullScreenHeight, int fullScreenWidth) {
		int maxDigit = 8;
		int minDigit = 1;
		int maxFraction = 11;
		int minFraction = 0;
		System.out.println("Start to edit height value");
		String currentDigit = PrometheusHelper.getCurrentValueInPicker(0);
		int currentDigitNumber = Integer.valueOf(currentDigit.substring(0, 1));
		digit = digit < minDigit ? minDigit : digit > maxDigit ? maxDigit
				: digit;
		System.out.println("Height digit is changed from: "
				+ currentDigitNumber + " to " + digit);
		editValueOnPicker(currentDigitNumber, digit, 0, fullScreenHeight,
				fullScreenWidth);

		String currentFraction = PrometheusHelper.getCurrentValueInPicker(1);
		int currentFractionNumber = Integer.valueOf(currentFraction.substring(
				0, currentFraction.indexOf("\"")));
		fraction = fraction < minFraction ? minFraction
				: fraction > maxFraction ? maxFraction : fraction;
		System.out.println("Height fraction is changed from: "
				+ currentFractionNumber + " to " + fraction);
		editValueOnPicker(currentFractionNumber, fraction, 1, fullScreenHeight,
				fullScreenWidth);

	}

	public static void editSleepGoal(int hour, int minute,
			int fullScreenHeight, int fullScreenWidth) {
		int maxHour = 16;
		int minHour = 1;
		int maxMinute = 45;
		int minMinute = 0;
		System.out
				.println("Start to edit sleep goal to " + hour + ":" + minute);
		String currentHour = PrometheusHelper.getCurrentValueInPicker(0);
		int currentHourNumber = Integer.valueOf(currentHour);
		hour = hour < minHour ? minHour : hour > maxHour ? maxHour : hour;
		System.out.println("Hour value is changed from: " + currentHourNumber
				+ " to " + hour);
		editSleepGoalOnPicker(currentHourNumber, hour, 0, fullScreenHeight,
				fullScreenWidth, 1);

		String currentMinute = PrometheusHelper.getCurrentValueInPicker(1);
		int currentMinuteNumber = Integer.valueOf(currentMinute);
		minute = minute < minMinute ? minMinute
				: minute > maxMinute ? maxMinute : minute;
		System.out.println("Minute value is changed from: "
				+ currentMinuteNumber + " to " + minute);
		editSleepGoalOnPicker(currentMinuteNumber, minute, 1, fullScreenHeight,
				fullScreenWidth, 15);

	}

	public static void editValueOnPicker(int oldValue, int newValue,
			int pickerIndex, int fullScreenHeight, int fullScreenWidth) {
		ViewNode pickerNode = ViewUtils.findView("ShineCustomEditText", "mID",
				DefaultStrings.ShineCustomEditTextInPickerId, pickerIndex);
		int delta = Math.abs(newValue - oldValue);
		if (newValue < oldValue) {
			Settings.swipeDownValuePicker(fullScreenHeight, fullScreenWidth,
					Gui.getHeight(), Gui.getWidth(), pickerNode, delta);
		} else {
			Settings.swipeUpValuePicker(fullScreenHeight, fullScreenWidth,
					Gui.getHeight(), Gui.getWidth(), pickerNode, delta);
		}
	}

	public static void editSleepGoalOnPicker(int oldValue, int newValue,
			int pickerIndex, int fullScreenHeight, int fullScreenWidth,
			int delta) {
		ViewNode pickerNode = ViewUtils.findView("ShineCustomEditText", "mID",
				DefaultStrings.ShineCustomEditTextInPickerId, pickerIndex);
		int steps = Math.abs(newValue - oldValue) / delta;
		if (newValue < oldValue) {
			Settings.swipeDownValuePicker(fullScreenHeight, fullScreenWidth,
					Gui.getHeight(), Gui.getWidth(), pickerNode, steps);
		} else {
			Settings.swipeUpValuePicker(fullScreenHeight, fullScreenWidth,
					Gui.getHeight(), Gui.getWidth(), pickerNode, steps);
		}
	}

	public static String getMonthString(int monthNumber,
			boolean isLongMonthString) {
		return isLongMonthString ? longMonths[monthNumber - 1]
				: shortMonths[monthNumber - 1];
	}

	public static boolean coin() {
		return new Random().nextBoolean();
	}

	public static void manualInputActivity(String hour, String minute,
			int duration, int steps) {
		HomeScreen.tapManual();
		// input record
		HomeScreen.inputManualTime(hour, minute);
		HomeScreen.intputActivity(String.valueOf(duration),
				String.valueOf(steps));
		ShortcutsTyper.delayTime(500);
		HomeScreen.saveManual();
	}

	public static void manualInputSleep() {
		HomeScreen.tapManual();
		HomeScreen.sleepManual();
		ShortcutsTyper.delayTime(30000);
	}

	public static void pullToRefresh(int fullScreenWidth, int fullScreenHeight) {
		Gui.swipe(fullScreenWidth / 2, fullScreenHeight / 2,
				fullScreenWidth / 2, fullScreenHeight / 2 + 1200);
		Gui.touch(fullScreenWidth / 2, fullScreenHeight / 2 + 1200,
				TouchPressType.DOWN);
	}

	public static float calculateCalories(float points, float weightInLbs,
			float fullBMR, int currentMinute) {
		return (float) MVPCalculator.calculateCalories(points, weightInLbs,
				fullBMR, currentMinute);
	}

	public static float calculateFullBMR(float weightInLbs,
			float heightInInches, int age, boolean isMale) {
		return (float) MVPCalculator.calculateFullBMR(weightInLbs,
				heightInInches, age, isMale);
	}
}
