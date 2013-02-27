package com.misfit.ta.android.gui;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.chimpchat.core.TouchPressType;

public class SetUpDevice {
	public void setupDevice() {
		Gui.touch(Gui.getCoordinators("ImageButton", "mID", "id/buttonSetup"),
				TouchPressType.DOWN);
	}
}
