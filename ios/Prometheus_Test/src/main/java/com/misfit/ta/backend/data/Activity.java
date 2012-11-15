package com.misfit.ta.backend.data;

public class Activity {

    public int activityType;
    public float activityValue;
    public boolean detail;
    public float endLatitude;
    public float endLongitude;
    public int endTime;
    public float startLatitude;
    public float startLongitude;
    public int startTime;
    public float timeZoneOffset;
    public int weatherCode;
    public int weekDay;
    public String dateIdentifier;
    public Goal dailyGoal;
    public boolean deleted;
    public String serverId;
    public int lastUpdated;
    public boolean needsSync;
    public int getActivityType() {
        return activityType;
    }
    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }
    public float getActivityValue() {
        return activityValue;
    }
    public void setActivityValue(float activityValue) {
        this.activityValue = activityValue;
    }
    public boolean isDetail() {
        return detail;
    }
    public void setDetail(boolean detail) {
        this.detail = detail;
    }
    public float getEndLatitude() {
        return endLatitude;
    }
    public void setEndLatitude(float endLatitude) {
        this.endLatitude = endLatitude;
    }
    public float getEndLongitude() {
        return endLongitude;
    }
    public void setEndLongitude(float endLongitude) {
        this.endLongitude = endLongitude;
    }
    public int getEndTime() {
        return endTime;
    }
    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
    public float getStartLatitude() {
        return startLatitude;
    }
    public void setStartLatitude(float startLatitude) {
        this.startLatitude = startLatitude;
    }
    public float getStartLongitude() {
        return startLongitude;
    }
    public void setStartLongitude(float startLongitude) {
        this.startLongitude = startLongitude;
    }
    public int getStartTime() {
        return startTime;
    }
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
    public float getTimeZoneOffset() {
        return timeZoneOffset;
    }
    public void setTimeZoneOffset(float timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }
    public int getWeatherCode() {
        return weatherCode;
    }
    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }
    public int getWeekDay() {
        return weekDay;
    }
    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }
    public String getDateIdentifier() {
        return dateIdentifier;
    }
    public void setDateIdentifier(String dateIdentifier) {
        this.dateIdentifier = dateIdentifier;
    }
    public Goal getDailyGoal() {
        return dailyGoal;
    }
    public void setDailyGoal(Goal dailyGoal) {
        this.dailyGoal = dailyGoal;
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
