package com.misfit.ta.backend.aut;

import java.util.List;
import java.util.Vector;

import junit.framework.Assert;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.annotations.*;

import com.misfit.ta.backend.data.*;
import com.misfit.ta.backend.rest.profile.SyncRest;
import com.misfit.ta.backend.rest.signin.*;

public class SyncTest 
{
	Logger logger;
	SignInData user;
	List<Header> headers;
	
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
		logger = Util.setupLogger(SyncBackend.class);
		
		// create user
		user = new SignInData("qa@test.test", "misfit1");
		
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
		
		System.out.println(oldData.getString());
		System.out.println(newData.getString());
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
		oldData.setLastUpdated(oldData.getLastUpdated() + 1);
		
		double newW = Double.parseDouble(oldData.getValue("weight")) + 1;
		double newH = Double.parseDouble(oldData.getValue("height")) + 1;
		
		oldData.setValue("weight", newW);
		oldData.setValue("height", newH);
		
		sync(oldData);
		SyncData newData = logIn(user).syncData;
		
		Assert.assertTrue(!newData.getValue("weight").equals(oldData.getValue("weight")));
		Assert.assertTrue(!newData.getValue("height").equals(oldData.getValue("height")));
		
		Assert.assertTrue(Double.parseDouble(newData.getValue("weight")) == newW);
		Assert.assertTrue(Double.parseDouble(newData.getValue("height")) == newH);
	}	

	@Test
	public void case3()
	{
		System.out.println("Adasdasdasdasd");
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
		
		SyncData oldData = logIn(user).syncData;
		System.out.println("LOG CASE3:" + oldData.getString());
		oldData.setLastUpdated(0);
		System.out.println("LOG CASE3:" + oldData.getString());
		
		double newW = Double.parseDouble(oldData.getValue("weight")) + 1;
		double newH = Double.parseDouble(oldData.getValue("height")) + 1;
		
		oldData.setValue("weight", newW);
		oldData.setValue("height", newH);
		
		sync(oldData);
		SyncData newData = logIn(user).syncData;
		
		Assert.assertTrue(newData.getValue("weight").equals(oldData.getValue("weight")));
		Assert.assertTrue(newData.getValue("height").equals(oldData.getValue("height")));
	}
	
}