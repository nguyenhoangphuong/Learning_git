package com.misfit.ta.ios.tests;

import static com.google.resting.component.EncodingTypes.UTF8;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.Resting;
import com.google.resting.component.EncodingTypes;
import com.google.resting.component.impl.json.JSONRequestParams;
import com.google.resting.json.JSONException;
import com.google.resting.method.post.PostHelper;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.sync.sdk.SDKSyncLog;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws JSONException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

//		BackendHelper.unlink("nhhai16991@gmail.com", "qqqqqq");
//		BackendHelper.link("nhhai16991@gmail.com", "qqqqqq", "9876543210");
		
		String path = "rawdata/vyanh02@gmail.com/test1/12/1392170920";
		long timestamp = System.currentTimeMillis() / 1000;
		String email = "and046@a.a";
		SDKSyncLog syncLog = DataGenerator.createSDKSyncLogFromFilesInFolder(timestamp, email, "ABCDEFGHIJ", path);
		logger.info(syncLog.toJson().toString());
		
		String url = "https://stg-vpc-dc-elb-1444600000.us-east-1.elb.amazonaws.com/v2/events";
//		String url = "https://www.google.com.vn/";
		int port = 443;
		
		BaseParams params = new BaseParams();
		params.addHeader("access_key_id", "39347523984598654-ajwoeifja399438ga3948g494g843fff");
		params.addHeader("Content-Type", "application/json");
//		params.addHeader("Content-Encoding", "application/json");
//		params.addParam("session", syncLog.toJson().toString());
//		RequestHelper.post(url, port, params);
		
		JSONRequestParams json = new JSONRequestParams();
		json.add("session", syncLog.toJson().toString());
//		PostHelper.post(url, port, UTF8, json, params.headers);
		MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq");
//		PostHelper.post(syncLog.toJson().toString(), 
//				EncodingTypes.UTF8, 
//				url, port,  
//				params.headers);
		
//		PostHelper.post(syncLog.toJson().toString(), 
//				EncodingTypes.UTF8, 
//				url, port, 
//				null, 
//				params.headers, com.google.resting.component.content.ContentType.APPLICATION_JSON);
		
//		logger.info(Resting.get(url, port, params.params, EncodingTypes.UTF8, params.headers).getContentData().getContent().toString());
		

	}

}