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
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.utils.ShortcutsTyper;

public class Debug {
	
	private static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) {
		Gui.init("192.168.1.188");
		
//		Timeline.openTile("6:42pm");
		for(int i = 0; i < 15; i++) {
		Gui.touchAVIew("PTTimelineItemSessionView", "6:42pm");
		Timeline.closeCurrentTile();
		}
	}
}
