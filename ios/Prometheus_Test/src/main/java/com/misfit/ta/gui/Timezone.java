package com.misfit.ta.gui;

public class Timezone {
	public static void changeTimezone(int offset) {
		Gui.setTimezoneWithGMTOffset(offset);
	}
	
	public static void touchTimezoneWithLabel(String label) {
		Gui.touchAVIew("UILabel", label);
	}
	
	public static void closeTimeTravelTile() {
		Gui.touchAVIew("UILabel", DefaultStrings.TileTimeTravelLabel);
	}
}
