package com.misfit.ta.backend.data;

public class TimeFrame {
    public String endTime;
    public String startTime;
    public String timeZoneOffsetInSeconds;
    public String childrenTimeFrames;
    public String goals;
    public String parentTimeFrame;

    public String deleted;
    public String serverId;
    public String lastUpdated;
    public String needsSync;
    
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getTimeZoneOffsetInSeconds() {
        return timeZoneOffsetInSeconds;
    }
    public void setTimeZoneOffsetInSeconds(String timeZoneOffsetInSeconds) {
        this.timeZoneOffsetInSeconds = timeZoneOffsetInSeconds;
    }
    public String getChildrenTimeFrames() {
        return childrenTimeFrames;
    }
    public void setChildrenTimeFrames(String childrenTimeFrames) {
        this.childrenTimeFrames = childrenTimeFrames;
    }
    public String getGoals() {
        return goals;
    }
    public void setGoals(String goals) {
        this.goals = goals;
    }
    public String getParentTimeFrame() {
        return parentTimeFrame;
    }
    public void setParentTimeFrame(String parentTimeFrame) {
        this.parentTimeFrame = parentTimeFrame;
    }
    public String getDeleted() {
        return deleted;
    }
    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
    public String getServerId() {
        return serverId;
    }
    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
    public String getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public String getNeedsSync() {
        return needsSync;
    }
    public void setNeedsSync(String needsSync) {
        this.needsSync = needsSync;
    }  
}
