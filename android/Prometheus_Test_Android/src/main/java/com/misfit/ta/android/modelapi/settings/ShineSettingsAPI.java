package com.misfit.ta.android.modelapi.settings;

import java.io.File;

import org.graphwalker.generators.PathGenerator;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.gui.Gui;
import com.misfit.ta.android.gui.PrometheusHelper;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class ShineSettingsAPI extends ModelAPI {
	public ShineSettingsAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	private boolean displayClock = true;
	private boolean showProgressFirst = true;
	private boolean useMiles = true;
	private boolean useLbs = true;
	private boolean trackSleep = true;

	/**
	 * This method implements the Edge 'e_ChangeClock'
	 * 
	 */
	public void e_ChangeClock() {
		Gui.touchAView("CheckBox", "mID", DefaultStrings.ShineSettingsDisplayClockCheckBoxId);
		if (displayClock) {
			if (showProgressFirst) {
				ViewNode node = ViewUtils.findView("RadioButton", "mID", DefaultStrings.ShineSettingsRadioButtonId1, 0);
				Gui.touchAView(node, 1);
			} else {
				ViewNode node = ViewUtils.findView("RadioButton", "mID", DefaultStrings.ShineSettingsRadioButtonId2, 0);
				Gui.touchAView(node, 1);
			}
		}
	}

	/**
	 * This method implements the Edge 'e_ChangeSleepTracking'
	 * 
	 */
	public void e_ChangeSleepTracking() {
		Gui.touchAView("Switch", "mID", DefaultStrings.ShineSettingsSleepTrackingSwitchId);
	}

	/**
	 * This method implements the Edge 'e_ChangeUnits'
	 * 
	 */
	public void e_ChangeUnits() {
		if (useMiles) {
			ViewNode node = ViewUtils.findView("RadioButton", "mID", DefaultStrings.ShineSettingsRadioButtonId1, 1);
			Gui.touchAView(node, 1);
		} else {
			ViewNode node = ViewUtils.findView("RadioButton", "mID", DefaultStrings.ShineSettingsRadioButtonId2, 1);
			Gui.touchAView(node, 1);
		}
		
		if (useLbs) {
			ViewNode node = ViewUtils.findView("RadioButton", "mID", DefaultStrings.ShineSettingsRadioButtonId1, 2);
			Gui.touchAView(node, 1);
		} else {
			ViewNode node = ViewUtils.findView("RadioButton", "mID", DefaultStrings.ShineSettingsRadioButtonId2, 2);
			Gui.touchAView(node, 1);
		}
	}

	/**
	 * This method implements the Edge 'e_ChooseShineSettings'
	 * 
	 */
	public void e_ChooseShineSettings() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		ShortcutsTyper.delayOne();
		PrometheusHelper.signUp();
		ShortcutsTyper.delayTime(2000);
	}

	/**
	 * This method implements the Edge 'e_PressBack'
	 * 
	 */
	public void e_PressBack() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_Stay'
	 * 
	 */
	public void e_Stay() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_ProfileView'
	 * 
	 */
	public void v_ProfileView() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_UpdatedProfileView'
	 * 
	 */
	public void v_UpdatedProfileView() {
		// TODO:
	}

}
