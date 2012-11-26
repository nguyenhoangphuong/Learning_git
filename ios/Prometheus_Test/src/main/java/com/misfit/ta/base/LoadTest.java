package com.misfit.ta.base;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.aut.connection.CThread;
import com.misfit.ta.backend.temp.BackupServerTest;

public class LoadTest 
{
	// logger
	protected Logger logger = Util.setupLogger(BackupServerTest.class);
	
	// fields
	public int numberOfThread;
	protected CThread[] threads;
	
	public long totalTime;
	public int successNumber;
	public int failNumber;
	
	static public long timeOut = 100000;
	
	// constructor
	public LoadTest(CThread[] threads)
	{
		this.numberOfThread = threads.length;
		this.threads = threads;
		
		this.totalTime = 0;
		this.successNumber = 0;
		this.failNumber = 0;
	}
	
	public LoadTest(CThread sample, int numberOfThread)
	{
		this.numberOfThread = numberOfThread;
		this.threads = new CThread[numberOfThread];
		for(int i = 0; i < numberOfThread; i++)
			threads[i] = sample.duplicate();
		
		this.totalTime = 0;
		this.successNumber = 0;
		this.failNumber = 0;
	}
	
	// methods
	public void RunTest()
	{
		// start time
		long t1 = System.currentTimeMillis();
		
		// start all threads
		for(int i = 0; i < numberOfThread; i++)
			threads[i].start();
		
		// wait until all threads have finished
		while(true)
		{
			// check threads have finished
			int i = 0;
			for(i = 0; i < numberOfThread; i++)
			{
				if(threads[i].getState() != Thread.State.TERMINATED)
				{
					try 
					{
						Thread.sleep(100);
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					break;
				}
			}
			
			if(i == numberOfThread)
			{
				Finished();
				break;
			}
			
			// check timed out
			long t2 = System.currentTimeMillis();
			if(t2 - t1 > timeOut)
			{
				System.out.println("Timed out");
				Finished();
				break;
			}
		}
		
		// end time
		long t2 = System.currentTimeMillis();
		
		// total time
		this.totalTime = t2 - t1;
	}
	
	public void PrintResults()
	{
		System.out.println("Total threads: " + numberOfThread);
    	System.out.println("Average time: " + totalTime / numberOfThread);
    	System.out.println("Successful threads: " + successNumber);
    	System.out.println("Failed threads: " + failNumber);
	}
	
	// virtual methods
	protected void Finished()
	{
		for(int i = 0; i < numberOfThread; i++)
		{
			if(threads[i].getState() == Thread.State.TERMINATED)
			{
				if( (threads[i]).isSuccess )
					this.successNumber++;
				else
					this.failNumber++;
			}
		}
	}
	
}
