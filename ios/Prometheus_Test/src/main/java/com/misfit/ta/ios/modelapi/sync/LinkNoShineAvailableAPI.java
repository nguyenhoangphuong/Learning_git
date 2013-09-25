package com.misfit.ta.ios.modelapi.sync;

import java.io.File;
import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.gui.HomeScreen;
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
		String email = MVPApi.generateUniqueEmail();
		LaunchScreen.launch();

		SignUp.tapSignUp();
		ShortcutsTyper.delayTime(1000);
		SignUp.enterEmailPassword(email, "qwerty1");
		ShortcutsTyper.delayTime(10000);

		SignUp.enterGender(true);
		SignUp.enterBirthDay();
		SignUp.enterHeight();
		SignUp.enterWeight();
		SignUp.tapNext();

		ShortcutsTyper.delayTime(1000);
		SignUp.setGoal(1);
		SignUp.tapNext();
		
		// link v14@qa.com account to v14 shine
		Long now = System.currentTimeMillis() / 1000;
		String token = MVPApi.signIn("v14@qa.com", "test12").token;	
		Pedometer pedo = MVPApi.getPedometer(token);
		pedo.setFirmwareRevisionString(MVPApi.LATEST_FIRMWARE_VERSION_STRING);
		pedo.setSerialNumberString("XXXXXV0014");
		pedo.setLocalId("pedometer-" + MVPApi.generateLocalId());
		pedo.setLastSyncedTime(now);
		pedo.setLinkedTime(now);
		pedo.setUpdatedAt(now);
		MVPApi.updatePedometer(token, pedo);
	}

	public void e_triggerLink() {
		ShortcutsTyper.delayTime(1000);
		Sync.tapToSync();
		
		int totalTime = 0;
		int TIME_OUT = 300;
		while(!Sync.hasAlert() && !HomeScreen.isToday()) {
			ShortcutsTyper.delayTime(1000);
			
			totalTime += 1;
			if (totalTime > TIME_OUT)
				break;
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
		Assert.assertTrue(Sync.hasNoShineAvailableMessage() ||
				Sync.hasUnableToLinkMessage(), 
				"Current view is SignUp - Alert No Shine Available");
	}

}
