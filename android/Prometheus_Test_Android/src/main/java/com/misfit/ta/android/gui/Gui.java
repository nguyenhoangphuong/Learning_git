package com.misfit.ta.android.gui;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

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
		Gui.printView();
		Gui.shutdown();
	}
}
