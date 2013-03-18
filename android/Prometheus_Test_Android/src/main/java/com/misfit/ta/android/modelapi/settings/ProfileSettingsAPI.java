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
	private Field latestUpdatedField;
	private enum Field {
		 NAME, BIRTHDATE, HEIGHT, WEIGHT, GENDER, UNITS;  //; is optional
	}
	/**
	 * This method implements the Edge 'e_ChooseSettings'
	 * 
	 */
	public void e_ChooseSettings() {
		ShortcutsTyper.delayTime(3000);
		HomeScreen.tapSettingsMenu();
		ShortcutsTyper.delayTime(3000);
		Settings.tapSettings();
		ShortcutsTyper.delayTime(3000);
	}

	/**
	 * This method implements the Edge 'e_EditBirthDate'
	 * 
	 */
	public void e_EditBirthDate() {
		Settings.editBirthDate("04", "Mar", "1979");
		latestUpdatedField = Field.BIRTHDATE;
	}

	/**
	 * This method implements the Edge 'e_EditGender'
	 * 
	 */
	public void e_EditGender() {
		Settings.editGender(true);
		latestUpdatedField = Field.GENDER;
	}

	/**
	 * This method implements the Edge 'e_EditHeight'
	 * 
	 */
	public void e_EditHeight() {
		Settings.editHeight(7,5);
		latestUpdatedField = Field.HEIGHT;
	}

	/**
	 * This method implements the Edge 'e_EditName'
	 * 
	 */
	public void e_EditName() {
		Settings.editName("thy vo");
		latestUpdatedField = Field.NAME;
	}

	/**
	 * This method implements the Edge 'e_EditPreferedUnits'
	 * 
	 */
	public void e_EditPreferedUnits() {
		Settings.editPreferredUnits();
		latestUpdatedField = Field.UNITS;
	}

	/**
	 * This method implements the Edge 'e_EditWeight'
	 * 
	 */
	public void e_EditWeight() {
		Settings.editWeight(119, 8);
		latestUpdatedField = Field.WEIGHT;
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
		Gui.pressBack();
		ShortcutsTyper.delayTime(3000);
		HomeScreen.tapSettingsMenu();
		ShortcutsTyper.delayTime(3000);
		Settings.tapSettings();
		ShortcutsTyper.delayTime(3000);
	}
	private void check() {
		switch (latestUpdatedField) {
			case BIRTHDATE:
				Assert.assertEquals("03/04/1979", Settings.getCurrentBirthDate());
				break;
			case NAME:
				Assert.assertEquals("thy vo", Settings.getCurrentName());
				break;
			case HEIGHT:
				Assert.assertEquals("7\' 5\"", Settings.getCurrentHeight());
				break;
			case WEIGHT:
				Assert.assertEquals("119.8 lbs", Settings.getCurrentWeight());
				break;
			case UNITS:
				Assert.assertEquals("U.S.", Settings.getCurrentUnit());
				break;
			case GENDER:
				Assert.assertEquals("Male", Settings.getCurrentGender());
				break;
			default:
				System.out.println("Nothing to check");
				break;
		}
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
		// TODO: there should be another check here, but at this moment, the current views isn't re-loaded 
	}

}
