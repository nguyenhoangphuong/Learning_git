package com.misfit.ta.android.gui;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.xml.sax.SAXException;

import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.chimpchat.ChimpChat;
import com.misfit.ta.android.chimpchat.core.IChimpDevice;
import com.misfit.ta.android.chimpchat.core.PhysicalButton;
import com.misfit.ta.android.chimpchat.core.TouchPressType;
import com.misfit.ta.android.hierarchyviewer.HierarchyViewer;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.utils.ScreenShooter;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.Settings;

public class Gui extends com.misfit.ta.android.Gui {

	static {
		activityName = "com.misfit.mobile.android.ui/.activity.MainActivity";
		packageName = "com.misfit.mobile.android.ui";
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public static void main(String[] args) {
		Gui.init();
		Gui.shutdown();
	}
}
