package com.misfit.ta.android.modelapi.homescreen;

import java.io.File;
import java.util.Random;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.gui.HomeScreen;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class DayProgressAPI extends ModelAPI {
	public DayProgressAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private boolean todayStatus;
	private boolean previousDayStatus;

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_Keep'
	 * 
	 */
	public void e_Keep() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_SetActivity'
	 * 
	 */
	public void e_SetActivity() {
		ShortcutsTyper.delayTime(3000);
		HomeScreen.tapManual();
		Random rn = new Random();
		int n = rn.nextInt() % 2;
		ShortcutsTyper.delayTime(3000);
		if (n == 0) {
			HomeScreen.chooseRandomManual();
		} else {
			HomeScreen.chooseDormantManual();
		}
		ShortcutsTyper.delayTime(3000);
		HomeScreen.saveManual();
		ShortcutsTyper.delayTime(3000);
	}

	/**
	 * This method implements the Edge 'e_ViewPreviousDay'
	 * 
	 */
	public void e_ViewPreviousDay() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_ViewToday'
	 * 
	 */
	public void e_ViewToday() {
		todayStatus = hasRecord();
	}

	/**
	 * This method implements the Vertex 'v_PreviousDay'
	 * 
	 */
	public void v_PreviousDay() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_Today'
	 * 
	 */
	public void v_Today() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_UpdatePreviousDay'
	 * 
	 */
	public void v_UpdatePreviousDay() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_UpdateToday'
	 * 
	 */
	public void v_UpdateToday() {
		if (!todayStatus) {
			Assert.assertTrue(hasRecord());
		}
		// TODO: there should be a check for the percentage value of goal and the progress circle display ...
	}

	private boolean hasRecord() {
		return ViewUtils.isVisible(ViewUtils.findView("TextView", "mID",
				"id/textPercentageNumber", 0))
				&& ViewUtils.isVisible(ViewUtils.findView("TextView", "mID",
						"id/textPercentage", 0))
				&& ViewUtils.isVisible(ViewUtils.findView("TextView", "mID",
						"id/textOfGoal", 0))
				&& !ViewUtils.isVisible(ViewUtils.findView("TextView", "mID",
						"id/textNoRecord", 0));
	}

}
