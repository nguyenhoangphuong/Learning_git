package com.misfit.ta.backend;

import com.misfit.ta.Settings;

public class BackendTestEnvironment {

	static private String environment = Settings.getParameter("MVPBackendEnviroment");
	static public Boolean RequestLoggingEnable = 
			Settings.getParameter("MVPBackendRequestLoggingEnable") == null ? true :
				Settings.getParameter("MVPBackendRequestLoggingEnable").equals("true");
	
	static public boolean isStaging() {
		
		return environment == null || environment.equalsIgnoreCase("staging");
	}
	
	static public boolean isProduction() {
		
		return !isStaging();
	}

}
