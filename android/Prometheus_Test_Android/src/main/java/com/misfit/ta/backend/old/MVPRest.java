package com.misfit.ta.backend.old;

import static com.google.resting.component.EncodingTypes.UTF8;

import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.Header;
import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.Resting;
import com.google.resting.component.RequestParams;
import com.google.resting.component.content.IContentData;
import com.google.resting.component.impl.BasicRequestParams;
import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.method.post.PostHelper;
import com.misfit.ta.Settings;

/* To be updated:
 * - Ensure response != in check error methods and get response methods
 * - Use object to send different request without construct new object
 */

public abstract class MVPRest {
	
    // static
    static public String baseAddress = Settings.getValue("syncServerBaseAddress");
    static public int port = Integer.parseInt(Settings.getValue("syncServerPort"));
    protected String apiUrl;
    
    // fields
    protected RequestParams params = new BasicRequestParams();
    
    
    protected ServiceResponse response = null;
    protected IContentData contentData = null;

    
    protected Object requestObj;
    protected String extendUrl = "";
    protected Object responseObj = null;
    private String url;
    
    protected Logger logger = Util.setupLogger(MVPRest.class);

    // constructor
    public MVPRest(Object requestObj) {
        this.requestObj = requestObj;
        params.add("api_key", "76801581");
    }

    // abstract methods
    public abstract void formatRequest();

    public abstract void formatResponse();

    // methods   
    public void post() {
    	
        url = baseAddress + apiUrl + extendUrl;
        System.out.println("LOG [MVPRest.post]: url= " + url);
        formatRequest();
        response = Resting.post(url, port, params);
        
        // progress response
        contentData = response.getContentData();
        System.out.println("LOG [MVPRest.post]: " + contentData);
        formatResponse();
    }

    public void postWithHeader(List<Header> headers) {
    	
        url = baseAddress + apiUrl + extendUrl;
        formatRequest();
        response = PostHelper.post(url, port, UTF8, params, headers);
        
        // progress response
        contentData = response.getContentData();
        formatResponse();

    }

    public boolean isServerErr() {
        return response.getStatusCode() == 500;
    }

    public boolean isClientErr() {
        return response.getStatusCode() == 404;
    }

    public boolean isError() {
        return response.getStatusCode() != 200 && response.getStatusCode() != 404 && response.getStatusCode() != 500;
    }

    public int statusCode() {
    	return response.getStatusCode();
    }
    
    public Object content() {
        return responseObj;
    }

    public String errorMessage() {
        if (response.getStatusCode() != 200) {
            JSONObject json = (JSONObject) JSONSerializer.toJSON(contentData.toString());
            return json.getString("error_message");
        }

        return null;
    }
}
