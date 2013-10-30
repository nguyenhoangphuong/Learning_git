package com.misfit.ta.ios.tests;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.PrometheusHelper;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) {
		
		Gui.init("192.168.1.115");
//		PrometheusHelper.enterBirthDay("1991", "September", "16");
		PrometheusHelper.enterHeight("4'", "8\\\"", true);
//		MVPApi.userInfo(MVPApi.signIn("nhhai16991@gmail.com", "qqqqqq").token);
	}
}
