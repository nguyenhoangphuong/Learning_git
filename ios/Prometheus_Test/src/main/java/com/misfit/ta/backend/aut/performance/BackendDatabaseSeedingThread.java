package com.misfit.ta.backend.aut.performance;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.AccountResult;
import com.misfit.ta.backend.data.ProfileData;
import com.misfit.ta.backend.data.ProfileResult;

public class BackendDatabaseSeedingThread implements Runnable {

    private String password = "misfit1";
    private String udid;

    private int userCount = 0;
    private JSONArray timelineItems;
    private JSONArray graphItems;
    private ResultLogger rlog;

    Logger logger = Util.setupLogger(BackendDatabaseSeedingThread.class);

    public BackendDatabaseSeedingThread(int userCount, JSONArray timelineItems,
            JSONArray graphItems, ResultLogger rlog) {
        this.userCount = userCount;
        this.timelineItems = timelineItems;
        this.graphItems = graphItems;
        this.rlog = rlog;
    }

    public void run() {

        ProfileData profile = DefaultValues.DefaultProfile();

        logger.info(" ===============  User " + userCount + " =================");
        String email = MVPApi.generateUniqueEmail();
        long temp = System.currentTimeMillis();
        udid = temp + "" + temp + "" + temp + "" + temp;
        // sign up first
        long s1 = System.currentTimeMillis();
        AccountResult r = MVPApi.signUp(email, password, udid);
        long s2 = System.currentTimeMillis();
        String token = r.token;
        Assert.assertTrue(r.isOK(), "Status code is not 200: " + r.statusCode);

        // // sign out then
        // long s3 = System.currentTimeMillis();
        // MVPApi.signOut(token);
        // long s4 = System.currentTimeMillis();
        //
        // // sign in
        // long s5 = System.currentTimeMillis();
        // r = MVPApi.signIn(email, "misfit1", udid);
        // long s6 = System.currentTimeMillis();
        // token = r.token;
        // Assert.assertTrue(r.isOK(), "Status code is not 200: " +
        // r.statusCode);

        // createProfile
        long s7 = System.currentTimeMillis();
        ProfileResult result = MVPApi.createProfile(token, profile);
        long s8 = System.currentTimeMillis();
        Assert.assertTrue(result.isOK(), "Status code is not 200: " + result.statusCode);

        // get Profile
        // long s9 = System.currentTimeMillis();
        // result = MVPApi.getProfile(token);
        // long s10 = System.currentTimeMillis();
        //
        // // update profile
        // ProfileData newProfile = result.profile;
        // newProfile.name = profile.name + "_new";
        // newProfile.updatedAt += 100;
        // long s11 = System.currentTimeMillis();
        // result = MVPApi.updateProfile(token, newProfile,
        // profile.serverId);
        // long s12 = System.currentTimeMillis();
        // Assert.assertTrue(result.isExisted(), "Status code is not 210: "
        // + result.statusCode);
        //
        //

        // create timeline items and graph items
        // generate timeline items
        long s13 = System.currentTimeMillis();
        ServiceResponse response = MVPApi.createTimelineItems(token, timelineItems);
        long s14 = System.currentTimeMillis();
        Assert.assertTrue(response.getStatusCode() <= 210, "Status code is > 210: " + response.getStatusCode());

        long s15 = System.currentTimeMillis();
        response = MVPApi.createGraphItems(token, graphItems);
        long s16 = System.currentTimeMillis();
        Assert.assertTrue(response.getStatusCode() <= 210, "Status code is > 210: " + response.getStatusCode());
        System.out.println("LOG [BackendStressTestThread.run]: ------------------------------------ DONE");
        
        rlog.log((userCount + 1) + "\t" + (s2 - s1) + "\t"
        // + (s4 - s3) + "\t"
        // + (s6 - s5) + "\t" + (s8 - s7) + "\t"
        // + (s10 - s9) + "\t" + (s12 - s11) + "\t"
                + (s14 - s13) + "\t" + (s16 - s15) + "\t" + email);

    }

}