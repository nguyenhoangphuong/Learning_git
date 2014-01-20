package com.misfit.ta.ios.tests;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.RequestHelper;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.gui.AppInstaller;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	
	public static void main(String[] args) throws IOException {
		
//		String token = MVPApi.signIn("nhhai16991@gmail.com", "qqqqqq").token;
//		List<TimelineItem> items = MVPApi.getTimelineItems(token, System.currentTimeMillis() / 1000 - 3600 * 24 * 15, Integer.MAX_VALUE, 0);
//		for(TimelineItem item : items) {
//			logger.info(item.toJson());
//		}
//		Gui.init("192.168.1.111");
//		Gui.hasAlert();
//		Gui.touchPopupButton("OK");
		
		AppInstaller.launchInstrument();
		
//		Thread server1Thread = new Thread(new Runnable() {
//			public void run() {
//				ResourceConfig rc = new PackagesResourceConfig("com.misfit.ta.backend.aut.server.server1");
//				try {
//					HttpServerFactory.create("http://localhost:9999/", rc).start();
//				} catch (IllegalArgumentException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		
//		Thread server2Thread = new Thread(new Runnable() {
//			public void run() {
//				ResourceConfig rc = new PackagesResourceConfig("com.misfit.ta.backend.aut.server.server2");
//				try {
//					HttpServerFactory.create("http://localhost:9998/", rc).start();
//				} catch (IllegalArgumentException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//        
//		server1Thread.start();
//		server2Thread.start();
        
	}
}