package com.misfit.ta.gui;

import org.testng.Assert;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;

public class Timezone {
	
	public static void changeTimezone(int offset) {
		Gui.setTimezoneWithGMTOffset(offset);
	}

	public static void assertTimeZoneTile(int currentTimezone, int previousTimezone, int delta, boolean isForward) {
		
		// content detail
		String content = String.format("UTC%+d to UTC%+d", previousTimezone, currentTimezone);
		String label = String.format("UTC%+d", currentTimezone);
		String[] detail = isForward ? DefaultStrings.TimezoneGainedTimeLabel : DefaultStrings.TimezoneLostTimeLabel;
		
		// tap on tile and check its content
		Timeline.dragUpTimelineAndHandleTutorial();
		Timeline.openTile(label);
		Assert.assertTrue(Timeline.isTimezoneTileCorrect(content, delta, detail),
				"Time travel tile's detail is correct");
		Timeline.closeCurrentTile();
		Timeline.dragDownTimeline();
	}
	
	public static GoalsResult searchGoal(String email, String password) {
		
		String token = MVPApi.signIn(email, password).token;
		GoalsResult goalsResult = MVPApi.searchGoal(token, 0l, (long)Integer.MAX_VALUE, 0l);
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
