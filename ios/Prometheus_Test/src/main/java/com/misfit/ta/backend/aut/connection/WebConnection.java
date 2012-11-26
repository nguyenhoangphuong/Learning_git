package com.misfit.ta.backend.aut.connection;

import com.google.resting.Resting;
import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.backend.temp.BackupServerTest;


public class WebConnection extends CThread 
{
	// fields
    private String url;
    
    // constructor
    public WebConnection(String url)
    {
       this.url = url;
    }
    
    // must-be-implemented methods
    public void run() 
    {
    	long t1 = System.currentTimeMillis();
        ServiceResponse response = Resting.get(url, 80);
        long t2 = System.currentTimeMillis();
               
        isSuccess = (response.getStatusCode() == 200);
        timeTaken = t2 - t1;
        
        if (!isSuccess) {
            System.out.println("------------------ ERROR : " + response.getStatusCode() + " ----------------------");
            System.out.println("LOG [BackupServerTest.Connection.run]: " + response);
        } else {  
            System.out.println("LOG [BackupServerTest.Connection.run]: " + response);
        }
    }
    
    public CThread duplicate()
    {
    	return new WebConnection(url);
    }
}

