package com.misfit.ta.gui;

import org.apache.commons.lang.StringUtils;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class HomeScreen {
    public static void tapSettings() {
		int num = Gui.countViewWithName("UIButton");
		Gui.touchAVIew("UIButton", num - 6);
    }

    public static void tapSyncTray() {
    	int num = Gui.countViewWithName("UIButton");
		Gui.touchAVIew("UIButton", num - 8);
    }

    public static void tapOpenManualInput() {
        Gui.touchAVIew("UIButton", "Manual");
    }
    
    public static void tapRandom() {
        Gui.touchAVIew("UIButtonLabel", "Random");
    }

    public static void tapDormant() {
        Gui.touchAVIew("UIButtonLabel", "Dormant");
    }

    public static void enterManualActivity(String[] times, int duration, int steps) 
    {
        PrometheusHelper.inputManualRecord(times, duration, steps);
    }

    public static void tapSave() {
        Gui.touchAVIew("UIButtonLabel", "Save");
    }

    public static void goToPreviousDays(int days) {
        for (int i = 0; i < days; i++) {
            Gui.swipeRight(500);
        }
    }

    public static void goToNextDays(int days) {
        for (int i = 0; i < days; i++) {
            Gui.swipeLeft(500);
        }
    }

    public static boolean isToday() {
        //return "TODAY".equals(Gui.getProperty("UILabel", 0, "text"));
    	return ViewUtils.isExistedView("UILabel", "TODAY");
    }

    public static boolean isYesterday() {
        //return "YESTERDAY".equals(Gui.getProperty("UILabel", 0, "text"));
    	return ViewUtils.isExistedView("UILabel", "YESTERDAY");
    }

    public static void closeIntructions()
    {
		Gui.swipeLeft(500);
		ShortcutsTyper.delayTime(1000);
		Gui.swipeLeft(500);
		ShortcutsTyper.delayTime(1000);
		Gui.swipeLeft(500);
		ShortcutsTyper.delayTime(1000);
		Gui.touchAVIew("UIButtonLabel", "OK, I GOT IT");
		ShortcutsTyper.delayTime(1000);
    }
    
    /**
     * Open/Close to tag an activity on timeline
     * 
     * @param n
     */
    public static void tapTagging(int n) {
        Gui.touchAVIew("PTTimelineActivityTaggingView", n);
    }

    public static boolean viewPoints() {
        return !StringUtils.isEmpty(Gui.getProperty(ViewUtils.findView("UILabel", "POINTS"), "isHidden"));
    }

    public static boolean viewSteps() {
        return !StringUtils.isEmpty(Gui.getProperty(ViewUtils.findView("UILabel", "STEPS"), "isHidden"));
    }

    public static boolean viewCalories() {
        return !StringUtils.isEmpty(Gui.getProperty(ViewUtils.findView("UILabel", "CALORIES"), "isHidden"));
    }

    public static boolean viewNoDataYet() {
        return !StringUtils.isEmpty(Gui.getProperty("UILabel", "NO DATA YET!", "isHidden"));
    }

    public static boolean syncTrayIsClosed()
    {
    	return !ViewUtils.isExistedView("UILabel", "DETECTING SHINE");
    }
    
    public static void sync() {
        PrometheusHelper.sync();
    }
}
