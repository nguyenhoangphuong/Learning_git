package com.misfit.ta.gui;

import java.util.Calendar;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class HomeScreen {

	/* Navigation */
	public static void tapOpenSettingsTray() {
		PrometheusHelper.handleUpdateFirmwarePopup();
		Gui.touchAVIew("UILabel", DefaultStrings.MenuButton);
	}

	public static void tapSettings() {
		Gui.touchAVIew("UILabel", DefaultStrings.SettingsButton);
	}

	public static void tapAdjustGoal() {
		Gui.touchAVIew("UILabel", DefaultStrings.MyGoalButton);
	}

	public static void tapMyShine() {
		Gui.touchAVIew("UILabel", DefaultStrings.MyShineButton);
	}

	public static void tapOpenManualInput() {
		Gui.touchAVIew("UIButtonLabel", "Manual");
	}

	public static void tapProgressCircle() {
		Gui.touchAVIew("PTGoalCircleView", 0);
	}

	public static void chooseSleep() {
		Gui.touchAVIew("UILabel", DefaultStrings.SleepLabel);
	}

	public static void chooseCycling() {
		Gui.touchAVIew("UILabel", DefaultStrings.CyclingLabel);
	}

	public static void chooseSwimming() {
		Gui.touchAVIew("UILabel", DefaultStrings.SwimmingLabel);
	}

	public static void chooseTennis() {
		Gui.swipeUp(10);
		Gui.touchAVIew("UILabel", DefaultStrings.TennisLabel);
	}

	public static void chooseSoccer() {
		Gui.swipeUp(10);
		Gui.touchAVIew("UILabel", DefaultStrings.SoccerLabel);
	}

	public static void chooseBasketball() {
		Gui.swipeUp(10);
		Gui.touchAVIew("UILabel", DefaultStrings.BasketballLabel);
	}

	public static void tapArrowButtonToday() {
		Gui.touchAVIew("UIButton", 0);
	}

	/* Manual input */
	public static void tapRandom() {
		Gui.touchAVIew("UIButtonLabel", "Random");
	}

	public static void tapDormant() {
		Gui.touchAVIew("UIButtonLabel", "Dormant");
	}

	public static void tapGenerate1440Activities() {
		Gui.touchAVIew("UIButtonLabel", "Generate 1440 activities");
	}

	public static void tapSave() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.SaveButton);
	}

	public static void enterManualActivity(String[] times, int duration,
			int steps) {
		PrometheusHelper.inputManualRecord(times, duration, steps);
	}

	/* Timeline */
	public static void goToPreviousDays(int days) {
		for (int i = 0; i < days; i++) {
			Gui.touchAVIew("UIButton", 4);
		}
	}

	public static void goToNextDays(int days) {
		for (int i = 0; i < days; i++) {
			Gui.touchAVIew("UIButton", 5);
		}
	}
	
	public static void goToLastWeek() {
		Gui.touchAVIew("UIButton", 4);
	}
	
	public static void goToThisWeek() {
		// from last week to this week
		Gui.touchAVIew("UIButton", 5);
	}

	public static void dragUpTimeline() {
		Gui.drag(240, 300, 240, 100);
	}

	public static void dragDownTimeline() {
		Gui.drag(240, 200, 240, 400);
	}

	public static void sync() {
		PrometheusHelper.sync();
	}

	public static void pullToRefresh() {
		Gui.swipeLeft(1000);
	}

	/* Visible checking */
	public static boolean isToday() {
		return ViewUtils.isExistedView("UILabel", DefaultStrings.TodayTitle);
	}

	public static boolean isYesterday() {
		return ViewUtils
				.isExistedView("UILabel", DefaultStrings.YesterdayTitle);
	}

	public static boolean isADayBefore(int days) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, -days);
		StringBuilder title = new StringBuilder(PrometheusHelper.getDayOfWeek(
				now.get(Calendar.DAY_OF_WEEK), false));
		title.append(", ");
		title.append(PrometheusHelper.getMonthString(
				now.get(Calendar.MONTH) + 1, false));
		title.append(" ");
		title.append(now.get(Calendar.DATE));
		System.out.println(title.toString());
		return ViewUtils.isExistedView("UILabel", title.toString());
	}

	public static boolean isThisWeek() {
		return ViewUtils.isExistedView("UILabel", DefaultStrings.ThisWeekTitle);
	}

	public static boolean isLastWeek() {
		return ViewUtils.isExistedView("UILabel", DefaultStrings.LastWeekTitle);
	}

	public static boolean isPointEarnedProgessCircle() {
		String text = Gui.getProperty("UILabel", 3, "text");
		return text.matches("^of .* points$");
	}

	public static boolean isSummaryProgressCircle() {
		String text = Gui.getProperty("PTRichTextLabel", 0, "text");
		return text.matches(".* steps$");
	}

	public static boolean isTodayDefault() {
		return ViewUtils.isExistedView("UILabel",
				DefaultStrings.NoActivityLabel);
	}

	// alerts
	public static boolean hasSuggestWearingPositionForSwimmingMessage() {
		return Gui.hasAlert()
				&& DefaultStrings.WearingPositionForSwimmingContent.equals(Gui
						.getPopupContent())
				&& DefaultStrings.WearingPositionForSwimmingTitle.equals(Gui
						.getPopupTitle());
	}

	public static boolean hasSuggestWearingPositionForCyclingMessage() {
		return Gui.hasAlert()
				&& DefaultStrings.WearingPositionForCyclingContent.equals(Gui
						.getPopupContent())
				&& DefaultStrings.WearingPositionForCyclingTitle.equals(Gui
						.getPopupTitle());
	}

	public static boolean hasSuggestWearingPositionForSleepMessage() {
		return Gui.hasAlert()
				&& DefaultStrings.WearingPositionForSleepContent.equals(Gui
						.getPopupContent())
				&& DefaultStrings.WearingPositionForSleepTitle.equals(Gui
						.getPopupTitle());
	}

	public static void changeToDayView() {
		Gui.touchAVIew("UILabel", DefaultStrings.TodayButton);
	}

	public static void changeToWeekView() {
		Gui.touchAVIew("UILabel", DefaultStrings.WeekButton);
	}
}
