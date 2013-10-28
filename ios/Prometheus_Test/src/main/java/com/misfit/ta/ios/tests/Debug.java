package com.misfit.ta.ios.tests;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.gui.Gui;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) {
		
//		Gui.init("192.168.1.144");
//		Gui.printView();
//		MVPApi.userInfo(MVPApi.signIn("nhhai16991@gmail.com", "qqqqqq").token);
		String token = MVPApi.signIn("qa104@a.a", "qqqqqq").token;
		for(int i = 1 ; i <= 4; i++)
			BackendHelper.completeGoalInPast(token, i);
	}
}
