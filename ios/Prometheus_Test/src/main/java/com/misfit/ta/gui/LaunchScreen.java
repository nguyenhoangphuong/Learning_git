package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class LaunchScreen {
    public static void tapIHaveAShine() {
        Gui.touchAVIew("UIButtonLabel", "I HAVE A SHINE");
    }

    public static void tapSetUpYourShine() {
        Gui.touchAVIew("PTRichTextLabel", "Need help to _set up_ your Shine?");
    }
    
    public static void closeHardwareTutorial() {
    	// touch all 3 close buttons to make sure tutorials close
        Gui.touchButton(5);
        Gui.touchButton(6);
        Gui.touchButton(7);
    }

    public static void tapAgreeOnPopup() {
        Gui.touchPopupButton(1); // Agree
    }
    
    public static void tapAgreeInNDA() {
    	Gui.touchAVIew("UIButtonLabel", "AGREE");
    }

    public static void tapViewNDA() {
        Gui.touchPopupButton(0); // View NDA
    }
    
    public static boolean isAtLaunchScreen()
    {
    	return ViewUtils.isExistedView("UIButtonLabel", "I HAVE A SHINE");
    }
    
    public static void launch() {
    	tapAgreeOnPopup();
    	ShortcutsTyper.delayTime(1000);
    	tapIHaveAShine();
    	ShortcutsTyper.delayTime(1000);
//    	tapSkip();
//    	ShortcutsTyper.delayTime(5000);
    }
}
