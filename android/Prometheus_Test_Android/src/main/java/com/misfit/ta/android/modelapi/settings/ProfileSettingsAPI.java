package com.misfit.ta.android.modelapi.settings;
import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.Gui;
import com.misfit.ta.android.gui.HomeScreen;
import com.misfit.ta.android.gui.Settings;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class ProfileSettingsAPI extends ModelAPI {
	public ProfileSettingsAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	/**
	 * This method implements the Edge 'e_ChooseSettings'
	 * 
	 */
	public void e_ChooseSettings() {
		HomeScreen.tapSettingsMenu();
		ShortcutsTyper.delayTime(1000);
		Settings.tapSettings();
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_EditBirthDate'
	 * 
	 */
	public void e_EditBirthDate() {
		Settings.editBirthDate("04", "05", "1989");
	}

	/**
	 * This method implements the Edge 'e_EditGender'
	 * 
	 */
	public void e_EditGender() {
		Settings.editGender(false);
	}

	/**
	 * This method implements the Edge 'e_EditHeight'
	 * 
	 */
	public void e_EditHeight() {
		Settings.editHeight(7,9);
	}

	/**
	 * This method implements the Edge 'e_EditName'
	 * 
	 */
	public void e_EditName() {
		Settings.editName("thy vo");
	}

	/**
	 * This method implements the Edge 'e_EditPreferedUnits'
	 * 
	 */
	public void e_EditPreferedUnits() {
		Assert.assertTrue(Settings.hasPreferredUnitsField());
	}

	/**
	 * This method implements the Edge 'e_EditWeight'
	 * 
	 */
	public void e_EditWeight() {
		Settings.editWeight(120, 8);
	}

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
		ShortcutsTyper.delayTime(1000);
		Gui.pressBack();
		HomeScreen.tapSettingsMenu();
		ShortcutsTyper.delayTime(1000);
		Settings.tapSettings();
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_PressBack'
	 * 
	 */
	public void e_PressBack() {
		Settings.backToDayView();
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
	 * This method implements the Vertex 'v_UpdateProfile'
	 * 
	 */
	public void v_UpdateProfile() {
		// TODO:
	}

}
