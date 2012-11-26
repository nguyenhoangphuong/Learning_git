package com.misfit.ta.backend.temp;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.Settings;
import com.misfit.ta.utils.ShortcutsTyper;

public class BackupServerTest {

    private Logger logger = Util.setupLogger(BackupServerTest.class);
    public static String url = "http://184.72.64.53/";
    
    public static int waitTime = 10000;
    public static int count = 1;
    public static int started = 0;
    public static int success = 0;
    
    public static int threadId =0;
    public static int numOfConnections = 10;
    public static int left = numOfConnections; 
    public static StringBuffer timeBuffer = new StringBuffer();
    private static long totalTime = 0;
    
    public static void main(String[] args) {
        waitTime = Integer.parseInt(Settings.getParameter("waitTime"));
        numOfConnections = Integer.parseInt(Settings.getParameter("numOfConnections"));
        url = Settings.getParameter("url");
        left = numOfConnections;
        
        System.out.println("LOG [BackupServerTest.main]: Parameters: \n" +
        		"URL: " + url + 
        		"\nnumOfConnections: " + numOfConnections +
        		"\nwaitTime: " + waitTime);
        
        
        
        BackupServerTest test = new BackupServerTest();
        test.runTest(numOfConnections);
    }
    
    public void runTest(int numOfConnections) {
        for (int i=0; i< numOfConnections; i++) {
            Thread thread = new Thread (new SyncConnection(url));
            started++;
            thread.start();
            ShortcutsTyper.delayTime(10000);
        }
        
        long start = System.currentTimeMillis();
        long end = start;
        while (left > 0 && (end - start < waitTime)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            end = System.currentTimeMillis();
        }
        
        try {
            FileUtils.writeStringToFile(new File("logs/report.txt"), timeBuffer.toString());
        } catch (IOException e) {
        }
        
        logger.info("\n==========================");
        logger.info("Success: " + success + "/" + started);
        logger.info("Average req time: " + ((float)totalTime/success) + " seconds");
        
    }
    
    
    public static void informDone() {
        System.out.println("LOG [BackupServerTest.informDone]: thread id= " + (numOfConnections - left) + " is DONE");
        left--;
    }
    
    public static void informSuccess() {
        success++;  
    }
   
    public static void informSuccess(long time) {
        success++;
        float a = (float)time/1000;
        timeBuffer.append(a + "\n");
        totalTime += a;
    }
    
}
