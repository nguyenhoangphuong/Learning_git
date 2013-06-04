package com.misfit.ta.gui;

import java.util.Calendar;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class HomeScreen {
	
	/* Navigation */
    public static void tapOpenSyncTray() {
   	 Gui.touchAVIew("UIButton", 5);
	}
    
    public static void tapSettings() {
    	Gui.touchAVIew("UILabel", "Settings");    
    }
    
    public static void tapAdjustGoal() {
    	Gui.touchAVIew("UILabel", "My Goal");
    }

    public static void tapOpenManualInput() {
        Gui.touchAVIew("UIButtonLabel", "Manual");
    }
    
    
    /* Manual input */
    public static void tapToday() {
    	Gui.touchAVIew("UIButton", 2);
    }
    
    public static void tapRandom() {
        Gui.touchAVIew("UIButtonLabel", "Random");
    }

    public static void tapDormant() {
        Gui.touchAVIew("UIButtonLabel", "Dormant");
    }

    public static void tapGenerate1440Activities() {
    	Gui.touchAVIew("UIButtonLabel", "Generate 1440 activities");
    }
    
    public static void tapSave() {
        Gui.touchAVIew("UIButtonLabel", "Save");
    }
    
    public static void enterManualActivity(String[] times, int duration, int steps) 
    {
        PrometheusHelper.inputManualRecord(times, duration, steps);
    }


    /* Timeline */
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

    public static void dragUpTimeline() {
    	Gui.drag(240, 300, 240, 100);
    }
    
    public static void dragDownTimeline() {
    	Gui.drag(240, 200, 240, 400);
    }
    
    public static void sync() {
        PrometheusHelper.sync();
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
		ShortcutsTyper.delayTime(2000);
    }
    
    
    /* Visible checking */
    public static boolean isToday() {
    	return ViewUtils.isExistedView("UILabel", "Today");
    }

    public static boolean isYesterday() {
    	return ViewUtils.isExistedView("UILabel", "Yesterday");
    }
    
    public static boolean isADayBefore(int days) {
    	Calendar now = Calendar.getInstance();
    	now.add(Calendar.DATE,-days);
    	StringBuilder title = new StringBuilder(PrometheusHelper.getDayOfWeek(now.get(Calendar.DAY_OF_WEEK), false));
    	title.append(", ");
    	title.append(PrometheusHelper.getMonthString(now.get(Calendar.MONTH) + 1, false));
    	title.append(" ");
    	int date = Integer.valueOf(now.get(Calendar.DATE));
    	if (date >= 10) {
    		title.append(now.get(Calendar.DATE));
    	} else {
    		title.append("0").append(now.get(Calendar.DATE));
    	}
    	return ViewUtils.isExistedView("UILabel", title.toString());
    }

    public static boolean isThisWeek() {
    	return ViewUtils.isExistedView("UILabel", "This week");
    }
    
    public static boolean isLastWeek() {
    	return ViewUtils.isExistedView("UILabel", "Last week");
    }
    
    public static boolean isNoDataProgressCircle() {
    	String text = Gui.getProperty("PTRichTextLabel", 0, "text");
    	return text.equals("Zip. Zero. Nada.");
    }
    
    public static boolean isPointEarnedProgessCircle() {
        String text = Gui.getProperty("PTRichTextLabel", 0, "text");
        return text.matches("^of .* points$");
    }
    
    public static boolean isPointRemainProgessCircle() {
    	String text = Gui.getProperty("PTRichTextLabel", 0, "text");
    	return text.matches(".* points to go$");
    }
    
    public static boolean isSummaryProgressCircle() {
    	String text = Gui.getProperty("PTRichTextLabel", 0, "text");
    	return text.matches(".* steps$");
    }

}
