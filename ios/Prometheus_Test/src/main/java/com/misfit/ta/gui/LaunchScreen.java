package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class LaunchScreen {
	
    public static void tapIHaveAShine() {
        Gui.touchAVIew("UIButtonLabel", " I HAVE A SHINE");
    }

    public static boolean isAtLaunchScreen()
    {
    	return ViewUtils.isExistedView("UIButtonLabel", " I HAVE A SHINE");
    }
    
    public static boolean isAtInitialScreen()
    {
    	return ViewUtils.isExistedView("UIButtonLabel", " SIGN IN") &&
    		   ViewUtils.isExistedView("UIButtonLabel", " SIGN UP");
    }
    
    public static void launch() {
    	tapIHaveAShine();
    	ShortcutsTyper.delayOne();
    }

}
