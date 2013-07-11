package com.misfit.ta.backend.aut.performance;

import java.util.Random;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.AccountResult;
import com.misfit.ta.backend.data.GoalsResult;
import com.misfit.ta.backend.data.ProfileData;
import com.misfit.ta.backend.data.ProfileResult;
import com.misfit.ta.backend.data.goal.ProgressData;
import com.misfit.ta.backend.data.pedometer.Pedometer;

public class BackendDatabaseSeedingThread implements Runnable {

    private String password = "misfit1";
    private String udid;

    private int userCount = 0;
    private JSONArray timelineItems;
    private JSONArray graphItems;
    private ResultLogger rlog;
    
    private String token;
    
    private long s1, s2, s3, s4, s5, s6, s7, s8;
    private long s9, s10, s11, s12, s13, s14, s15, s16; 
    private long sPedoCreate, sPedoCreate1, sPedoShow1, sPedoShow, sPedoUpdate, sPedoUpdate1, sGetDevice1, sGetDevice; 
    private long sGetUnlink, sGetUnlink1, sCreateGoal, sCreateGoal1, sGetGoal, sGetGoal1, sUpdateGoal, sUpdateGoal1, sSearchGoal, sSearchGoal1;
    
    private boolean randomized = false;
    private long totalTime;
    private long countRequest;
    

    Logger logger = Util.setupLogger(BackendDatabaseSeedingThread.class);

    public BackendDatabaseSeedingThread(int userCount, JSONArray timelineItems, JSONArray graphItems, ResultLogger rlog, boolean randomized) {
        this.userCount = userCount;
        this.timelineItems = timelineItems;
        this.graphItems = graphItems;
        this.rlog = rlog;
        this.randomized = randomized;
    }
    
    public void run() {


        logger.info(" ===============  User " + userCount + " =================");
        String email = MVPApi.generateUniqueEmail();
        long temp = System.currentTimeMillis();
        udid = temp + "" + temp + "" + temp + "" + temp;
        // sign up first
         s1 = System.currentTimeMillis();
        AccountResult r = MVPApi.signUp(email, password, udid);
         s2 = System.currentTimeMillis();
        token = r.token;
        Assert.assertTrue(r.isOK(), "Status code is not 200: " + r.statusCode);
        
        
        
        int operation = -1;
        if (randomized) {
        Random rand = new Random(System.currentTimeMillis());
            operation = (randomized) ? rand.nextInt(6) : -1;
            System.out.println("LOG [BackendDatabaseSeedingThread.run]: random:  " + operation);
        }
        
        System.out.println("LOG [BackendDatabaseSeedingThread.run]: operation: " + operation + " : " + (operation == 0 || operation <= -1));
        
        if (operation == 0 || operation <= -1) {
            doAccountOperation(email);
        }
        if (operation == 1 || operation <= -1) {
            doProfileOperation();
        }
        
        if (operation == 2 || operation <= -1) {
            doPedometerOperations();
        }
        
        if (operation == 3 || operation <= -1) {
            doLinkinOperation();
        }
        
        if (operation == 4 || operation <= -1) {
            doGoalOperation();
        }
        
        if (operation == 5 || operation <= -1) {
            doTimelineOperation();
        }
    
        
        System.out.println("LOG [BackendStressTestThread.run]: ------------------------------------ DONE");

        
        rlog.log((userCount + 1) + "\t" 
                + (s2 - s1) + "\t" 
                + (s4 - s3) + "\t"
                + (s6 - s5) + "\t" 
                + (s8 - s7) + "\t"
                + (s10 - s9) + "\t" 
                + (s12 - s11) + "\t"
                + (sPedoCreate1 - sPedoCreate) + "\t"
                + (sPedoShow1 - sPedoShow) + "\t"
                
                + (sPedoUpdate1 - sPedoUpdate) + "\t"
                
                + (sGetDevice1- sGetDevice) + "\t"
                + (sGetUnlink1- sGetUnlink) + "\t"
                + (sCreateGoal1- sCreateGoal) + "\t"
                + (sGetGoal1- sGetGoal) + "\t"
                + (sUpdateGoal1- sUpdateGoal) + "\t"
                + (sSearchGoal1- sSearchGoal) + "\t"
                
                + (s14 - s13) + "\t"
                + (s16 - s15) + "\t" + email);
        totalTime = (s2 - s1) 
        		+ (s4 - s3)      
        		+ (s6 - s5) 
                + (s8 - s7) 
                + (s10 - s9) 
                + (s12 - s11) 
                + (sPedoCreate1 - sPedoCreate) 
                + (sPedoShow1 - sPedoShow) 
                + (sPedoUpdate1 - sPedoUpdate)
                + (sGetDevice1- sGetDevice) 
                + (sGetUnlink1- sGetUnlink) 
                + (sCreateGoal1- sCreateGoal) 
                + (sGetGoal1- sGetGoal) 
                + (sUpdateGoal1- sUpdateGoal) 
                + (sSearchGoal1- sSearchGoal) 
                + (s14 - s13) 
                + (s16 - s15);
        rlog.log("---Total time: " + totalTime);
        rlog.log("---Request count: " + countRequest);
    }
    
