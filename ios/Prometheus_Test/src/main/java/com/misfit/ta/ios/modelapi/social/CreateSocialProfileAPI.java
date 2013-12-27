package com.misfit.ta.ios.modelapi.social;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.ios.AutomationTest;

public class CreateSocialProfileAPI extends ModelAPI {
	protected static final Logger logger = Util
			.setupLogger(CreateSocialProfileAPI.class);

	public CreateSocialProfileAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	
	public void e_init() {
		
		PrometheusHelper.signUpDefaultProfile();
	}
	
	public void e_goToLeaderBoard() {
		
		
	}
	
	public void e_goToWorldView() {
		
	}
	
	public void e_tapDontHaveFacebook() {
		
	}
	
	public void e_inputInvalidAvatar() {
		
	}
	
	public void e_inputInvalidName() {
		
	}
	
	public void e_inputInvalidHandle() {
		
	}
	
	public void e_inputDuplicatedHandle() {
		
	}
	
	public void e_submitValidProfile() {
		
	}
	
	public void e_tapGoForIt() {
		
	}
	
	public void e_tapGoToSocialProfile() {
		
	}
	
	
	
	public void v_HomeScreen() {
		
	}
	
	public void v_LeaderBoardDefault() {
		
	}
	
	public void v_WorldViewDefault() {
		
	}
	
	public void v_SocialProfileRegister() {
		
	}
	
	public void v_InvalidAvatar() {
		
	}
	
	public void v_InvalidName() {
		
	}
	
	public void v_InvalidHandle() {
		
	}
	
	public void v_DuplicatedHandle() {
		
	}
	
	public void v_WordViewUpdated() {
		
	}
	
	public void v_LeaderBoardUpdated() {
		
	}
	
	public void v_LeaderBoard() {
		
	}

	public void v_SocialProfile() {
		
	}
}
