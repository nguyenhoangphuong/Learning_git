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
    
    protected long startTime;
    protected long endTime;
    protected long totalTime;

    
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
    
    public void printSummary() {
    	
    	logger.info("------------------------------------------------------------------");
    	logger.info("Start time: " + this.startTime);
    	logger.info("End time: " + this.endTime);
    	logger.info("Total running time: " + this.totalTime);
    	logger.info("------------------------------------------------------------------");
    }

}
