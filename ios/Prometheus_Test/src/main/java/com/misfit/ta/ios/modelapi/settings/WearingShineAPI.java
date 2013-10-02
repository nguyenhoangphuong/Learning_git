package com.misfit.ta.ios.modelapi.settings;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.Gui;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.gui.DefaultStrings;
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

	private boolean isInit = true;
	private int positionIndex = -1;

	/**
	 * This method implements the Edge 'e_Back'
	 * 
	 */
	public void e_Back() {
		HomeSettings.tapBack();
		ShortcutsTyper.delayTime(2500);
	}

	/**
	 * This method implements the Edge 'e_CheckEnd'
	 * 
	 */
	public void e_CheckEnd() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_ChoosePosition'
	 * 
	 */
	public void e_ChoosePosition() {
		isInit = false;
		if (positionIndex < 5) {
			positionIndex++;
			System.out.println("DEBUG position index " + positionIndex);
			Gui.touchAVIew("UIImageView", positionIndex + 2);
		}
	}

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		PrometheusHelper.signUp(MVPApi.generateUniqueEmail(), "qwerty1", true,
				16, 9, 1991, true, "5'", "8\\\"", "120", ".0", 1);
		ShortcutsTyper.delayTime(5000);
		HomeScreen.tapOpenSettingsTray();
		ShortcutsTyper.delayTime(300);
		HomeScreen.tapMyShine();
		ShortcutsTyper.delayTime(300);
	}

	/**
	 * This method implements the Edge 'e_Stay'
	 * 
	 */
	public void e_Stay() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_ToWearingShine'
	 * 
	 */
	public void e_ToWearingShine() {
		goToWearingShine();
	}

	private void goToWearingShine() {
		Gui.swipeUp(50);
		Gui.touchAVIew("UILabel", DefaultStrings.WearingShineButton);
	}

	/**
	 * This method implements the Vertex 'v_EndInput'
	 * 
	 */
	public void v_EndInput() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_MyShine'
	 * 
	 */
	public void v_MyShine() {
		Assert.assertTrue(HomeSettings.isAtMyShine(),
				"Current view is My Shine");
	}

	/**
	 * This method implements the Vertex 'v_UpdatedWearingShine'
	 * 
	 */
	public void v_UpdatedWearingShine() {
		if (positionIndex != -1 && positionIndex < 5) {
			Assert.assertTrue(isSelectedPosition(positionIndex),
					"Wearing position is selected OK with index "
							+ positionIndex);
		}
	}

	/**
	 * This method implements the Vertex 'v_WearingShine'
	 * 
	 */
	public void v_WearingShine() {
		if (isInit) {
			boolean hasSelectedPosition = false;
			for (int i = 0; i < 5; i++) {
				Assert.assertTrue(ViewUtils.isExistedView("UILabel",
						DefaultStrings.PositionText[i]),
						"Wearing position text is correct with index " + i);
				Assert.assertTrue(ViewUtils.isExistedView("PTRichTextLabel",
						DefaultStrings.PositionDescription[i]),
						"Wearing position description is correct with index "
								+ i);
				if (isSelectedPosition(i)) {
					hasSelectedPosition = true;
					break;
				}
			}
			Assert.assertTrue(!hasSelectedPosition,
					"There's no default selected position");
			isInit = false;
		}
	}

	private boolean isSelectedPosition(int index) {
		return "1".equals(Gui.getProperty("PTSettingsWearingPositionViewCell",
				index, "isSelected"));
	}

	public void e_ToUpdatedWearingShine() {
		goToWearingShine();
	}
}
