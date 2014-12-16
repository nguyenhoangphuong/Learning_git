package com.misfit.ta.backend.api;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.component.EncodingTypes;
import com.google.resting.component.content.IContentData;
import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.backend.BackendTestEnvironment;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.report.TRS;

public class RequestHelper {

    protected static Logger logger = Util.setupLogger(RequestHelper.class);

    public static final String HTTP_POST = "post";
    public static final String HTTP_GET = "get";
    public static final String HTTP_DELETE = "delete";
    public static final String HTTP_PUT = "put";

    // request helpers
    static public ServiceResponse request(String type, String url, Integer port, BaseParams requestInf, String JsonText) {
    	 if (requestInf == null) {
             requestInf = new BaseParams();
             requestInf.headers.clear();
         }
         
         // build uri
         URI uri = UriBuilder.fromUri(url).build();
         if (port != null)
             uri = UriBuilder.fromUri(url).port(port).build();

         // log address
         if(BackendTestEnvironment.RequestLoggingEnable) {
         	logger.info(type.toUpperCase() + ": " + url + " - port: " + uri.getPort());
         	logger.info("Request headers: " + requestInf.getHeadersAsJsonString());
         	logger.info("Request params: " + JsonText);
         }
         
         // send to TRS
         TRS.instance().addStep(type.toUpperCase() + ": " + url + " - port: " + uri.getPort(), null);
         TRS.instance().addCode("Request headers: " + requestInf.getHeadersAsJsonString(), null);
         TRS.instance().addCode("Request params: " + JsonText, null);

         // wrapper send request
         ServiceResponse response = null;
         ResultLogger.registerRequest();
         if (type.equalsIgnoreCase(MVPApi.HTTP_POST))
             response = doPost(uri, port, requestInf.headers, JsonText);
         else if (type.equalsIgnoreCase(MVPApi.HTTP_GET))
             response = doGet(uri, port, requestInf.headers);
         else if (type.equalsIgnoreCase(MVPApi.HTTP_PUT))
             response = doPut(uri, port, requestInf.headers, JsonText);
         else if (type.equalsIgnoreCase(MVPApi.HTTP_DELETE))
             response = doDelete(uri, port, requestInf.headers);

         // log result
         IContentData rawData = response.getContentData();
         int error = response.getStatusCode();

         ResultLogger.registerResponse();
         ResultLogger.addErrorCode(error);

         if(BackendTestEnvironment.RequestLoggingEnable) {
         	logger.info("Response raw Data: " + rawData);
         	logger.info("Response code: " + error + "\n\n");
         }
         
         // send to TRS
         TRS.instance().addCode("Response raw Data: " + rawData, null);
         TRS.instance().addCode("Response code: " + error + "\n\n", null);

         return response;
    }
    static public ServiceResponse request(String type, String url, Integer port, BaseParams requestInf) {
        
        if (requestInf == null) {
            requestInf = new BaseParams();
            requestInf.headers.clear();
        }
        
        // build uri
        URI uri = UriBuilder.fromUri(url).build();
        if (port != null)
            uri = UriBuilder.fromUri(url).port(port).build();

        // log address
        if(BackendTestEnvironment.RequestLoggingEnable) {
        	logger.info(type.toUpperCase() + ": " + url + " - port: " + uri.getPort());
        	logger.info("Request headers: " + requestInf.getHeadersAsJsonString());
        	logger.info("Request params: " + requestInf.getParamsAsJsonString());
        }
        
        // send to TRS
        TRS.instance().addStep(type.toUpperCase() + ": " + url + " - port: " + uri.getPort(), null);
        TRS.instance().addCode("Request headers: " + requestInf.getHeadersAsJsonString(), null);
        TRS.instance().addCode("Request params: " + requestInf.getParamsAsJsonString(), null);

        // wrapper send request
        ServiceResponse response = null;
        ResultLogger.registerRequest();
        if (type.equalsIgnoreCase(MVPApi.HTTP_POST))
            response = doPost(uri, port, requestInf);
        else if (type.equalsIgnoreCase(MVPApi.HTTP_GET))
            response = doGet(uri, port, requestInf);
        else if (type.equalsIgnoreCase(MVPApi.HTTP_PUT))
            response = doPut(uri, port, requestInf);
        else if (type.equalsIgnoreCase(MVPApi.HTTP_DELETE))
            response = doDelete(uri, port, requestInf);

        // log result
        IContentData rawData = response.getContentData();
        int error = response.getStatusCode();

        ResultLogger.registerResponse();
        ResultLogger.addErrorCode(error);

        if(BackendTestEnvironment.RequestLoggingEnable) {
        	logger.info("Response raw Data: " + rawData);
        	logger.info("Response code: " + error + "\n\n");
        }
        
        // send to TRS
        TRS.instance().addCode("Response raw Data: " + rawData, null);
        TRS.instance().addCode("Response code: " + error + "\n\n", null);

        return response;
    }

