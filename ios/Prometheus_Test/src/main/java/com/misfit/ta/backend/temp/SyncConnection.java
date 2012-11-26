package com.misfit.ta.backend.temp;

import com.misfit.ta.backend.aut.SignInTest;
import com.misfit.ta.backend.data.AuthToken;


public class SyncConnection extends Thread {
    
	int id;
    String url;
    
    public SyncConnection(String url) {
       this.url = url;
    }
    
    public void run() {

        long start = System.currentTimeMillis();
        AuthToken token = SignInTest.signIn("qa-fullplan@a.a", "qwerty1");
        long end = System.currentTimeMillis();
        
        if (token != null && token.token != null && !token.token.isEmpty()) {
            BackupServerTest.informSuccess(end-start);
        }
        
        BackupServerTest.informDone();
    }
}

