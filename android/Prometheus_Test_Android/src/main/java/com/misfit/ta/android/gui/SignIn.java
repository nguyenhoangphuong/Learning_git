package com.misfit.ta.android.gui;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;

public class SignIn {

	public static void chooseSignIn() {
		Gui.touchAView("Button", "mID", "id/buttonHaveAccount");
	}

	public static void pressBack() {
		Gui.touchAView("ImageButton", "mID", "id/buttonPrevious");
	}

	public static boolean isInitViewVisible() {
		return ViewUtils.findView("Button", "mID", "id/buttonHaveAccount", 0) != null;
	}

	public static boolean isSignInVisible() {
		return ViewUtils.findView("EditText", "mID", "id/editPassword", 0) != null;
	}

	private void fillSignIn(String email, String password) {
		Gui.touchAView("EditText", "mID", "id/editEmail");
		Gui.type(email);
		Gui.touchAView("EditText", "mID", "id/editPassword");
		Gui.type(password);
	}

	public void signIn() {
		Gui.touchAView("Button", "mID", "id/buttonHaveAccount");
		fillSignIn("test27@thy.com", "test12");
		Gui.pressBack();
		Gui.touchAView("ImageButton", "mID", "id/buttonNext");
	}

}
