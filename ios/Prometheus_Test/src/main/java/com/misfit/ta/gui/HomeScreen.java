package com.misfit.ta.gui;

import org.apache.commons.lang.StringUtils;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class HomeScreen {
    public static void tapSettings() {
        Gui.touchAVIew("UIButton", 6);
    }

    public static void tapSyncTray() {
        Gui.touchAVIew("UIButton", 5);
    }

    public static void tapOpenManualInput() {
        Gui.touchAVIew("UIButton", 1);
    }

    public static void tapRandom() {
        Gui.touchAVIew("UIButtonLabel", "Random");
    }

    public static void tapDormant() {
        Gui.touchAVIew("UIButtonLabel", "Dormant");
    }

    public static void enterManualActivity(String time, int duration, int steps) {
        // TODO: enter time

        // enter duration
        Gui.touchAVIew("UITextField", 2);
        Gui.type(String.valueOf(duration));
        ShortcutsTyper.delayTime(500);

        // enter steps
        Gui.touchAVIew("UITextField", 3);
        Gui.type(String.valueOf(steps));
        ShortcutsTyper.delayTime(500);
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
        return "TODAY".equals(Gui.getProperty("UILabel", 1, "text"));
    }

    public static boolean isYesterday() {
        return "YESTERDAY".equals(Gui.getProperty("UILabel", 1, "text"));
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
        return !StringUtils.isEmpty(Gui.getProperty(ViewUtils.findView("UILabel", "NO DATA YET!"), "isHidden"));
    }

    public static void sync() {
        PrometheusHelper.sync();
    }
}
