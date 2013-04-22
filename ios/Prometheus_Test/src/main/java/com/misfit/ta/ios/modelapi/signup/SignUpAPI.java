package com.misfit.ta.ios.modelapi.signup;

import java.io.File;
import java.util.Date;
import java.util.Random;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.SignUp;

public class SignUpAPI extends ModelAPI {
    public SignUpAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
        super(automation, model, efsm, generator, weight);
    }

    private static String year;
    private static String month;
    private static String day;
    private static String hFraction;
    private static String hDigit;
    private static String wDigit;
    private static String wFraction;
    private static boolean isMale;
    private static boolean isUSMetric;
    private static String[] months = { "January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December" };
    private static int goal = 0;

    /**
     * This method implements the Edge 'e_Back'
     * 
     */
    public void e_Back() {
        SignUp.tapPrevious();
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Edge 'e_ChooseSignUp'
     * 
     */
    public void e_ChooseSignUp() {
        SignUp.tapSignUp();
        ShortcutsTyper.delayTime(5000);
    }

    /**
     * This method implements the Edge 'e_Init'
     * 
     */
    public void e_Init() {
        LaunchScreen.launch();
    }

    /**
     * This method implements the Edge 'e_Next'
     * 
     */
    public void e_Next() {
        SignUp.tapNext();
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Edge 'e_SetGoal'
     * 
     */
    public void e_SetGoal() {
        goal = new Random().nextInt(3);
        SignUp.setGoal(goal);
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_SignOut'
     * 
     */
    public void e_SignOut() {
        HomeScreen.tapSettings();
        Gui.swipeUp(1000);
        HomeSettings.tapSignOut();
        ShortcutsTyper.delayTime(3000);
    }

    /**
     * This method implements the Edge 'e_SubmitValidEmailPassword'
     * 
     */
    public void e_SubmitValidEmailPassword() {
        SignUp.enterEmailPassword(generateEmail(), "test12");
        ShortcutsTyper.delayTime(8000);
    }

    /**
     * This method implements the Edge 'e_Sync'
     * 
     */
    public void e_Sync() {
        SignUp.sync();
        ShortcutsTyper.delayTime(5000);
        for (int i = 0; i < 4; i++) {
        	Gui.touchAVIew("PTGoalCircleView", 0);
        	ShortcutsTyper.delayTime(300);
        }
    }

    /**
     * This method implements the Edge 'e_inputBirthDay'
     * 
     */
    public void e_inputBirthDay() {
        generateBirthDay();
        SignUp.enterBirthDay(year, month, day);
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Edge 'e_inputHeight'
     * 
     */
    public void e_inputHeight() {
        generateHeight(true);
        SignUp.enterHeight(hDigit, hFraction, true);
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_inputSex'
     * 
     */
    public void e_inputSex() {
        isMale = new Random().nextBoolean();
        SignUp.enterGender(isMale);
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_inputWeight'
     * 
     */
    public void e_inputWeight() {
        generateWeight(true);
        SignUp.enterWeight(wDigit, wFraction, true);
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Vertex 'v_HomeScreen'
     * 
     */
    public void v_HomeScreen() {
        // TODO:
    }

    /**
     * This method implements the Vertex 'v_InitialView'
     * 
     */
    public void v_InitialView() {
        // TODO:
    }

    /**
     * This method implements the Vertex 'v_SignUpStep1'
     * 
     */
    public void v_SignUpStep1() {
        Assert.assertTrue(SignUp.isSignUpStep1View(), "This is not sign up step1 view.");
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Vertex 'v_SignUpStep2'
     * 
     */
    public void v_SignUpStep2() {
        Assert.assertTrue(SignUp.isSignUpStep2View(), "This is not sign up step2 view.");
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Vertex 'v_SignUpStep4Updated'
     * 
     */
    public void v_SignUpStep4Updated() {
        Assert.assertTrue(SignUp.getCurrentGoal() == goal, "Goal is not updated");
    }

    /**
     * This method implements the Vertex 'v_SignUpStep3'
     * 
     */
    public void v_SignUpStep3() {
        Assert.assertTrue(SignUp.isSignUpStep3View(), "This is not sign up step3 view.");
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Vertex 'v_SignUpStep4'
     * 
     */
    public void v_SignUpStep4() {
        Assert.assertTrue(SignUp.isSignUpStep4View(), "This is not sign up step4 view.");
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Vertex 'v_SignUpStep4UpdatedBirthday'
     * 
     */
    public void v_SignUpStep2UpdatedBirthday() {
        Assert.assertTrue(SignUp.isSignUpStep2View() && isUpdatedBirthday(),
                "This is not sign up step2 updated birthday view.");
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Vertex 'v_SignUpStep4UpdatedSex'
     * 
     */
    public void v_SignUpStep2UpdatedSex() {
        Assert.assertTrue(SignUp.isSignUpStep2View() && isUpdatedGender(),
                "This is not sign up step2 updated gender view.");
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Vertex 'v_SignUpStep4UpdatedHeight'
     * 
     */
    public void v_SignUpStep2UpdatedHeight() {
        Assert.assertTrue(SignUp.isSignUpStep2View() && isUpdatedHeight(),
                "This is not sign up step2 updated height view.");
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Vertex 'v_SignUpStep4UpdatedWeight'
     * 
     */
    public void v_SignUpStep2UpdatedWeight() {
        Assert.assertTrue(SignUp.isSignUpStep2View() && isUpdatedWeight(),
                "This is not sign up step2 updated weight view.");
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Vertex 'v_SignUpStep5'
     * 
     */
    public void v_SignUpStep5() {
        Assert.assertTrue(SignUp.isSignUpStep5View(), "This is not sign up step5 view.");
        ShortcutsTyper.delayTime(500);
    }

    private String generateEmail() {
        Date today = new Date();
        return "qatest" + String.valueOf(today.getTime()) + "@test.com";
    }

    private void generateBirthDay() {
        Random generator = new Random();
        year = String.valueOf(1930 + generator.nextInt(71));
        month = months[generator.nextInt(12)];
        day = String.valueOf(generator.nextInt(28) + 1);
    }

    private void generateHeight(boolean USMetric) {
        Random generator = new Random();
        isUSMetric = USMetric;
        if (isUSMetric) {
            hDigit = String.valueOf(3 + generator.nextInt(6)) + "'";
            hFraction = String.valueOf(generator.nextInt(12)) + "\\\"";
        } else {
            hDigit = String.valueOf(generator.nextInt(2) + 1);
            int fraction = generator.nextInt(51);
            if (fraction < 10) {
                hFraction = ".0" + String.valueOf(fraction);
            } else {
                hFraction = "." + String.valueOf(fraction);
            }
        }
    }

    private void generateWeight(boolean USMetric) {
        Random generator = new Random();
        isUSMetric = USMetric;
        if (isUSMetric) {
            wDigit = String.valueOf(77 + generator.nextInt(295));
        } else {
            wDigit = String.valueOf(35 + generator.nextInt(115));
        }
        wFraction = "." + String.valueOf(generator.nextInt(10));
    }

    private boolean isUpdatedGender() {
        return true;
    }

    private boolean isUpdatedBirthday() {
        String birthday = Gui.getProperty(ViewUtils.findView("UILabel", 0, ViewUtils.generateFindViewStatement(
                "PTDatePickerControl", 0)), "text");
        String expectedBirthday = month.substring(0, 3);
        if (Integer.valueOf(day) < 10) {
            expectedBirthday += " 0" + day;
        } else {
            expectedBirthday += " " + day;
        }
        expectedBirthday += ", " + year;
        return expectedBirthday.equals(birthday);
    }

    private boolean isUpdatedHeight() {
        String height = Gui.getProperty(ViewUtils.findView("UILabel", 0, ViewUtils.generateFindViewStatement(
                "PTHeightPickerControl", 0)), "text");
        boolean USMetric = !height.contains("m");
        String expectedHeight = hDigit;
        if (!USMetric) {
            expectedHeight += hFraction + " m";
        } else {
            expectedHeight += hFraction.substring(0, hFraction.length() - 2) + "\"";
        }
        return USMetric == isUSMetric && expectedHeight.equals(height);
    }

    private boolean isUpdatedWeight() {
        String weight = Gui.getProperty(ViewUtils.findView("UILabel", 0, ViewUtils.generateFindViewStatement(
                "PTWeightPickerControl", 0)), "text");
        boolean USMetric = weight.contains("lbs");
        String expectedWeight = wDigit + wFraction;
        if (USMetric) {
            expectedWeight += " lbs";
        } else {
            expectedWeight += " kg";
        }
        return USMetric == isUSMetric && expectedWeight.equals(weight);
    }
}
