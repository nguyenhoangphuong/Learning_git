package com.misfit.ta.ios.modelapi.settings;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.Gui;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;

public class WearingShineAPI extends ModelAPI {
	public WearingShineAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private boolean isInit = true;
	private int positionIndex = -1;


	public void e_Init() {
		
		PrometheusHelper.signUpDefaultProfile();
		HomeScreen.tapOpenSettingsTray();
		HomeScreen.tapMyShine();
	}
	
	public void e_ToWearingShine() {
		
		goToWearingShine();
	}
	
	public void e_ToUpdatedWearingShine() {
		goToWearingShine();
	}

	public void e_Back() {
		
		HomeSettings.tapBack();
		PrometheusHelper.waitForView("UILabel", DefaultStrings.MyShineTitle);
	}

	public void e_ChoosePosition() {
		
		isInit = false;
		if (positionIndex < 5) {
			positionIndex++;
			System.out.println("DEBUG position index " + positionIndex);
			Gui.touchAVIew("UIImageView", positionIndex + 2);
		}
	}

	private void goToWearingShine() {
		
		Gui.swipeUp(50);
		Gui.touchAVIew("UILabel", DefaultStrings.WearingShineButton);
	}


	
	public void v_EndInput() {
		// TODO:
	}

	public void v_MyShine() {
		Assert.assertTrue(HomeSettings.isAtMyShine(),
				"Current view is My Shine");
	}

	public void v_UpdatedWearingShine() {
		if (positionIndex != -1 && positionIndex < 5) {
			Assert.assertTrue(isSelectedPosition(positionIndex),
					"Wearing position is selected OK with index "
							+ positionIndex);
		}
	}

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
}