    public void doAccountOperation(String email) {
        // // sign out then
        s3 = System.currentTimeMillis();
       MVPApi.signOut(token);
        s4 = System.currentTimeMillis();
       //
       // // sign in
        s5 = System.currentTimeMillis();
       AccountResult r = MVPApi.signIn(email, "misfit1", udid);
        s6 = System.currentTimeMillis();
       token = r.token;
       countRequest = 2;
       Assert.assertTrue(r.isOK(), "Status code is not 200: " + r.statusCode);
    }
    public void doProfileOperation() {
        ProfileData profile = DefaultValues.DefaultProfile();
        // createProfile
        s7 = System.currentTimeMillis();
       ProfileResult result = MVPApi.createProfile(token, profile);
        s8 = System.currentTimeMillis();
       Assert.assertTrue(result.isOK(), "Status code is not 200: " + result.statusCode);

       // get Profile
        s9 = System.currentTimeMillis();
       result = MVPApi.getProfile(token);
        s10 = System.currentTimeMillis();

       // // update profile
       ProfileData newProfile = result.profile;
       newProfile.name = profile.name + "_new";
       newProfile.updatedAt += 100;
       s11 = System.currentTimeMillis();
       result = MVPApi.updateProfile(token, newProfile, profile.serverId);
       s12 = System.currentTimeMillis();
       countRequest = 3;
       Assert.assertTrue(result.isOK(), "Status code is not 200: " + result.statusCode);
    }
    
    public void doPedometerOperations() {
        long now = System.currentTimeMillis()/1000;
        sPedoCreate = System.currentTimeMillis();
        Pedometer pedo = MVPApi.createPedometer(token, "myserial", "hw1234", now, now, now, "localId", null, now);
        sPedoCreate1 = System.currentTimeMillis();
        
        
         sPedoShow = System.currentTimeMillis();
        pedo = MVPApi.showPedometer(token);
         sPedoShow1 = System.currentTimeMillis();
        
        
        pedo.setUpdatedAt(System.currentTimeMillis()/1000);
         sPedoUpdate= System.currentTimeMillis();
        pedo = MVPApi.updatePedometer(token, "myserial", "hw1234", now, now, now, "localId", null, now);
         sPedoUpdate1 = System.currentTimeMillis();
         countRequest = 3;
    }
    
    public void doLinkinOperation() {
        sGetDevice= System.currentTimeMillis();
        String status = MVPApi.getDeviceLinkingStatus(token,"myserial");
         sGetDevice1 = System.currentTimeMillis();
        
         sGetUnlink= System.currentTimeMillis();
        status = MVPApi.unlinkDevice(token,"myserial");
         sGetUnlink1 = System.currentTimeMillis();
         countRequest = 2;
    }
    
    public void doGoalOperation() {
        long now = System.currentTimeMillis();
        Vector<Integer> points = new Vector<Integer>();
        points.add(0);
        points.add(1);
        points.add(2);
        ProgressData progressData = new ProgressData(points, 100, 200);
        sCreateGoal= System.currentTimeMillis();
        GoalsResult goalResult = MVPApi.createGoal(token, 2500, now, now + 8400, 3, 2, 0, progressData, "mylocalid");
        sCreateGoal1 = System.currentTimeMillis();
        
        sGetGoal= System.currentTimeMillis();
        MVPApi.getGoal(token, goalResult.goals[0].getServerId());
        sGetGoal1 = System.currentTimeMillis();
        
        long sUpdateGoal= System.currentTimeMillis();
        MVPApi.updateGoal(token, now + 234, 2500, now, now + 8400, 3, 2, 0, progressData, "mylocalid");
        long sUpdateGoal1 = System.currentTimeMillis();
        
        sSearchGoal= System.currentTimeMillis();
        MVPApi.searchGoal(token, now, now + 8400, now);
        sSearchGoal1 = System.currentTimeMillis();
        countRequest = 4;
    }
    
    public void doTimelineOperation() {
        
        // create timeline items and graph items
        // generate timeline items
         s13 = System.currentTimeMillis();
        ServiceResponse response = MVPApi.createTimelineItems(token, timelineItems);
         s14 = System.currentTimeMillis();
        Assert.assertTrue(response.getStatusCode() <= 210, "Status code is > 210: " + response.getStatusCode());
    
         s15 = System.currentTimeMillis();
        response = MVPApi.createGraphItems(token, graphItems);
         s16 = System.currentTimeMillis();
         countRequest = 2;
        Assert.assertTrue(response.getStatusCode() <= 210, "Status code is > 210: " + response.getStatusCode());
        
    }
}
