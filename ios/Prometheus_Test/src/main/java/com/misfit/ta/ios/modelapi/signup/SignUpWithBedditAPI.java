package com.misfit.ta.ios.modelapi.signup;

import java.io.File;
import java.util.Calendar;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.SignUp;
import com.misfit.ta.gui.SleepViews;
import com.misfit.ta.gui.Timeline;

public class SignUpWithBedditAPI extends ModelAPI {
	
    public SignUpWithBedditAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
        super(automation, model, efsm, generator, weight);
    }

    private String email = MVPApi.generateUniqueEmail();
    
    public void e_init() {
    	
    	// go to sign up view
    	LaunchScreen.launch();
    	SignUp.tapSignUp();
    	
    	// input email and password
    	SignUp.enterEmailPassword(email, "qqqqqq");
        PrometheusHelper.waitForView("UILabel", DefaultStrings.SignUpProfileTitle);
        
        // input profile
        SignUp.enterGender(true);
        SignUp.enterBirthDay();
        SignUp.enterHeight();
        SignUp.enterWeight();
        
        // select beddit
        e_Next();
    }
    
    public void e_SelectBeddit() {
    	SignUp.tapSelectDevice(SignUp.SELECT_BEDDIT);
    }
    
    public void e_connect() {
    	
        SignUp.connectSimulatedBeddit();
        ShortcutsTyper.delayTime(3000);
    }
    
    public void e_Next() {
    	SignUp.tapNext();
    }
    
    public void e_goToActivityTimeline() {
    	
    	HomeScreen.tapActivityTimeline();
    }
    
    public void e_signOutAndSignInAgain() {
    	
    	PrometheusHelper.signOut();
    	PrometheusHelper.signIn(email, "qqqqqq");
    }
    
    public void e_linkShine() {
    	
    	HomeScreen.tapLinkNow();
    	PrometheusHelper.sync();
		PrometheusHelper.waitForView("PTRichTextLabel", DefaultStrings.TutorialFirstPageLabel);
		
		PrometheusHelper.handleTutorial();
		PrometheusHelper.handleUpdateFirmwarePopup();
    }
    
    public void v_SelectDevice() {
    	Assert.assertTrue(SignUp.isSelectDeviceView(), "This is not select device view");
    }
    
    public void v_SignUpConnectBeddit() {
    	
    	Assert.assertTrue(SignUp.isSignUpConnectBedditView(), "Current view is Sign Up - Connect Beddit");
    }
    
    public void v_SleepTimeline() {
    	
    	int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    	if(hourOfDay > 6 && hourOfDay < 20) { 
	    	Assert.assertTrue(HomeScreen.isSleepTimeline(), "Current view is Sleep Timeline");
	    	Assert.assertTrue(HomeScreen.isToday(), "Current date title is Today");
    	}
    	else {
    		Assert.assertTrue(SleepViews.isTonightUtilitiesView(), "Current view is Tonight");
    	}
    }
    
    public void v_ActivityTimeline() {
    	
    	Assert.assertTrue(HomeScreen.isActivityTimelineBedditPR(), "Current view is Activity Timeline - BedditPR view");
    }
    
    public void v_HomeScreen() {
    	
    	Assert.assertTrue(HomeScreen.isActivityTimeline(), "Current view is Activity Timeline");
    	Assert.assertTrue(!HomeScreen.isActivityTimelineBedditPR(), "No Beddit PR info anymore");
    	
    	Timeline.dragUpTimeline();
    	Assert.assertTrue(ViewUtils.isExistedView("UILabel", DefaultStrings.TileTapMeLabel), "Tap Me! tile is displayed");
    	Assert.assertTrue(ViewUtils.isExistedView("UILabel", DefaultStrings.TileActivitiesLabel), "Activities tile is displayed");
    	Assert.assertTrue(ViewUtils.isExistedView("UILabel", DefaultStrings.TileMilestonesLabel), "Milestones tile is displayed");
    }
    
    public void v_Wait() {
    	ShortcutsTyper.delayTime(5000);
    }
}
	