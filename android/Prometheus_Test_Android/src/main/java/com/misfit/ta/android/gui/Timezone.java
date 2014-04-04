package com.misfit.ta.android.gui;

import org.testng.Assert;

import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;

public class Timezone {

	public static GoalsResult searchGoal(String email, String password) {

		String token = MVPApi.signIn(email, password).token;
		GoalsResult goalsResult = MVPApi.searchGoal(token, 0l,
				(long) Integer.MAX_VALUE, 0l);
		return goalsResult;
	}

	public static void assertGoal(String email, String password,
			long beforeStartTime, long beforeEndTime, int delta,
			long beforeOffset, boolean isForward) {

		GoalsResult goalsResult = Timezone.searchGoal(email, password);
		Goal goal = goalsResult.goals[0];
		long afterStartTime = goal.getStartTime();
		long afterEndTime = goal.getEndTime();
		long afterOffset = goal.getTimeZoneOffsetInSeconds();
		Assert.assertTrue(afterStartTime == beforeStartTime,
				"Correct start time after changing timezone");
		Assert.assertTrue(afterEndTime == beforeEndTime + delta * 3600
				* (isForward ? -1 : 1),
				"Correct end time after changing timezone");
		Assert.assertTrue(afterOffset == beforeOffset,
				"Correct offset after changing timezone");
	}

	public static void changeTimezone(int offset) {
		HomeScreen.tapManual();
		int timezoneOffsetInSeconds = offset * 3600;
		Gui.touchAView("TextView", "mID", DefaultStrings.TimezoneEditTextId);
		Gui.type(String.valueOf(timezoneOffsetInSeconds));
		HomeScreen.saveManual();
	}

}
