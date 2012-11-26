package com.misfit.ta.backend.aut.connection;

import com.google.resting.Resting;
import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.backend.aut.BackupServerTest;


public class WebConnection extends Thread {
    private int id;
    private String url;
    public WebConnection(String url) {
       this.url = url;
    }
    
    public void run() {
        
        ServiceResponse response = Resting.get(url, 80);
        int statusCode = response.getStatusCode();
        if (statusCode != 200) {
            System.out.println("------------------ ERROR : " + statusCode + " ----------------------");
            System.out.println("LOG [BackupServerTest.Connection.run]: " + response);
        } else {  
            System.out.println("LOG [BackupServerTest.Connection.run]: " + response);
            BackupServerTest.informSuccess();
        }
        BackupServerTest.informDone();
    }
}

