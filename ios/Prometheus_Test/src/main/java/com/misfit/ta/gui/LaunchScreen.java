package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class LaunchScreen {
	
    public static void tapIHaveAShine() {
        Gui.touchAVIew("UIButtonLabel", " I HAVE A SHINE");
    }

    public static void tapSetUpYourShine() {
        Gui.touchAVIew("PTRichTextLabel", "_Set up_ your Shine in 30...");
    }
    
    public static void closeHardwareTutorial() {
    	// touch all 4 close buttons 
    	// to make sure tutorials close
        Gui.touchButton(5);
        Gui.touchButton(6);
        Gui.touchButton(7);
        Gui.touchButton(8);
    }

    public static void tapAgreeOnPopup() {
        Gui.touchPopupButton(1); // Agree
    }
    
    public static void tapAgreeInNDA() {
    	Gui.touchAVIew("UIButtonLabel", "AGREE");
    }
    
    public static void tapDisagreeInNDA() {
    	Gui.touchAVIew("UIButtonLabel", "DISAGREE");
    }

    public static void tapViewNDA() {
        Gui.touchPopupButton(0); // View NDA
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
    	tapAgreeOnPopup();
    	ShortcutsTyper.delayOne();
    	tapIHaveAShine();
    	ShortcutsTyper.delayOne();
    }

}
