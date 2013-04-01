package com.misfit.ta.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.Processes;
import com.misfit.ta.Settings;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ProcessFinder;
import com.misfit.ta.utils.ShortcutsTyper;

public class Gui {

    private static String fruitStrapPath = "";
    private static String template = "";
    private static Logger logger = Util.setupLogger(Gui.class);

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

    public static void init() {
        NuRemoteClient.init();
    }

    public static void shutdown() {
        NuRemoteClient.close();
        logger.info("Closing down. BYE!");
    }

    public static void printView() {
        NuRemoteClient.sendToServer("(((UIApplication sharedApplication) keyWindow) recursiveDescription)");
        String views = NuRemoteClient.getLastMessage();
        views = views.replaceAll(">>    \\|", ">>\n");
        views = views.replaceAll("    \\|", "  ");
        System.out.println("LOG [Gui.printView]: views: \n" + views);
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

    public static void touchButton(int index) {
        touchAVIew("UIButton", index);
    }

    public static void touchButton(String text) {
        touchAVIew("UIButton", text);
    }

    // TODO
    public static void longTouchAView(String type, String text) {
        String message = "(Gui longTouchAViewWithViewName: @\"%type\" andTitle: @\"%text\")";
        message = message.replace("%type", type);
        message = message.replace("%text", text);
        System.out.println("LOG [Gui.touchAVIew]: " + message);

        NuRemoteClient.sendToServer(message);
    }

    // ----- keyboard -----
    public static void setDefaultKeyboard() {
        NuRemoteClient.sendToServer("(Gui setDefaultKeyboard)");
    }

    public static void type(String text) {
        setDefaultKeyboard();
        String message = "(Gui typeString: @\"%text\")";
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
        String message = "(Gui dragFromX: %x1 fromY: %y1 toX: %x2 andToY: %y2";
        message = message.replace("%x1", String.valueOf(x1));
        message = message.replace("%y1", String.valueOf(y1));
        message = message.replace("%x2", String.valueOf(x2));
        message = message.replace("%y2", String.valueOf(y2));
        NuRemoteClient.sendToServer(message);
    }

    // ---- pop up ----
    public static void touchPopupButton(int index) {
        String message = "(Gui touchUIButtonInPopUpWithIndex: %index)";
        message = message.replace("%index", String.valueOf(index));
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
        String message = "(Gui touchAUIButtonInNavigationBarWithTitle: @\"%title\")";
        message = message.replace("%title", title);
        NuRemoteClient.sendToServer(message);
    }

    // ---- text of control ----
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

    public static void start() {
        logger.info("Will start the app");
        // String target = Settings.getTarget();
        // String appPath = Settings.getAppPath();

        stopApp();
        int noOfTries = 0;
        boolean connected = false;
        while (!connected && noOfTries < 1) {
            logger.info("Startup attempt: " + noOfTries + 1);
            // if (target.contains("Sim")) {
            // String clientLog = "logs/client.log";
            // String family = target.contains("iPhone") ? "iphone" : "ipad";
            // String[] cmd = {"/usr/local/bin/ios-sim", "launch", appPath,
            // "--family", family, "--stderr", clientLog, "--exit"};
            // runCmd(cmd);
            // ShortcutsTyper.delayTime(2000);
            // } else {
            // System.out.println("LOG [Gui.start]: current uuid=" +
            // Gui.getCurrentUdid());
            // System.out.println("LOG [Gui.start]: " + Gui.getAppPath());
            // String[] cmd = { "instruments", "-w", Gui.getCurrentUdid(), "-t",
            // template, Gui.getAppPath() };
            // Processes.runCmd(cmd);

            // }

            // ----

            ProcessBuilder pb = new ProcessBuilder();
            String command = "instruments -w " + Gui.getCurrentUdid() + " -t " + template + " " + Gui.getAppPath()
                    + " -e UIARESULTSPATH logs";
            logger.info("Command: " + command);
            System.out.println("LOG [AppHelper.launchInstrument]: command= \n" + command);
            pb.command("instruments", "-w", Gui.getCurrentUdid(), "-t", template, Gui.getAppPath(), "-e",
                    "UIARESULTSPATH", "logs");
            String result = runProcess(pb, true, true);
            ShortcutsTyper.delayTime(10000);
            // ---

            connected = NuRemoteClient.init();
            noOfTries++;
        }
    }

    public static void stopApp() {
        String deviceId = Gui.getCurrentUdid();
        ProcessFinder.kill(deviceId);
        Processes.killHard(deviceId);
    }
    
    public static void captureScreen(String name) {
        NuRemoteClient.captureScreen();
    }

    public static void main(String[] args) throws IOException {
        
        Gui.init();
        Gui.captureScreen("dad");
        Gui.shutdown();
        
        // Gui.uninstall(getCurrentUdid());
        // Gui.install();
//        Gui.start();

        // Gui.getUdids();
        // System.out.println("LOG [Gui.main]: " + getCurrentUdid());

        // Gui.init();

        // touchButton("LOG IN WITH FACEBOOK");
        // Gui.touchAVIew("UIButtonLabel", "SIGN UP" );
        // printView();
        // setDefaultKeyboard();
        // touch(200, 300, 2000);
        // System.out.println("LOG [Gui.main]: " + getPopupTitle());
        // System.out.println("LOG [Gui.main]: " + getPopupContent());

        // swipeUp(300);
        // ShortcutsTyper.delayTime(500);
        // pressNext();
        // ShortcutsTyper.delayTime(500);
        // type("Something else");
        // ShortcutsTyper.delayTime(500);
        // pressDone();
        // ViewUtils.findView("UITextField", 0);
        // NuRemoteClient.sendToServer("(set a (ViewUtils findViewWithViewName: @\"UITextField\" andIndex: 0))");
        // NuRemoteClient.sendToServer("(Gui enterTextForView: a withText: @\"something\")");
        // setText("a", "abcdef");
        //
        // String name = ViewUtils.findView("UILabel", 0);
        // System.out.println("LOG [Gui.main]: " + getProperty(name, "text"));

        // getProperty(viewObject, property)
        // System.out.println("LOG [Gui.main]: " + name);
        // setText(name, "something");
        // /type("some email@address.com");
        // pressNext();
        // pressDone();
        // setPicker(0, "February");
        // dismissPicker();

        // System.out.println("LOG [Gui.main]: " + getText("UIButton", 0));
        // Gui.shutdown();
    }

}
