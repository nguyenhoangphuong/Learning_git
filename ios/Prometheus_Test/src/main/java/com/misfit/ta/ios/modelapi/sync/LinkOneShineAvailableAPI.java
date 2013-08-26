package com.misfit.ta.ios.modelapi.sync;

import java.io.File;
import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.SignUp;
import com.misfit.ta.gui.Sync;
import com.misfit.ta.ios.AutomationTest;

public class LinkOneShineAvailableAPI extends ModelAPI {
	protected static final Logger logger = Util.setupLogger(LinkOneShineAvailableAPI.class);

	public LinkOneShineAvailableAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	// edge
	public void e_init() {
		
		// log in and store token
		String token = MVPApi.signIn("v14@qa.com", "test12", "0123456789").token;
		MVPApi.unlinkDevice(token, "XXXXXV0014");
		
		
		// sign in
		LaunchScreen.launch();
		Sync.signIn();
		ShortcutsTyper.delayTime(1000);
		Sync.openSyncView();
		ShortcutsTyper.delayTime(1000);
	}

	public void e_triggerLink() {
		ShortcutsTyper.delayTime(1000);
		Sync.tapLinkShine();
		ShortcutsTyper.delayTime(2000);
		Sync.tapToSync();
		
		while(!HomeScreen.isToday()) {
			if(Sync.hasAlert())				
				break;
			
			ShortcutsTyper.delayTime(1000);
		}
	}


	// vertex
	public void v_LinkShine() {
		Assert.assertTrue(SignUp.isSignUpPairingView(), "Current view is HomeScreen - Link Shine");
	}

	public void v_HomeScreen() {
		Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen - Today");
	}

}
