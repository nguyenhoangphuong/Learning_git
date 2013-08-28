package com.misfit.ta.backend.data.timeline;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class ActivitySessionItem extends TimelineItemBase {

    private long duration;
    private double distance;

    private long startTime;
    private double calories;
    private int activityType = 0;
    private int point;
    private int steps;

    public ActivitySessionItem(long timestamp, long duration, double distance, long startTime, double calories,
            int activityType, int point, int steps) {
        super(TYPE_SESSION, timestamp);
        this.duration = duration;
        this.distance = distance;
        this.startTime = startTime;
        this.calories = calories;
        this.activityType = activityType;
        this.point = point;
        this.steps = steps;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.accumulate("duration", duration);
            object.accumulate("distance", distance);
            object.accumulate("startTime", startTime);
            object.accumulate("calories", calories);
            object.accumulate("activityType", activityType);
            object.accumulate("point", point);
            object.accumulate("steps", steps);
            return object;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        
    }

}
