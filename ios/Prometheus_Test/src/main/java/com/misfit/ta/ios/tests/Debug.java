package com.misfit.ta.ios.tests;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.common.MVPEnums;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.utils.ShortcutsTyper;

public class Debug {
	
	private static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) {
//		Gui.init("192.168.1.188");
		
//		Timeline.openTile("6:42pm");
		for(int i = 1; i <= 50; i++) {
			String s1 = MVPCalculator.convertNearestTimeInMinuteToString(MVPCalculator.calculateNearestTimeRemainInMinute(i * 100, MVPEnums.ACTIVITY_WALKING));
			String s2 = MVPCalculator.convertNearestTimeInMinuteToString(MVPCalculator.calculateNearestTimeRemainInMinute(i * 100, MVPEnums.ACTIVITY_RUNNING));
			String s3 = MVPCalculator.convertNearestTimeInMinuteToString(MVPCalculator.calculateNearestTimeRemainInMinute(i * 100, MVPEnums.ACTIVITY_SWIMMING));
			System.out.println((i * 100) + " points: " + s1 + " - " + s2 + " - " + s3);
		}
	}
}
