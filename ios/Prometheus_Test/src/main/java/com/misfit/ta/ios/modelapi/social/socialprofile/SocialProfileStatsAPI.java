package com.misfit.ta.ios.modelapi.social.socialprofile;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.gui.AppInstaller;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.ios.AutomationTest;

public class SocialProfileStatsAPI extends ModelAPI {
	protected static final Logger logger = Util
			.setupLogger(SocialProfileStatsAPI.class);

	public SocialProfileStatsAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}


	public void e_installAndLaunchAppWithoutSocialFeature() {
		
		if(!AppInstaller.checkAppsExist(AppInstaller.MVP_19))
			Assert.fail("Try to install MVP19 but cannot find the app");
		
		AppInstaller.installAndLaunchApp(AppInstaller.MVP_19);
	}
	
	public void e_signUpAndInputStats() {
		
		PrometheusHelper.signUpDefaultProfile();
		
	}
	
	public void e_upgradeAndLaunchLatestApp() {
		
	}
	
	public void e_tapToJoinSocial() {
		
	}
	
	public void e_submitProfile() {
		
	}
	
	public void e_completeGoalAndReachNewPersonalBest() {
		
	}
	
	public void e_goToSocialProfile() {
		
	}
	
	
	
	public void v_LaunchScreen() {
		
	}
	
	public void v_HomeScreen() {
		
	}
	
	public void v_ProfilePreview() {
		
	}
	
	public void v_SocialProfile() {
		
	}
	
}