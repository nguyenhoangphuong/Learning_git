package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;
import java.util.Calendar;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.aut.AutomationTest;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class LogActivityAPI extends ModelAPI {

	public static final String[] arrActivities = new String[] { "Basketball",
			"Tennis", "Swimming", "Soccer", "Cycling", "Running", "Yoga",
			"Dancing" };
	public static final String[] arrLevel = new String[] { "Mild", "Moderate",
			"Intense" };

	public LogActivityAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private int number = 0;
	private int numberOfLevel = 0;
	private String[] startTime = null;
	private String[] endTime = null;
	/**
	 * This method implements the Edge 'e_cancel'
	 * 
	 */
	public void e_cancel() {
		HomeScreen.tapCancel();
	}

	/**
	 * This method implements the Edge 'e_goToLogExercise'
	 * 
	 */
	public void e_goToLogExercise() {
		HomeScreen.tapLogExercise();
	}

	/**
	 * This method implements the Edge 'e_goToTaggingActivityScreen'
	 * 
	 */
	public void e_goToTaggingActivityScreen() {
		HomeScreen.tapAddTrackingActivity();
	}

	/**
	 * This method implements the Edge 'e_init'
	 * 
	 */
	public void e_init() {
		PrometheusHelper.signUpDefaultProfile();
	}

	/**
	 * This method implements the Edge 'e_save'
	 * 
	 */
	public void e_save() {
		// Random activity
		number = PrometheusHelper.randInt(0, 7);
		System.out.println("Random activity : " + arrActivities[number]);
		int i = 4;
		if (number < i) {
			do {
				HomeScreen.tapBackwardActivity();
				i--;
			} while (number != i);
		} else if (number > i) {
			do {
				HomeScreen.tapForwardActivity();
				i++;
			} while (number != i);
		}
		Assert.assertTrue(
				ViewUtils.isExistedView("UILabel", arrActivities[number]),
				"It is not correct activity");

		// Random level when activity is yoga or dancing
		if (number == 6 || number == 7) {
			numberOfLevel = PrometheusHelper.randInt(2, 4);
			HomeScreen.tapLevelActivity(numberOfLevel);
			PrometheusHelper.waitForView("UILabel", arrLevel[numberOfLevel - 2]);
			Assert.assertTrue(ViewUtils.isExistedView("UILabel",
					arrLevel[numberOfLevel - 2]), "It is not correct level");
		}

		// Choose start time
		HomeScreen.tapStartTimeButton();
		Calendar now = Calendar.getInstance();
		System.out.println("Current time : " + now.get(Calendar.HOUR_OF_DAY)
				+ ":" + now.get(Calendar.MINUTE) + ":"
				+ now.get(Calendar.SECOND));
		int hour = now.get(Calendar.HOUR_OF_DAY);
		boolean isAM = true;
		if(hour > 12){
			hour -= 12;
			isAM = false;
		}
		endTime = new String[]{"Today", String.valueOf(hour), String.valueOf(now.get(Calendar.MINUTE)), isAM ? "AM" : "PM"};
		now.add(Calendar.MINUTE, -5);

		System.out.println("New time after substracting 5 minutes : "
				+ now.get(Calendar.HOUR_OF_DAY) + ":"
				+ now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND));

		
		System.out.println("Hour : " + hour + " Minutes : " + now.get(Calendar.MINUTE));
		startTime = new String[]{"Today", String.valueOf(hour), String.valueOf(now.get(Calendar.MINUTE)), isAM ? "AM" : "PM"};
//		HomeScreen.inputTime(startTime);
		// Choose end time
		HomeScreen.tapEndTimeButton();
//		now.add(Calendar.MINUTE, -1);
//		hour = now.get(Calendar.HOUR_OF_DAY);
//		isAM = true;
//		if(hour > 12){
//			hour -= 12;
//			isAM = false;
//		}
		
//		HomeScreen.inputTime(endTime);
		HomeScreen.tapSave();
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		Assert.assertTrue(HomeScreen.isHomeScreen(),
				"Current View is Home Screen");
	}

	/**
	 * This method implements the Vertex 'v_HomeScreenUpdated'
	 * 
	 */
	public void v_HomeScreenUpdated() {
		ShortcutsTyper.delayTime(1000);
		Timeline.dragUpTimelineAndHandleTutorial();
		Gui.swipeUp(1000);
		checkTiles();
		Timeline.dragDownTimeline();
	}
	
	public void checkTiles(){
		boolean isChange = false;
		String title = startTime[1] + ":" + String.format("%02d", Integer.parseInt(startTime[2])) + startTime[3].toLowerCase();
		if(!ViewUtils.isExistedView("UILabel", title)){
			title = startTime[1] + ":" + String.format("%02d", Integer.parseInt(startTime[2]) - 1) + startTime[3].toLowerCase();
			isChange = true;
		}
		System.out.println("Title : " + title);
		
		Timeline.openTile(title);
		Gui.captureScreen("ActivityTile-" + System.nanoTime());
		if(isChange){
			endTime[0] = endTime[1] + ":" + String.format("%02d", Integer.parseInt(endTime[2]) - 1) + endTime[3].toLowerCase();
		}else{
			endTime[0] = endTime[1] + ":" + String.format("%02d", Integer.parseInt(endTime[2])) + endTime[3].toLowerCase();
		}
		String time = title + " - " + endTime[0];
		System.out.println("Time title : " + time);
		String message = "";
		if(number != 6 && number != 7){
			message = arrLevel[1].toUpperCase() + " " + arrActivities[number].toUpperCase();
		}else{
			message = arrLevel[numberOfLevel].toUpperCase() + " " + arrActivities[number].toUpperCase();
		}
		ShortcutsTyper.delayTime(2000);
		System.out.println("Message : " + message);
		Assert.assertTrue(Timeline.isTimelineSession(time, message), "It's not correct activity tile created");
		Timeline.closeTile(time);
	}

	/**
	 * This method implements the Vertex 'v_TaggingActivityScreen'
	 * 
	 */
	public void v_TaggingActivityScreen() {
		Assert.assertTrue(HomeScreen.isDialogTracking(),
				"Current View is Tagging Activity Screen");
	}

	/**
	 * This method implements the Vertex 'v_LogActivityScreen'
	 * 
	 */
	public void v_LogActivityScreen() {
		Assert.assertTrue(HomeScreen.isLogActivityScreen(),
				"Current View is Log Activity Screen");
	}

}
