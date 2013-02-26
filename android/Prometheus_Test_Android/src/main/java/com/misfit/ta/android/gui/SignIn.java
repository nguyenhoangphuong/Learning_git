package com.misfit.ta.android.gui;

import com.misfit.ta.android.ViewUtils;

public class SignIn {
    
    public static void chooseSignIn() {
        Gui.touchAView("Button", 0);
    }
    
    public static void pressBack() {
        Gui.touchAView("ImageButton", 0);
    }
    
    public static boolean isInitViewVisible() {
        return ViewUtils.findView("Button", "mText", "I HAVE AN ACCOUNT", 0) != null;
    }
    
    public static boolean isSignInVisible() {
        return ViewUtils.findView("EditText", "mID", "password", 0) != null;
    }
    
    

}
