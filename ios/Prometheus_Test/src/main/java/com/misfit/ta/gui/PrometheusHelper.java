package com.misfit.ta.gui;

import java.io.File;
import java.util.Random;
import org.testng.annotations.Test;
import com.misfit.ta.utils.ShortcutsTyper;

public class PrometheusHelper {
	
	
	/* Views Helper */
	public static void enterGender(boolean isMale) {
		Gui.touchAVIew("UIButtonLabel", isMale ? "Male" : "Female");
	}

	public static void enterBirthDay(String year, String month, String day) {
		Gui.touchAVIew("PTDatePickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		Gui.setPicker(0, month);
		Gui.setPicker(1, day);
		Gui.setPicker(2, year);
		Gui.dismissPicker();
		ShortcutsTyper.delayTime(1000);
	}

	public static void enterHeight(String digit, String fraction,
			boolean isUSUnit) {
		/* Example for ft in Gui.setPicker(1, "9\\\""); */
		Gui.touchAVIew("PTHeightPickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		Gui.setPicker(2, isUSUnit ? "ft in" : "m");
		Gui.setPicker(0, digit);
		Gui.setPicker(1, fraction);
		Gui.dismissPicker();
		ShortcutsTyper.delayTime(1000);
	}

	public static void enterWeight(String digit, String fraction,
			boolean isUSUnit) {
		Gui.touchAVIew("PTWeightPickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		Gui.setPicker(2, isUSUnit ? "lbs" : "kg");
		Gui.setPicker(0, digit);
		Gui.setPicker(1, fraction);
		Gui.dismissPicker();
		ShortcutsTyper.delayTime(1000);
	}

	public static boolean hasInvalidEmailMessage() {
		return Gui.getPopupTitle().equals("Error")
				&& (Gui.getPopupContent().equals(
						DefaultStrings.InvalidEmailMessage) || Gui
						.getPopupContent().equals(
								DefaultStrings.ForgotInvalidEmailMessage));
	}

	public static boolean hasInvalidPasswordMessage() {
		return Gui.getPopupTitle().equals("Error")
				&& Gui.getPopupContent().equals(
						DefaultStrings.InvalidPasswordMessage);
	}

	public static void sync() {
		Gui.sync();
	}

	public static void enterEmailPassword(String email, String password) {
		Gui.touchAVIew("PTEmailVerifyingTextField", 0);
		String txtEmail = Gui.getProperty("PTEmailVerifyingTextField", 0, "text");
		System.out.println("Deleting: " + txtEmail);
		for (int i = 0; i < txtEmail.length(); i++) {
			Gui.pressDelete();
		}
		ShortcutsTyper.delayTime(800);
		Gui.type(email);
		ShortcutsTyper.delayTime(300);
		Gui.pressNext();
		ShortcutsTyper.delayTime(300);
		Gui.pressDelete();
		Gui.type(password);
		ShortcutsTyper.delayTime(300);
		Gui.pressDone();
	}

	
	
	/* Utilities */
	private static String[] longMonths = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private static String[] sortMonths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	public static int randInt(int includeFrom, int excludeTo)
	{
		Random r = new Random();
		return r.nextInt(excludeTo - includeFrom) + includeFrom;
	}
	
	public static boolean coin()
	{
		Random r = new Random();
		return r.nextBoolean();
	}
	
	public static float calculatePoint(int steps, int minutes)
	{
		return steps * (0.25f + 0.01f * (Math.max(115f, steps * 1f/ minutes) - 115));
	}
	
	public static float calculateMiles(int steps, int heightInInches)
	{
		return steps * 0.414f * heightInInches / 63360;
	}
	
	public static float calculateCalories(float points, float weightInKg)
	{
		return points * weightInKg / 600f;
	}
	
	public static String getMonthString(int monthNumber, boolean isLongMonthString)
	{
		return isLongMonthString ? longMonths[monthNumber - 1] : sortMonths[monthNumber - 1];
	}

	public static String formatBirthday(String year, String monthString, String day)
	{
		return monthString.substring(0, 3) + " " +  String.format("%02d", Integer.parseInt(day)) + ", " + year;
	}
	
	public static String formatProfile(boolean isMale, boolean isUSUnit, String h1, String h2, String w1, String w2)
	{
		return  (isMale ? "Male" : "Female") + " - " +
				h1 + h2 + (isUSUnit ? "" : " m") + " - " +
				w1 + w2 + (isUSUnit ? " lbs" : " kg");
	}
	
	
	
	/* Quick navigation */
	public static void signUp(String email, String password, boolean isMale, int date, int month, int year, 
			boolean isUSUnit, String h1, String h2, String w1, String w2, int goalLevel)
	{
		SignUp.tapSignUp();
		ShortcutsTyper.delayTime(1000);
		SignUp.enterEmailPassword(email, password);
		ShortcutsTyper.delayTime(5000);
		SignUp.enterGender(isMale);
		SignUp.enterBirthDay(year + "", PrometheusHelper.getMonthString(month, true), date + "");
		SignUp.enterHeight(h1, h2, isUSUnit);
		SignUp.enterWeight(w1, w2, isUSUnit);
		SignUp.tapNext();
		ShortcutsTyper.delayTime(1000);
		SignUp.setGoal(goalLevel);
		SignUp.tapNext();
		ShortcutsTyper.delayTime(1000);
		SignUp.sync();
		ShortcutsTyper.delayTime(10000);
		SignUp.tapFinishSetup();
		ShortcutsTyper.delayTime(3000);
	}
	
	
	
	//public static void main(String[] args)
	@Test
	public void test()
	{
//		Gui.init("192.168.1.247");
//		Gui.printView();
		//HomeScreen.tapSyncTray();
		//HomeScreen.tapSettings();
		
		//Gui.tapNext();
		
		//PrometheusHelper.signUp(MVPApi.generateUniqueEmail(), "qwerty1", true, 16, 9, 1991, true, "5'", "8\\\"", "120", ".0", 1);
		File f = new File("script/automation/alertupport.js");
		System.out.print(f.getAbsolutePath());
	}
}
