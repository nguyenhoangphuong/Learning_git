package com.misfit.ta.gui;

import java.io.File;
import java.io.FileNotFoundException;

import com.misfit.ios.AppHelper;
import com.misfit.ta.Settings;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ShortcutsTyper;

public class AppInstaller {

	private static String[] MVPAppPaths = new String[] {
		"apps/mvp17.1/Prometheus.ipa",
		"apps/mvp18.1/Prometheus.ipa",
		"apps/mvp19/Prometheus.ipa",
		"apps/mvp20/Prometheus.ipa",
		"apps/mvp21/Prometheus.ipa",
		"apps/mvp22/Prometheus.ipa",
	};
	
	public static int MVP_17_1 = 0;
	public static int MVP_18_1 = 1;
	public static int MVP_19 = 2;
	public static int MVP_20 = 3;
	public static int MVP_21 = 4;
	public static int MVP_22 = 5;
	
	static public boolean checkAppsExist(int mvp) {
		
		// make sure old app file exist
		if(mvp < 0 || mvp >= MVPAppPaths.length)
			return false;

		String pathToApp = MVPAppPaths[mvp];
		try {
			Files.getFile(pathToApp);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		File f = new File(pathToApp);
		if(!f.exists())
			return false;

		return true;
	}
	
	static public boolean checkLatestAppExist() {
		
		// check app in appPath
		String pathToNewApp = Settings.getParameter("appPath");
		if(pathToNewApp == null)
			return false;

		File f2 = new File(pathToNewApp);
		if(!f2.exists())
			return false;
		
		return true;
	}

	static public void killInstrument() {
		
		InstrumentHelper instrument = new InstrumentHelper();
		instrument.kill();
	}
	
	static public void launchInstrument() {
		
		InstrumentHelper instrument = new InstrumentHelper();
        instrument.start();
		ShortcutsTyper.delayTime(15000);
    	Gui.init(Settings.getParameter("DeviceIP"));
	}
	
		
	static public void installAndLaunchApp(int mvp) {

		// install app
		String pathToApp = MVPAppPaths[mvp];
		killInstrument();
		Gui.uninstall(Gui.getUdids().get(0));
		Gui.install(Gui.getUdids().get(0), pathToApp);
		launchInstrument();
	}

	static public void installAndLaunchLatestAppWithoutUninstall() {

		String pathToNewApp = Settings.getParameter("appPath");
		File f2 = new File(pathToNewApp);
		pathToNewApp = f2.getAbsolutePath();
		
		// install app
		killInstrument();
		Gui.install(Gui.getUdids().get(0), pathToNewApp);
		launchInstrument();
	}
	
}
