package com.misfit.ta.website;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.annotations.Test;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.RequestHelper;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.base.Clock;

public class BlogStressTest {

    protected int NUMBER_OF_USERS = 20;
    protected int NUMBER_OF_THREADS = 2;

    Logger logger = Util.setupLogger(BlogStressTest.class);

    public BlogStressTest() {
    }
    
    public void setParameters(int numberOfUsers, int numberOfThreads) {
        NUMBER_OF_THREADS = numberOfThreads;
        NUMBER_OF_USERS = numberOfUsers;
        
    }

    @Test(groups = { "blog_stress" })
    public void signupOneMillionUsers(boolean randomizedOperations) {


        int userCount = 0;
        long start = System.currentTimeMillis();

        Collection<Future<?>> futures = new LinkedList<Future<?>>();
        
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        while (userCount < NUMBER_OF_USERS) {
            for (int threads = 0; threads < Math.min(NUMBER_OF_THREADS, NUMBER_OF_USERS - userCount); threads++) {
//                BackendDatabaseSeedingThread test = new BackendDatabaseSeedingThread(userCount, timelineItems,
//                        graphItems, rlog, randomizedOperations);
                BlogTestThread test = new BlogTestThread(userCount);
                futures.add(executor.submit(test));
                userCount++;
            }
   
        }
        executor.shutdown();
        
        for (Future<?> future:futures) {
            try {
                future.get();
            } catch (Exception e) {
            }
        }
        
        long now = System.currentTimeMillis();
        ResultLogger.totalTestRunTime = (now - start);
        System.out.println("LOG [BackendDatabaseSeeding.signupOneMillionUsers]: " + ResultLogger.totalTestRunTime);
        
        ResultLogger.logErrorSummary();
    }

    
    public static void main(String[] args) {
        BlogStressTest test = new BlogStressTest();
        
        if(args.length == 2) {
        	int numberOfUsers = Integer.valueOf(args[0]);
        	int numberOfThreads = Integer.valueOf(args[1]);
        	
        	test.setParameters(numberOfUsers, numberOfThreads);
        }
        
        test.signupOneMillionUsers(false);
        
    }
    
    class BlogTestThread implements Runnable {
        
        int threadCount =0;
        public BlogTestThread(int threadCount) {
            this.threadCount = threadCount;
        }
        public void run() {
            BaseParams param = new BaseParams();
            byte[] encodedBytes = Base64.encodeBase64("admin@misfit.com:User@123".getBytes());
            String encoding = new String(encodedBytes);
            param.addHeader("Authorization", "Basic " + encoding);
            
            
            
            ServiceResponse res = RequestHelper.get("http://blog.int.misfitwearables.com", 80, param);
            System.out.println("LOG [Debug.main]: " + res.getStatusCode());
            
        }
        
    }

}
