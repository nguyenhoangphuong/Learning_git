package com.misfit.ta.backend.data;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import com.google.resting.component.impl.json.JSONRequestParams;

public class BaseParams {
	
	// fields: params and headers
	public List<Header> headers = new Vector<Header>();
	public JSONRequestParams params = new JSONRequestParams();

	// constructor
	public BaseParams() {
		// api key
		this.addHeader("api_key", "76801581");
		this.addHeader("platform", "ios");
	}

	// public functions to add params/headers
	public void addHeader(String key, String value) {
		BasicHeader header = new BasicHeader(key, value);
		headers.add(header);
	}

	public void addParam(String key, String value) {
		params.add(key, value);
	}

	public void addObjectParam(String key, Object value) {
		params.add(key, (String) value);
	}

	public void addJsonParam(String key, JSONBuilder json) {
		params.add(key, json.toString());
	}

	public String getParamsAsJsonString() {
		JSONBuilder json = new JSONBuilder();
		List<NameValuePair> pairs = params.getRequestParams();
		Iterator<NameValuePair> iter = pairs.iterator();

		while (iter.hasNext()) {
			NameValuePair pair = iter.next();
			json.addValue(pair.getName(), pair.getValue());
		}

		return json.toJSONString();
	}
	
	public String getHeadersAsJsonString() {
		JSONBuilder json = new JSONBuilder();
		Iterator<Header> iter = headers.iterator();

		while (iter.hasNext()) {
			Header header = iter.next();
			json.addValue(header.getName(), header.getValue());
		}

		return json.toJSONString();
	}

}
