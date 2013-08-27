package com.misfit.ta.backend.aut.correctness;

import junit.framework.Assert;

import org.testng.annotations.*;

import com.misfit.ta.backend.data.*;
import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.api.*;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;

public class BackendSignInTC extends BackendAutomation {
    String validEmail;
    String invalidEmail;
    String validPassword = "qwerty1";
    String invalidPassword = "wrong123";

    String udid;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        validEmail = MVPApi.generateUniqueEmail();
        invalidEmail = MVPApi.generateUniqueEmail() + ".invalid";
        udid = DefaultValues.UDID;
        MVPApi.signUp(validEmail, validPassword, udid);

    }

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignInWrongEmail() {
        AccountResult r = MVPApi.signIn(invalidEmail, validPassword, udid);
        r.printKeyPairsValue();

        Assert.assertTrue("Status code is not 200", !r.isOK());
        Assert.assertEquals("Status code is 404", r.statusCode, 404);
        Assert.assertEquals("Error message content", r.errorMessage, DefaultValues.WrongAccountMsg);
        Assert.assertTrue("Empty authenticate token", r.token == null || r.token.isEmpty());
    }

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignInWrongPassword() {
        AccountResult r = MVPApi.signIn(validEmail, invalidPassword, udid);
        r.printKeyPairsValue();

        Assert.assertTrue("Status code is not 200", !r.isOK());
        Assert.assertEquals("Status code is 404", r.statusCode, 404);
        Assert.assertEquals("Error message content", r.errorMessage, DefaultValues.WrongAccountMsg);
        Assert.assertTrue("Empty authenticate token", r.token == null || r.token.isEmpty());
    }

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignInValidAccount() {
        AccountResult r = MVPApi.signIn(validEmail, validPassword, udid);
        r.printKeyPairsValue();

        Assert.assertTrue("Status code is 200", r.isOK());
        Assert.assertTrue("Empty error message", r.isEmptyErrMsg());
        Assert.assertTrue("Authenticate token is not empty", !r.token.isEmpty());
    }

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignInWithTwoDevices() {
        AccountResult r1 = MVPApi.signIn(validEmail, validPassword, udid);
        r1.printKeyPairsValue();

        AccountResult r2 = MVPApi.signIn(validEmail, validPassword, DefaultValues.UDID2);
        r2.printKeyPairsValue();

        Assert.assertTrue("Different token between two devices", !r1.token.equals(r2.token));
    }

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignBackInDifferentToken() {
        // check if token changed after sign out and sign back in again
        AccountResult r1 = MVPApi.signIn(validEmail, validPassword, udid);
        MVPApi.signOut(r1.token);
        AccountResult r2 = MVPApi.signIn(validEmail, validPassword, udid);

        Assert.assertTrue("Different token after sign out and sign in again", !r1.token.equals(r2.token));
    }

}
