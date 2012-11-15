package com.misfit.ta.backend.rest.signin;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.misfit.ta.backend.data.AuthToken;
import com.misfit.ta.backend.data.SignInData;
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
            responseObj = new AuthToken(token, type);
        } catch (Exception e) {
            responseObj = new AuthToken("", "");
        }

    }
}
