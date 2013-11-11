package com.misfit.ta.gui;

import java.util.Calendar;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;

public class HomeScreen {

	/* Navigation */
	public static void tapOpenSettingsTray() {
//		PrometheusHelper.handleUpdateFirmwarePopup();
		Gui.touchAVIew("UIButton", DefaultStrings.MenuButtonId);
	}

	public static void tapSettings() {
		Gui.touchAVIew("PTListItemView", DefaultStrings.SettingsButtonId);
	}

	public static void tapAdjustGoal() {
		Gui.touchAVIew("PTListItemView", DefaultStrings.MyGoalButtonId);
	}

	public static void tapMyShine() {
		Gui.touchAVIew("PTListItemView", DefaultStrings.MyShineButtonId);
	}

	public static void tapOpenManualInput() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.ManualButton);
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
		Gui.touchAVIew("UILabel", DefaultStrings.TennisLabel);
	}

	public static void chooseSoccer() {
		Gui.swipeUp(10);
		Gui.touchAVIew("UILabel", DefaultStrings.SoccerLabel);
	}

	public static void chooseBasketball() {
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

	public static void tap180MinNap() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings._180MinNap);
	}
	
	public static void tap8HourSleep() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings._8HourSleep);
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

	public static void sync() {
		PrometheusHelper.sync();
	}

	public static void pullToRefresh() {
		
		// TODO: use something generic, currently we have to call dev code directly
		// swipe works on iOS6 but doesn't on iOS7
		
		//Gui.swipeLeft(1000);
		NuRemoteClient.sendToServer("((ViewUtils findViewWithViewName:@\"PTGoalCircleHorizontalScrollView\" andIndex:0) animateToWaitingPositionBeforeSyncingFinish)");
		NuRemoteClient.sendToServer("((ViewUtils findViewWithViewName:@\"PTGoalCircleHorizontalScrollView\" andIndex:0) startLoadingAnimation)");
		NuRemoteClient.sendToServer("((PTClientServerSyncService sharedService) startSyncing)");
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

	public static boolean isEditTagTutorial() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.TutorialForUpdateActivityTag) &&
				ViewUtils.isExistedView("UIButtonLabel", DefaultStrings.EndTutorialButton);
	}
	
	public static boolean isDeleteSleepTutorial() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.TutorialForDeleteSleepTile) &&
				ViewUtils.isExistedView("UIButtonLabel", DefaultStrings.EndTutorialButton);
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
