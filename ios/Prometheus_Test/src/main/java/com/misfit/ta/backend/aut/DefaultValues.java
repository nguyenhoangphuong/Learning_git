package com.misfit.ta.backend.aut;

import com.misfit.ta.backend.data.ProfileResult;
import com.misfit.ta.backend.data.ProfileResult.ProfileData;
import com.misfit.ta.backend.data.ProfileResult.RelativeLevelData;

public class DefaultValues 
{

	static public String UDID = "f230d0c4e69f08cb31e8535f5b512ed7c140289b";
	static public String UDID2 = "085f57cd4d36d46c06de244fd2be00f4a2ec40c0";
	static public String ArbitraryToken = "134654678931452236-arbitrarytokenasdasfjk";
	
	static public String InvalidEmail = "Invalid email";
	static public String DuplicateEmail = "Sorry, someone else has used this before";
	static public String InvalidPassword = "Sorry, the password should have at least 6 characters, at least 1 digit and 1 letter";
	static public String WrongAccountMsg = "Incorrect email or password";
	static public String InvalidAuthToken = "Invalid auth token";
	
	static public ProfileData DefaultProfile()
	{	
		ProfileData p = new ProfileResult.ProfileData();
		
		p.localId = System.currentTimeMillis() + "-" + System.nanoTime();
		p.weight = 144.4;
		p.height = 66.0;
		p.unit = 1;
		p.gender = 0;
		p.dateOfBirth = (long)684954000;
		p.name = "Tears";
		p.latestVersion = "8";
		p.goalLevel = 4;
		p.trackingDeviceId = DefaultValues.UDID;
		
		p.userRelativeLevelsNSData = new RelativeLevelData[10];
		p.userRelativeLevelsNSData[0] = new ProfileResult.RelativeLevelData(1, 2, 240);
		p.userRelativeLevelsNSData[1] = new ProfileResult.RelativeLevelData(2, 3, 280);
		p.userRelativeLevelsNSData[2] = new ProfileResult.RelativeLevelData(3, 4, 320);
		p.userRelativeLevelsNSData[3] = new ProfileResult.RelativeLevelData(4, 5, 360);
		p.userRelativeLevelsNSData[4] = new ProfileResult.RelativeLevelData(5, 6, 400);
		p.userRelativeLevelsNSData[5] = new ProfileResult.RelativeLevelData(6, 7, 440);
		p.userRelativeLevelsNSData[6] = new ProfileResult.RelativeLevelData(7, 8, 480);
		p.userRelativeLevelsNSData[7] = new ProfileResult.RelativeLevelData(8, 9, 520);
		p.userRelativeLevelsNSData[8] = new ProfileResult.RelativeLevelData(9, 10, 560);
		p.userRelativeLevelsNSData[9] = new ProfileResult.RelativeLevelData(10, 11, 600);
		
		return p;
	}

}
