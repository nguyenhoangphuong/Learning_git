package com.misfit.ta.backend.aut.connection;

import com.misfit.ta.backend.aut.SignInTest;
import com.misfit.ta.backend.data.AuthToken;

public class LogInConnection extends CThread
{
	// fields
	private AuthToken token = null;
	private String username;
	private String password;
	
	// constructor
	public LogInConnection()
	{
		username = "qa-fullplan@a.a";
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
        token = SignInTest.signIn(username, password);
        long t2 = System.currentTimeMillis();
        
        isSuccess = token != null && !token.token.isEmpty();
        timeTaken = t2 - t1;
    }
 
    public CThread duplicate()
    {
		return new LogInConnection(username, password);
    }
}
