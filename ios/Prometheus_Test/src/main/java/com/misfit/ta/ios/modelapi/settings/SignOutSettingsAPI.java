package com.misfit.ta.ios.modelapi.settings;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.SignIn;
import com.misfit.ta.ios.AutomationTest;

public class SignOutSettingsAPI extends ModelAPI {
	public SignOutSettingsAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	
	private String email = MVPApi.generateUniqueEmail();
	private String password = "qwerty1";
	
	private boolean isMale = true;
	private int date = 16;
	private int month = 9;
	private int year = 1991;
	private boolean isUSUnit = false;
	private String h1 = "1";
	private String h2 = ".71";
	private String w1 = "69";
	private String w2 = ".5";
	
	private int goalValue = 2500;
	
	private String wearAt = "";
	
	private String lastStartTime = "";
	private int lastDuration = 0;
	private int lastSteps = 0;
	private int hour = 1;
	
	
	public void e_Init() 
	{
		// This model require none
	}

	public void e_SignUp() 
	{
		// sign up an account
		PrometheusHelper.signUp(email, password, isMale, date, month, year, isUSUnit, h1, h2, w1, w2, 1);
	}
	
	public void e_ToSettings() 
	{
		HomeScreen.tapOpenSettingsTray();
		ShortcutsTyper.delayTime(500);
		HomeScreen.tapSettings();
		ShortcutsTyper.delayOne();
	}

	public void e_ToWearingShineSettings() 
	{
		HomeSettings.tapWearingShine();
		ShortcutsTyper.delayOne();
	}
	
	public void e_ToGoalSettings()
	{
		//TODO: no goal settings anymore
	}
	
	public void e_ToProfileSettings() 
	{
		HomeSettings.tapYourProfile();
		ShortcutsTyper.delayOne();
	}
	
	public void e_BackFromSettings() 
	{
		HomeSettings.tapBack();
		ShortcutsTyper.delayOne();
	}
	
	public void e_ChangeProfile()
	{
		isMale = PrometheusHelper.coin();
		date = PrometheusHelper.randInt(1, 28);
		month = PrometheusHelper.randInt(1, 13);
		year = PrometheusHelper.randInt(1980, 1995);
		h1 = PrometheusHelper.randInt(1, 3) + "";
		h2 = "." + PrometheusHelper.randInt(50, 70);
		w1 = PrometheusHelper.randInt(50, 100) + "";
		w2 = "." + PrometheusHelper.randInt(0, 10);
		
		HomeSettings.updateGender(isMale);
		ShortcutsTyper.delayOne();
		HomeSettings.updateBirthDay(year + "", month + "", date + "");
		ShortcutsTyper.delayOne();
		HomeSettings.updateHeight(h1, h2, isUSUnit);
		ShortcutsTyper.delayOne();
		HomeSettings.updateWeight(w1, w2, isUSUnit);
		ShortcutsTyper.delayOne();
	}

	public void e_ChangeWearingShine() 
	{
		int index = PrometheusHelper.randInt(0, DefaultStrings.WearingLocations.length);
		wearAt = DefaultStrings.WearingLocations[index];
		HomeSettings.setWearingShineAt(wearAt);
		ShortcutsTyper.delayOne();
	}

	public void e_ChangeGoal()
	{
		goalValue = PrometheusHelper.randInt(25, 45) * 100;
		HomeSettings.setSpinnerGoal(goalValue);
		ShortcutsTyper.delayOne();
	}
	
	public void e_ConfirmProfile() 
	{
		HomeSettings.tapBack();
		ShortcutsTyper.delayOne();
	}

	public void e_ConfirmWearingShine() 
	{
		HomeSettings.tapBack();
		ShortcutsTyper.delayOne();
	}

	public void e_ConfirmGoal()
	{
		HomeSettings.tapDoneAtNewGoal();
		ShortcutsTyper.delayOne();
	}
	
