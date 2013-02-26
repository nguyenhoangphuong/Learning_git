package com.misfit.ta.backend.old;

import junit.framework.Assert;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.misfit.ta.backend.LoadTest;


public class StressTest 
{
	// fields
	static private int total = 500;
	static private double maximumTime = 3000;
	
	// test setups
	@BeforeMethod
	public void beforeMethod()
	{
		System.out.println("=============================================================================================");
	}
	
	// test methods
    @Test
    public void SignInConcurrentStress()
    {    	
    	LoadTest test = new LoadTest(new LogInConnection(), total);
    	test.RunTest();
    	test.PrintResults();
    	
    	double aver = test.totalTime / test.numberOfThread;
    	Assert.assertTrue(aver <= maximumTime);
    	Assert.assertTrue(test.failNumber == 0);
    }

    @Test
    public void ServerConcurrentStress()
    {
    	LoadTest test = new LoadTest(new WebConnection("http://192.168.1.102:3000/"), total);
    	test.RunTest();
    	test.PrintResults();
    	
    	double aver = test.totalTime / test.numberOfThread;
    	Assert.assertTrue(aver <= maximumTime);
    	Assert.assertTrue(test.failNumber == 0);
    }
}
