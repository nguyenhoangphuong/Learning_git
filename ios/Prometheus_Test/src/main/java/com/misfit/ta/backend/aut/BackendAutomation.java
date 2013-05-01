/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.misfit.ta.backend.aut;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class BackendAutomation extends com.misfit.ta.aut.AutomationTest 
{

    private static final String[] results = { "PASSED", "FAILED", "UNKNOW" };
    private static final Logger logger = Util.setupLogger(BackendAutomation.class);

    @BeforeMethod(alwaysRun = true)
    public void setUpTest(Method method) 
    {
        logger.info("==================================================================================================================");
        logger.info("|  Start of test case: " + method.getName());
        logger.info("==================================================================================================================");
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUpTest(Method method, ITestResult tr)
    {
        logger.info("==================================================================================================================");
        logger.info("|  End of test case: " + method.getName());
        logger.info("|  Result: " + results[tr.getStatus() - 1]);
        logger.info("==================================================================================================================\n\n");
    }

}
