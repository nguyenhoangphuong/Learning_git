package com.misfit.ta.backend;

import com.misfit.ta.backend.data.UserData;

public class SignUpRest extends MVPRest
{
	public SignUpRest(UserData userData)
	{
		// parent constructor
		super(userData);
		
		// api root
		apiUrl = "signup/";
	}
	
	@Override
	void formatRequest() 
	{
		UserData data = (UserData)requestObj;	
		params.add("email", data.email);
		params.add("password", data.password);
	}
	
	@Override
	void formatResponse()
	{
		
	}

}
