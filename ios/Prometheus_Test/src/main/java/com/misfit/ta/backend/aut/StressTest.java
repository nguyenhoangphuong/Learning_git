package com.misfit.ta.backend.aut;

import junit.framework.Assert;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.misfit.ta.backend.aut.connection.LogInConnection;
import com.misfit.ta.backend.aut.connection.WebConnection;
import com.misfit.ta.base.LoadTest;


public class StressTest 
{
	// fields
	static private int total = 100;
	static private double maximumTime = 3000;
	
	// test setups
	@BeforeMethod
	public void beforeMethod()
	{
		System.out.println("=============================================================================================");
	}
	
	// test methods
    @Test
    public void SignInStress() 
    {
    	int count = total;
    	long t1 = System.currentTimeMillis();
    	
    	while(count > 0)
    	{
    		SignInTest.signIn("qa-fullplan@a.a", "qwerty1");
    		count--;
    	}
    	
    	long t2 = System.currentTimeMillis();
    	double aver = (t2 - t1) / total	;
    	
    	System.out.println("Average response time: " + aver);
    	Assert.assertTrue(aver <= maximumTime);
    }

    @Test
    public void SignInConcurrentStress()
    {
    	/*
    	LogInConnection[] cons = new LogInConnection[20];
    	for(int i = 0; i < 20; i++)
    		cons[i] = new LogInConnection();
    	*/
    	
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
