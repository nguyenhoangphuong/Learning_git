package com.misfit.ta.backend.aut.performance;

import org.testng.Assert;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.AccountResult;
import com.misfit.ta.backend.data.ProfileData;
import com.misfit.ta.backend.data.ProfileResult;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.utils.TextTool;

public class BackendCacheTesting {

    public static final String SERVER1 = "https://testproduction.misfitwearables.com/shine/v7/";
    public static final String SERVER2 = "https://testproduction.misfitwearables.com/shine/v7/";

    public String email = MVPApi.generateUniqueEmail();
    public String token;

    public static void main(String[] args) {

        BackendCacheTesting test = new BackendCacheTesting();
        test.runProfileTest();
        test.runPedometerTest();

    }

    public BackendCacheTesting() {
        change(SERVER1);
        AccountResult result = MVPApi.signUp(email, "misfit1", TextTool.getRandomString(6, 8));
        token = result.token;
    }

    public void runProfileTest() {
        change(SERVER1);
        ProfileData profile = DefaultValues.DefaultProfile();
        ProfileResult result = MVPApi.createProfile(token, profile);
        Assert.assertTrue(result.isOK(), "Status code is not 200: " + result.statusCode);

        change(SERVER2);
        // get Profile
        result = MVPApi.getProfile(token);
        Assert.assertTrue(result.isOK(), "Status code is not 200: " + result.statusCode);
        Assert.assertTrue(profile.name.equalsIgnoreCase(result.profile.name), "Name is different, old: " + profile.name
                + ", new: " + result.profile.name);

        change(SERVER2);
        // update profile
        ProfileData newProfile = result.profile;
        newProfile.name = profile.name + "_new";
        result = MVPApi.updateProfile(token, newProfile, profile.serverId);

        change(SERVER1);
        // get Profile
        result = MVPApi.getProfile(token);
        Assert.assertTrue(result.isOK(), "Status code is not 200: " + result.statusCode);
        Assert.assertTrue(newProfile.name.equalsIgnoreCase(result.profile.name), "server 1: Name is different, local: "
                + newProfile.name + ", server: " + result.profile.name);

        change(SERVER2);

        // get Profile
        result = MVPApi.getProfile(token);
        Assert.assertTrue(result.isOK(), "Status code is not 200: " + result.statusCode);
        Assert.assertTrue(newProfile.name.equalsIgnoreCase(result.profile.name), "server 2: Name is different, old: "
                + newProfile.name + ", new: " + result.profile.name);
    }

    public void runPedometerTest() {
        long now = System.currentTimeMillis()/1000;
        String serial = TextTool.getRandomString(7, 8);
        String localId = TextTool.getRandomString(7, 8);

        change(SERVER1);
        Pedometer pedo = MVPApi.createPedometer(token, serial, "hw1234", now, now, now, localId, null, now);
        Assert.assertTrue(pedo.getSerialNumberString().equalsIgnoreCase(serial), "Wrong serial return: "
                + pedo.getSerialNumberString());

        change(SERVER2);
        pedo = MVPApi.showPedometer(token);
        Assert.assertTrue(pedo.getSerialNumberString().equalsIgnoreCase(serial), "Wrong serial return: "
                + pedo.getSerialNumberString());

        change(SERVER1);
        now = System.currentTimeMillis()/1000;
        String newSerial = TextTool.getRandomString(7, 8);
        pedo = MVPApi.updatePedometer(token, newSerial, "hw1234", now, now, now, localId, null, now);
        Assert.assertTrue(pedo.getSerialNumberString().equalsIgnoreCase(newSerial), 
                "Wrong serial number: " + pedo.getSerialNumberString());
        
        change(SERVER2);
        pedo = MVPApi.showPedometer(token);
        Assert.assertTrue(pedo.getSerialNumberString().equalsIgnoreCase(newSerial), "Wrong serial return: "
                + pedo.getSerialNumberString());
    }

    public void change(String server) {
        MVPApi.baseAddress = server;
    }

}
