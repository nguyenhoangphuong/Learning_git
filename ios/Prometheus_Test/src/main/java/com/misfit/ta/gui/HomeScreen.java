package com.misfit.ta.gui;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class HomeScreen {
    public static void tapSettings() {
		Gui.touchAVIew("UIButtonLabel", "setting"); 
		//this button is temporary since we don't have real Shine and there's no way we get to setting screen through sync tray
    }

    public static void tapSyncTray() {
    	int num = Gui.countViewWithName("UIButton");
		Gui.touchAVIew("UIButton", num - 8);
		//TODO: change button. This one is invalid 
	}

    public static void tapOpenManualInput() {
        Gui.touchAVIew("UIButtonLabel", "Manual");
    }
    
    public static void tapToday() {
    	Gui.touchAVIew("UIButtonLabel", "Today");
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
            ShortcutsTyper.delayTime(2000);
        }
    }

    public static void goToNextDays(int days) {
        for (int i = 0; i < days; i++) {
            Gui.swipeLeft(500);
            ShortcutsTyper.delayTime(2000);
        }
    }

    public static boolean isToday() {
    	return ViewUtils.isExistedView("UILabel", "TODAY");
    }

    public static boolean isYesterday() {
    	return ViewUtils.isExistedView("UILabel", "YESTERDAY");
    }
    
    public static boolean isADayBefore(int days) {
    	Calendar now = Calendar.getInstance();
    	now.add(Calendar.DATE,-days);
    	StringBuilder title = new StringBuilder(PrometheusHelper.getDayOfWeek(now.get(Calendar.DAY_OF_WEEK), false));
    	title.append(", ");
    	title.append(PrometheusHelper.getMonthString(now.get(Calendar.MONTH) + 1, false));
    	title.append(" ");
    	int date = Integer.valueOf(now.get(Calendar.DATE));
    	if (date > 10) {
    		title.append(now.get(Calendar.DATE));
    	} else {
    		title.append("0").append(now.get(Calendar.DATE));
    	}
    	return ViewUtils.isExistedView("UILabel", title.toString());
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
        return !StringUtils.isEmpty(Gui.getProperty("UILabel", "Zip.Zero.Nada", "isHidden"));
    }

    public static void sync() {
        PrometheusHelper.sync();
    }
}
