package com.misfit.ta.backend.old;

import java.util.List;
import java.util.Vector;

import junit.framework.Assert;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.annotations.*;

import com.misfit.ta.backend.data.*;

public class SyncTest 
{
	protected static Logger logger = Util.setupLogger(SyncTest.class);
	SignInData user;
	List<Header> headers;
	
	private String username = "5.5@1.1";
	private String password = "qwerty1";
	
	// base methods
	public AuthToken logIn(SignInData u)
	{
		SignInRest rest = new SignInRest(u);
		rest.post();
		
        return (AuthToken)rest.content();
	}
	
	public AuthToken signUp(SignInData u)
	{
		SignUpRest rest = new SignUpRest(u);
		rest.post();
		
        return (AuthToken)rest.content();
	}
	
	public SyncData sync(SyncData data)
	{
		SyncRest rest = new SyncRest(data);
		rest.postWithHeader(headers);
		
		return (SyncData)rest.content();
	}
	
	// test setups
	@BeforeClass
	public void setUp()
	{		
		// create user
		user = new SignInData(username, password);
		
		// create authen token header
		headers = new Vector<Header>();
		AuthToken auth = logIn(user);
        BasicHeader header = new BasicHeader("auth_token", auth.token);
        headers.add(header);
	}
	
	@BeforeMethod
	public void beforeMethod()
	{
		System.out.println("=============================================================================================");
	}
	
	// test methods
	@Test
	public void case1()
	{
		/* Scenerio:
		 * ---------------------------------------
		 * - Log in
		 * - Sync
		 * - Log in
		 * => Check: sync data doesnt change
		 * ---------------------------------------
		 */
		SyncData oldData = logIn(user).syncData;	
		sync(oldData);
		SyncData newData = logIn(user).syncData;
		
		Assert.assertTrue(oldData.getString().equals(newData.getString()));
		Assert.assertTrue(oldData.timestamp != newData.timestamp);
	}
	
	@Test
	public void case2()
	{
		/* Scenerio:
		 * ---------------------------------------
		 * - Log in
		 * - Update sync data
		 * - Increase lastUpdated
		 * - Sync
		 * - Log in
		 * => Check: sync data change
		 * ---------------------------------------
		 */
		
		SyncData oldData = logIn(user).syncData;
		oldData.changeLastUpdated(true);
			
		double newW = Double.parseDouble(oldData.getValue("weight")) + 1;
		double newH = Double.parseDouble(oldData.getValue("height")) + 1;
		
		oldData.setValue("weight", newW);
		oldData.setValue("height", newH);
		
		sync(oldData);
		SyncData newData = logIn(user).syncData;
		
		Assert.assertTrue(Double.parseDouble(newData.getValue("weight")) == newW);
		Assert.assertTrue(Double.parseDouble(newData.getValue("height")) == newH);
	}	

	@Test
	public void case3()
	{
		/* Scenerio:
		 * ---------------------------------------
		 * - Log in
		 * - Update sync data
		 * - DESCREASE lastUpdated
		 * - Sync
		 * - Log in
		 * => Check: sync data doesn't change
		 * ---------------------------------------
		 */
	    SyncData tmp = logIn(user).syncData;
		SyncData oldData = new SyncData(tmp.timestamp, tmp.objects.toString());
		oldData.changeLastUpdated(false);
		
		double newW = Double.parseDouble(oldData.getValue("weight")) + 1;
		double newH = Double.parseDouble(oldData.getValue("height")) + 1;

		tmp.setValue("weight", newW);
		tmp.setValue("height", newH);
		
		sync(tmp);
		SyncData newData = logIn(user).syncData;	
		
		Assert.assertTrue(newData.getValue("weight").equals(oldData.getValue("weight")));
		Assert.assertTrue(newData.getValue("height").equals(oldData.getValue("height")));
	}
    
}
