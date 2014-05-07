package com.misfit.ta.website;

import java.util.Collection;
import java.util.Date;
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

    protected int NUMBER_OF_USERS = 1000;
    protected int NUMBER_OF_THREADS = 200;
    static int errorCount = 0;

    Logger logger = Util.setupLogger(BlogStressTest.class);
    static ResultLogger rlog = ResultLogger.getLogger("BlogStressTest_"+ (new Date()));

    public BlogStressTest() {
        NUMBER_OF_USERS = Settings.getInt("NUMBER_OF_USERS");
        NUMBER_OF_THREADS = Settings.getInt("NUMBER_OF_THREADS");
    
    }
    
    public void setParameters(int numberOfUsers, int numberOfThreads) {
        NUMBER_OF_THREADS = numberOfThreads;
        NUMBER_OF_USERS = numberOfUsers;
        
    }

    @Test(groups = { "blog_stress" })
    public void blogStressTest(boolean randomizedOperations) {

        rlog.log("UserCount\tErrorCode");
        
        System.out.println("LOG [BlogStressTest.blogStressTest]: NUMBER_OF_USERS= "+ NUMBER_OF_USERS);
        System.out.println("LOG [BlogStressTest.blogStressTest]: NUMBER_OF_THREADS= " + NUMBER_OF_THREADS);
        
        int userCount = 0;
        long start = System.currentTimeMillis();

        Collection<Future<?>> futures = new LinkedList<Future<?>>();
        
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        while (userCount < NUMBER_OF_USERS) {
            for (int threads = 0; threads < Math.min(NUMBER_OF_THREADS, NUMBER_OF_USERS - userCount); threads++) {
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
        System.out.println("LOG [BlogStressTest.blogStressTest]: " + ResultLogger.totalTestRunTime);
        
        ResultLogger.logErrorSummary();
    }

    
    public static void main(String[] args) {
        BlogStressTest test = new BlogStressTest();
        
        if(args.length == 2) {
        	int numberOfUsers = Integer.valueOf(args[0]);
        	int numberOfThreads = Integer.valueOf(args[1]);
        	
        	test.setParameters(numberOfUsers, numberOfThreads);
        }
        
        test.blogStressTest(false);
        
        rlog.log("\n\n\n" +
        		"ErrorCount= " + errorCount);
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
            
            boolean failed = false;
            
            ServiceResponse res = RequestHelper.get("http://blog.int.misfitwearables.com", 80, param);
            String post = res.getContentData().toString();
            if (res.getStatusCode() != 200 || !post.contains("test third post")) { 
                errorCount++;
                failed = true;
            }
            rlog.log(threadCount+ "\t" + res.getStatusCode() + "\t" + ((failed) ? "Failed": "") );
            
        }
        
    }

}
