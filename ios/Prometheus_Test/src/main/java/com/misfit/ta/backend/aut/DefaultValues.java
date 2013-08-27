package com.misfit.ta.backend.aut;

import java.util.ArrayList;
import java.util.Calendar;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.ProgressData;
import com.misfit.ta.backend.data.goal.TrippleTapData;
import com.misfit.ta.backend.data.profile.DisplayUnit;
import com.misfit.ta.backend.data.profile.ProfileData;

public class DefaultValues {

    static public String UDID = "f230d0c4e69f08cb31e8535f5b512ed7c140289b";
    static public String UDID2 = "085f57cd4d36d46c06de244fd2be00f4a2ec40c0";
    static public String ArbitraryToken = "134654678931452236-arbitrarytokenasdasfjk";

    static public String InvalidEmail = "Sorry, this email is invalid";
    static public String DuplicateEmail = "Sorry, someone else has used this before";
    static public String InvalidPassword = "Sorry, this password is too short";
    static public String WrongAccountMsg = "Incorrect email or password";
    static public String InvalidAuthToken = "Invalid auth token";

    static public ProfileData DefaultProfile() {
        ProfileData p = new ProfileData();

        p.setLocalId(System.currentTimeMillis() + "-" + System.nanoTime());
        p.setUpdatedAt((long)(System.currentTimeMillis() / 1000));
        
        p.setWeight(144.4);
        p.setHeight(66.0);
        p.setGender(0);
        p.setDateOfBirth((long) 684954000);
        p.setName("Tears");

        p.setGoalLevel(4);
        p.setLatestVersion("8");
        p.setWearingPosition("Wrist");
        p.setPersonalRecords(null);
        p.setDisplayedUnits(new DisplayUnit());
                
        return p;
    }
    
    static public Goal DefaultGoal() {
    	
    	ProgressData progressData = new ProgressData();
    	progressData.setFullBmrCalorie(1500);
    	Goal g = new Goal();
    	
    	g.setLocalId(System.currentTimeMillis() + "-" + System.nanoTime());
    	g.setUpdatedAt((long)(System.currentTimeMillis() / 1000));
    	
    	g.setValue(1000.0);
    	g.setStartTime(MVPApi.getDayStartEpoch(System.currentTimeMillis() / 1000));
    	g.setEndTime(MVPApi.getDayEndEpoch(System.currentTimeMillis() / 1000));
    	g.setTimeZoneOffsetInSeconds(7 * 3600);
    	g.setProgressData(progressData);
    	g.setTripleTapTypeChanges(new ArrayList<TrippleTapData>());
    	
    	return g;
    }
    
    static public Goal GoalForDate(int date, int month, int year) {
    	
    	Calendar cal = Calendar.getInstance();
    	cal.set(year, month, date);
    	
    	ProgressData progressData = new ProgressData();
    	progressData.setFullBmrCalorie(1500);
    	Goal g = new Goal();
    	
    	g.setLocalId(cal.getTimeInMillis() + "-" + System.nanoTime());
    	g.setUpdatedAt((long)(System.currentTimeMillis() / 1000));
    	
    	g.setValue(1000.0);
    	g.setStartTime(MVPApi.getDayStartEpoch(cal.getTimeInMillis() / 1000));
    	g.setEndTime(MVPApi.getDayEndEpoch(cal.getTimeInMillis() / 1000));
    	g.setTimeZoneOffsetInSeconds(7 * 3600);
    	g.setProgressData(progressData);
    	g.setTripleTapTypeChanges(new ArrayList<TrippleTapData>());
    	
    	return g;
    }

}
