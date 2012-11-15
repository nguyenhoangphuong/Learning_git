package com.misfit.ta.backend.rest.profile;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.misfit.ta.backend.data.AuthToken;
import com.misfit.ta.backend.data.SyncData;
import com.misfit.ta.backend.rest.MVPRest;

public class SyncRest extends MVPRest {

    public SyncRest(SyncData syncData) {
        super(syncData);
        apiUrl = "sync/";
        System.out.println("LOG [SyncRest.SyncRest]: ---------");
    }

    @Override
    public void formatRequest() {
        SyncData syncData = (SyncData) requestObj;
//        syncData.activities = null;
//        syncData.goals =null;
//        syncData.plans = null;
//        syncData.timeFrames = null;
//
////        JSON content = JSONSerializer.toJSON(syncData);
        
        params.add("lastSuccessfullySynced", "0");
        params.add("objects", Data.profile);
    }

    @Override
    public void formatResponse() {

//        System.out.println("LOG [SignInRest.formatResponse]: " + contentData);
        JSONObject json = (JSONObject) JSONSerializer.toJSON(contentData.toString());

        try {
            String objects = json.getString("objects");
            long lastSuccessfullySync = json.getLong("last_successfully_synced");
            System.out.println("LOG [SyncRest.formatResponse]: " + lastSuccessfullySync);
            System.out.println("LOG [SyncRest.formatResponse]: objects: " + objects);
//            Profile profile = 
        } catch (Exception e) {
            responseObj = new AuthToken("", "");
        }

    }
}
