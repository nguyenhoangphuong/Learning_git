package com.misfit.ta.backend.aut;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.annotations.Test;

import com.misfit.ta.backend.data.AuthToken;
import com.misfit.ta.backend.data.SignInData;
import com.misfit.ta.backend.rest.signin.SignInRest;
import com.misfit.ta.backend.rest.signin.SignUpRest;

public class SignInTest 
{
    // fields
    private static Logger logger = Util.setupLogger(SignInTest.class);

    private long id = System.currentTimeMillis();
    private String email = "generate-" + id + "@qa.com";
    private String existedEmail = "a@a.a";
    private String emptyEmail = "signin_empty@qa.com";
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

        // right info with empty plan
        Assert.assertFalse(signIn(emptyEmail, password).token.isEmpty());
        
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


    public static void main(String[] args)
    {
    	signUp("signin_empty1@qa.com", "qwerty1");
    	signIn("signin_empty1@qa.com", "qwerty1");
    }
}