    static public ServiceResponse post(String url, Integer port, BaseParams requestInf) {
        return request("post", url, port, requestInf);
    }
    
    static public ServiceResponse post(String url, Integer port, BaseParams headerRequestInf, String JsonText) {
        return request("post", url, port, headerRequestInf, JsonText);
    }

    static public ServiceResponse get(String url, Integer port, BaseParams requestInf) {
        return request("get", url, port, requestInf);
    }

    static public ServiceResponse put(String url, Integer port, BaseParams requestInf) {
        return request("put", url, port, requestInf);
    }

    static public ServiceResponse delete(String url, Integer port, BaseParams requestInf) {
        return request("delete", url, port, requestInf);
    }

    // execute http requests
    static private ServiceResponse doPost(URI url, Integer port, BaseParams requestInf) {

        EntityBuilder entityBuilder = org.apache.http.client.entity.EntityBuilder.create();
        entityBuilder.setText(requestInf.getParamsAsJsonString());

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeaders(requestInf.headers.toArray(new Header[requestInf.headers.size()]));
        httpPost.setEntity(entityBuilder.build());

        return excuteHttpRequest(httpPost);
    }

    static private ServiceResponse doPost(URI url, Integer port, List<Header> headers, String JsonText) {

        EntityBuilder entityBuilder = org.apache.http.client.entity.EntityBuilder.create();
        entityBuilder.setText(JsonText);

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeaders(headers.toArray(new Header[headers.size()]));
        httpPost.setEntity(entityBuilder.build());

        return excuteHttpRequest(httpPost);
    }

    
    static private ServiceResponse doGet(URI url, Integer port, BaseParams requestInf) {

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeaders(requestInf.headers.toArray(new Header[requestInf.headers.size()]));

        return excuteHttpRequest(httpGet);
    }
    
    static private ServiceResponse doGet(URI url, Integer port, List<Header> headers) {
    	HttpGet httpGet = new HttpGet(url);
        httpGet.setHeaders(headers.toArray(new Header[headers.size()]));

        return excuteHttpRequest(httpGet);
    }

    static private ServiceResponse doPut(URI url, Integer port, BaseParams requestInf) {

        EntityBuilder entityBuilder = org.apache.http.client.entity.EntityBuilder.create();
        entityBuilder.setText(requestInf.getParamsAsJsonString());

        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeaders(requestInf.headers.toArray(new Header[requestInf.headers.size()]));
        httpPut.setEntity(entityBuilder.build());

        return excuteHttpRequest(httpPut);
    }
    
    static private ServiceResponse doPut(URI url, Integer port, List<Header> headers, String JsonText) {

        EntityBuilder entityBuilder = org.apache.http.client.entity.EntityBuilder.create();
        entityBuilder.setText(JsonText);

        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeaders(headers.toArray(new Header[headers.size()]));
        httpPut.setEntity(entityBuilder.build());

        return excuteHttpRequest(httpPut);
    }

    static private ServiceResponse doDelete(URI url, Integer port, BaseParams requestInf) {

        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setHeaders(requestInf.headers.toArray(new Header[requestInf.headers.size()]));

        return excuteHttpRequest(httpDelete);
    }
    
    static private ServiceResponse doDelete(URI url, Integer port,  List<Header> headers) {

        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setHeaders(headers.toArray(new Header[headers.size()]));

        return excuteHttpRequest(httpDelete);
    }

    protected static ServiceResponse excuteHttpRequest(HttpUriRequest httprequest) {

        try {
            CloseableHttpClient httpclient = new InsecureHttpClientHelper().getInsecureCloseableHttpClient();
            long start = System.currentTimeMillis();
            CloseableHttpResponse response = httpclient.execute(httprequest);
            ServiceResponse sr = new ServiceResponse(response, EncodingTypes.UTF8);
            long end = System.currentTimeMillis();
            logger.info("Time taken in REST: " + (end - start));
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);

            response.close();

            return sr;
        } catch (Exception e) {
            return null;
        }
    }

}
