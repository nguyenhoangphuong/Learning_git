package com.misfit.ta.backend.aut;

import com.misfit.ta.backend.data.*;
import com.misfit.ta.backend.rest.*;

public class TestRest 
{
	public static void main(String[] args) 
	{
		UserData user = new UserData("test@misfitwearables.com", "password123");
		
		SignInRest rest = new SignInRest(user);
		rest.post();
		
		System.out.println(rest.isClientErr());
		System.out.println(rest.isServerErr());
		System.out.println(rest.isError());
		System.out.println(rest.errorMessage());
		
		LoginToken token = (LoginToken) rest.content();
		System.out.println(token.token + ", " + token.type);
	}
}