package com.misfit.ta.backend.data;

public class Goal {
    public int activityType;
    public String goalValue = "";
    public TimeFrame timeFrame;
    public String activities;
    public int startTime;
    public int endTIme;
    public boolean deleted = false;
    public String serverId = "";
    public int lastUpdated = 0;
    public boolean needsSync = false;
    public int getActivityType() {
        return activityType;
    }
    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }
    public String getGoalValue() {
        return goalValue;
    }
    public void setGoalValue(String goalValue) {
        this.goalValue = goalValue;
    }
    public TimeFrame getTimeFrame() {
        return timeFrame;
    }
    public void setTimeFrame(TimeFrame timeFrame) {
        this.timeFrame = timeFrame;
    }
    public String getActivities() {
        return activities;
    }
    public void setActivities(String activities) {
        this.activities = activities;
    }
    public int getStartTime() {
        return startTime;
    }
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
    public int getEndTIme() {
        return endTIme;
    }
    public void setEndTIme(int endTIme) {
        this.endTIme = endTIme;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public String getServerId() {
        return serverId;
    }
    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
    public int getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(int lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public boolean isNeedsSync() {
        return needsSync;
    }
    public void setNeedsSync(boolean needsSync) {
        this.needsSync = needsSync;
    }
    
    
}
