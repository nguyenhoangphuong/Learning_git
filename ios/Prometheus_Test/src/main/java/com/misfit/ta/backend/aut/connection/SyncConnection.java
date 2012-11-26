package com.misfit.ta.backend.aut.connection;

import com.google.resting.Resting;
import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.backend.aut.BackupServerTest;
import com.misfit.ta.backend.aut.SyncBackend;
import com.misfit.ta.backend.data.AuthToken;


public class SyncConnection extends Thread {
    private int id;
    private String url;
    public SyncConnection(String url) {
       this.url = url;
    }
    
    public void run() {
        
        SyncBackend conn = new SyncBackend();
        long start = System.currentTimeMillis();
        AuthToken token = conn.signIn("q@q.q", "misfit1");
        long end = System.currentTimeMillis();
        
        if (token != null && token.token != null && !token.token.isEmpty()) {
            BackupServerTest.informSuccess(end-start);
        }
        
        BackupServerTest.informDone();
    }
}

