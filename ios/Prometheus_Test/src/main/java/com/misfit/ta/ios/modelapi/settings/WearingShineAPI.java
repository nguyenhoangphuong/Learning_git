package com.misfit.ta.ios.modelapi.settings;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class WearingShineAPI extends ModelAPI {
	public WearingShineAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	/**
	 * This method implements the Edge 'e_BackToHomeScreen'
	 * 
	 */
	private int index = -1;
	private String[] upperTitle = {"HEAD", "UPPER ARM", "CHEST", "WRIST", "WAIST", "FOOT/ANKLE"};
	private String[] lowerTitle = {"Head", "Upper Arm", "Chest", "Wrist", "Waist", "Ankle/Foot"};
	private String[] content = {
			"Clip Shine to your hat or glasses for an eye-catching look.",
			"The Shine clasp clips conveniently to your blouse, polo or Def Leppard concert T-shirt.",
			"Shine clips to your shirt collar or pocket. Or use the necklace accessory to add a sleek, elegant touch to your outfit.",
			"If you want to use Shine as a watch, there's no better place than the wrist. Just make sure you use the official wristband or leather wristband to keep Shine secure!",
			"Clip Shine to your belt, pocket or shirt hem. Shine can be a fashion statement or unobtrusive, depending on your mood.",
			"Shine's clasp clips securely to your shoe, sock or shoelace."
	};
	public void e_BackToHomeScreen() {
		HomeSettings.tapBack();
		ShortcutsTyper.delayOne();
	}

	/**
	 * This method implements the Edge 'e_ChooseSettings'
	 * 
	 */
	public void e_ChooseSettings() {
		HomeScreen.tapOpenSyncTray();
		ShortcutsTyper.delayOne();
		HomeScreen.tapSettings();
		ShortcutsTyper.delayOne();
	}

	/**
	 * This method implements the Edge 'e_ChooseWearingShine'
	 * 
	 */
	public void e_ChooseWearingShine() {
		HomeSettings.tapWearingShine();
		ShortcutsTyper.delayOne();
	}

	/**
	 * This method implements the Edge 'e_DoneEditting'
	 * 
	 */
	public void e_DoneEditting() {
		HomeSettings.tapBack();
		ShortcutsTyper.delayTime(5000);
	}

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		PrometheusHelper.signUp(MVPApi.generateUniqueEmail(), "qwerty1", true,
				16, 9, 1991, true, "5'", "8\\\"", "120", ".0", 1);
		ShortcutsTyper.delayTime(5000);
	}

	/**
	 * This method implements the Edge 'e_TapChest'
	 * 
	 */
	public void e_TapChest() {
		HomeSettings.tapChest();
		index = 2;
		ShortcutsTyper.delayTime(300);
	}

	/**
	 * This method implements the Edge 'e_TapFoot'
	 * 
	 */
	public void e_TapFoot() {
		HomeSettings.tapFootAnkle();
		index = 5;
		ShortcutsTyper.delayTime(300);
	}

	/**
	 * This method implements the Edge 'e_TapHead'
	 * 
	 */
	public void e_TapHead() {
		HomeSettings.tapHead();
		index = 0;
		ShortcutsTyper.delayTime(300);
	}

	/**
	 * This method implements the Edge 'e_TapUpperArm'
	 * 
	 */
	public void e_TapUpperArm() {
		HomeSettings.tapUpperArm();
		index = 1;
		ShortcutsTyper.delayTime(300);
	}

	/**
	 * This method implements the Edge 'e_TapWaist'
	 * 
	 */
	public void e_TapWaist() {
		HomeSettings.tapWaist();
		index = 4;
		ShortcutsTyper.delayTime(300);
	}

	/**
	 * This method implements the Edge 'e_TapWrist'
	 * 
	 */
	public void e_TapWrist() {
		HomeSettings.tapWrist();
		index = 3;
		ShortcutsTyper.delayTime(300);
	}
	
	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		// check if this is Homescreen
		Assert.assertTrue(HomeScreen.isToday(), "Current screen is Homescreen");
	}

	/**
	 * This method implements the Vertex 'v_Settings'
	 * 
	 */
	public void v_Settings() {
		if (index != -1) {
			Assert.assertTrue(ViewUtils.isExistedView("UILabel", lowerTitle[index]), "Wearing Shine should be " + lowerTitle[index]);
			ShortcutsTyper.delayTime(300);
		}
	}

	/**
	 * This method implements the Vertex 'v_WearingShineView'
	 * 
	 */
	public void v_WearingShineView() {
		Assert.assertTrue(HomeSettings.isAtWearingShine(),
				"This is not Wearing Shine view");
		ShortcutsTyper.delayTime(300);

	}

	/**
	 * This method implements the Vertex 'v_WearingShineViewUpdated'
	 * 
	 */
	public void v_WearingShineViewUpdated() {
		Assert.assertTrue(ViewUtils.isExistedView("MFVInsetLabel", content[index]), "Content of tip is not correct!!! It should be " + content[index]);
		if (index == 3) {
			Assert.assertTrue(ViewUtils.isExistedView("UIButtonLabel", "BUY WRISTBAND"), "There should be a button to buy wristband");
		} else if (index == 2) {
			Assert.assertTrue(ViewUtils.isExistedView("UIButtonLabel", "BUY NECKLACE"), "There should be a button to buy necklace");
		}
		ShortcutsTyper.delayTime(300);
	}

}
