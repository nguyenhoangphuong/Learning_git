/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.misfit.ta.ios.aut;


import com.misfit.ios.AppHelper;
import com.misfit.ta.ios.AutomationTest;

/**
 * 
 * @author qa
 */
public class Test extends AutomationTest {
    public static void main(String[] args) {
        AppHelper.launchInstrument(AppHelper.getCurrentUdid(), AppHelper.getAppPath(), 
                "script/test1.js");    
    }
}
