package com.misfit.ta.android.aut;

import java.io.IOException;

import org.graphwalker.exceptions.StopConditionException;
import org.testng.annotations.Test;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.gui.HomeScreen;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.utils.ShortcutsTyper;

public class SyncingTest {
	private static int NUMBER_OF_SYNC = 10;
	
	@Test(groups = { "android", "Prometheus", "Syncing", "AndroidAutomation", "ContinuousSyncing", "ManualSyncing", "Excluded" })
	public void ManualSyncContinously() throws InterruptedException, StopConditionException, IOException {
		int countSuccessfulSync = 0;
		int countFailedSync = 0;

		Gui.init();
		int height = Gui.getScreenHeight();
		int width = Gui.getScreenWidth();
		boolean firstFailedSync = true;
		System.out.println("Full screen height: " + height);
		System.out.println("Full screen width: " + width);
		
		int popupHeight = 0;
		int popupWidth = 0;
		HomeScreen.tapDebug();
		for (int i = 0; i < NUMBER_OF_SYNC; i++) {
			System.out.println("****Start syncing #" + i);
			System.out.println("***Tap to sync #" + i);
			Gui.touchAView("ThreeStateView", "mID", "id/action_three_state_imageview");
			while("Syncing".equals(HomeScreen.getDebugValues()[5])) {
				System.out.println("****Still Syncing");
				ShortcutsTyper.delayTime(100);
				Gui.setInvalidView();
			}
			System.out.println("***Finish syncing #" + i);
			ShortcutsTyper.delayTime(3000);
			
			if (hasFailedSyncPopup()) {
				System.out.println("***Has failed sync popup#" + i);
				if (firstFailedSync) {
					popupHeight = Gui.getHeight();
					popupWidth = Gui.getWidth();
					firstFailedSync = false;
				}
				System.out.println("Popup height: " + popupHeight);
				System.out.println("Popup width: " + popupWidth);
				System.out.println("Ooops sync failed");
				ViewNode okButton = ViewUtils.findView("TextView", "mText", "OK", 0);
		        Gui.touchViewOnPopup(height, width, popupHeight, popupWidth, okButton);
		        countFailedSync++;
			} else {
				countSuccessfulSync++;
			}
			
			// Magic line which makes ViewServer reload views after we dismiss popup  
			ShortcutsTyper.delayTime(50);
		}
		System.out.println("Total successful syncs: " + countSuccessfulSync);
		System.out.println("Total failed syncs: " + countFailedSync);
		
		Gui.shutdown();
	}
	
	private boolean hasFailedSyncPopup() {
		 ViewNode node = ViewUtils.findView("TextView", "mText", "Your sync failed. Can you try syncing again?", 0);
		 return node != null;
	}

}
