package com.misfit.ta.backend.rest;

import net.sf.json.*;

import com.misfit.ta.backend.data.SyncData;

public class SyncRest extends MVPRest {

    public SyncRest(SyncData syncData) {
        super(syncData);
        apiUrl = "sync/";
    }

    @Override
    public void formatRequest() {
    	
        SyncData syncData = (SyncData) requestObj;
        
        params.add("lastSuccessfullySynced", syncData.timestamp.toString());
        params.add("objects", syncData.objects);
    }

    @Override
    public void formatResponse() {
    	
    	System.out.println("LOG [SyncRest.formatResponse]: " + contentData);
        JSONObject json = (JSONObject) JSONSerializer.toJSON(contentData.toString());
        
        // get objects
        String s = contentData.toString();
        int start = s.indexOf("{\"profile\":");
        int end = s.indexOf("\"last_successfully_synced\":");
        
        try {
        	
            //String objects = json.getString("objects");
        	String objects = s.substring(start, end - 1);
            Long timestamp = json.getLong("last_successfully_synced");
            responseObj = new SyncData(timestamp, objects);
        } 
        catch (Exception e) {
            responseObj = new SyncData(0, "");
        }

    }
}