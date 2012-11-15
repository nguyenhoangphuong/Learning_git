package com.misfit.ta.backend.rest.signin;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.misfit.ta.backend.data.AuthToken;
import com.misfit.ta.backend.data.SignInData;
import com.misfit.ta.backend.data.SyncData;
import com.misfit.ta.backend.rest.MVPRest;

public class SignInRest extends MVPRest {

    public SignInRest(SignInData userData) {
        // parent constructor
        super(userData);
        // api root
        apiUrl = "login/";
    }

    @Override
    public void formatRequest() {
    	
        SignInData data = (SignInData) requestObj;
        params.add("email", data.email);
        params.add("password", data.password);
    }

    @Override
    public void formatResponse() {

        System.out.println("LOG [SignInRest.formatResponse]: " + contentData);
        JSONObject json = (JSONObject) JSONSerializer.toJSON(contentData.toString());

        try {
        	
            String token = json.getString("auth_token");
            String type = json.getString("type");
            
            String objects = json.getString("objects");
            Long timestamp = json.getLong("last_successfully_synced");
            SyncData data = new SyncData(timestamp, objects);
            
            responseObj = new AuthToken(token, type, data);
        } 
        catch (Exception e) {
            responseObj = new AuthToken("", "", null);
        }

    }
}
