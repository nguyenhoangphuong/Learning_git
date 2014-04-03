package com.misfit.ta.backend;

import com.misfit.ta.Settings;

public class BackendTestEnvironment {

	static private String environment = Settings.getParameter("MVPBackendEnviroment");
	
	static public boolean isStaging() {
		
		return environment == null || environment.equalsIgnoreCase("staging");
	}
	
	static public boolean isProduction() {
		
		return !isStaging();
	}

}
