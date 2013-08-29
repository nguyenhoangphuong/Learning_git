package com.misfit.ta.backend.aut.correctness;


import org.testng.Assert;
import org.testng.annotations.*;

import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.api.*;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;

public class BackendSignInTC extends BackendAutomation {
	
    String validEmail;
    String invalidEmail;
    String validPassword = "qwerty1";
    String invalidPassword = "wrong123";
    
	String[] wrongFormatEmails = { "", "wrong100", "wrong2@", "wrong3 @a.b" };
	String wellFormatEmail = "wellformat@qa.com";
	String[] wrongFormatPasswords = { "", "short" };
	String wellFormatPassword = "qwerty1";
    
    @BeforeClass(alwaysRun = true)
    public void setUp() {
        validEmail = MVPApi.generateUniqueEmail();
        invalidEmail = MVPApi.generateUniqueEmail() + ".invalid";
        MVPApi.signUp(validEmail, validPassword);

    }
    
    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signup" })
	public void SignInWrongFormatEmails() {
		for (int i = 0; i < wrongFormatEmails.length; i++) {
			AccountResult r = MVPApi.signIn(wrongFormatEmails[i], wellFormatPassword);
			r.printKeyPairsValue();
			
			Assert.assertEquals(r.isNotFound(), "Status code is 404");
			Assert.assertEquals(DefaultValues.InvalidEmail, r.errorMessage, "Error message content");
			Assert.assertTrue(r.token == null || r.token.isEmpty(), "Empty authenticate token");
		}
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signup" })
	public void SignInWrongFormatPasswords() {
		for (int i = 0; i < wrongFormatPasswords.length; i++) {
			AccountResult r = MVPApi.signIn(wellFormatEmail, wrongFormatPasswords[i]);
			r.printKeyPairsValue();

			Assert.assertTrue(r.isNotFound(), "Status code is 404");
			Assert.assertEquals(DefaultValues.InvalidPassword, r.errorMessage, "Error message content");
			Assert.assertTrue(r.token == null || r.token.isEmpty(), "Empty authenticate token");
		}
	}

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignInWrongEmail() {
        AccountResult r = MVPApi.signIn(invalidEmail, validPassword);
        r.printKeyPairsValue();

        Assert.assertTrue(r.isNotFound(), "Status code is 404");
        Assert.assertEquals(r.errorMessage, DefaultValues.WrongAccountMsg, "Error message content");
        Assert.assertTrue(r.token == null || r.token.isEmpty(), "Empty authenticate token");
    }

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignInWrongPassword() {
        AccountResult r = MVPApi.signIn(validEmail, invalidPassword);
        r.printKeyPairsValue();

        Assert.assertTrue(r.isNotFound(), "Status code is 404");
        Assert.assertEquals(r.errorMessage, DefaultValues.WrongAccountMsg, "Error message content");
        Assert.assertTrue(r.token == null || r.token.isEmpty(), "Empty authenticate token");
    }

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignInValidAccount() {
        AccountResult r = MVPApi.signIn(validEmail, validPassword);
        r.printKeyPairsValue();

        Assert.assertTrue(r.isOK(), "Status code is 200");
        Assert.assertTrue(r.isEmptyErrMsg(), "Empty error message");
        Assert.assertTrue(!r.token.isEmpty(), "Authenticate token is not empty");
    }

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignInWithTwoDevices() {
        AccountResult r1 = MVPApi.signIn(validEmail, validPassword);
        r1.printKeyPairsValue();

        AccountResult r2 = MVPApi.signIn(validEmail, validPassword);
        r2.printKeyPairsValue();

        Assert.assertNotEquals(r1.token, r2.token, "Different token between two devices");
    }

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignBackInDifferentToken() {
        // check if token changed after sign out and sign back in again
        AccountResult r1 = MVPApi.signIn(validEmail, validPassword);
        MVPApi.signOut(r1.token);
        AccountResult r2 = MVPApi.signIn(validEmail, validPassword);

        Assert.assertNotEquals(r1.token, r2.token, "Different token after sign out and sign in again");
    }

}
