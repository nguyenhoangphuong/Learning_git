package com.misfit.ta.android.gui;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.utils.ShortcutsTyper;

public class SignIn {

	public static void chooseSignIn() {
		Gui.touchAView("TextView", "mID", DefaultStrings.SignInButtonTextId);
	}

	public static void pressBack() {
		Gui.touchAView("ImageButton", "mID", DefaultStrings.ImageBackButtonId);
	}

	public static boolean isInitViewVisible() {
		ShortcutsTyper.delayTime(1000);
		return ViewUtils.findView("TextView", "mID", DefaultStrings.SignInButtonTextId, 0) != null;
	}

	public static boolean isSignInVisible() {
		ShortcutsTyper.delayTime(1000);
		return ViewUtils.findView("EditText", "mID", DefaultStrings.SignInEmailTextViewId, 0) != null;
	}

	public static void fillSignIn(String email, String password) {
		Gui.touchAView("EditText", "mID", DefaultStrings.SignInEmailTextViewId);
		Gui.type(email);
		Gui.touchAView("EditText", "mID", DefaultStrings.SignInPasswordTextViewId);
		Gui.type(password);
		Gui.pressBack();
	}

	public static void pressNext() {
		Gui.touchAView("ImageButton", "mID", DefaultStrings.ImageNextButtonId);
	}

	public static boolean hasInvalidEmailMessage() {
		ShortcutsTyper.delayTime(500);
		return ViewUtils.findView("TextView", "mID", "id/textErrorMessage", 0).text.equals("\\nInvalid email");
	}

	public static boolean hasInvalidPasswordMessage() {
		ShortcutsTyper.delayTime(500);
		return ViewUtils.findView("TextView", "mID", "id/textErrorMessage", 0).text.equals("\\nSorry, the password should have at least 6 characters, at least 1 digit and 1 letter");
	}
	public static boolean hasIncorrectEmailPasswordMessage() {
		 ViewNode node = ViewUtils.findView("TextView", "mText", DefaultStrings.IncorrectSignInMessage, 0);
		 return node != null;
	}
	
	public static void tapForgotPassword() {
		Gui.touchAView("Button", "mID", "id/buttonForgotPassword");
	}

}
