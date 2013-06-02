package com.misfit.ta.backend.aut;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.resting.json.JSONArray;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.data.ProfileData;
import com.misfit.ta.backend.data.graph.GraphItem;
import com.misfit.ta.backend.data.timeline.ActivitySessionItem;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.TimelineItemBase;
import com.misfit.ta.backend.data.timeline.WeatherItem;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class BackendStressTest extends BackendAutomation {

    private String password = "misfit1";
    private String udid;

    private static int NUMBER_OF_ITEMS_PER_DAY = 1;
    private static int NUMBER_OF_DAYS = 4;
    private static int NUMBER_OF_USERS = 1;
    private static int NUMBER_OF_THREADS=10;

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
        tmp = Settings.getParameter("NUMBER_OF_THREADS");
        if (tmp != null && !tmp.isEmpty()) {
            NUMBER_OF_THREADS = Integer.parseInt(tmp);
        }

        logger.info("Params: \n" + "NUMBER_OF_USERS: " + NUMBER_OF_USERS + "\n" 
                                + "NUMBER_OF_DAYS: " + NUMBER_OF_DAYS + "\n" 
                                + "NUMBER_OF_ITEMS_PER_DAY: " + NUMBER_OF_ITEMS_PER_DAY + "\n"
                                + "NUMBER_OF_THREADS: " + NUMBER_OF_THREADS + "\n");
    }

    @Test(groups = { "backend_stress" })
    public void signupOneMillionUsers() {

        ResultLogger rlog = ResultLogger.getLogger("signup_a_million_users");
        rlog.log("Number of try\t" + "signUpTime\t" + "signOutTime\t" + "signInTime\t" + "createProfileTime\t"
                + "getProfileTime\t" + "updateProfileTime\t" + "addTimelineItems\t" + "addGraphItems\t" + "email");
        
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
        

        int userCount = 0;
        
        //---------------------------------
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        
        while (userCount < NUMBER_OF_USERS) {
            for (int threads=0; threads < NUMBER_OF_THREADS; threads++) {
                BackendStressTestThread test = new BackendStressTestThread(userCount, timelineItems, graphItems, rlog, this);
                executor.execute(test);
                
            }
            userCount++;
            System.out.println("LOG [BackendStressTest.signupOneMillionUsers]: adding more thread here: " +userCount);
        }
        executor.shutdown();
    }
    
    public void testThreadDone() {
        
    }
}
