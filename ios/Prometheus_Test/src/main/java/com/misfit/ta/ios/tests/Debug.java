package com.misfit.ta.ios.tests;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

import com.misfit.ios.AppHelper;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.data.sync.SyncDebugLog;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.common.MVPEnums;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.SignUp;
import com.misfit.ta.gui.Sync;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.report.TRS;
import com.misfit.ta.utils.ShortcutsTyper;

public class Debug {
	
	private static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) {
		
		Gui.init("192.168.1.185");
		
		int hour = 2;
		boolean usUnit = true;
		boolean[] passes = new boolean[] { false, false, false };
		String title = hour + ":00am";
		String[] messages = usUnit ? Timeline.LifetimeDistanceInUSUnitMessages : Timeline.LifetimeDistanceInSIUnitMessages;

		for (int i = 0; i < 3; i++) {
			
			Timeline.openTile(title);
			Gui.captureScreen("streaktile-" + System.nanoTime());

			if (!passes[0] && Timeline.isLifetimeDistanceBadgeTileCorrect(title, 2, messages[0]))
				passes[0] = true;

			if (!passes[1] && Timeline.isLifetimeDistanceBadgeTileCorrect(title, 6, messages[1]))
				passes[1] = true;
			
			if (!passes[2] && Timeline.isLifetimeDistanceBadgeTileCorrect(title, 12, messages[2]))
				passes[2] = true;

			Timeline.closeCurrentTile();
//			ShortcutsTyper.delayTime(1000);
		}
		
		Assert.assertTrue(passes[0] && passes[1] && passes[2], "All tiles have correct content");
	}
}
