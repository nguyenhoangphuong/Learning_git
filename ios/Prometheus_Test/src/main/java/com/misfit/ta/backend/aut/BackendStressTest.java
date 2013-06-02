package com.misfit.ta.backend.aut;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.AccountResult;
import com.misfit.ta.backend.data.ProfileData;
import com.misfit.ta.backend.data.ProfileResult;
import com.misfit.ta.backend.data.graph.GraphItem;
import com.misfit.ta.backend.data.timeline.ActivitySessionItem;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.TimelineItemBase;
import com.misfit.ta.backend.data.timeline.WeatherItem;
import com.misfit.ta.utils.TextTool;

public class BackendStressTest extends BackendAutomation {

    private String password = "misfit1";
    private String udid;

    private static int NUMBER_OF_ITEMS_PER_DAY = 1;
    private static int NUMBER_OF_DAYS = 4;
    private static int NUMBER_OF_USERS = 1;

    Logger logger = Util.setupLogger(BackendStressTest.class);

    @Override
    @BeforeMethod(alwaysRun = true)
    public void setUpTest(Method method) {
        super.setUpTest(method);
        String tmp = Settings.getParameter("NUMBER_OF_ITEMS_PER_DAY");
        if (tmp != null && !tmp.isEmpty()) {
            NUMBER_OF_ITEMS_PER_DAY = Integer.parseInt(tmp);
        }
        tmp = Settings.getParameter("NUMBER_OF_DAYS");
        if (tmp != null && !tmp.isEmpty()) {
            NUMBER_OF_DAYS = Integer.parseInt(tmp);
        }
        tmp = Settings.getParameter("NUMBER_OF_USERS");
        if (tmp != null && !tmp.isEmpty()) {
            NUMBER_OF_USERS = Integer.parseInt(tmp);
        }

        logger.info("Params: \n" + "NUMBER_OF_USERS: " + NUMBER_OF_USERS + "\n" + "NUMBER_OF_DAYS: " + NUMBER_OF_DAYS
                + "\n" + "NUMBER_OF_ITEMS_PER_DAY: " + NUMBER_OF_ITEMS_PER_DAY + "\n");
    }

    @Test(groups = { "backend_stress" })
    public void signupOneMillionUsers() {

        ResultLogger rlog = ResultLogger.getLogger("signup_a_million_users");
        rlog.log("Number of try\t" + "signUpTime\t" + "signOutTime\t" + "signInTime\t" + "createProfileTime\t"
                + "getProfileTime\t" + "updateProfileTime\t" + "addTimelineItems\t" + "addGraphItems\t" + "email");
        ProfileData profile = DefaultValues.DefaultProfile();
        
        
        long now = System.currentTimeMillis() / 1000;

        JSONArray timelineItems = new JSONArray();
        JSONArray graphItems = new JSONArray();

        // data for 30 day
        for (int k = 0; k < NUMBER_OF_DAYS; k++) {
            long tmp = now - (k * 3600 * 24);
            // one weather per day
            WeatherItem weather = new WeatherItem(tmp, 100, 200, "Stockholm");
            TimelineItem timeline = new TimelineItem(TimelineItemBase.TYPE_WEATHER, tmp, tmp, weather, TextTool
                    .getRandomString(19, 20), null, null);
            timelineItems.put(timeline.toJson());

            for (int j = 0; j < NUMBER_OF_ITEMS_PER_DAY; j++) {
                // one timeline item per hour
                tmp -= 3600;

                ActivitySessionItem session = new ActivitySessionItem(tmp, 2222, 22, tmp, 22, 2, 22, 22);
                TimelineItem tmpItem = new TimelineItem(TimelineItemBase.TYPE_SESSION, tmp, tmp, session, TextTool
                        .getRandomString(19, 20), null, null);
                timelineItems.put(tmpItem.toJson());

                // one graph item per hour
                GraphItem graphItem = new GraphItem(tmp, 50, TextTool.getRandomString(19, 20), tmp);
                graphItems.put(graphItem.toJson());

            }

        }
        

        for (int i = 0; i < NUMBER_OF_USERS; i++) {
            logger.info(" ===============  User " + i + " =================");
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

            rlog.log((i + 1) + "\t" + (s2 - s1) + "\t"
            // + (s4 - s3) + "\t"
            // + (s6 - s5) + "\t" + (s8 - s7) + "\t"
            // + (s10 - s9) + "\t" + (s12 - s11) + "\t"
             + (s14 - s13) + "\t" + (s16 - s15) + "\t"
                    + email);

        }
    }
}
