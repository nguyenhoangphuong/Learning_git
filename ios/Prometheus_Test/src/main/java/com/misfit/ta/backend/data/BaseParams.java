package com.misfit.ta.backend.data;

import java.util.List;
import java.util.Vector;

import net.sf.json.util.JSONBuilder;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.google.resting.component.RequestParams;
import com.google.resting.component.impl.json.JSONRequestParams;

public class BaseParams {
    // fields: params and headers
    public List<Header> headers = new Vector<Header>();
//    public RequestParams params = new BasicRequestParams();
    public JSONRequestParams params = new JSONRequestParams();

    // constructor
    public BaseParams() {
        // api key
        this.addHeader("api_key", "76801581");
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

}
