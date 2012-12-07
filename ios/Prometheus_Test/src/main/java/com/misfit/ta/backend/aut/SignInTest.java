package com.misfit.ta.backend.aut;

import java.util.List;
import java.util.Vector;

import junit.framework.Assert;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.annotations.Test;

import com.misfit.ta.backend.data.AuthToken;
import com.misfit.ta.backend.data.SignInData;
import com.misfit.ta.backend.data.SyncData;
import com.misfit.ta.backend.rest.profile.SyncRest;
import com.misfit.ta.backend.rest.signin.SignInRest;
import com.misfit.ta.backend.rest.signin.SignUpRest;

public class SignInTest {
    // fields
    private static Logger logger = Util.setupLogger(SignInTest.class);

    private long id = System.currentTimeMillis();
    private String email = "generate-" + id + "@qa.com";
    private String existedEmail = "qa@a.a";
    private String password = "qwerty1";

    // test methods
    @Test
    public void TestSignUp() {
        // generated account
        Assert.assertFalse(signUp(email, password).token.isEmpty());

        // existed account
        Assert.assertTrue(signUp(email, password).token.isEmpty());
    }

    @Test
    public void TestSignIn() {
        // right info
        Assert.assertFalse(signIn(existedEmail, password).token.isEmpty());

        // wrong info
        Assert.assertTrue(signIn(email, "asdasd").token.isEmpty());
    }

    // helpers
    private static AuthToken register(String username, String password, int registrationType) {
        SignInData user = new SignInData(username, password);

        SignInRest rest = null;
        switch (registrationType) {
        case 0:
            rest = new SignInRest(user);
            break;
        case 1:
            rest = new SignUpRest(user);
            break;
        }

        rest.post();
        AuthToken token = (AuthToken) rest.content();
        logger.info("Token = " + token.token + ", Type =" + token.type);
        return token;
    }

    public static AuthToken signIn(String username, String password) {
        return register(username, password, 0);
    }

    public static AuthToken signUp(String username, String password) {
        return register(username, password, 1);
    }


    public static void main(String[] args) {
        System.out.println("LOG [SignInTest.main]: testttttttttttttttttt");
        // create authen token header
        List<Header> headers = new Vector<Header>();
        AuthToken auth = SignInTest.signIn("stress@m.qa", "qwerty1");
        BasicHeader header = new BasicHeader("auth_token", auth.token);
        headers.add(header);
        
//        
//        long lastSuccess = auth.syncData.getLastUpdated();
//        SyncData oldData = auth.syncData;
//        
//
//        // get time
//        StringBuffer buf = new StringBuffer();
//
//        for (int i = 1; i <= 1; i++) {
//            lastSuccess++;
//            oldData.setLastUpdated(0);
//            System.out.println("LOG [SignInTest.main]: old data= \n" + oldData.getString());
//            SyncRest rest = new SyncRest(oldData);
//            long start = System.currentTimeMillis();
//            rest.postWithHeader(headers);
//            long end = System.currentTimeMillis();
//            SyncData data = (SyncData) rest.content();
//            logger.info(data.objects);
//            buf.append(String.valueOf(end - start) + "\n");
//        }
//
//        System.out.println("LOG [SignInTest.main]: Time: \n" + buf.toString());
    }
}
