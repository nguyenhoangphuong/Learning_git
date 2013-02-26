package com.misfit.ta.backend.old;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.annotations.Test;


public class SignInTest 
{
    // fields
    private static Logger logger = Util.setupLogger(SignInTest.class);

    private long id = System.currentTimeMillis();
    private String email = "generate-" + id + "@qa.com";
    private String existedEmail = "5.5@1.1";
    private String password = "qwerty1";

    // test methods
    @Test
    public void TestSignUp() 
    {
        // generated account
        Assert.assertFalse(signUp(email, password).token.isEmpty());

        // existed account
        Assert.assertTrue(signUp(email, password).token.isEmpty());
    }

    @Test
    public void TestSignIn() 
    {
        // right info
        Assert.assertFalse(signIn(existedEmail, password).token.isEmpty());
        
        // wrong info
        Assert.assertTrue(signIn(email, "asdasd12131").token.isEmpty());
    }

    // helpers
    private static AuthToken register(String username, String password, int registrationType) 
    {
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

    public static AuthToken signIn(String username, String password) 
    {
        return register(username, password, 0);
    }

    public static AuthToken signUp(String username, String password) 
    {
        return register(username, password, 1);
    }
}
