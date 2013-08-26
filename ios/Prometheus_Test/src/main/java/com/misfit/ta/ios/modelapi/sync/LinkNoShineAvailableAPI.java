package com.misfit.ta.ios.modelapi.sync;

import java.io.File;
import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.SignUp;
import com.misfit.ta.gui.Sync;
import com.misfit.ta.ios.AutomationTest;

public class LinkNoShineAvailableAPI extends ModelAPI {
	protected static final Logger logger = Util.setupLogger(LinkNoShineAvailableAPI.class);

	public LinkNoShineAvailableAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	// edge
	public void e_init() {
		
		// sign up a new account and go to LinkShine screen
		LaunchScreen.launch();

		SignUp.tapSignUp();
		ShortcutsTyper.delayTime(1000);
		SignUp.enterEmailPassword(MVPApi.generateUniqueEmail(), "qwerty1");
		ShortcutsTyper.delayTime(10000);

		SignUp.enterGender(true);
		SignUp.enterBirthDay();
		SignUp.enterHeight();
		SignUp.enterWeight();
		SignUp.tapNext();

		ShortcutsTyper.delayTime(1000);
		SignUp.setGoal(1);
		SignUp.tapNext();
	}

	public void e_triggerLink() {
		ShortcutsTyper.delayTime(1000);
		Sync.tapToSync();
		
		while(!Sync.hasAlert()) {
			ShortcutsTyper.delayTime(1000);
		}
	}

	public void e_confirm() {
		Sync.tapOK();
	}
	
	public void e_tapNext() {
		SignUp.tapNext();
	}

	// vertex
	public void v_LinkShine() {
		Assert.assertTrue(SignUp.isSignUpPairingView(), "Current view is SignUp - Link Shine");
	}

	public void v_NoShineAvailable() {
		Assert.assertTrue(Sync.hasNoShineAvailableMessage(), "Current view is SignUp - Alert No Shine Available");
	}

}
