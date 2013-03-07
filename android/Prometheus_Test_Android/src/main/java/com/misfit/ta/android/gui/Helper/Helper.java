package com.misfit.ta.android.gui.Helper;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Vector;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.utils.ShortcutsTyper;

public class Helper {

	public static void wait(int miliseconds) {
		ShortcutsTyper.delayTime(miliseconds);
	}

	public static void wait1() {
		ShortcutsTyper.delayTime(1000);
	}

	public static String createRandomString() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32).substring(0, 10);
	}

	public static void tapLevelMild() {
		Gui.touchAView("ImageButton", "mID", "id/buttonLevelMild");
	}

	public static void tapLevelDormant() {
		Gui.touchAView("ImageButton", "mID", "id/buttonLevelDormant");
	}

	public static void tapLevelModerate() {
		Gui.touchAView("ImageButton", "mID", "id/buttonLevelModerate");
	}

	public static void tapLevelActive() {
		Gui.touchAView("ImageButton", "mID", "id/buttonLevelActive");
	}

	public static void changeTimeContext() {
		Gui.touchAView("CheckedTextView", "mID", "id/text1");
	}
	
	/**
	 * Please be sure to call this function before you tap to display any popup
	 * @return
	 */
	public static int[] getScreenSizeBeforePopUp() {
		Vector<ViewNode> views = Gui.getCurrentViews();
		ShortcutsTyper.delayTime(10000);
		return new int[] { Gui.getScreenWidth(), Gui.getScreenHeight() };
	}
	public static int[] getTouchPointOnPopup(ViewNode viewNode, int deltaX, int deltaY) {
		int s[] = Helper.getScreenSizeBeforePopUp() ;
		int h = Gui.getHeight(); 
    	int w = Gui.getWidth();
    	System.out.println("LOG [Gui.main]: view" + h + ":" + w);
    	int p[] = Gui.getCoordinators(viewNode);
    	int newP[] = {0,0};
		newP[0] = (s[0]- w) / 2 + p[0] + deltaX;
		newP[1] = (s[1]- h) / 2 + p[1] + deltaY;
		return newP;
	}
}
