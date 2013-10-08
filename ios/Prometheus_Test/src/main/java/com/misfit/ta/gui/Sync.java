package com.misfit.ta.gui;

import com.misfit.ta.utils.ShortcutsTyper;

public class Sync {
	
	public static void signIn() {
		signIn("v14@qa.com", "test12");
	}
	
	public static void signIn(String email, String password) {
		LaunchScreen.launch();
		SignIn.tapLogIn();
		ShortcutsTyper.delayTime(1000);
		SignIn.enterEmailPassword(email, password);
		// wait for sync data
		ShortcutsTyper.delayTime(25000);
	}

	public static void tapToSync() {
		Gui.tapToSync();
	}
	
	public static void tapLinkShine() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.LinkShineButton);
	}

	public static void openSyncView() {
		PrometheusHelper.handleUpdateFirmwarePopup();
		Gui.touchAVIew("UILabel", DefaultStrings.MenuButton);
		ShortcutsTyper.delayTime(3000);
	}

	
	public static boolean hasAlert() {
		return Gui.hasAlert();
	}
	
    public static boolean hasNoShineAvailableMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.NoShineAvailableMessage) && Gui.getPopupTitle().equals(DefaultStrings.NoShineAvailableTitle);
	}

	public static boolean hasUnableToLinkMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.UnableToLinkMessage) && Gui.getPopupTitle().equals(DefaultStrings.UnableToLinkTitle);
	}

	public static boolean hasSyncFailedMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.SyncFailedMessage) && Gui.getPopupTitle().equals(DefaultStrings.Title);
	}
	
	public static boolean hasShineOutOfSyncMessage() {
		
		return Gui.getPopupContent().equals(DefaultStrings.ShineOutOfSyncMessage) && Gui.getPopupTitle().equals(DefaultStrings.ShineOutOfSyncTitle);
	}
	
	public static void tapOK() {
		// popup error message
		Gui.touchPopupButton(0);
	}

	public static void tapPopupSyncLater() {
		
		Gui.touchPopupButton(1);
	}
	
	public static void tapPopupSyncNow() {
		
		Gui.touchPopupButton(0);
	}
	
}
