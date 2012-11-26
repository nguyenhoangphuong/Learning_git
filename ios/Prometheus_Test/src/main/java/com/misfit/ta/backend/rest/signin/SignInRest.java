package com.misfit.ta.backend.rest.signin;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.misfit.ta.backend.data.AuthToken;
import com.misfit.ta.backend.data.SignInData;
import com.misfit.ta.backend.data.SyncData;
import com.misfit.ta.backend.rest.MVPRest;

public class SignInRest extends MVPRest {

    private Logger logger = Util.setupLogger(MVPRest.class);

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

        logger.debug("Response content: " + contentData);
        try {
            JSONObject json = (JSONObject) JSONSerializer.toJSON(contentData.toString());
        	
            String token = json.getString("auth_token");
            String type = json.getString("type");
            
            String objects = json.getString("objects");
            Long timestamp = json.getLong("last_successfully_synced");
            SyncData data = new SyncData(timestamp, objects);
            
            responseObj = new AuthToken(token, type, data);
            
            if (response.getStatusCode() !=200) {
                System.out.println("\n\n\nLOG [SignInRest.formatResponse]: =========================== ERROR");
            }
        } 
        catch (Exception e) {
            responseObj = new AuthToken("", "", null);
        }

    }
}
