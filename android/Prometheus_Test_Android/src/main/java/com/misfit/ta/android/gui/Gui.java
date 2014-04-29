package com.misfit.ta.android.gui;

import java.io.IOException;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.utils.ShortcutsTyper;

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
		int fullScreenHeight = 1184;
		int fullScreenWidth = 768;
		System.out.println(HomeScreen.getTotalSleep());
		Gui.shutdown();
	}
}
