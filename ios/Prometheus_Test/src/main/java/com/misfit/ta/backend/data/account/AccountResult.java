package com.misfit.ta.backend.data.account;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONException;
import com.misfit.ta.backend.data.BaseResult;

public class AccountResult extends BaseResult {
    // fields
    public String token;
    public String type;

    // constructor
    public AccountResult(ServiceResponse response) {
        super(response);

        // result

        try {
            if (!json.isNull("auth_token"))
                this.token = json.getString("auth_token");
            if (!json.isNull("type"))
                this.type = json.getString("type");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // add to base hashmap
        this.pairResult.put("auth_token", this.token);
        this.pairResult.put("type", this.type);
    }

}
