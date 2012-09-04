/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.misfit.ta.ios.aut;


import com.misfit.ios.AppHelper;
import com.misfit.ta.Settings;
import com.misfit.ta.ios.AutomationTest;

/**
 * 
 * @author qa
 */
public class Test extends AutomationTest {
    public static void main(String[] args) {
        String bundleName = Settings.getParameter("appBundle");
        System.out.println("LOG [Test.main]: " + bundleName);
//        AppHelper.uninstall(AppHelper.getCurrentUdid());
       AppHelper.cleanCache();
    }
}
