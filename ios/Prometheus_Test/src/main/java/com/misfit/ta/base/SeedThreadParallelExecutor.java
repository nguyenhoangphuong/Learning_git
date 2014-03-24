package com.misfit.ta.base;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.graphwalker.Util;


public class SeedThreadParallelExecutor {
	
    // logger
    protected Logger logger = Util.setupLogger(SeedThreadParallelExecutor.class);

    
    // fields
    protected SeedThread seed;
    protected int numberOfThread;
    protected int numberOfSeed;
    
    public long startTime;
    public long endTime;
    public long totalTime;

    
    // constructor
    public SeedThreadParallelExecutor(SeedThread seed, int numberOfSeed, int numberOfThread) {
    	
    	this.seed = seed;
    	this.numberOfSeed = numberOfSeed;
    	this.numberOfThread = numberOfThread;       
    }
    
    
    // methods
    public void execute() {
    	
    	this.startTime = System.currentTimeMillis() / 1000;
    	
    	Collection<Future<?>> futures = new LinkedList<Future<?>>();
    	ExecutorService executor = Executors.newFixedThreadPool(this.numberOfThread);
    	
    	int seedCount = 0;
        while (seedCount < this.numberOfSeed) {
        	
            for (int threads = 0; threads < Math.min(numberOfThread, numberOfSeed - seedCount); threads++) {

                futures.add(executor.submit(seed.duplicate()));
                seedCount++;
            }
   
        }
        
        executor.shutdown();
        
        for (Future<?> future:futures) {
            try {
                future.get();
            } catch (Exception e) {
            }
        }
        
        this.endTime = System.currentTimeMillis() / 1000;
        this.totalTime = this.endTime - this.startTime;
        
        
    }
    
    public String getSummary() {
    	
    	String summary = "";
    	summary += "\n------------------------------------------------------------------";
    	summary += "\nSeed count: " + this.numberOfSeed;
    	summary += "\nThread count: " + this.numberOfThread;
    	summary += "\nStart time: " + this.startTime;
    	summary += "\nEnd time: " + this.endTime;
    	summary += "\nTotal running time: " + this.totalTime;
    	summary += "\n------------------------------------------------------------------\n";
    	
    	return summary;
    }
    
    public void printSummary() {
    	
    	String[] lines = getSummary().split("\n");
    	for(String line : lines)
    		logger.info(line);
    }

}