	public void e_InputActivity() 
	{
		String[] times = { String.format("%d", hour > 12 ? 12 - hour : hour), "00", hour < 12 ? "AM" : "PM" };
		lastStartTime = String.format("%d", hour > 12 ? 12 - hour : hour) + ":00" + (hour < 12 ? " am": " pm");
		lastDuration = PrometheusHelper.randInt(5, 9);
		lastSteps = lastDuration * PrometheusHelper.randInt(100, 180);
		hour++;
		
		PrometheusHelper.inputManualRecord(times, lastDuration, lastSteps);
		ShortcutsTyper.delayOne();
	}

	public void e_SignOut() 
	{
		ShortcutsTyper.delayTime(5000);
		HomeSettings.tapSignOut();
		ShortcutsTyper.delayTime(1000);
		HomeSettings.tapSignOut();
	}
	
	public void e_LogIn() 
	{
		SignIn.tapLogIn();
		ShortcutsTyper.delayOne();
		SignIn.enterEmailPassword(email, password);
		
		// wait for app to fetch data from server
		ShortcutsTyper.delayTime(25000);
	}


	
	
	public void v_InitScreen() 
	{
		// nothing
	}
	
	public void v_StartScreen() 
	{
		Assert.assertTrue(ViewUtils.isExistedView("UIButtonLabel", " SIGN UP"), "Sign up button is avaible");
		Assert.assertTrue(ViewUtils.isExistedView("UIButtonLabel", " SIGN IN"), "Sign in button is avaible");
	}
	
	public void v_HomeScreen() 
	{
		Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen - Today");
	}

	public void v_HomeScreenUpdated() 
	{
		// we can test if the screen is updated
		// but sign out test didn't intend to test this
		// module test will handle this
	}
	
	public void v_HomeScreenAfterLogin() 
	{
		// check if records are saved
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", this.lastStartTime), "Start time displayed correctly");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%d", this.lastSteps)), "Steps displayed correctly");
	}

	public void v_Settings()
	{
		Assert.assertTrue(HomeSettings.isAtSettings(), "Current view is Settings");
	}
	
	public void v_SettingsAfterLogin() {
	}

	public void v_ProfileSettings() 
	{
		Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen - Today");
	}

	public void v_ProfileSettingsUpdated() 
	{
		// we can test if the screen is updated
		// but sign out test didn't intend to test this
		// module test will handle this
	}
	
	public void v_ProfileSettingsAfterLogin()
	{
		// check if profile is saved
		boolean male = Gui.getProperty("UIButton", "Male", "isSelected").equals("1") ? true : false;
		String birthday = PrometheusHelper.formatBirthday(year + "", PrometheusHelper.getMonthString(month, true), date + "");
		String weight = w1 + w2 + (isUSUnit ? " lbs" : " kg");
		String height = h1 + h2 + (isUSUnit ? "" : " m");
		
		Assert.assertTrue(isMale == male, "Gender should be " + (isMale ? "Male" : "Female"));
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", birthday), "Birthday should be " + birthday);
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", weight), "Weight should be " + weight);
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", height), "Height should be " + height);
	}

	public void v_GoalSettings()
	{
		Assert.assertTrue(HomeSettings.isAtEditGoal(), "Current view is Settings - Adjust Goal");
	}
	
	public void v_GoalSettingsUpdated()
	{
		// we can test if the screen is updated
		// but sign out test didn't intend to test this
		// module test will handle this
	}
	
	public void v_GoalSettingsAfterLogin()
	{
		// Note: block by NumberSpinner values
		
		// check if goal is saved
		//String actual = Gui.getProperty("PTRichTextLabel", 0, "text");
		//String expect = this.goalValue + "";
		//Assert.assertTrue(actual.indexOf(expect) >= 0, "Default goal value is correct");
	}
	
	public void v_WearingShineSettings() 
	{
		Assert.assertTrue(HomeSettings.isAtWearingShine(), "Current view is Settings - Wearing Shine");
	}

	public void v_WearingShineSettingsUpdated() 
	{
		// we can test if the screen is updated
		// but sign out test didn't intend to test this
		// module test will handle this
	}
	
	public void v_WearingShineSettingsAfterLogin()
	{
		// check if wearing shine location is saved
		// TODO:
	}

}
