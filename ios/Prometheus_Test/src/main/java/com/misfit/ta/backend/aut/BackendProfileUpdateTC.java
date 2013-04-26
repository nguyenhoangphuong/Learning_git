package com.misfit.ta.backend.aut;

import junit.framework.Assert;

import org.testng.annotations.*;

import com.misfit.ta.backend.data.*;
import com.misfit.ta.backend.api.*;
import com.misfit.ta.ios.AutomationTest;

public class BackendProfileUpdateTC extends AutomationTest {

    String email;
    String password = "qwerty1";
    String udid;
    ProfileData defaultProfile;

    @BeforeClass
    public void setUp() {
        udid = DefaultValues.UDID;
        email = MVPApi.generateUniqueEmail();
        String token = MVPApi.signUp(email, password, udid).token;
        MVPApi.createProfile(token, defaultProfile);
        defaultProfile = DefaultValues.DefaultProfile();
    }

    @Test(groups = { "ios", "MVPBackend", "api", "profile" })
    public void UpdateProfileUsingValidToken() {
        // sign in and use valid token to update profile
        String token = MVPApi.signIn(email, password, udid).token;
        defaultProfile = MVPApi.getProfile(token).profile;
        ProfileData data = new ProfileData();

        data.updatedAt = defaultProfile.updatedAt + 10;
        data.name = "Dandelion" + System.nanoTime();
        data.weight = defaultProfile.weight + 1;

        ProfileResult r = MVPApi.updateProfile(token, data, defaultProfile.serverId);
        r.printKeyPairsValue();

        Assert.assertTrue("Status code is OK", r.statusCode == 200);
        Assert.assertTrue("Updated had changed", !defaultProfile.updatedAt.equals(r.profile.updatedAt));
        Assert.assertTrue("Name had changed", !defaultProfile.name.equals(r.profile.name));
        Assert.assertTrue("Weight had changed", !defaultProfile.weight.equals(r.profile.weight));
        Assert.assertTrue("Birthday had not changed", defaultProfile.dateOfBirth.equals(r.profile.dateOfBirth));

    }

    @Test(groups = { "ios", "MVPBackend", "api", "profile" })
    public void UpdateProfileUsingExpiredToken() {
        // sign in and sign out then and use old token to update profile

        String token = MVPApi.signIn(email, password, udid).token;
        defaultProfile = MVPApi.getProfile(token).profile;
        MVPApi.signOut(token);

        ProfileData data = new ProfileData();

        data.updatedAt = defaultProfile.updatedAt + 10;
        data.name = "Dandelion" + System.nanoTime();
        data.weight = defaultProfile.weight + 1;

        ProfileResult r = MVPApi.updateProfile(token, data, defaultProfile.serverId);
        r.printKeyPairsValue();

        Assert.assertTrue("Status code is 401", r.statusCode == 401);
        Assert.assertTrue("Profile is null", r.profile == null);
    }

    @Test(groups = { "ios", "MVPBackend", "api", "profile" })
    public void UpdateProfileWithoutToken() {
        // sign in and use arbitrary token to update profile

        String token = MVPApi.signIn(email, password, udid).token;
        defaultProfile = MVPApi.getProfile(token).profile;
        MVPApi.signOut(token);

        ProfileData data = new ProfileData();

        data.updatedAt = defaultProfile.updatedAt + 10;
        data.name = "Dandelion" + System.nanoTime();
        data.weight = defaultProfile.weight + 1;

        ProfileResult r = MVPApi.updateProfile(DefaultValues.ArbitraryToken, data, defaultProfile.serverId);
        r.printKeyPairsValue();

        Assert.assertTrue("Status code is 401", r.statusCode == 401);
        Assert.assertTrue("Profile is null", r.profile == null);
    }

    @Test(groups = { "ios", "MVPBackend", "api", "profile" })
    public void UpdateProfileHadBeenUpdated() {
        // sign in and decrease updatedAt of profile then use that to update
        // profile

        String token = MVPApi.signIn(email, password, udid).token;
        defaultProfile = MVPApi.getProfile(token).profile;
        ProfileData data = new ProfileData();

        data.updatedAt = defaultProfile.updatedAt - 10;
        data.name = "Dandelion" + System.nanoTime();
        data.weight = defaultProfile.weight + 1;

        ProfileResult r = MVPApi.updateProfile(token, data, defaultProfile.serverId);
        r.printKeyPairsValue();

        Assert.assertTrue("Status code is OK", r.statusCode == 210);
        Assert.assertTrue("Updated had changed", !defaultProfile.updatedAt.equals(r.profile.updatedAt));
        Assert.assertTrue("Name had changed", !defaultProfile.name.equals(r.profile.name));
        Assert.assertTrue("Weight had changed", !defaultProfile.weight.equals(r.profile.weight));
        Assert.assertTrue("Birthday had not changed", defaultProfile.dateOfBirth.equals(r.profile.dateOfBirth));
    }

}
