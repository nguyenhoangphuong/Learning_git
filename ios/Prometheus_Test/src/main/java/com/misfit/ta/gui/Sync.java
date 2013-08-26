package com.misfit.ta.gui;

import com.misfit.ta.utils.ShortcutsTyper;

public class Sync {
	public static void signIn() {
		LaunchScreen.launch();
		SignIn.tapLogIn();
		ShortcutsTyper.delayTime(1000);
		SignIn.enterEmailPassword("v14@qa.com", "test12");
		// wait for sync data
		ShortcutsTyper.delayTime(25000);
	}

	public static void tapToSync() {
		Gui.tapToSync();
	}
	
	public static void tapLinkShine() {
		Gui.touchAVIew("UIButtonLabel", "Link Shine");
	}

	public static void openSyncView() {
		Gui.touchAVIew("UILabel", "Sync");
		ShortcutsTyper.delayTime(3000);
	}

	public static boolean hasAlert() {
		return Gui.hasAlert();
	}
	
    public static boolean hasSyncFailedMessage() {
    	return PrometheusHelper.hasNoShineAvailableMessage();
    }
    
    public static boolean hasNoShineAvailableMessage() {
    	return PrometheusHelper.hasSyncFailedMessage();
    }

	public static void tapOK() {
		// popup error message
		Gui.touchPopupButton(0);
	}
}
