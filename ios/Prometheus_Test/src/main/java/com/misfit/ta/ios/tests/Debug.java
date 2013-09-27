package com.misfit.ta.ios.tests;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

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
import com.misfit.ta.gui.Sync;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.report.TRS;
import com.misfit.ta.utils.ShortcutsTyper;

public class Debug {
	
	private static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) {
		Gui.init("192.168.1.188");
		HomeScreen.tapOpenSettingsTray();
		HomeScreen.tapSettings();
		
			HomeSettings.tapKm();
		HomeSettings.tapBack();
	}
}
