package com.misfit.ta.android.gui;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

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
    }
}
