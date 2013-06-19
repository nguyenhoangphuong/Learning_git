package com.misfit.ta.backend.data;

import java.util.ArrayList;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class ActivityResult extends BaseResult {
    // fields
    public Object startTime;
    public Object endTime;
    public Object modifiedSince;
    public Object timezoneOffset;
    public Object activityType;
    public Object bipedalCount;
    public Object intensityLevel;

    // methods
    /**
     * Get ActivityResult, given its index, from a list. This list is wrapped by
     * a ServiceResponse object which is known for sure to be gotten by
     * searchActivities
     * 
     * @param response
     *            the response from searchActivities
     * @param index
     *            activity index on the list within response
     * @return ActivityResult
     * @throws JSONException
     */
    public static ArrayList<ActivityResult> getActivityResults(ServiceResponse response) throws JSONException {
        ArrayList<ActivityResult> activities = new ArrayList<ActivityResult>();
        JSONObject responseBody = new JSONObject(response.getResponseString());
        JSONArray jsonActivities = responseBody.getJSONArray("activities");

        for (int i = 0; i < jsonActivities.length(); i++) {
            ActivityResult activity = ActivityResult.getFields(jsonActivities.getJSONObject(i), response);

            activities.add(activity);
        }

        return activities;
    }

    protected static ActivityResult getFields(JSONObject jsonActivity, ServiceResponse response) throws JSONException {
        ActivityResult activity = new ActivityResult(response);

        activity.startTime = jsonActivity.get("startTime");
        activity.endTime = jsonActivity.get("endTime");
        activity.modifiedSince = jsonActivity.get("modifiedSince");
        activity.timezoneOffset = jsonActivity.get("timezoneOffset");
        activity.activityType = jsonActivity.get("activityType");
        activity.bipedalCount = jsonActivity.get("bipedalCount");
        activity.intensityLevel = jsonActivity.get("intensityLevel");

        return activity;
    }

    // constructor
    public ActivityResult(ServiceResponse response) {
        this(response, false);
    }

    public ActivityResult(ServiceResponse response, boolean leaveFieldsNull) {
        super(response);

        if (!leaveFieldsNull) {
            // result
            try {
                this.startTime = json.getString("startTime");
                this.endTime = json.getString("endTime");
                this.modifiedSince = json.getString("modifiedSince");
                this.timezoneOffset = json.getString("timezoneOffset");
                this.activityType = json.getString("activityType");
                this.bipedalCount = json.getString("bipedalCount");
                this.intensityLevel = json.getString("intensityLevel");

                // add to base hashmap
                this.pairResult.put("startTime", this.startTime);
                this.pairResult.put("endTime", this.endTime);
                this.pairResult.put("modifiedSince", this.modifiedSince);
                this.pairResult.put("timezoneOffset", this.timezoneOffset);
                this.pairResult.put("activityType", this.activityType);
                this.pairResult.put("bipedalCount", this.bipedalCount);
                this.pairResult.put("intensityLevel", this.intensityLevel);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           
        }
    }
}
