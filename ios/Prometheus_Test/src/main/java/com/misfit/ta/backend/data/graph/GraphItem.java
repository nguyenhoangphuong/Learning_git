package com.misfit.ta.backend.data.graph;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class GraphItem {

    private long timestamp;
    private double averageValue;
    private String localId;
    private long updatedAt;

    public GraphItem(long timestamp, double averageValue, String localId, long updatedAt) {
        this.timestamp = timestamp;
        this.averageValue = averageValue;
        this.localId = localId;
        this.updatedAt = updatedAt;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(double averageValue) {
        this.averageValue = averageValue;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.accumulate("averageValue", averageValue);
            object.accumulate("localId", localId);
            object.accumulate("updatedAt", updatedAt);
            object.accumulate("timestamp", timestamp);
            return object;
            
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
