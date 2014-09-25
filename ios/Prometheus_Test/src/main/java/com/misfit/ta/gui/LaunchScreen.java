package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;

public class LaunchScreen {

	public static boolean isAtInitialScreen() {
		return ViewUtils.isExistedView("UIButtonLabel",
				DefaultStrings.LogInFacebookButton)
				&& ViewUtils.isExistedView("UIButtonLabel",
						DefaultStrings.SignUpButton);
	}

}
