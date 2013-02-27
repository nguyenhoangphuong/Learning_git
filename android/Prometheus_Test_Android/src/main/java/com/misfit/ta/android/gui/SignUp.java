package com.misfit.ta.android.gui;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;

public class SignUp {
	public static void chooseSignUp() {
		Gui.touchAView("Button", "mID", "id/buttonNotHaveAccount");
	}

	public static void pressBack() {
		Gui.touchAView("ImageButton", "mID", "id/buttonPrevious");
	}

	public static void fillRegisterProfile(String name, String email,
			String password, boolean isMale, boolean isMetric) {
		Gui.touchAView("EditText", "mID", "id/userinfo_nameEdit");
		Gui.type(name);
		Gui.touchAView("EditText", "mID", "id/userinfo_emailEdit");
		Gui.type(email);
		Gui.touchAView("EditText", "mID", "id/userinfo_passwordEdit");
		Gui.type(password);
		ViewNode maleRadioButton = ViewUtils.findView("RadioButton", "mID",
				"id/userinfo_maleButton", 0);
		if (isMale && !maleRadioButton.isChecked) {
			Gui.touchAView("RadioButton", "mID", "id/userinfo_maleButton");
		} else if (maleRadioButton.isChecked) {
			Gui.touchAView("RadioButton", "mID", "id/userinfo_femaleButton");
		}
		ViewNode metricRadioButton = ViewUtils.findView("RadioButton", "mID",
				"id/userinfo_metricButton", 0);
		if (isMetric && !metricRadioButton.isChecked) {
			Gui.touchAView("RadioButton", "mID", "id/userinfo_metricButton");
		} else if (metricRadioButton.isChecked) {
			Gui.touchAView("RadioButton", "mID", "id/userinfo_usButton");
		}
		// TODO fill info for birthdate, weight, height

	}

	public static boolean isSignUpVisible() {
		return ViewUtils.findView("EditText", "mID",
				"id/userinfo_passwordEdit", 0) != null;
	}

}
