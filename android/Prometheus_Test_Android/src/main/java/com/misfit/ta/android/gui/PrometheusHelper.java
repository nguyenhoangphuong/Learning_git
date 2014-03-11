package com.misfit.ta.android.gui;

import java.util.Random;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class PrometheusHelper {
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

		ShortcutsTyper.delayTime(4000);
		SignUp.inputSex(true);
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

		SignUp.linkShine();
	}

	public static void signUp() {
		String email = generateUniqueEmail();
		signUp(email);
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
}
