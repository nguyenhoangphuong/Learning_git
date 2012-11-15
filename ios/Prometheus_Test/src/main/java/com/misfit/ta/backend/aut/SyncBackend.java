package com.misfit.ta.backend.aut;

import java.util.List;
import java.util.Vector;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.data.*;
import com.misfit.ta.backend.rest.profile.SyncRest;
import com.misfit.ta.backend.rest.signin.SignInRest;
import com.misfit.ta.backend.rest.signin.SignUpRest;
import com.misfit.ta.backend.rest.signin.TryoutRest;


public class SyncBackend {
	
    private Logger logger = Util.setupLogger(SyncBackend.class);
    
    public static void main(String[] args) {
        SyncBackend test = new SyncBackend();
        //test.sync();
        test.signUp("qa2@test.test", "misfit1");
    }
    
    private static void testSignIn() {
        SyncBackend test = new SyncBackend();
        test.signIn("t@t.t", "1234");
        test.signIn("tung@misfitwearables.com", "misfit1");
        test.signUp("t@t.t", "1234");
        test.signUp("t@t.t", "misfit1");
        test.tryout();
    }

    private AuthToken register(String username, String password, int registrationType) {
        SignInData user = new SignInData(username, password);
        
        SignInRest rest = null;
        switch (registrationType) {
        case 0:
            rest = new SignInRest(user);
            break;
        case 1:
            rest = new SignUpRest(user);
            break;
        case 2:
            rest = new TryoutRest(user);
            break;
        }
       
        rest.post();
        AuthToken token = (AuthToken) rest.content();
        logger.info("Token= " + token.token + ", type=" + token.type);
        return token;
    }
    
    public AuthToken signIn(String username, String password) {
        return register(username, password, 0);
    }
    
    public AuthToken signUp(String username, String password) {
        return register(username, password, 1);
    }
    
    public AuthToken tryout() {
        return register("", "", 2);
    }
    
    public void sync() {
    	
        SyncData data = SyncData.getDefault();
        SyncRest rest = new SyncRest(data);
 
        List<Header> headers = new Vector<Header>();
        BasicHeader header = new BasicHeader("auth_token", "BaFygpRsHFRDJDV45H7Q");
        headers.add(header);
        
        rest.postWithHeader(headers);
        data = (SyncData)rest.content();
        
        System.out.println(data.getValue("personalPlans"));
        data.setValue("personalPlans", "this is profiles info");
        System.out.println(data.getValue("personalPlans"));
    }
}