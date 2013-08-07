/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.misfit.ta.ios;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.*;

import com.misfit.ios.AppHelper;
import com.misfit.ta.Settings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.InstrumentHelper;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ShortcutsTyper;

public class AutomationTest extends com.misfit.ta.aut.AutomationTest {
    protected static boolean debug = false;
    private InstrumentHelper instrument = new InstrumentHelper();

    static
    {
        String isDebug = Settings.getParameter("MVPDebug");
        debug = (isDebug != null && isDebug.equalsIgnoreCase("true"));
    }

    @Override
    @BeforeMethod(alwaysRun = true)
    public void setUpTest(Method method) 
    {
        super.setUpTest(method);

        Files.delete("instrumentscli0.trace");

        if (!debug)
        {
    		AppHelper.cleanCache();
    		ShortcutsTyper.delayTime(1000);
        }
        
        instrument.kill();
        instrument.start();
		ShortcutsTyper.delayTime(20000);
    	Gui.init(Settings.getParameter("DeviceIP"));
    }

    @Override
    @AfterMethod(alwaysRun = true)
    public void cleanUpTest(Method method, ITestResult tr)
    {
    	Gui.shutdown();
    	instrument.stop();
    	instrument.kill();
    	
        super.cleanUpTest(method, tr);
    }

}
