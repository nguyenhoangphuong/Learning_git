package com.misfit.ta.android.modelapi.homescreen;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.Gui;
import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.gui.PrometheusHelper;
import com.misfit.ta.modelAPI.ModelAPI;

public class DayProgressAPI extends ModelAPI {
	public DayProgressAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	private int days = PrometheusHelper.randInt(1, 10);
	
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
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_ViewPreviousDay'
	 * 
	 */
	public void e_ViewPreviousDay() {
		Gui.swipeRight(days);
	}

	/**
	 * This method implements the Edge 'e_ViewToday'
	 * 
	 */
	public void e_ViewToday() {
		Gui.swipeLeft(days);
	}

	/**
	 * This method implements the Vertex 'v_PreviousDay'
	 * 
	 */
	public void v_PreviousDay() {
		Assert.assertTrue(!isTodayView(), "This is not Today view");
	}

	/**
	 * This method implements the Vertex 'v_Today'
	 * 
	 */
	public void v_Today() {
		Assert.assertTrue(isTodayView(), "This is Today view");
	}

	/**
	 * This method implements the Vertex 'v_UpdateToday'
	 * 
	 */
	public void v_UpdateToday() {
		// TODO:
	}
	
	private boolean isTodayView() {
		return ViewUtils.findView("TextView", "mText", DefaultStrings.WalkingLeftText, 0) != null;
	}
}
