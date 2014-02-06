package com.misfit.ta.backend.aut;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.testng.annotations.BeforeClass;

import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.internalapi.MVPApi;

public class OpenAPIAutomationBase extends BackendAutomation {

	
	protected static String ClientKey = Settings.getParameter("MVPOpenAPIClientID");
	
	
	// each class has 2 user authorized to app: you and me, 
	// and a non-authorized stranger
	protected String myEmail;
	protected String yourEmail;
	protected String strangerEmail;
	
	protected String myToken;
	protected String yourToken;
	protected String strangerToken;
	
	protected String myUid;
	protected String yourUid;
	protected String strangerUid;
	
	
	// set up class
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		
		myEmail = MVPApi.generateUniqueEmail();
		yourEmail = MVPApi.generateUniqueEmail();
		strangerEmail = MVPApi.generateUniqueEmail();
		
		myToken = MVPApi.signUp(myEmail, "qqqqqq").token;
		yourToken = MVPApi.signUp(yourEmail, "qqqqqq").token;
		strangerToken = MVPApi.signUp(strangerEmail, "qqqqqq").token;
		
		myUid = MVPApi.getUserId(myToken);
		yourUid = MVPApi.getUserId(yourToken);
		strangerUid = MVPApi.getUserId(strangerToken);
	}
	
	protected static String getDateString(long timestamp) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp * 1000);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		return formatter.format(cal.getTime());
	}
	
	protected static String getISOTime(long timestamp) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp * 1000);
		
		TimeZone tz = TimeZone.getTimeZone("UTC");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
		df.setTimeZone(tz);
		
		return df.format(cal.getTime());
	}
}
