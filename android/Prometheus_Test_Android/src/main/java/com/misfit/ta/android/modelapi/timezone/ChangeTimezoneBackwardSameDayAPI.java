package com.misfit.ta.android.modelapi.timezone;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;

public class ChangeTimezoneBackwardSameDayAPI extends ModelAPI {
	private String email = "";
	private String password = "qwerty1";
	private int currentTimezone = 4;
	private int previousTimezone = 4;
	private int delta = 1;
	private long beforeStartTime = 0;
	private long beforeEndTime = 0;
	private long beforeOffset = 0;

	public ChangeTimezoneBackwardSameDayAPI(AutomationTest automation,
			File model, boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	// edge
	public void e_init() {
	}

	public void e_changeTimezone() {
	}

	// vertex
	public void v_HomeScreen() {

	}

	public void v_HomeScreenUpdated() {

	}
}
