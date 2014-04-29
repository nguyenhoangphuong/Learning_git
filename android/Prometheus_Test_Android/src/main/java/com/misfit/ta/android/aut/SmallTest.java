package com.misfit.ta.android.aut;

import org.testng.annotations.Test;

import java.io.IOException;

import org.graphwalker.exceptions.StopConditionException;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.gui.SignIn;
import com.misfit.ta.android.gui.SignUp;

public class SmallTest extends AutomationTest {
	@Test(groups = { "small", "Prometheus", "settings" })
    public void goalSettingsTest() throws InterruptedException, StopConditionException, IOException {
		Gui.printView();
		SignIn.chooseSignIn();
	}
	
	@Test(groups = { "small", "Prometheus", "settings" })
    public void goalSettingsTestA() throws InterruptedException, StopConditionException, IOException {
		Gui.printView();
		SignUp.chooseSignUp();
	}
}
