package com.misfit.ta.ios.modelapi.signin;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.SignIn;
import com.misfit.ta.ios.AutomationTest;

public class SuccessfulSignInAPI extends ModelAPI {
    public SuccessfulSignInAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator,
            boolean weight) {
        super(automation, model, efsm, generator, weight);
    }

    /**
     * This method implements the Edge 'e_Init'
     * 
     */
    public void e_Init() {
    	LaunchScreen.launch();
    }
    
    /**
     * This method implements the Edge 'e_ChooseSignIn'
     * 
     */
    public void e_ChooseSignIn() {
        SignIn.tapLogIn();
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_FillCorrectEmailPassword'
     * 
     */
    public void e_FillCorrectEmailPassword() {
        SignIn.enterEmailPassword("thy@misfitwearables.com", "test12");
        ShortcutsTyper.delayTime(5000);
        
        // wait for sync data
        ShortcutsTyper.delayTime(25000);
    }  
    
    /**
     * This method implements the Vertex 'v_InitialView'
     * 
     */
    public void v_InitialView() {
        // check if this is starting screen
    	Assert.assertTrue(LaunchScreen.isAtInitialScreen(), "Current view is InitialView");
    }
    
    /**
     * This method implements the Vertex 'v_HomeScreen'
     * 
     */
    public void v_HomeScreen() {
        // check if this screen is home screen
    	Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen - Today");
    }

    /**
     * This method implements the Vertex 'v_SignInVisible'
     * 
     */
    public void v_SignInVisible() {
        Assert.assertTrue(SignIn.isLoginView(), "This is not sign in view.");
    }

}
