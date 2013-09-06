package com.misfit.ta.gui;

import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.utils.ShortcutsTyper;

public class Timezone {
	public static void changeTimezone(int offset) {
		Gui.setTimezoneWithGMTOffset(offset);
	}
	
	public static void touchTimezoneWithLabel(String label) {
		Gui.touchAVIew("UILabel", label);
	}
	
	public static void closeTimeTravelTile() {
		Gui.touchAVIew("UILabel", DefaultStrings.TileTimeTravelLabel);
	}
	
	public static void assertTimeZoneTile(String detail, int currentTimezone, int previousTimezone, int delta) {
		Gui.dragUpTimeline();
		ShortcutsTyper.delayOne();
		// check if there is a time travel tile
		String content = "";
		String label = "";
		if (previousTimezone >= 0) {
			content+= "UTC+" + String.valueOf(previousTimezone);
		} else {
			content+= "UTC" + String.valueOf(previousTimezone);
		}
		if (currentTimezone >= 0) {
			label = "UTC+" + String.valueOf(currentTimezone);
			content+= " to UTC+" + String.valueOf(currentTimezone);
		} else {
			label = "UTC" + String.valueOf(currentTimezone);
			content+= " to UTC" + String.valueOf(currentTimezone);
		}
		
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", label), "Time travel tile's title is visible");

		// tap on time travel tile and check content
		Gui.dragUpTimeline();
		Timezone.touchTimezoneWithLabel(label);
		ShortcutsTyper.delayOne();
		System.out.println("DEBUG: " + String.valueOf(currentTimezone));
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", content), "Time travel detail title is valid");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", detail), "Time travel message is valid");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", delta), "Time delta is valid");
		ShortcutsTyper.delayOne();
		ShortcutsTyper.delayOne();
		// close the time travel tile
		Timezone.closeTimeTravelTile();
		ShortcutsTyper.delayOne();
		Gui.dragDownTimeline();
		ShortcutsTyper.delayOne();
	}
	
	public static GoalsResult searchGoal(String email, String password) {
		String token = MVPApi.signIn(email, password).token;
		GoalsResult goalsResult = MVPApi.searchGoal(token, 0, Integer.MAX_VALUE, 0);
		return goalsResult;
	}

	public static void assertGoal(String email, String password, long beforeStartTime, long beforeEndTime, int delta, long beforeOffset, boolean isForward) {
		GoalsResult goalsResult = Timezone.searchGoal(email, password);
		Goal goal = goalsResult.goals[0];
		long afterStartTime = goal.getStartTime();
		long afterEndTime = goal.getEndTime();
		long afterOffset = goal.getTimeZoneOffsetInSeconds();
		Assert.assertTrue(afterStartTime == beforeStartTime, "Correct start time after changing timezone");
		Assert.assertTrue(afterEndTime == beforeEndTime + delta * 3600 * (isForward ? -1 : 1), "Correct end time after changing timezone");
		Assert.assertTrue(afterOffset == beforeOffset, "Correct offset after changing timezone");
	}
}
