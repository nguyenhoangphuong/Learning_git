package com.misfit.ta.android.modelapi.settings;

import java.io.File;

import org.graphwalker.generators.PathGenerator;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.gui.Gui;
import com.misfit.ta.android.gui.HomeScreen;
import com.misfit.ta.android.gui.PrometheusHelper;
import com.misfit.ta.android.gui.Settings;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class ShineSettingsAPI extends ModelAPI {
	public ShineSettingsAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private boolean showProgressFirst = true;
	private boolean useMiles = true;
	private boolean useLbs = true;
	private int fullScreenHeight;
	private int fullScreenWidth;

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		ShortcutsTyper.delayOne();
		PrometheusHelper.signUp();
		ShortcutsTyper.delayTime(2000);
		fullScreenHeight = Gui.getScreenHeight();
		fullScreenWidth = Gui.getScreenWidth();
	}

	/**
	 * This method implements the Edge 'e_ChangeDisplayOrder'
	 * 
	 */
	public void e_ChangeDisplayOrder() {
		ShortcutsTyper.delayOne();
		boolean isProgressShowedFirst = Settings.isProgressShowedFirst();
		showProgressFirst = PrometheusHelper.coin();
		if (isProgressShowedFirst == showProgressFirst) {
			System.out.println("****** Touch Cancel");
			Gui.setInvalidView();
			ShortcutsTyper.delayOne();
			PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth, DefaultStrings.CancelText);
			return;
		}
		if (showProgressFirst) {
			ViewNode progressButton = ViewUtils.findView("RadioButton", "mID",
					DefaultStrings.ShineSettingsRadioButtonId, 0);
			Gui.touchViewOnPopup(fullScreenHeight, fullScreenWidth,
					Gui.getScreenHeight(), Gui.getScreenWidth(), progressButton);
		} else {
			ViewNode clockButton = ViewUtils.findView("RadioButton", "mID",
					DefaultStrings.ShineSettingsRadioButtonId, 1);
			Gui.touchViewOnPopup(fullScreenHeight, fullScreenWidth,
					Gui.getScreenHeight(), Gui.getScreenWidth(), clockButton);
		}
	}

	/**
	 * This method implements the Edge 'e_ChangeDistanceUnits'
	 * 
	 */
	public void e_ChangeDistanceUnits() {
		ShortcutsTyper.delayOne();
		boolean isMilesUnit = Settings.isMilesUnit();
		useMiles = PrometheusHelper.coin();
		if (isMilesUnit == useMiles) {
			System.out.println("****** Touch Cancel");
			Gui.setInvalidView();
			ShortcutsTyper.delayOne();
			PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth, DefaultStrings.CancelText);
			return;
		}
		if (useMiles) {
			ViewNode milesButton = ViewUtils.findView("RadioButton", "mID",
					DefaultStrings.ShineSettingsRadioButtonId, 0);
			Gui.touchViewOnPopup(fullScreenHeight, fullScreenWidth,
					Gui.getScreenHeight(), Gui.getScreenWidth(), milesButton);
		} else {
			ViewNode kmButton = ViewUtils.findView("RadioButton", "mID",
					DefaultStrings.ShineSettingsRadioButtonId, 1);
			Gui.touchViewOnPopup(fullScreenHeight, fullScreenWidth,
					Gui.getScreenHeight(), Gui.getScreenWidth(), kmButton);
		}
	}

	/**
	 * This method implements the Edge 'e_ChangeWeightUnits'
	 * 
	 */
	public void e_ChangeWeightUnits() {
		ShortcutsTyper.delayOne();
		boolean isLbsUnit = Settings.isLbsUnit();
		useLbs = PrometheusHelper.coin();
		if (isLbsUnit == useLbs) {
			System.out.println("****** Touch Cancel");
			Gui.setInvalidView();
			ShortcutsTyper.delayOne();
			PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth, DefaultStrings.CancelText);
			Gui.setInvalidView();
			ShortcutsTyper.delayOne();
			Gui.swipe(100, 200, 100, fullScreenHeight);
			return;
		}
		if (useLbs) {
			ViewNode lbsButton = ViewUtils.findView("RadioButton", "mID",
					DefaultStrings.ShineSettingsRadioButtonId, 0);
			Gui.touchViewOnPopup(fullScreenHeight, fullScreenWidth,
					Gui.getScreenHeight(), Gui.getScreenWidth(), lbsButton);
		} else {
			ViewNode kgButton = ViewUtils.findView("RadioButton", "mID",
					DefaultStrings.ShineSettingsRadioButtonId, 1);
			Gui.touchViewOnPopup(fullScreenHeight, fullScreenWidth,
					Gui.getScreenHeight(), Gui.getScreenWidth(), kgButton);
		}
		// We swiped to open the weight view then we have to return to the top
		Gui.setInvalidView();
		ShortcutsTyper.delayOne();
		Gui.swipe(100, 200, 100, fullScreenHeight);
	}

	/**
	 * This method implements the Edge 'e_ChooseShineSettings'
	 * 
	 */
	public void e_ChooseShineSettings() {
		HomeScreen.openDashboardMenu(fullScreenHeight, fullScreenWidth);
		ShortcutsTyper.delayTime(2000);
		Gui.setInvalidView();
		ShortcutsTyper.delayTime(5000);
		Settings.tapShineSettings();
		ShortcutsTyper.delayTime(3000);
	}

	/**
	 * This method implements the Edge 'e_PressBack'
	 * 
	 */
	public void e_PressBack() {
		ShortcutsTyper.delayTime(2000);
		Gui.setInvalidView();
//		PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth,
//				DefaultStrings.SyncLaterText);
		ShortcutsTyper.delayTime(2000);
		Gui.touchAView("TextView", "mText", DefaultStrings.ShineSettingsText);
	}

	/**
	 * This method implements the Edge 'e_Stay'
	 * 
	 */
	public void e_Stay() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_ToDisplayOrder'
	 * 
	 */
	public void e_ToDisplayOrder() {
		ShortcutsTyper.delayOne();
		Settings.openDisplayOrderPopup();
	}

	/**
	 * This method implements the Edge 'e_ToDistanceView'
	 * 
	 */
	public void e_ToDistanceView() {
		ShortcutsTyper.delayOne();
		Settings.openDistancePopup();
	}

	/**
	 * This method implements the Edge 'e_ToWeightView'
	 * 
	 */
	public void e_ToWeightView() {
		ShortcutsTyper.delayOne();
		Settings.openWeightPopup(fullScreenHeight);
	}

	/**
	 * This method implements the Edge 'e_TurnClockOff'
	 * 
	 */
	public void e_TurnClockOff() {
		ShortcutsTyper.delayOne();
		Settings.showClock(false);
	}

	/**
	 * This method implements the Edge 'e_TurnClockOn'
	 * 
	 */
	public void e_TurnClockOn() {
		ShortcutsTyper.delayOne();
		Settings.showClock(true);
	}

	/**
	 * This method implements the Vertex 'v_DisplayOrderView'
	 * 
	 */
	public void v_DisplayOrderView() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_DistanceView'
	 * 
	 */
	public void v_DistanceView() {
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

	/**
	 * This method implements the Vertex 'v_WeightView'
	 * 
	 */
	public void v_WeightView() {
		// TODO:
	}
}
