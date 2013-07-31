/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.misfit.ta.ios;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.misfit.ta.report.TRS;

public class TRSAutomation
{

	private static Logger logger = Util.setupLogger(TRSTests.class);
	private static int[] trsResult = {0, 1, -1, 0};
	
	public String getGroups(Method method) 
	{
        try 
        {
            logger.debug("Method:" + method);
            Annotation[] annotations = method.getAnnotations();
            logger.debug("Annotation:" + annotations);

            for (Annotation annotation : annotations) 
            {
                if (annotation instanceof Test) 
                {
                    Test test = (Test) annotation;
                    String[] groups = test.groups();
                    String s = "";
                    
                    for(String group : groups)
                    	s += (", " + group);
                    
                    if(s == "")
                    	return "";
                    
                    return s.substring(2);
                }
            }
        }
        catch (Exception e) 
        {
            Util.logStackTraceToError(e);
            Assert.fail(e.getMessage());
        }

        return "";
    }
	
	public Test getTest(Method method)
	{
		logger.debug("Method:" + method);
		Annotation[] annotations = method.getAnnotations();
		logger.debug("Annotation:" + annotations);

		for (Annotation annotation : annotations) 
		{
			if (annotation instanceof Test)
				return (Test) annotation;
		}
		
		return null;
	}
	
	
	@BeforeSuite(alwaysRun = true)
	public void beforeSuite()
	{
		TRS.instance().startSuite("iOS");
	}
	
	@AfterSuite(alwaysRun = true)
	public void afterSuite()
	{
		TRS.instance().endSuite();
	}

    @BeforeMethod(alwaysRun = true)
    public void setUpTest(Method method) 
    {
    	String testClass = this.getClass().getSimpleName();
    	String testMethod = method.getName();
    	String testGroups = getGroups(method);
    	
    	TRS.instance().startTest(testClass, testMethod, testGroups);
    }
 
    @AfterMethod(alwaysRun = true)
    public void cleanUpTest(Method method, ITestResult tr)
    {
    	TRS.instance().endTest(trsResult[tr.getStatus()]);
    }

}
