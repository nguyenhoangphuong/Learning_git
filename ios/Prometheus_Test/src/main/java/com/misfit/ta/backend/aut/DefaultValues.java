package com.misfit.ta.backend.aut;

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

}
