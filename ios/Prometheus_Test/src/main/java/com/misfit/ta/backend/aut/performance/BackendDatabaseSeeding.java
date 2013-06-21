package com.misfit.ta.backend.aut.performance;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.annotations.Test;

import com.google.resting.json.JSONArray;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.ResultLogger;

public class BackendDatabaseSeeding {

    private static int NUMBER_OF_ITEMS_PER_DAY = 1;
    private static int NUMBER_OF_DAYS = 1;
    private static int NUMBER_OF_USERS = 20;
    private static int NUMBER_OF_THREADS = 2;

    Logger logger = Util.setupLogger(BackendDatabaseSeeding.class);

    public void setUpTest() {
        NUMBER_OF_ITEMS_PER_DAY = Settings.getInt("NUMBER_OF_ITEMS_PER_DAY");
        NUMBER_OF_DAYS = Settings.getInt("NUMBER_OF_DAYS");
        NUMBER_OF_USERS = Settings.getInt("NUMBER_OF_USERS");
        NUMBER_OF_THREADS = Settings.getInt("NUMBER_OF_THREADS");
    }

    @Test(groups = { "backend_stress" })
    public void signupOneMillionUsers() {
        setUpTest();


        logger.info("Params: \n" + "NUMBER_OF_USERS: " + NUMBER_OF_USERS + "\n" + "NUMBER_OF_DAYS: " + NUMBER_OF_DAYS
                + "\n" + "NUMBER_OF_ITEMS_PER_DAY: " + NUMBER_OF_ITEMS_PER_DAY + "\n" + "NUMBER_OF_THREADS: "
                + NUMBER_OF_THREADS + "\n");

        ResultLogger rlog = ResultLogger.getLogger("signup_a_million_users_" 
                    + NUMBER_OF_THREADS+"thread_" 
                    + (NUMBER_OF_ITEMS_PER_DAY * NUMBER_OF_DAYS) + "record_" + NUMBER_OF_USERS + "user");
        rlog.log("Params: \n" + "NUMBER_OF_USERS: " + NUMBER_OF_USERS + "\n" + "NUMBER_OF_DAYS: " + NUMBER_OF_DAYS
                + "\n" + "NUMBER_OF_ITEMS_PER_DAY: " + NUMBER_OF_ITEMS_PER_DAY + "\n" + "NUMBER_OF_THREADS: "
                + NUMBER_OF_THREADS + "\n");
        rlog.log("Number of try\t" 
                + "signUpTime\t" 
                + "signOut\t"
                + "signIn\t"
                + "createProfile\t"
                + "getProfile\t"
                + "updateProfile\t"
                + "createPedo\t"
                + "showPedo\t"
                + "updatePedo\t"
                + "getLinkedDevice\t"
                + "unlinkDevice\t"
                + "createGoal\t"
                + "getGoal\t"
                + "updateGoal\t"
                + "searchGoal\t"
                + "addTimelineItems\t" 
                + "addGraphItems\t" + "email");

        JSONArray timelineItems = new JSONArray();
        JSONArray graphItems = new JSONArray();

        JSONArray[] array = MVPApi.generateTimelineItemsAndGraphItems();
        timelineItems = array[0];
        graphItems = array[1];

        int userCount = 0;

        // // ---------------------------------
        
        Collection<Future<?>> futures = new LinkedList<Future<?>>();
        
      
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        while (userCount < NUMBER_OF_USERS) {
            for (int threads = 0; threads < NUMBER_OF_THREADS; threads++) {
                BackendDatabaseSeedingThread test = new BackendDatabaseSeedingThread(userCount, timelineItems,
                        graphItems, rlog);
                futures.add(executor.submit(test));
                userCount++;
            }
        }
        executor.shutdown();
        
        for (Future<?> future:futures) {
            try {
                future.get();
            } catch (Exception e) {
            }
        }
        ResultLogger.logErrorSummary();
        System.out.println("LOG [BackendDatabaseSeeding.enclosing_method]:--------------------------------dadad ");
    }

    public static void main(String[] args) {
        BackendDatabaseSeeding test = new BackendDatabaseSeeding();
        test.signupOneMillionUsers();
    }

}
