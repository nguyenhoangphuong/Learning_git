package com.misfit.ta.gui;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;

public class HomeSettings {
	
	/* Navigation */
	public static void tapBack() {
		Gui.touchAVIew("UIButton", "Back");
	}
	
	public static void tapCancel()	{
		Gui.touchAVIew("UIButton", "Cancel");
	}

    public static void tapYourProfile() {
        Gui.touchAVIew("UILabel", "My Profile");
    }

    public static void tapWearingShine() {
        Gui.touchAVIew("UILabel", "Wearing Position");
    }
    
    public static void tapFahrenheit() {
    	Gui.touchAVIew("UIButtonLabel", "°F");
    }
    
    public static void tapCelcius() {
    	Gui.touchAVIew("UIButtonLabel", "°C");
    }
    
    public static void tapMi() {
    	Gui.touchAVIew("UIButtonLabel", "mi");
    }
    
    public static void tapKm() {
    	Gui.touchAVIew("UIButtonLabel", "km");
    }
    
    public static void tapLbs() {
    	Gui.touchAVIew("UIButtonLabel", "lbs");
    }
    
    public static void tapKg() {
    	Gui.touchAVIew("UIButtonLabel", "kg");
    }
    
    public static void tapFAQ() {
    	Gui.touchAVIew("UILabel", "FAQ");
    }

    public static void tapLegalAndPrivacy() {
        Gui.touchAVIew("UILabel", "Legal & Privacy");
    }
    
    public static void tapSendFeedback() {
        Gui.touchAVIew("UILabel", "Send feedback");
    }
    
    public static void tapDebug() {
        Gui.touchAVIew("UILabel", "Debug");
    }
    
    public static void tapLink() {
        Gui.touchAVIew("UILabel", "Link");
    }
    
    public static void tapUnlink() {
        Gui.touchAVIew("UILabel", "Unlink");
    }
    
    
    public static void tapSignOut() {
        Gui.touchAVIew("UIButtonLabel", "SHINE OUT");
    }
    
    public static void chooseSignOut() {
    	 Gui.touchAVIew("UIButtonLabel", "Shine out");
    }
    
    
    /* Update profile */
    public static void updateGender(boolean isMale) {
        PrometheusHelper.enterGender(isMale);
    }

    public static void updateBirthDay(String year, String month, String day) {
        PrometheusHelper.enterBirthDay(year, month, day);
    }

    public static void updateHeight(String digit, String fraction, boolean isUSUnit) {
        PrometheusHelper.enterHeight(digit, fraction, isUSUnit);
    }

    public static void updateWeight(String digit, String fraction, boolean isUSUnit) {
        PrometheusHelper.enterWeight(digit, fraction, isUSUnit);
    }
    
    
    /* Set Daily Goal */
    public static void tapDoneAtNewGoal() {
    	Gui.touchAVIew("UIButton", "Save");
    }

    public static void setSpinnerGoal(int value) {
    	String message = "((((((UIApplication sharedApplication) keyWindow) rootViewController) viewControllers) lastObject) setSpinnerValue: " +value+ ")";
    	NuRemoteClient.sendToServer(message);
    }
    
    public static int getSpinnerGoal()
    {
    	return Gui.getSpinnerValue("PTNumberSpinner", 0);
    }
    
    public static void tapOKAtNewGoalPopup()
    {
    	Gui.touchPopupButton(0);
    }

    public static boolean hasDontForgetMessage()
    {
    	return Gui.getPopupContent().equals(DefaultStrings.NewGoalInstruction);
    }
    
    
    /* Wearing Shine */
    public static void setWearingShineAt(String location)
    {
    	if(location == DefaultStrings.WearingLocations[0])
    	{
    		// head
    	}
    	else if(location == DefaultStrings.WearingLocations[0])
    	{
    		// head
    	}
    	else if(location == DefaultStrings.WearingLocations[0])
    	{
    		// head
    	}
    	else if(location == DefaultStrings.WearingLocations[0])
    	{
    		// head
    	}
    	else if(location == DefaultStrings.WearingLocations[0])
    	{
    		// head
    	}
    	else if(location == DefaultStrings.WearingLocations[0])
    	{
    		// head
    	}
    	
    }
    
    public static void tapHead() {
    	Gui.touchAVIew("UIButton", 0);
    }
    
    public static void tapUpperArm() {
    	Gui.touchAVIew("UIButton", 1);
    }
    
    public static void tapChest() {
    	Gui.touchAVIew("UIButton", 2);
    }
    
    public static void tapWrist() {
    	Gui.touchAVIew("UIButton", 3);
    }
    
    public static void tapWaist() {
    	Gui.touchAVIew("UIButton", 4);
    }
    
    public static void tapFootAnkle() {
    	Gui.touchAVIew("UIButton", 5);
    }

    
    /* Mode Debug */
    public static void tapDoneAtDebug()
    {
    	Gui.touchAVIew("UIButtonLabel", "Done");
    }  
    
    
    /* Visible Check */
    public static boolean isAtSettings()
    {
    	return ViewUtils.isExistedView("UILabel", "Settings");
    }

    public static boolean isAtEditProfile()
    {
    	return ViewUtils.isExistedView("UILabel", "Edit Profile");
    }
    
    public static boolean isAtEditGoal()
    {
    	return ViewUtils.isExistedView("UILabel", "My Goal");
    }
    
    public static boolean isAtWearingShine()
    {
    	return ViewUtils.isExistedView("UILabel", "Where to wear");
    }
    
}
