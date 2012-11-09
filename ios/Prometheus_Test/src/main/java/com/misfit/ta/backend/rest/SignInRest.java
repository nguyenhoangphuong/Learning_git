package com.misfit.ta.backend.rest;

import com.misfit.ta.backend.data.*;
import net.sf.json.*;

public class SignInRest extends MVPRest
{
	public SignInRest(UserData userData)
	{
		// parent constructor
		super(userData);
		
		// api root
		apiUrl = "login/";
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
		JSONObject json = (JSONObject) JSONSerializer.toJSON(contentData.toString());        
        String token = json.getString("auth_token");
        String type = json.getString( "type" );
        
        responseObj = new LoginToken(token, type);
	}
}
