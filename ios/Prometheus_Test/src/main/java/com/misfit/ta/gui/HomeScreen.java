package com.misfit.ta.gui;

import java.util.Calendar;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.gui.social.LeaderboardView;
import com.misfit.ta.utils.ShortcutsTyper;

public class HomeScreen {

	/* Navigation */
	public static void tapOpenSettingsTray() {
		PrometheusHelper.handleUpdateFirmwarePopup();
		tapOpenSettingsTrayWithoutPopupHandling();
	}
	
	public static void tapTrackNow(){
		Gui.touchATaggedView("UIButton", 1);
	}
	
	public static boolean isTrueActivity(String activity){
		return ViewUtils.isExistedView("UILabel", activity);
	}
	
	public static boolean isActivityDialog(){
		return ViewUtils.isExistedView("UIButtonLabel", DefaultStrings.StartButton);
	}
	
	public static void tapOpenSettingsTrayWithoutPopupHandling() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.MenuButtonTag);
	}

	public static void tapSettings() {
		Gui.touchAVIew("PTListItemView", DefaultStrings.SettingsButton);
	}

	public static void tapStartButton(){
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.StartButton);
	}
	
	public static void tapStopButton(){
		Gui.touchAVIew("UIButton", DefaultStrings.StopButton);
	}
	
	public static void tapAdjustGoal() {
		Gui.touchAVIew("PTListItemView", DefaultStrings.MyGoalButton);
	}

	public static void tapSetAlarmTime(){
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.AlarmButton);
	}
	
	public static void tapChangeSleepSound(){
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.SleepSoundButton);
	}
	public static void tapMyShine() {
		Gui.touchAVIew("PTListItemView", DefaultStrings.MyShineButton);
	}
	
	public static void tapDevices(){
		Gui.touchAVIew("PTListItemView", DefaultStrings.DevicesButton);
	}
	
	public static void tapActiveDevice(){
		Gui.touchAVIew("PTDeviceItemView", 1);
	}

	public static void tapSetWeightGoal() {
		Gui.touchAVIew("UIButton", DefaultStrings.SetWeightGoalButton);
	}
	
	
	public static void tapOpenManualInput() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.ManualButton);
	}

	public static boolean isSleepSoundScreen(){
		return ViewUtils.isExistedView("UILabel", DefaultStrings.SleepSoundLabel);
	}
	
	public static void tapProgressCircle() {
		Gui.touchAVIew("PTGoalCircleView", 0);
	}

	public static void tapArrowButtonToday() {
		Gui.touchAVIew("UIButton", DefaultStrings.TodayButtonTag);
	}
	
	public static void tapActivityTimeline() {
		Gui.touchAVIew("UIButton", DefaultStrings.MenuActivityTimelineButtonTag);
	}
	
	public static void tapSleepTimeline() {
		Gui.touchAVIew("UIButton", DefaultStrings.MenuSleepTimelineButtonTag);
	}
	
	public static void tapWeightTimeline() {
		Gui.touchAVIew("UIButton", DefaultStrings.MenuWeightTimelineButtonTag);
	}
	
	public static void tapMenuSocial() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.MenuSocial);
		PrometheusHelper.waitForThrobberToDissappear();
	}
	
	
	public static void goToTodayViewOfLeaderboard() {
		tapMenuSocial();
		LeaderboardView.tapToday();
	}
	
	
	public static void tapWordView() {
		tapMenuSocial();
		ShortcutsTyper.delayTime(2000);
		Gui.touchAVIew("UIButton", DefaultStrings.WorldViewButtonLabel);
	}
	
	public static void tapSocialProfile() {
		tapMenuSocial();
		Gui.touchAVIew("UIButton", DefaultStrings.SocialProfileButtonLabel);
	}
	
	public static void tapProfileSocial() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.SocialProfileButtonLabel);
	}
	
	public static void tapAddTrackingActivity() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.AddTrackingActivity);
	}
	
	public static void tapLogExercise() {
		Gui.touchATaggedView("UIButton", 2);
	}
	
	public static void tapLinkNow() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.LinkNowButton);
	}
	
	public static void tapDone(){
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.DoneButton);
	}
	
	public static void tapCancel(){
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.CancelButton);
	}
	
	public static void tapForwardActivity(){
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.ForwardLabel);
	}
	
	public static void tapBackwardActivity(){
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.BackwardLabel);
	}
	
	public static void tapSyncLater(){
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.SyncLaterButton);
	}
	
	public static boolean isDialogTracking(){
		return ViewUtils.isExistedView("UIButton", DefaultStrings.TrackNowButton);
	}
	
	static public void switchAutoSleepTrackingOn() {

		String cmd = "((ViewUtils findViewWithViewName:@\"UISwitch\" andIndex: 1) setOn:1 animated:1)";
		NuRemoteClient.sendToServer(cmd);
	}
	
	static public void switchAutoSleepTrackingOff() {

		String cmd = "((ViewUtils findViewWithViewName:@\"UISwitch\" andIndex: 1) setOn:0 animated:1)";
		NuRemoteClient.sendToServer(cmd);
	}
	
	static public void switchActivityTaggingOn() {

		String cmd = "((ViewUtils findViewWithViewName:@\"UISwitch\" andIndex: 0) setOn:1 animated:1)";
		NuRemoteClient.sendToServer(cmd);
	}
	
	static public void switchActivityTaggingOff() {

		String cmd = "((ViewUtils findViewWithViewName:@\"UISwitch\" andIndex: 0) setOn:0 animated:1)";
		NuRemoteClient.sendToServer(cmd);
	}
	

	/* Manual input */
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
	public static void tapPreviousDayButton(int days) {
		for (int i = 0; i < days; i++) {
			Gui.touchAVIew("UIButton", "<");
		}
	}

	public static void tapNextDayButton(int days) {
		for (int i = 0; i < days; i++) {
			Gui.touchAVIew("UIButton", ">");
		}
	}
	
	public static void tapLevelActivity(int level){
		Gui.touchATaggedView("UIButton", level);
	}
	
	public static void tapStartTimeButton(){
		Gui.touchATaggedView("UIButton", 1);
	}
	
	public static void tapEndTimeButton(){
		Gui.touchATaggedView("UIButton", 2);
	}
	
	public static void sync() {
		PrometheusHelper.sync();
	}

	public static void pullToRefresh() {
				
		Gui.swipeLeft(1000);
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}

	
	/* Visible checking */
	public static boolean isHomeScreen() {
		return ViewUtils.isExistedView("UIButton", DefaultStrings.MenuButtonTag);
	}
	
	public static boolean isAlarmScreen(){
		return ViewUtils.isExistedView("UILabel", DefaultStrings.TonightTitle);
	}
	
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
		return !isSummaryProgressCircle();
	}

	public static boolean isSummaryProgressCircle() {
		String text = Gui.getProperty("PTRichTextLabel", 0, "text");
		return text.matches(".* steps$");
	}

	public static boolean isTutorialProgressCircle() {
		
		String text = Gui.getProperty("PTBottomHalfCircleLabel", 0, "text");
		return text.contains("tap for steps");
	}

	public static boolean isNoActivity() {
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
	
	
	public static boolean isActivityTimeline() {

		return ViewUtils.isExistedView("PTGoalCircleView", 0);
	}

	public static boolean isActivityTimelineBedditPR() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.LinkNowButton) &&
				ViewUtils.isExistedView("UILabel", DefaultStrings.BuyNowButton);
	}
	
	public static boolean isSleepTimeline() {

		return ViewUtils.isExistedView("PTSleepGoalCircleView", 0);
	}

	public static boolean isWeightTimeline() {

		return ViewUtils.isExistedView("UILabel", DefaultStrings.CurrentWeightLabel);
	}
	
	public static boolean isWeightTimeline(String currentWeightLabel) {

		return ViewUtils.isExistedView("UILabel", currentWeightLabel);
	}
	
	public static boolean isWeightTimelineInitial() {

		return ViewUtils.isExistedView("UILabel", DefaultStrings.SetWeightGoalButton);
	}
	
	public static boolean isLogActivityScreen(){
		return ViewUtils.isExistedView("UILabel", DefaultStrings.LogActivityLabel);
	}
	
	// get
	public static String getCurrentActivityGoalString() {

		String circleView = String.format("(ViewUtils findViewWithViewName: \"%s\" andIndex: %d)", "PTGoalCircleView", 0);
		String labelView = String.format("(ViewUtils findViewWithViewName: \"%s\" andIndex: %d inView: %s)", "UILabel", 1, circleView);
		String cmd = String.format("(Gui getValueWithPropertyName: @\"%s\" inView: %s)", "text", labelView);

		NuRemoteClient.sendToServer(cmd);
		return NuRemoteClient.getLastMessage();
	}

	public static String getCurrentSleepGoalString() {

		String circleView = String.format("(ViewUtils findViewWithViewName: \"%s\" andIndex: %d)", "PTSleepGoalCircleView", 0);
		String labelView = String.format("(ViewUtils findViewWithViewName: \"%s\" andIndex: %d inView: %s)", "UILabel", 1, circleView);
		String cmd = String.format("(Gui getValueWithPropertyName: @\"%s\" inView: %s)", "text", labelView);

		NuRemoteClient.sendToServer(cmd);
		return NuRemoteClient.getLastMessage();
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
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.WeekDayButtonTag);
	}

	public static void changeToWeekView() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.WeekDayButtonTag);
	}

	public static void setSound(String[] sounds){
		Gui.touchAVIew("UILabel", sounds[0]);
		Gui.swipeUp(150);
		Gui.touchAVIew("UILabel", sounds[1]);
	}
	
	public static void inputTime(String[] times){
		Gui.touchAVIew("UIDatePicker", 3);
		Gui.setPicker(0, times[0]);
		Gui.setPicker(1, times[1]);
		Gui.setPicker(2, times[2]);
		Gui.setPicker(3, times[3]);
	}
}
