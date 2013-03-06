package com.misfit.ta.android.gui.Helper;

import java.math.BigInteger;
import java.security.SecureRandom;

import com.misfit.ta.android.Gui;
import com.misfit.ta.utils.ShortcutsTyper;

public class Helper {
	
	public static void main(String[] args)
	{
		Gui.printView(Gui.getCurrentViews());
	}
	
	
	
	public static void wait(int miliseconds){
		ShortcutsTyper.delayTime(miliseconds);
	}
	
	public static void wait1(){
		ShortcutsTyper.delayTime(1000);
	}  

	public static String createRandomString()
	{
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
}
