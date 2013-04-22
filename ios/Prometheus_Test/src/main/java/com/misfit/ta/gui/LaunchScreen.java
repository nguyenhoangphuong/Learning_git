package com.misfit.ta.gui;

import com.misfit.ta.utils.ShortcutsTyper;

public class LaunchScreen {
    public static void tapIHaveAShine() {
        Gui.touchAVIew("UIButtonLabel", "I HAVE A SHINE");
    }

    public static void tapIDontHaveAShine() {
        Gui.touchAVIew("UIButtonLabel", "I DON'T HAVE A SHINE");
    }

    public static void tapSkip() {
        Gui.touchAVIew("UIButtonLabel", "SKIP");
    }

    public static void tapAgree() {
        // choose Agree on popup View NDA
        Gui.touchPopupButton(1); // Agree
    }

    public static void tapViewNDA() {
        Gui.touchPopupButton(0);
    }
    
    public static void launch() {
    	tapAgree();
    	ShortcutsTyper.delayTime(300);
    	tapIHaveAShine();
    	ShortcutsTyper.delayTime(300);
    	tapSkip();
    	ShortcutsTyper.delayTime(5000);
    }
}
