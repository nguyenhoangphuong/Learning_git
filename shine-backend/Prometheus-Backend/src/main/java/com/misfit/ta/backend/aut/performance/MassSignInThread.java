package com.misfit.ta.backend.aut.performance;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.metawatch.MVPApi;

public class MassSignInThread implements Runnable {

	Logger logger = Util.setupLogger(MassSignInThread.class);

	public MassSignInThread() {
	}

	public void run() {
	    long now = System.currentTimeMillis();
	    long start = now - 259200;
	    String token = "1469078439194972865-a9195054310dbc5a6bcab46a31f3b847";
	    MVPApi.getGraphItems(token, 0l , now, 0l);
	    
		System.out.println("LOG [BackendStressTestThread.run]: ------------------------------------ DONE");
	}


}
