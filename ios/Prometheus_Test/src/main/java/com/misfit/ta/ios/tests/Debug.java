package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.gui.social.LeaderboardView;
import com.misfit.ta.gui.social.SocialProfileView;
import com.misfit.ta.gui.social.WorldFeedView;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws IOException {
		
		
//		Gui.init("192.168.1.115");		
//		SocialProfileView.tapAlbumImage(0);
		BackendHelper.link("nhhai16991@gmail.com", "qqqqqq", "science019");
	}
}
	