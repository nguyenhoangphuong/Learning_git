package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class LaunchScreen {

	public static void tapIHaveAShine() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.HaveShineButton);
	}

	public static boolean isAtLaunchScreen() {
		return ViewUtils.isExistedView("UIButtonLabel",
				DefaultStrings.HaveShineButton);
	}

	public static boolean isAtInitialScreen() {
		return ViewUtils.isExistedView("UIButtonLabel",
				DefaultStrings.LogInFacebookButton)
				&& ViewUtils.isExistedView("UIButtonLabel",
						DefaultStrings.SignUpButton);
	}

	public static void launch() {
		tapIHaveAShine();
		ShortcutsTyper.delayOne();
	}

}
