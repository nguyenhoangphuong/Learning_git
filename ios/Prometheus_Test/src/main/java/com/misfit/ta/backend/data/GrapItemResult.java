package com.misfit.ta.backend.data;

import com.google.resting.component.impl.ServiceResponse;

public class GrapItemResult extends BaseResult {
    // fields
    public Object timestamp;
    public Object clientId;
    public Object averageValue;
    public Object userId;
    public Object updatedAt;
    public Object createdAt;

    // constructor
    public GrapItemResult(ServiceResponse response) {
        super(response);
        
        // results
        this.timestamp = json.getString("timestamp");
        this.clientId = json.getString("client_id");
        this.averageValue = json.getString("average_value");
        this.userId = json.getString("user_id");
        this.updatedAt = json.getString("updated_at");
        this.createdAt = json.getString("created_at");
        
        // add to base hashmap
        this.pairResult.put("timestamp", this.timestamp);
        this.pairResult.put("client_id", this.clientId);
        this.pairResult.put("average_value", this.averageValue);
        this.pairResult.put("user_id", this.userId);
        this.pairResult.put("updated_at", this.updatedAt);
        this.pairResult.put("created_at", this.createdAt);
    }
}
