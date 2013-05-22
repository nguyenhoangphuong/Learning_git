package com.misfit.ta.backend.aut;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.AccountResult;
import com.misfit.ta.backend.data.BaseResult;

public class BackendSignOutTC extends BackendAutomation {
    String email;
    String password = "qwerty1";
    String udid;
    Logger logger = Util.setupLogger(BackendSignOutTC.class);

    @BeforeClass
    public void beforeClass() {
        email = MVPApi.generateUniqueEmail();
        udid = DefaultValues.UDID;
        MVPApi.signUp(email, password, udid);
    }

    @Test(groups = { "ios", "MVPBackend", "api", "signout" })
    public void SignOutValidToken() {
        // sign in then use the token to sign out

        String token = MVPApi.signIn(email, password, udid).token;
        BaseResult r = MVPApi.signOut(token);
        r.printKeyPairsValue();

        Assert.assertTrue("Status code is 200", r.isOK());
    }

    @Test(groups = { "ios", "MVPBackend", "api", "signout" })
    public void SignOutExpiredToken() {
        // sign in then sign out
        // sign in again and use old token from 1st sign in to sign out

        String token = MVPApi.signIn(email, password, udid).token;
        logger.info("Token 1: " +  token);
        MVPApi.signOut(token);
        AccountResult result = MVPApi.signIn(email, password, udid);
        logger.info("Token 2: " + result.token);

        BaseResult r = MVPApi.signOut(token);
        r.printKeyPairsValue();

        Assert.assertTrue("Status code is 401", r.statusCode == 401);
        Assert.assertEquals("Invalid auth token error message", r.errorMessage, DefaultValues.InvalidAuthToken);
    }

    @Test(groups = { "ios", "MVPBackend", "api", "signout" })
    public void SignOutInvalidToken() {
        // sign out with arbitrary / wrong format token

        MVPApi.signIn(email, password, udid);
        BaseResult r = MVPApi.signOut("12312578919836435123125-wrongformatorarbitrarytoken");
        r.printKeyPairsValue();

        Assert.assertTrue("Status code is 401", r.statusCode == 401);
        Assert.assertEquals("Invalid auth token error message", r.errorMessage, DefaultValues.InvalidAuthToken);
    }

    @Test(groups = { "ios", "MVPBackend", "api", "signout" })
    public void SignOutOnTwoDevices() {
        // sign in on two devices (two udid) then sign out each devices
        // two sign out must be success

        String token1 = MVPApi.signIn(email, password, udid).token;
        String token2 = MVPApi.signIn(email, password, DefaultValues.UDID2).token;

        // sign out on 1st device
        BaseResult r1 = MVPApi.signOut(token1);
        r1.printKeyPairsValue();

        Assert.assertTrue("Status code is 200", r1.isOK());

        // sign out on 2nd device
        BaseResult r2 = MVPApi.signOut(token2);
        r2.printKeyPairsValue();

        Assert.assertTrue("Status code is 200", r2.isOK());
    }

}
