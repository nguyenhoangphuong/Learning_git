package com.misfit.ta.backend.data.timeline;

import com.misfit.ta.backend.data.JSONBuilder;

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

    public String toJson() {
        JSONBuilder builder = new JSONBuilder();
        builder.addValue("duration", duration);
        builder.addValue("distance", distance);
        builder.addValue("startTime", startTime);
        builder.addValue("calories", calories);
        builder.addValue("activityType", activityType);
        builder.addValue("point", point);
        builder.addValue("steps", steps);
        return builder.toJSONString();
    }

}
