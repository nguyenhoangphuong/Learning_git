package com.misfit.ta.ios.tests;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) {
		
		Gui.init("192.168.1.115");
		
//		String scrollView = "(ViewUtils findViewWithViewName:@\"PTGoalCircleHorizontalScrollView\" andIndex:0)";
//		NuRemoteClient.sendToServer(String.format("(%s scrollViewWillBeginDragging:%s)", scrollView, scrollView));
//		NuRemoteClient.sendToServer(String.format("(%s scrollViewDidScroll:%s)", scrollView, scrollView));
//		NuRemoteClient.sendToServer(String.format("(%s scrollViewDidEndDecelerating:%s willDecelerate:NO)", scrollView, scrollView));
//		NuRemoteClient.sendToServer(String.format("(%s scrollViewDidEndDragging:%s)", scrollView, scrollView));
		
//		Gui.swipe(20, 150, 0, 150);

		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", "Loading");

	}
}
