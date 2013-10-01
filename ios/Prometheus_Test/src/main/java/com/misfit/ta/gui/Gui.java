package com.misfit.ta.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

import com.misfit.ios.AppHelper;
import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.Settings;
import com.misfit.ta.report.TRS;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ProcessFinder;
import com.misfit.ta.utils.ShortcutsTyper;

public class Gui {

    private static String fruitStrapPath = "";
    private static String template = "";
    private static Logger logger = Util.setupLogger(Gui.class);
    
    private static boolean flightMode = false;
    private static boolean bluetooth = false;
    private static boolean locationService = false;

    static {
        try {
            fruitStrapPath = Files.getExecutableFile(Settings.getValue("fruitStrapPath"));
        } catch (Exception e) {
            try {
                Files.extractJar("tools", true);
                fruitStrapPath = Files.getExecutableFile(Settings.getValue("fruitStrapPath"));
            } catch (IOException e1) {
                // e1.printStackTrace();
                // logger.debug(e1);
            }
        }
        logger.info("Fruitstrap path= " + fruitStrapPath);
        try {
            // Files.extractJar(Settings.getValue("instrumentsTemplate"),
            // false);
            File file = Files.getFile(Settings.getValue("instrumentsTemplate"));
            if (file.length() <= 0) {
                file.delete();
                Files.extractJar("tools", true);
                file = Files.getFile(Settings.getValue("instrumentsTemplate"));
            }

            template = file.getAbsolutePath();

            // Files.extractJar(Settings.getValue("instrumentsTemplate"),
            // false);
        } catch (Exception e) {
            logger.debug(e.toString());
            // e.printStackTrace();
        }
        logger.info("template= " + template);
    }

    /**
     * Install app, given device UDID and app path
     * 
     * @param deviceUDID
     * @param bundlePath
     */
    public static void install(String deviceUDID, String bundlePath) {
        logger.info("Installing app=" + bundlePath + " to device=" + deviceUDID);
        ProcessBuilder pb1 = new ProcessBuilder("chmod", "777", fruitStrapPath);
        runProcess(pb1);
        System.out.println("LOG [Gui.install]: " + bundlePath);
        ProcessBuilder pb = new ProcessBuilder(fruitStrapPath, "install", "--id", deviceUDID, "--bundle", bundlePath);

        runProcess(pb, true, true);
    }

    public static String runProcess(ProcessBuilder pb) {
        return runProcess(pb, false, false);
    }

    public static String runProcess(ProcessBuilder pb, boolean failOnError) {
        return runProcess(pb, true, false);
    }

    public static String runProcess(ProcessBuilder pb, boolean failOnError, boolean verbose) {

        Process p;
        BufferedReader br = null;
        try {
            p = pb.start();
            InputStream is = p.getInputStream();
            // read it with BufferedReader
            br = new BufferedReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
                if (verbose) {
                    logger.info(line);
                }
            }

            String result = sb.toString();
            // read error with BufferedReader
            br = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
                System.out.println("LOG [Gui.runProcess]: " + line);
            }
            String error = sb.toString();
            if (error != null && error.length() > 0) {
                logger.error("Error when install the app:\n" + error);
                if (failOnError) {
                    checkForError(error);

                }
            }

