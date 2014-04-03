package com.misfit.ta.backend.aut.performance.backendapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.goal.ProgressData;
import com.misfit.ta.backend.data.goal.TripleTapData;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.profile.ProfileResult;
import com.misfit.ta.backend.data.sync.SyncLog;
import com.misfit.ta.utils.TextTool;

public class BackendSyncSimulationThread implements Runnable {

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
    private long sGetUnlink, sGetUnlink1, sCreateGoal, sCreateGoal1, sGetGoal, sGetGoal1, sUpdateGoal, sUpdateGoal1, sSearchGoal, sSearchGoal1, sSyncLog, sSyncLog1;
    
    private boolean randomized = false;
    private long userRequestTime;
    private long countRequest;
    private String mySerial = TextTool.getRandomString(5, 6) + System.currentTimeMillis();
    

    Logger logger = Util.setupLogger(BackendSyncSimulationThread.class);

    public BackendSyncSimulationThread(int userCount, JSONArray timelineItems, JSONArray graphItems, ResultLogger rlog, boolean randomized) {
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
        AccountResult r = MVPApi.signUp(email, password);
         s2 = System.currentTimeMillis();
         ResultLogger.totalTime += s2 - s1;
        token = r.token;
//        Assert.assertTrue(r.isOK(), "Status code is not 200: " + r.statusCode);
        
        
       
        
        int operation = Settings.getInt("OPERATION");
        if (randomized) {
        Random rand = new Random(System.currentTimeMillis());
            operation = (randomized) ? rand.nextInt(6) : -1;
            System.out.println("LOG [BackendDatabaseSeedingThread.run]: random:  " + operation);
        }
        
        System.out.println("LOG [BackendDatabaseSeedingThread.run]: operation: " + operation + " : " + (operation == 0 || operation <= -1));
        
        countRequest = 1;
        if (operation == 0 || operation <= -1) {
            doAccountOperation(email);
        }
        if (operation == 1 || operation <= -1) {
//            doProfileOperation();
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
            doSyncOperation();
        }
    
        
        System.out.println("LOG [BackendStressTestThread.run]: ------------------------------------ DONE");

        userRequestTime = (s2 - s1) 
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
                + (s16 - s15)
                + (sSyncLog1 - sSyncLog);

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
                + (s16 - s15) + "\t" 
                + (sSyncLog1 - sSyncLog) + "\t"
                + email + "\t" 
                + userRequestTime + "\t"
                + countRequest + "\t"
                + (String.valueOf(userRequestTime/countRequest))
                );
    }
    
    public void doAccountOperation(String email) {
        // // sign out then
        s3 = System.currentTimeMillis();
//       BaseResult br = MVPApi.signOut(token);
        s4 = System.currentTimeMillis();
        ResultLogger.totalTime += s4 - s3;
//        Assert.assertTrue(br.isOK(), "Status code is not 200: " + br.statusCode);
       
       // // sign in
        s5 = System.currentTimeMillis();
//       AccountResult r = MVPApi.signIn(email, "misfit1", udid);
        s6 = System.currentTimeMillis();
        ResultLogger.totalTime += s6 - s5;
//       token = r.token;
       countRequest += 2;
//       Assert.assertTrue(r.isOK(), "Status code is not 200: " + r.statusCode);
    }
    public void doProfileOperation() {
        ProfileData profile = DefaultValues.DefaultProfile();
        // createProfile
        s7 = System.currentTimeMillis();
        ProfileResult result = MVPApi.createProfile(token, profile);
        s8 = System.currentTimeMillis();
        ResultLogger.totalTime += s8 - s7;
//        Assert.assertTrue(result.isOK(), "Status code is not 200: " + result.statusCode);
        

       // get Profile
        s9 = System.currentTimeMillis();
       result = MVPApi.getProfile(token);
        s10 = System.currentTimeMillis();
        ResultLogger.totalTime += s10 - s9;
//        Assert.assertTrue(result.isOK(), "Status code is not 200: " + result.statusCode);

       // update profile
       ProfileData newProfile = result.profile;
       newProfile.setWeight(profile.getWeight() + 1);
//       newProfile.updatedAt += 100;
       s11 = System.currentTimeMillis();
       result = MVPApi.updateProfile(token, newProfile);
       s12 = System.currentTimeMillis();
       ResultLogger.totalTime += s12 - s11;
       countRequest += 3;
//       Assert.assertTrue(result.isExisted(), "Status code is not 210: " + result.statusCode);
    }
    
    public void doPedometerOperations() {
        long now = System.currentTimeMillis()/1000;
        sPedoCreate = System.currentTimeMillis();
        Pedometer pedo = MVPApi.createPedometer(token, mySerial, "hw1234", now, now, now, "localId", null, now);
        sPedoCreate1 = System.currentTimeMillis();
        ResultLogger.totalTime += sPedoCreate1 - sPedoCreate;
//        Assert.assertTrue(pedo != null, "Pedometer can not be created");
        
        
         sPedoShow = System.currentTimeMillis();
//        pedo = MVPApi.showPedometer(token);
         sPedoShow1 = System.currentTimeMillis();
         ResultLogger.totalTime += sPedoShow1 - sPedoShow;
//         Assert.assertTrue(pedo != null, "Can not get pedometer");
        
        
        pedo.setUpdatedAt(System.currentTimeMillis()/1000);
         sPedoUpdate= System.currentTimeMillis();
        pedo = MVPApi.updatePedometer(token, mySerial, "hw1234", now, now, now, "localId", null, now);
         sPedoUpdate1 = System.currentTimeMillis();
         ResultLogger.totalTime += sPedoUpdate1 - sPedoUpdate;
//         Assert.assertTrue(pedo != null, "Pedometer can not be updated");
         countRequest += 3;
    }
    
    public void doLinkinOperation() {
        sGetDevice= System.currentTimeMillis();
        String status = MVPApi.getDeviceLinkingStatus(token,mySerial);
         sGetDevice1 = System.currentTimeMillis();
         ResultLogger.totalTime += sGetDevice1 - sGetDevice;
//         Assert.assertTrue(status != null, "Can not get linking status");
        
         sGetUnlink= System.currentTimeMillis();
//        status = MVPApi.unlinkDevice(token,mySerial);
         sGetUnlink1 = System.currentTimeMillis();
         ResultLogger.totalTime += sGetUnlink1 - sGetUnlink;
//         Assert.assertTrue(status != null, "Can not unlink device");
         countRequest += 2;
    }
    
    public void doGoalOperation() {
//        long now = System.currentTimeMillis();
//        ProgressData progressData = new ProgressData(300, 5000, 1200, 500);
////        sCreateGoal= System.currentTimeMillis();
////        GoalsResult goalResult = MVPApi.createGoal(token, 2500, now, now + 8400, 
////        		0, progressData, new ArrayList<TripleTapData>(), "mylocalid", now);
//        sCreateGoal1 = System.currentTimeMillis();
//        ResultLogger.totalTime += sCreateGoal1 - sCreateGoal;
////        Assert.assertTrue(goalResult.isOK(), "Status code is not 200: " + goalResult.statusCode);
//        
//        sGetGoal= System.currentTimeMillis();
////        goalResult = MVPApi.getGoal(token, goalResult.goals[0].getServerId());
//        sGetGoal1 = System.currentTimeMillis();
//        ResultLogger.totalTime += sGetGoal1 - sGetGoal;
////        Assert.assertTrue(goalResult.isOK(), "Status code is not 200: " + goalResult.statusCode);
//        
//        sSearchGoal= System.currentTimeMillis();
////        goalResult = MVPApi.searchGoal(token, now, now + 8400, now);
//        sSearchGoal1 = System.currentTimeMillis();
//        ResultLogger.totalTime += sSearchGoal1 - sSearchGoal;
////        Assert.assertTrue(goalResult.isOK(), "Status code is not 200: " + goalResult.statusCode);
//        
//        sUpdateGoal= System.currentTimeMillis();
//        goalResult = MVPApi.updateGoal(token, now + 234, goalResult.goals[0].getServerId(), 
//        		2500, now, now + 8400, 0, progressData, new ArrayList<TripleTapData>(), "mylocalid");
//        sUpdateGoal1 = System.currentTimeMillis();
//        ResultLogger.totalTime += sUpdateGoal1 - sUpdateGoal;
//        Assert.assertTrue(goalResult.isExisted(), "Status code is not 210: " + goalResult.statusCode);
        
        
        countRequest += 2;
    }
    
    public void doTimelineOperation() {
        
        // create timeline items and graph items
        // generate timeline items
         s13 = System.currentTimeMillis();
        ServiceResponse response = MVPApi.createTimelineItems(token, timelineItems);
         s14 = System.currentTimeMillis();
         ResultLogger.totalTime += s14 - s13;
//        Assert.assertTrue(response.getStatusCode() <= 210, "Status code is > 210: " + response.getStatusCode());
    
         s15 = System.currentTimeMillis();
        response = MVPApi.createGraphItems(token, graphItems);
         s16 = System.currentTimeMillis();
         ResultLogger.totalTime += s16 - s15;
         countRequest += 2;
//        Assert.assertTrue(response.getStatusCode() <= 210, "Status code is > 210: " + response.getStatusCode());
        
    }
    
    public void doSyncOperation() {
  
        sSyncLog = System.currentTimeMillis();
        SyncLog log = DataGenerator.generateRandomSyncLog(System.currentTimeMillis() / 1000, 1, 60, null);
		MVPApi.pushSyncLog(token, log);
        sSyncLog1 = System.currentTimeMillis();
        countRequest += 1;
    }
}
