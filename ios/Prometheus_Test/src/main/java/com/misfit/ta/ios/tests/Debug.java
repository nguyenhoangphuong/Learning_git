package com.misfit.ta.ios.tests;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.utils.ShortcutsTyper;

public class Debug {
	
	private static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) {
		Gui.init("192.168.1.247");
		
		String startTime = 7 + ":00am";
		String endTime = 7 + ":50am";
		
		Timeline.openTile(startTime);
		Assert.assertTrue(Timeline.isActivityTileCorrect(startTime, endTime, 50, 500, DefaultStrings.WalkingLevel[2]),
				"Activity tile is displayed correctly");
		Timeline.closeCurrentTile();
	}
}
