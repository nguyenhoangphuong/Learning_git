package com.misfit.ta.backend;

import com.google.resting.Resting;
import com.google.resting.component.RequestParams;
import com.google.resting.component.content.IContentData;
import com.google.resting.component.impl.BasicRequestParams;
import com.google.resting.component.impl.ServiceResponse;

public abstract class MVPRest 
{
	// static
	static public String baseAddress = "https://staging-api.misfitwearables.com/shine/v1/";
	static public int port = 443;
	
	// fields
	protected RequestParams params = new BasicRequestParams();
	protected ServiceResponse response = null;
	protected IContentData contentData = null;
	
	protected String apiUrl;
	protected Object requestObj;
	protected String extendUrl = "";
	protected Object responseObj = null;
	
	// constructor
	public MVPRest(Object requestObj)
	{
		this.requestObj = requestObj;
		params.add("api_key", "76801581");
	}
	
	// abstract methods
	abstract void formatRequest();
	
	abstract void formatResponse();
	
	// methods
	public void post()
	{
		// request url
		String url = baseAddress + apiUrl + extendUrl;
		
		// send
		response = Resting.post(url, port, params);

		// progress response
		if(response.getStatusCode() == 200)
		{
			contentData = response.getContentData(); 
			formatResponse();
		}
	}

	
	public boolean isServerErr()
	{	
		return response.getStatusCode() == 500;
	}
	
	public boolean isClientErr()
	{
		return response.getStatusCode() == 404;
	}
	
	public boolean isError()
	{
		return  response.getStatusCode() != 200 && 
				response.getStatusCode() != 404 && 
				response.getStatusCode() == 500;
	}
	
	public Object content()
	{
		return responseObj;
	}
	
	public String errorMessage()
	{
		if(response.getStatusCode() != 200)
			;
	
		return null;
	}
}
