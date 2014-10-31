package com.misfit.ta.backend.data;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;

import com.google.resting.component.impl.json.JSONRequestParams;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class BaseParams {
	
	public static String CurrentLocale = "en";
	
	// fields: params and headers
	public List<Header> headers = new Vector<Header>();
	public JSONRequestParams params = new JSONRequestParams();
	com.google.resting.component.impl.BasicRequestParams asd;

	// constructor
	public BaseParams() {
		
		// api key
		this.addHeader("api_key", "76801581");
		this.addHeader("locale", CurrentLocale);
		this.addHeader("Content-Type", "application/json");
	}
	
	public BaseParams(String accessKeyId){
		this.addHeader("access_key_id", accessKeyId);
		this.addHeader("Content-Type", "application/json");
	}
	
	public BaseParams(String accessKeyId, boolean isBeddit){
		this.addHeader("access_key_id", accessKeyId);
		this.addHeader("Content-Type", "multipart/form-data");
		this.addHeader("boundary", "----WebKitFormBoundaryrGKCBY7qhFd3TrwA");
	}

	// public functions to add params/headers
	public void addBasicAuthorizationHeader(String username, String password) {
		
		String combine = username + ":" + password;
		String base64 = Base64.encodeBase64String(combine.getBytes()).replace("\r\n", "");

		addHeader("Authorization", "Basic " + base64);
	}

	public void addHeader(String key, String value) {
		BasicHeader header = new BasicHeader(key, value);
		headers.add(header);
	}

	public void removeHeader(String key) {
		
		for(Header header : headers) {
			
			if(header.getName().equals(key)) {
				headers.remove(header);
				return;
			}
		}
	}

	
	public void addParam(String key, String value) {
		params.add(key, value);
	}

	
	
	public String getParamsAsJsonString() {

		JSONObject jsonObj = new JSONObject();
		List<NameValuePair> pairs = params.getRequestParams();
		Iterator<NameValuePair> iter = pairs.iterator();

		while (iter.hasNext()) {
			NameValuePair pair = iter.next();
			
			// try to put as JSONObject
			try {
				String s = pair.getValue().replace("\\\"", "\"");
				JSONObject valueJsonObj = new JSONObject(s);
				jsonObj.put(pair.getName(), valueJsonObj);
				continue;
			} catch (JSONException e) {
			}
			
			// try to put as JSONArray
			try {
				String s = pair.getValue().replace("\\\"", "\"");
				JSONArray valueJsonArr = new JSONArray(s);
				jsonObj.put(pair.getName(), valueJsonArr);
				continue;
			} catch (JSONException e) {
			}
						
			// try to put as normal string
			try {
				jsonObj.put(pair.getName(), pair.getValue());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return jsonObj.toString();
	}
	
	public String getHeadersAsJsonString() {
		
		JSONObject jsonObj = new JSONObject();
		Iterator<Header> iter = headers.iterator();

		while (iter.hasNext()) {
			Header header = iter.next();
			try {
				jsonObj.put(header.getName(), header.getValue());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return jsonObj.toString();
	}

}
