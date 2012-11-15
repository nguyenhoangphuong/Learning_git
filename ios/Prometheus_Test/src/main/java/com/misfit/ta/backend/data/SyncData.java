package com.misfit.ta.backend.data;

public class SyncData {

    public Profile getProfile() {
        return profile;
    }
    public void setProfile(Profile profile) {
        this.profile = profile;
    }
    public PersonalPlan getPlans() {
        return plans;
    }
    public void setPlans(PersonalPlan plans) {
        this.plans = plans;
    }
    public Goal getGoals() {
        return goals;
    }
    public void setGoals(Goal goals) {
        this.goals = goals;
    }
    public Activity getActivities() {
        return activities;
    }
    public void setActivities(Activity activities) {
        this.activities = activities;
    }
    public TimeFrame getTimeFrames() {
        return timeFrames;
    }
    public void setTimeFrames(TimeFrame timeFrames) {
        this.timeFrames = timeFrames;
    }
    public Profile profile = new Profile("email@company.com",  "wUDf1GC1eRqLKJGTDE9w");
    public PersonalPlan plans = new PersonalPlan();
    public Goal goals = new Goal();
    public Activity activities = new Activity();
    public TimeFrame timeFrames = new TimeFrame();
    
    
}
