package com.misfit.ta.backend.data;

import com.google.resting.component.impl.ServiceResponse;

public class AccountResult extends BaseResult {
    // fields
    public String token;
    public String type;

    // constructor
    public AccountResult(ServiceResponse response) {
        super(response);

        // result
        if (json.containsKey("auth_token"))
        	this.token = json.getString("auth_token");
        
        if (json.containsKey("type"))
        	this.type = json.getString("type");

        // add to base hashmap
        this.pairResult.put("auth_token", this.token);
        this.pairResult.put("type", this.type);
    }

}
