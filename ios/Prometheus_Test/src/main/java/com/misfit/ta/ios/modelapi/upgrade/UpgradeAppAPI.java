package com.misfit.ta.ios.modelapi.upgrade;

import java.io.File;
import java.io.FileNotFoundException;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.AppHelper;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.Settings;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.InstrumentHelper;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.aut.AutomationTest;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.report.TRS;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class UpgradeAppAPI extends ModelAPI {

	public UpgradeAppAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	
	// static
	private static String[] MVPAppPaths = new String[] {
		"apps/mvp16.1/Prometheus.ipa",
		"apps/mvp17.1/Prometheus.ipa",
		"apps/mvp18.1/Prometheus.ipa",
		"apps/mvp19/Prometheus.ipa",
		"apps/mvp20/Prometheus.ipa",
	};
	
	public static int MVP_16_1 = 0;
	public static int MVP_17_1 = 1;
	public static int MVP_18_1 = 2;
	public static int MVP_19 = 3;
	public static int MVP_20 = 4;
	
	
	// fields
	public int fromMVP = -1;
	private String pathToOldApp = null;
	public boolean willCheckProfile = false;
	 	
	private String email = "";
	private int numberOfActivity = 1;
	private int totalPoint = numberOfActivity * (numberOfActivity + 1) / 2 * 100;
	private int totalStep = totalPoint * 10;
	private float totalMile = numberOfActivity * (numberOfActivity + 1) / 2 * 0.4f;
	
	
	private void checkAppsExist() {
		
		// make sure old app file exist
		if(fromMVP < 0 || fromMVP >= MVPAppPaths.length)
			Assert.fail("Path to old app is not set");

		pathToOldApp = MVPAppPaths[fromMVP];
		try {
			Files.getFile(pathToOldApp);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		File f = new File(pathToOldApp);
		if(!f.exists())
			Assert.fail("Path to old app not exist");
		
		pathToOldApp = f.getAbsolutePath();
		
		// make sure new app file exist
		String pathToNewApp = Settings.getParameter("appPath");
		if(pathToNewApp == null)
			Assert.fail("Path to new app is not set");
		
		File f2 = new File(pathToNewApp);
		if(!f2.exists())
			Assert.fail("Path to new app not exist");
	}
	
	private void killInstrument() {
		
		InstrumentHelper instrument = new InstrumentHelper();
		instrument.kill();
	}
	
	private void launchInstrument() {
		
		InstrumentHelper instrument = new InstrumentHelper();
        instrument.start();
		ShortcutsTyper.delayTime(15000);
    	Gui.init(Settings.getParameter("DeviceIP"));
	}
	
	
	public void e_installAndLaunchOldApp() {
		
		// check app before doing anything
		checkAppsExist();
		
		// install old app
		killInstrument();
		Gui.uninstall(Gui.getUdids().get(0));
		Gui.install(Gui.getUdids().get(0), pathToOldApp);
		launchInstrument();
	}
	
	public void e_signUp() {
		
		// sign up using default profile and goal
		email = PrometheusHelper.signUp();
		
		// link device to account
		BackendHelper.link(email, "qwerty1", TextTool.getRandomString(10, 10));
	}
	
	public void e_inputActivities() {
		
		// input some activities
		for(int i = 1; i <= numberOfActivity; i++) {
			
			String[] times = new String[] { String.valueOf(i), "00", "AM" };
			HomeScreen.tapOpenManualInput();
			PrometheusHelper.inputManualRecord(times, 10 * i, 1000  * i);
			HomeScreen.tapSave();
		}
	}
	
	public void e_signOut() {
		
		if(fromMVP == MVP_16_1)
			Gui.touchAVIew("UILabel", "Today");
				
		PrometheusHelper.signOut();
	}
	
	public void e_installAndLaunchLatestApp() {
		
		// kill app
		killInstrument();
		
		// install and launch new app
		AppHelper.install();
		
		// because install happen in new thread, 
		// make sure it finish before launching instrument
		ShortcutsTyper.delayTime(15000);
		launchInstrument();
	}
	
	public void e_logIn() {
		
		PrometheusHelper.signIn(email, "qwerty1");
		
		// wait for data
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}
	
	public void e_toProfile() {
		
		if(willCheckProfile) {
			HomeScreen.tapOpenSettingsTray();
			HomeScreen.tapSettings();
			ShortcutsTyper.delayTime(500);
			HomeSettings.tapYourProfile();
		}
		else
			TRS.instance().addCode("Don't check profile. Do nothing.", null);
	}
	
	
	public void v_LaunchScreen() {
		
		Assert.assertTrue(LaunchScreen.isAtLaunchScreen(), "Current view is LaunchScreen");
	}
	
	public void v_HomeScreen() {
		Assert.assertTrue(HomeScreen.isToday(), "Current view is Homescreen - Today");
	}
	
	public void v_InitialScreen() {
		
		Assert.assertTrue(LaunchScreen.isAtInitialScreen(), "Current view is InitialScreen");
	}
	
	public void v_HomeScreenAfterUpgrade() {
				
		// check current progress
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", totalPoint + ""), "Total points OK");
		HomeScreen.tapProgressCircle();
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("_%d_ steps", totalStep)), "Total steps OK");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("_%.1f_ miles", totalMile)), "Total distance OK");
		
		// check activities is saved
		Timeline.dragUpTimelineAndHandleTutorial();
		Gui.swipeUp(1000);
		for(int i = 1; i <= numberOfActivity; i++) {
			
			String startTime = i + ":00am";
			String endTime = i + ":" + (i * 10) + "am";
			Timeline.openTile(startTime);
			Assert.assertTrue(Timeline.isActivityTileCorrect(startTime, endTime, i * 10, i * 100, null), "Timeline item saved");
			Timeline.closeCurrentTile();
			
			// check hit goal 100 item
			if(i == 4) {
				
				Timeline.openTile("4:40am");
				Assert.assertTrue(Timeline.isDailyGoalMilestoneTileCorrect("4:40am", 1000, Timeline.DailyGoalMessagesFor100Percent),
						"Daily goal 100% tile saved");
				Timeline.closeCurrentTile();
			}
			
			// check hit goal 150 item
			if(i == 5) {

				Timeline.openTile("5:50am");
				Assert.assertTrue(Timeline.isDailyGoalMilestoneTileCorrect("5:50am", 1500, Timeline.DailyGoalMessagesFor150Percent),
						"Daily goal 150% tile saved");
				Timeline.closeCurrentTile();
			}
		}
	}
	
	public void v_ProfileAfterUpgrade() {
		
		if(willCheckProfile) {
			
			// sex
			boolean male = Gui.getProperty("UIButton", "Male", "isSelected").equals("1") ? true : false;
			Assert.assertTrue(male, "Gender is saved");
			
			// birthday
			String birthday = PrometheusHelper.formatBirthday("1991", "September" , "16");
			Assert.assertTrue(ViewUtils.isExistedView("UILabel", birthday), "Birthday is saved");
			
			// height
			String height = "5'8\\\"";
			Assert.assertTrue(ViewUtils.isExistedView("UILabel", height), "Height should be " + height);
			
			// weight
			String weight = "120.0 lbs";
			Assert.assertTrue(ViewUtils.isExistedView("UILabel", weight), "Weight should be " + weight);
		}
		else
			TRS.instance().addCode("Don't check profile. Do nothing.", null);
	}

}
