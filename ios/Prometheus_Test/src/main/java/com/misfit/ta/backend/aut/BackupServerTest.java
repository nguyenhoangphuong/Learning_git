package com.misfit.ta.backend.aut;

import com.google.resting.Resting;
import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.Settings;

public class BackupServerTest {

    public static String url = "http://184.72.64.53/";
    
    public static int waitTime = 10000;
    public static int count = 1;
    public static int started = 0;
    public static int success = 0;
    
    public static int threadId =0;
    public static int numOfConnections = 10;
    public static int left = numOfConnections; 
    
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
            Thread thread = new Thread (new Connection());
            started++;
            thread.start();
        }
        
        System.out.println("LOG [BackupServerTest.runTest]: started= "+ started);
        
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
        System.out.println("\n==========================");
        System.out.println("LOG [BackupServerTest.runTest]: success: " + success + "/" + started);
    }
    
   
    
    class Connection extends Thread {
        private int id;
        public Connection() {
           
        }
        
        public void run() {
            
            ServiceResponse response = Resting.get(url, 80);
            System.out.println("LOG [BackupServerTest.Connection.run]: threadid= "+ threadId++);
            int statusCode = response.getStatusCode();
            if (statusCode != 200) {
                System.out.println("------------------ ERROR : " + statusCode + " ----------------------");
                System.out.println("LOG [BackupServerTest.Connection.run]: " + response);
//            } else {  
                System.out.println("LOG [BackupServerTest.Connection.run]: " + response);
                success++;
            }
            left--;
        }
    }
    
    
}
