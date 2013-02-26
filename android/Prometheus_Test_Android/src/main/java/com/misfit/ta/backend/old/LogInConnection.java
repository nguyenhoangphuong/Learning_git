package com.misfit.ta.backend.old;

import org.apache.log4j.Logger;
import org.graphwalker.Util;


public class LogInConnection extends CThread
{
	private static Logger logger = Util.setupLogger(LogInConnection.class);
	
	// fields
	private AuthToken token = null;
	private String username;
	private String password;
	
	// constructor
	public LogInConnection()
	{
		username = "qq@a.a";
		password = "qwerty1";
	}
	
	public LogInConnection(String usr, String pwd)
	{
		username = usr;
		password = pwd;
	}
	
	// must-be-implemented functions
    public void run() 
    {	        
		long t1 = System.currentTimeMillis();
		
		// new sign in rest
		SignInData user = new SignInData(username, password);
		SignInRest rest = new SignInRest(user);
		rest.post();		
		token = (AuthToken) rest.content();
		
		long t2 = System.currentTimeMillis();
		
		// log
		logger.info("Token = " + token.token + ", Type =" + token.type);
		
		// check status code
		isSuccess = !rest.isClientErr() && !rest.isError() && !rest.isServerErr();
		timeTaken = t2 - t1;
		
		if(!isSuccess)
			logger.info("ERROR: sign in error: " + rest.statusCode());
    }
 
    public CThread duplicate()
    {
		return new LogInConnection(username, password);
    }
}