            return result;
        } catch (IOException e) {
            logger.info("LOG [AppHelper.runProcess]: fail to run: " + e);
            System.out.println("LOG [AppHelper.runProcess]: fail to run: " + e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return "UNKNOW ERROR";
    }

    private static void checkForError(String error) {

        // exception:
        if (error.indexOf("CGImageCreateWithImageProvider") > 0) {
            return;
        }
        // else fail
        if (error.indexOf("Failed") >= 0 || error.indexOf("Error") >= 0) {
            Assert.assertTrue(false, "Instruments failed with error: " + error);
        }
    }

    public static void uninstall(String deviceUUID, String bundleName) {
        ProcessBuilder pb = new ProcessBuilder(fruitStrapPath, "uninstall", "--id", deviceUUID, "--bundle", bundleName);
        runProcess(pb);
    }

    public static void uninstall(String deviceUUID) {
        String bundleName = Settings.getParameter("appBundle");
        ProcessBuilder pb = new ProcessBuilder(fruitStrapPath, "uninstall", "--id", deviceUUID, "--bundle", bundleName);
        runProcess(pb);
    }

    /**
     * Get the list of udids of attached devices.
     * 
     * @return
     */
    public static Vector<String> getUdids() {
        Vector<String> udids = new Vector<String>();
        String udidetect = Files.getExecutableFile(Settings.getValue("udidetect"));
        ProcessBuilder pb = new ProcessBuilder(udidetect, "-z");
        String results = runProcess(pb);
        logger.info("Attached devices:\n " + results);
        StringTokenizer token = new StringTokenizer(results);
        while (token.hasMoreTokens()) {
            udids.add(token.nextToken());
        }
        return udids;
    }

    public static String getCurrentUdid() {
        String result = Settings.getParameter("udid");
        if (result == null || result.length() < 1) {
            result = getUdids().elementAt(0);
        }
        return result;
    }

    public static String getAppPath() {
        String current = "";
        current = Settings.getParameter("appPath");
        return current;
    }

    /**
     * Get app path by declaring the name of the parameter containing the path
     * 
     * @param appPathParamName
     *            Name of the parameter which contains the app path
     * @return App path
     */
    public static String getAppPath(String appPathParamName) {
        return Settings.getParameter(appPathParamName);
    }

    public static String launchInstrument(String UDID, String appName, String testCase) {
        logger.info("Launching testcase\ntestCase= " + testCase + "\nUDID= " + UDID + "\nappName= " + appName);
        System.out.println("Launching testcase\ntestCase= " + testCase + "\nUDID= " + UDID + "\nappName= " + appName);
        File aCase;
        try {
            Files.emptyDir(new File("script/"), null);
            try {
                Files.getFile("script/");
            } catch (Exception e) {
                try {
                    logger.info("LOG [AppHelper.launchInstrument]: extracting scripts...");
                    Files.extractJar("script/", true);
                } catch (IOException e1) {
                    logger.info("LOG [AppHelper.launchInstrument]: error: " + e);
                    e1.printStackTrace();
                }
            }

            aCase = Files.getFile(testCase);
            if (aCase != null) {
                testCase = aCase.getAbsolutePath();
                System.out.println("LOG [AppHelper.launchInstrument]: " + testCase);
            }
        } catch (FileNotFoundException e) {
            logger.info("Failed to start a case: " + testCase + " because: " + e.toString());
            Assert.assertTrue(false, "Testcase FAILED: failed to load the test case");
            System.out.println("LOG [AppHelper.launchInstrument]: failed to start case: " + testCase);
        }

        ProcessBuilder pb;
        if (UDID.equalsIgnoreCase("SIM")) {

            pb = new ProcessBuilder();
            pb.command("instruments", "-t", template, appName, "-e", "UIASCRIPT", testCase, "-e", "UIARESULTSPATH",
                    "logs");
        } else {
            pb = new ProcessBuilder();
            String command = "instruments -w " + UDID + " -t " + template + " " + appName + " -e UIASCRIPT " + testCase
                    + " -e UIARESULTSPATH logs";
            logger.info("Command: " + command);
            System.out.println("LOG [AppHelper.launchInstrument]: command= \n" + command);
            pb.command("instruments", "-w", UDID, "-t", template, appName, "-e", "UIASCRIPT", testCase, "-e",
                    "UIARESULTSPATH", "logs");
        }
        String result = runProcess(pb, true, true);
        // logger.info("Instruments results: \n" + result);
        failOnError(result);

        return result;
    }

    private static void failOnError(String result) {
        if (result.toLowerCase().indexOf("uncaught javascript error") > 0 || result.toLowerCase().indexOf("fail") > 0) {
            Assert.assertTrue(false, "Test output contain fail string");
        }
    }

    /**
     * Clean cache (un-install) the app declared in appBundle param with the
     * option to install new app or not
     * 
     * @param cleanCacheOnly
     *            Specify whether the app in appPath param should be installed
     *            (cleanCacheOnly = false) or not (cleanCacheOnly = true)
     */
    public static void cleanCache(boolean cleanCacheOnly) {
        uninstall(getCurrentUdid());

        if (!cleanCacheOnly) {
            install(getCurrentUdid(), getAppPath());
        }
    }

    /**
     * Clean cache (un-install) the app declared in appBundle param. Then
     * install the app whose path is stored in appPath param
     */
    public static void cleanCache() {
        uninstall(getCurrentUdid());
        install(getCurrentUdid(), getAppPath());
    }

    /**
     * Clean cache (un-install) the app declared in appBundle param. Then
     * install app, given name of parameter containing app path
     * 
     * @param appPathParamName
     *            Name of the parameter which contains the app path
     */
    public static void cleanCache(String appPathParamName) {
        uninstall(getCurrentUdid());
        install(getCurrentUdid(), getAppPath(appPathParamName));
    }

    /**
     * Install the app whose path is stored in appPath param without
     * un-installing the current app
     */
    public static void install() {
        install(getCurrentUdid(), getAppPath());
    }

    /**
     * Install the app without un-installing the current app, given name of
     * parameter containing app path
     * 
     * @param appPathParamName
     *            Name of the parameter which contains the app path
     */
    public static void install(String appPathParamName) {
        install(getCurrentUdid(), getAppPath(appPathParamName));
    }

    public static void init(String ip) {
        NuRemoteClient.init(ip);
    }

    public static void shutdown() {
        NuRemoteClient.close();
        //Gui.stopApp();
        logger.info("Closing down. BYE!");
    }

    public static void printView() {
        NuRemoteClient.sendToServer("(((UIApplication sharedApplication) keyWindow) recursiveDescription)");
        String views = NuRemoteClient.getLastMessage();
        views = views.replaceAll(">>    \\|", ">>\n");
        views = views.replaceAll("    \\|", "  ");
        System.out.println("LOG [Gui.printView]: views: \n" + views);
    }
    
    public static int countViewWithName(String viewName)
    {
        NuRemoteClient.sendToServer("(((UIApplication sharedApplication) keyWindow) recursiveDescription)");
        String views = NuRemoteClient.getLastMessage();
        views = views.replaceAll(">>    \\|", ">>\n");
        views = views.replaceAll("    \\|", "  ");
        
        return StringUtils.countMatches(views, viewName + ":");
    }
    
	public static void printViewWithViewName(String viewName) {
		String message = "(Gui printViewWithViewName: @\"%viewName\")";
		message = message.replace("%viewName", viewName);
		NuRemoteClient.sendToServer(message);
		String views = NuRemoteClient.getLastMessage();

		views = views.replaceAll(">>    \\|", ">>\n");
		views = views.replaceAll("    \\|", "  ");
		System.out
				.println("LOG [Gui.printViewWithViewName]: views: \n" + views);
	}

	public static void selectPickerForCurrentUITextViewWithTitle(String title) {
		String message = "(Gui selectPickerForCurrentUITextViewWithTitle: @\"%title\")";
		message = message.replace("%title", title);
		NuRemoteClient.sendToServer(message);
	}
	
	public static void sync() {
		String message = "(((((((UIApplication sharedApplication) keyWindow) rootViewController) viewControllers) lastObject) syncController) userDidTriggerSyncingWithSimulatedShine)";
		NuRemoteClient.sendToServer(message);
	}
    	 
	public static void touchAVIew(String viewName) {
		String message = "(Gui touchAView:  %viewName)";
		message = message.replace("%viewName", viewName);
		NuRemoteClient.sendToServer(message);
	}

    /**
     * (Gui touchAViewWithViewName: @"UIButtonLabel" andTitle: @"SIGN UP") (Gui
     * touchAViewWithViewName: @"UITextField" andTitle: @"Email")
     * 
     * @param type
     * @param name
     */
    public static void touchAVIew(String type, String text) {
        String message = "(Gui touchAViewWithViewName: @\"%type\" andTitle: @\"%text\")";
        message = message.replace("%type", type);
        message = message.replace("%text", text);

        NuRemoteClient.sendToServer(message);
    }

    public static void touchAVIew(String type, int index) {
        String message = "(Gui touchAViewWithViewName: @\"%type\" andIndex: %index)";
        message = message.replace("%type", type);
        message = message.replace("%index", String.valueOf(index));

        NuRemoteClient.sendToServer(message);
    }
    
    /**
     * Tap Next
     * This method is used in Log In screen or Sign Up screens
     */
	public static void tapNext() {
		String message = "(Gui touchAView: ";
		message += ViewUtils.generateFindViewStatement("UIButton", 1, ViewUtils
				.generateFindViewStatement("UIView", 0, ViewUtils
						.generateFindViewStatement("PTTopNavigationView", 0)));
		message += ")";
		NuRemoteClient.sendToServer(message);
	}
	
	/**
	 * Tap Back
	 * This method is used in Log In screen or Sign Up screens
	 */
	public static void tapPrevious() {
		String message = "(Gui touchAView: ";
		message += ViewUtils.generateFindViewStatement("UIButton", 0, ViewUtils
				.generateFindViewStatement("UIView", 0, ViewUtils
						.generateFindViewStatement("PTTopNavigationView", 0)));
		message += ")";		NuRemoteClient.sendToServer(message);
	}

    public static void touchButton(int index) {
        touchAVIew("UIButton", index);
    }

    public static void touchButton(String text) {
        touchAVIew("UIButton", text);
    }

    public static void longTouchAView(String type, String text, long duration) {
        String message = "(Gui longTouchAViewWithViewName: @\"%type\" andTitle: @\"%text\" inDuration: %duration)";
        message = message.replace("%type", type);
        message = message.replace("%text", text);
        message = message.replace("%duration", String.valueOf(duration));
        NuRemoteClient.sendToServer(message);
    }
    
    public static void longTouchAView(String type, int index, long duration) {
        String message = "(Gui longTouchAViewWithViewName: @\"%type\" andIndex: %index inDuration: %duration)";
        message = message.replace("%type", type);
        message = message.replace("%index", String.valueOf(index));
        message = message.replace("%duration", String.valueOf(duration));
        NuRemoteClient.sendToServer(message);
    }
    
    public static void longTouch(int x, int y, long duration) {
        String message = "(Gui longTouchWithX: %x andY:%y inDuration: %duration)";
        message = message.replace("%x", String.valueOf(x));
        message = message.replace("%y", String.valueOf(y));
        message = message.replace("%duration", String.valueOf(duration));
        NuRemoteClient.sendToServer(message);
    }

    // ----- keyboard -----
    public static void moveCursorInCurrentTextViewTo(int index)
    {
    	String message = "(Gui moveCursorInCurrentTextViewTo: %index)";
    	message = message.replace("%index", String.valueOf(index));
    	NuRemoteClient.sendToServer(message);
    }
    
    public static void setDefaultKeyboard() {
        NuRemoteClient.sendToServer("(Gui setDefaultKeyboard)");
    }

    public static void type(String text) {
        String message = "(Gui typeString: @\"%text\")";
        message = message.replace("%text", text);
        NuRemoteClient.sendToServer(message);
    }
    
    public static void enterTextForView(int index, String text) {
        String message = "(Gui enterTextForViewAtIndex: %index withText: @\"%text\")";
        message = message.replace("%index", String.valueOf(index));
        message = message.replace("%text", text);
        NuRemoteClient.sendToServer(message);
    }
    
    
    /**
     * Set the text for a text field at a certain index.
     * 
     * @param index
     */
    public static void setText(int index, String text) {
    	String message = "(Gui enterTextForViewAtIndex %index withText:  @\"%text\")";
        message = message.replace("%index", String.valueOf(index));
        message = message.replace("%text", text);
        NuRemoteClient.sendToServer(message);
    }

    public static void setText(String viewObject, String text) {
        String message = "(Gui enterTextForView: %textView withText: @\"%text\")";
        message = message.replace("%textView", viewObject);
        message = message.replace("%text", text);
        NuRemoteClient.sendToServer(message);
    }
    
    public static void setText(String viewName, int index, String text) {
        String message = "(Gui enterTextForView: %textView withText: @\"%text\")";
        message = message.replace("%textView", ViewUtils.generateFindViewStatement(viewName, index));
        message = message.replace("%text", text);
        NuRemoteClient.sendToServer(message);
    }

    public static void pressNext() {
        NuRemoteClient.sendToServer("(Gui pressNext)");
    }

    public static void pressDone() {
        NuRemoteClient.sendToServer("(Gui pressDone)");
    }

    public static void pressReturn() {
        NuRemoteClient.sendToServer("(Gui pressReturn)");
    }

    public static void pressDelete() {
        NuRemoteClient.sendToServer("(Gui pressDelete)");
    }

    /**
     * Picker must be visible
     */
    public static void setPicker(int index, String value) {
        String message = "(Gui selectPickerRowWithTitle: @\"%value\" inColumn: %index)";
        message = message.replace("%value", value);
        message = message.replace("%index", String.valueOf(index));
        NuRemoteClient.sendToServer(message);
    }

    public static void dismissPicker() {
        NuRemoteClient.sendToServer("(Gui dismissPicker)");
    }

    public static void touch(int x, int y) {
        String message = "(Gui touchAtX: %x andY: %y)";
        message = message.replaceAll("%x", String.valueOf(x));
        message = message.replaceAll("%y", String.valueOf(y));
        NuRemoteClient.sendToServer(message);
    }

    public static void touch(int x, int y, long time) {
        String message = "(Gui longTouchWithX: %x andY: %y inDuration: %time)";
        message = message.replaceAll("%x", String.valueOf(x));
        message = message.replaceAll("%y", String.valueOf(y));
        message = message.replaceAll("%time", String.valueOf(time));
        NuRemoteClient.sendToServer(message);
    }

    public static void swipe(int x1, int y1, int x2, int y2)
    {
    	String message = String.format("(Gui dragFromX: %d fromY: %d toX: %d andToY: %d)", x1, y1, x2, y2);
		NuRemoteClient.sendToServer(message);
    }
    
    public static void swipeUp(int distance) {
        swipe(0, distance);
    }

    public static void swipeDown(int distance) {
        swipe(1, distance);
    }

    public static void swipeLeft(int distance) {
        swipe(2, distance);
    }

    public static void swipeRight(int distance) {
        swipe(3, distance);
    }
    
	public static void swipeRightToSetGoal() {
		String message = "(Gui doActionForSwipeGestureIndex: 1 viewName: @\"UIView\" viewIndex: 4 controller: @\"PTSetYourGoalViewController\" action: @\"didSwipe:\" direction: 1 state: 3)";
		NuRemoteClient.sendToServer(message);
	}
	
	public static void swipeLeftToSetGoal() {
		String message = "(Gui doActionForSwipeGestureIndex: 0 viewName: @\"UIView\" viewIndex: 4 controller: @\"PTSetYourGoalViewController\" action: @\"didSwipe:\" direction: 2 state: 3)";
		NuRemoteClient.sendToServer(message);
	}
    
    /**
     * 0: up 1: down 2: left 3: right
     */
    public static void swipe(int direction, int distance) {
        String message = "";
        switch (direction) {
        case 0:
            message = "(Gui swipeUpWithPixels: %distance)";
            break;
        case 1:
            message = "(Gui swipeDownWithPixels: %distance)";
            break;
        case 2:
            message = "(Gui swipeLeftWithPixels: %distance)";
            break;
        case 3:
            message = "(Gui swipeRightWithPixels: %distance)";
            break;
        }
        message = message.replace("%distance", String.valueOf(distance));
        System.out.println("LOG [Gui.swipe]: " + message);
        NuRemoteClient.sendToServer(message);
    }

    /**
     * 
     * @param index
     *            : the index of the scroll view if there are more
     */
    public static void scroll(int index, int x, int y) {
        String message = "(Gui scrollAtScrollViewIndex: %index withScrollX: %x andScrollY: %y)";
        message = message.replace("%index", String.valueOf(index));
        message = message.replace("%x", String.valueOf(x));
        message = message.replace("%y", String.valueOf(y));
        NuRemoteClient.sendToServer(message);
    }

    public static void scrollUp(int index) {
        String message = "(Gui scrollUpAtScrollViewIndex: %index)";
        message = message.replace("%index", String.valueOf(index));
        NuRemoteClient.sendToServer(message);
    }

    public static void scrollDown(int index) {
        String message = "(Gui scrollDownAtScrollViewIndex: %index)";
        message = message.replace("%index", String.valueOf(index));
        NuRemoteClient.sendToServer(message);
    }

    public static void scrollLeft(int index) {
        String message = "(Gui scrollLeftAtScrollViewIndex: %index)";
        message = message.replace("%index", String.valueOf(index));
        NuRemoteClient.sendToServer(message);
    }

    public static void scrollRight(int index) {
        String message = "(Gui scrollRightAtScrollViewIndex: %index)";
        message = message.replace("%index", String.valueOf(index));
        NuRemoteClient.sendToServer(message);
    }

    public static void drag(String viewObject, int x, int y) {
        String message = "(Gui dragAView: %view toX: %x andY: %y)";
        message = message.replace("%view", viewObject);
        message = message.replace("%x", String.valueOf(x));
        message = message.replace("%y", String.valueOf(y));
        NuRemoteClient.sendToServer(message);
    }

    public static void drag(int x1, int y1, int x2, int y2) {
        String message = "(Gui dragFromX: %x1 fromY: %y1 toX: %x2 andToY: %y2)";
        message = message.replace("%x1", String.valueOf(x1));
        message = message.replace("%y1", String.valueOf(y1));
        message = message.replace("%x2", String.valueOf(x2));
        message = message.replace("%y2", String.valueOf(y2));
        NuRemoteClient.sendToServer(message);
    }
    
    public static void drag(String viewType, int index, int dx, int dy) {
        String message = "(Gui dragAViewWithViewName: @\"%viewType\" andIndex: %index withXPixels: %dx andYPixels: %dy)";
        message = message.replace("%viewType", viewType);
        message = message.replace("%index", String.valueOf(index));
        message = message.replace("%dx", String.valueOf(dx));
        message = message.replace("%dy", String.valueOf(dy));
        NuRemoteClient.sendToServer(message);
    }
    
    public static void drag(String viewType, String title, int dx, int dy) {
        String message = "(Gui dragAViewWithViewName: @\"%viewType\" andTitle: @\"%title\" withXPixels: %dx andYPixels: %dy)";
        message = message.replace("%viewType", viewType);
        message = message.replace("%title", title);
        message = message.replace("%dx", String.valueOf(dx));
        message = message.replace("%dy", String.valueOf(dy));
        NuRemoteClient.sendToServer(message);
    }
    
    /* timeline */
	public static void dragUpTimeline() {
		String message = "((ViewUtils findViewWithViewName:@\"PTTimelineHorizontalScrollView\" andIndex: 0) animateViewToTop)";
		NuRemoteClient.sendToServer(message);
	}

	public static void dragDownTimeline() {
		String message = "((ViewUtils findViewWithViewName:@\"PTTimelineHorizontalScrollView\" andIndex: 0) animateViewToBottom)";
		NuRemoteClient.sendToServer(message);
	}
    
    
    // ---- spinner ----
    public static void setSpinnerValue(String spinnerViewName, int index, int value)
    {
    	String message = String.format(
    					"(set spinner (ViewUtils findViewWithViewName: @\"%s\" andIndex: %d))" +
    					"(spinner startAnimatingTo: %d in: 1)", spinnerViewName, index, value);
    	
    	NuRemoteClient.sendToServer(message);
    }
    
    public static int getSpinnerValue(String spinnerViewName, int index)
    {
    	String message = String.format(
    			"(Gui getValueWithViewName: @\"%s\" withPropertyName: @\"endValue\" andIndex: %d)", spinnerViewName, index);
    	NuRemoteClient.sendToServer(message);
        String value = NuRemoteClient.getLastMessage();
        
        return Integer.parseInt(value);
    }
    
    // ---- pop up ----
    public static void touchPopupButton(int index) {
        String message = "(Gui touchUIButtonInPopUpWithIndex: %index)";
        message = message.replace("%index", String.valueOf(index));
        NuRemoteClient.sendToServer(message);

    }
    
    public static void touchPopupButton(String text) {
        String message = "(Gui touchUIButtonInPopUpWithTitle: @\"%text\")";
        message = message.replace("%text", String.valueOf(text));
        NuRemoteClient.sendToServer(message);

    }

    
    public static void touchPopupDefaultButton() {
        String message = "(Gui touchDefaultUIButtonInPopUp)";
        NuRemoteClient.sendToServer(message);

    }

    public static String getPopupTitle() {
        NuRemoteClient.sendToServer("(Gui getTitlePopUp)");
        return NuRemoteClient.getLastMessage();
    }

    public static String getPopupContent() {
        NuRemoteClient.sendToServer("(Gui getContentPopUp)");
        return NuRemoteClient.getLastMessage();
    }

    
    // --- action sheet ---
    public static void touchActionSheetButton(int index) {
        String message = "(Gui touchUIButtonInActionSheetWithINdex: %index)";
        message = message.replace("%index", String.valueOf(index));
        NuRemoteClient.sendToServer(message);
    }

    public static void touchActionSheetButton(String title) {
        String message = "(Gui touchUIButtonInActionSheetWithINdex: @\"%title\")";
        message = message.replace("%title", title);
        NuRemoteClient.sendToServer(message);
    }
    
    public static void touchDestructiveActionSheetButton(String title) {
        String message = "(Gui touchDestructiveButtonInActionSheet)";
        NuRemoteClient.sendToServer(message);
    }
    
    
    // ---- tabbar ----
    public static void touchTabbar(int index) {
        String message = "(Gui touchATabBarButtonWithIndex: %index)";
        message.replace("%index", String.valueOf(index));
        NuRemoteClient.sendToServer(message);
    }

    
    // ---- navigation bar ----
    public static void touchNavigationButton(int index) {
        String message = "(Gui touchAUIButtonInNavigationBarAtIndex: %index)";
        message.replace("%index", String.valueOf(index));
        NuRemoteClient.sendToServer(message);
    }

    public static void touchNavigationButton(String title) {
        String message = "(Gui touchAUIButtonInNavigationBarWithTitle: @\"%title\")";
        message.replace("%title", title);
        NuRemoteClient.sendToServer(message);
    }

    
    // ----- screen size -----
    public static int getScreenWidth() {
        NuRemoteClient.sendToServer("(Gui screenWidth)");
        return Integer.parseInt(NuRemoteClient.getLastMessage());
    }

    public static int getScreenHeight() {
        NuRemoteClient.sendToServer("(Gui screenHeight)");
        return Integer.parseInt(NuRemoteClient.getLastMessage());
    }
    
    // ---- get value ----
    /**
     * Get value of a certain property on a view.
     * 
     * @param viewObject
     *            the view object found using ViewUtils
     * @param property
     *            what property to find
     * @return the value of the property.
     */
    public static String getProperty(String viewObject, String property) {
        String message = "(Gui getValueWithPropertyName: @\"%property\" inView: %view)";
        message = message.replace("%property", property);
        message = message.replace("%view", viewObject);
        NuRemoteClient.sendToServer(message);
        return NuRemoteClient.getLastMessage();
    }
    
    public static String getProperty(String viewType, int index, String property) {
        String message = "(Gui getValueWithViewName: @\"%viewType\" withPropertyName: @\"%property\" andIndex: %index)";
        message = message.replace("%viewType", viewType);
        message = message.replace("%property", property);
        message = message.replace("%index", String.valueOf(index));
        NuRemoteClient.sendToServer(message);
        return NuRemoteClient.getLastMessage();
    }
    
    public static String getProperty(String viewType, String title, String property) {
        String message = "(Gui getValueWithViewName: @\"%viewType\" withPropertyName: @\"%property\" andTitle: @\"%title\")";
        message = message.replace("%viewType", viewType);
        message = message.replace("%property", property);
        message = message.replace("%title", title);
        NuRemoteClient.sendToServer(message);
        return NuRemoteClient.getLastMessage();
    }
    
    
    /**
     * (Gui getTextOfViewName: @"UILabel" andIndex: 0)
     * 
     * @param type
     * @param index
     * @return
     */
    public static String getText(String type, int index) {
    	String message = "(Gui getTextOfViewName: @\"%type\" andIndex: %index)";
        message = message.replace("%type", type);
        message = message.replace("%index", String.valueOf(index));
        NuRemoteClient.sendToServer(message);
        return NuRemoteClient.getLastMessage();
    }
    
    // ---- bluetooth and location service functions ---------
    public static boolean toggleFlightMode() {
        NuRemoteClient.sendToServer("(Gui toggleFlightMode)");
        flightMode = !flightMode;
        return flightMode;
    }
    
    public static boolean isFlightModeOn() {
        return flightMode;
    }

    public static boolean toggleBluetooth() {
        NuRemoteClient.sendToServer("(Gui toggleBluetooth)");
        bluetooth = !bluetooth;
        return bluetooth;
    }
    
    public static boolean isBluetoothOn() {
        return bluetooth;
    }
    
    public static boolean toggleLocationService() {
        NuRemoteClient.sendToServer("(Gui toggleLocationService)");
        locationService = !locationService;
        return locationService;
    }
    
    public static boolean isLocationServiceOn() {
        return locationService;
    }

    public static void tapToSync() {
    	String message = "(Gui touchAViewWithViewName: @\"PTSyncTrayView\" andIndex: 0)";
    	NuRemoteClient.sendToServer(message);
    }
    
    public static boolean hasAlert() {
    	String message = "(ViewUtils findAlertView)";
    	NuRemoteClient.sendToServer(message);
    	String result = NuRemoteClient.getLastMessage();
    	return !StringUtils.isEmpty(result);
    }

    public static void start(String ip) 
    {
    	// DEPRECATED
    	// USE APPHELPER.CLEANCACHE
    	// APPHELPER.LAUCHINSTRUMENT
    	// GUI.INIT INSTEAD
    	
        logger.info("Kill the app");
        stopApp();
        
        logger.info("Starting app again");
        int noOfTries = 0;
        boolean connected = false;
        while (!connected && noOfTries < 1) 
        {
        	logger.info("Startup attempt: " + noOfTries + 1);
        	
        	// lauch instrument
        	logger.info("   - Launching instrucments...");
    		AppHelper.launchInstrument(AppHelper.getCurrentUdid(),
    				AppHelper.getAppPath(), "script/automation/alertsupport.js");
        	
//			(new Thread() 
//			{
//				public void run() 
//				{
//		        	String script = new File("script/automation/alertsupport.js").getAbsolutePath();
//		        	if(!new File(script).exists())
//		        	{
//		        		logger.info("Cannot find: " + script);
//		        		return;
//		        	}
//		        	
//		        	ProcessBuilder pb = new ProcessBuilder();
//		            String command = "instruments -w " + Gui.getCurrentUdid() 
//		            		+ " -t " + template + " " + Gui.getAppPath() 
//		            		+ " -e UIASCRIPT " + script
//		                    + " -e UIARESULTSPATH logs";
//		            logger.info("Command: " + command);
//
//		            pb.command("instruments", "-w", Gui.getCurrentUdid(), "-t", template, Gui.getAppPath(), "-e", "UIASCRIPT", script, "-e",
//		                    "UIARESULTSPATH", "logs");
//		        
//		            AppHelper.runProcess(pb, true, true);
//					//AppHelper.launchInstrument(Gui.getCurrentUdid(), Gui.getAppPath(), script);
//				}
//			}).start();

        	
        	
			// OLD VERSION OF LAUNCHING INSTRUMENTS
//        	// get absolute path of script file
//        	String script = new File("script/automation/alertsupport.js").getAbsolutePath();
//        	if(!new File(script).exists())
//        	{
//        		logger.info("Cannot find: " + script);
//        		return;
//        	}
        	
//            String command = "instruments -w " + Gui.getCurrentUdid() + " -t " + template + " " + Gui.getAppPath()
//                    + " -e UIARESULTSPATH logs -e UIASCRIPT " + script;
//
//            logger.info("   - Launching instrucments...");
//            logger.info("   - Command: " + command);
//            try
//            {
//                Runtime.getRuntime().exec(command);
//            } 
//            catch (IOException e)
//            {
//            	logger.error(e);
//                e.printStackTrace();
//            }

            logger.info("   - Init NuRemoteClinet... ");
            ShortcutsTyper.delayTime(25000);
            connected = NuRemoteClient.init(ip);
            noOfTries++;
        }
    }

    public static void stopApp() {
    	logger.info("Killing the app...");
        String deviceId = Gui.getCurrentUdid();
        ProcessFinder.kill(deviceId);
        //Processes.killHard(deviceId);
    }
    
    public static void captureScreen(String name) {
        File file = NuRemoteClient.captureScreen(name);
        
        if(file != null) {
        	TRS.instance().addShot(file.getAbsolutePath(), null);
        }
    }
    
    public static String printViewHierarchy() {
        NuRemoteClient.sendToServer("(Gui printView)");
        String views = NuRemoteClient.getLastMessage();
        System.out.println("LOG [Gui.printViewHierarchy]: view hierarchy: \n" + views);
        return views;
    }

    // timezone and localization
    public static void setTimezoneWithGMTOffset(int offset) {
    	String message = "(Gui setTimeZoneWithGMTOffset: %offset)";
        message = message.replace("%offset", String.valueOf(offset));
        NuRemoteClient.sendToServer(message);
    }
    
    public static boolean isRunningOnIOS7() {
    	String message = "(Gui isRunningOnIOS7)";
    	NuRemoteClient.sendToServer(message, true, false);
    	String result = NuRemoteClient.getLastMessage();
    	
    	if(result.isEmpty())
    		return false;
    	return "1".equals(result) ? true : false;
    }
    
}



