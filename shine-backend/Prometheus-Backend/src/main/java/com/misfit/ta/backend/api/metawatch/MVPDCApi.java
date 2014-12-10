package com.misfit.ta.backend.api.metawatch;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.RequestHelper;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.MetaWatch.MetaWatchModel;
import com.misfit.ta.backend.data.MetawatchModel.MetawatchProfileModel;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.utils.TextTool;

public class MVPDCApi extends RequestHelper {
	 // logger
    protected static Logger logger = Util.setupLogger(MVPDCApi.class);

    // fields
    public static String baseAddress = Settings.getValue("MVPBackendBaseAddress");
    public static Integer port = Settings.getValue("MVPBackendPort") == null ? null : Integer.parseInt(Settings
            .getValue("MVPBackendPort"));

    public static String dataCenterBaseAddress = "https://data.int.api.misfitwearables.com/";
    public static Integer dataCenterPort = Settings.getValue("MVPDataCenterPort") == null ? null : Integer
            .parseInt(Settings.getValue("MVPDataCenterPort"));

    private static String[] models = new String[]{ "HTC", "WindowPhone", "Iphone", "Ipod", "Samsung", "Nokia"};
    
    public static BaseResult pushMetaWatch(String accessKeyId, MetaWatchModel data){
    	String url = dataCenterBaseAddress + "metawatch";
    	
    	BaseParams requestInfo = new BaseParams(accessKeyId);
    	System.out.println("Data : " + data.toJson().toString());
    	ServiceResponse response = MVPApi.post(url, port, requestInfo, data.toJson().toString());
    	return new BaseResult(response);
    }
    
    public static String generateDataForMetawatch(){
    	return "data" + System.currentTimeMillis() + TextTool.getRandomString(50, 50).toLowerCase() + "@misfitqa.com";
    }
    
    public static String generateUserId(){
    	return TextTool.getRandomString(6, 6) + System.nanoTime() + "@misfitqa.com";
    }
    
    public static String generateDeviceModel(){
    	int number = MVPCommon.randInt(0, models.length - 1);
    	return models[number] + TextTool.getRandomString(4,4);
    }
    
    public static BaseResult pushBedditData(String accessKeyId){
    	String url = dataCenterBaseAddress + "beddit";
    	BaseParams requestInfo = new BaseParams(accessKeyId, true);
    	
    	
    	ServiceResponse response = MVPApi.post(url, port, requestInfo);
    	return new BaseResult(response);
    }
    
    public static String generateByteDataForMetaWatch() {
		return bytesToString(new byte[] { 1, 15, 84, 127, -19, -117, 0, 0,
				1, 26, 10, 20, 100, 15, 10, 20, 100, 15, 10, 20, 100, 15, 10,
				20, 100, 15, 10, 20, 100, 15, 10, 20, 100, 15, 10, 20, 100, 15,
				10, 20, 100, 15, 10, 20, 100, 15, 10, 20, 100, 15, 10, 20, 100,
				15, 10, 20, 100, 15, 10, 20, 100, 15, 10, 20, 100, 15, 10, 20,
				100, 15, 10, 20, 100, 15, 10, 20, 100, 15, 10, 20, 100, 15, 10,
				20, 100, 15, 10, 20, 100, 15, 10, 20, 100, 15, 10, 20, 100, 15,
				10, 20, 100, 15, 10, 20, 100, 15, 10, 20, 100, 15, 10, 20, 100,
				15, 10, 20, 100, 15, 10, 20, 100, 15, 10, 20, 100, 15, 10, 20,
				100, 15, 10, 20, 100, 15, 10, 20, 100, 15, 10, 20, 100, 15, 10,
				20, 100, 15, 10, 20, 100, 15, 10, 20, 100, 15, 10, 20, 100, 15,
				10, 20, 100, 15 });
	}
	
	private static String bytesToString(byte[] bytes, String seperator) {
		if (bytes == null || bytes.length == 0)
			return null;

		String format = "%02X";
		String formatWithSeperator = format + seperator;

		StringBuilder sb = new StringBuilder();

		int i = 0;
		while (i < bytes.length - 1) {
			sb.append(String.format(Locale.US, formatWithSeperator, bytes[i++]));
		}
		if (i == bytes.length - 1) {
			sb.append(String.format(Locale.US, format, bytes[i]));
		}
		return sb.toString().toLowerCase(Locale.US);
	}
	
	private static String bytesToString(byte[] bytes) {
		return bytesToString(bytes, "");
	}
	
	public static void main(String[] args){
		String accessKeyId = "56915349444458734-yGW6kkeeWJsdlkfi23jifjkdflkfkjf8";
		String secretKey = "eqFNHUAshCeQIZgSc48Yh80bTo9OVSYXkMfThspX";
		String userId = MVPApi.generateLocalId();
		String password = "qwerty";
		
//		MetaWatchModel metaWatch = new MetaWatchModel();
//		metaWatch.setData(generateByteDataForMetaWatch());
//		metaWatch.setUserId(userId);
//		metaWatch.setDeviceModel(MVPDCApi.generateDeviceModel());
//		
//		metaWatch.setSignature(metaWatch.calSignature(secretKey));
//		BaseResult result = MVPDCApi.pushMetaWatch(accessKeyId, metaWatch);
		
		// Step to get result
		System.err.println("-----Sign up misfit account");
		// sign up
		String email = MVPApi.generateUniqueEmail();
		MVPApi.signUp(email, password);
//		System.err.println("-----Register");
//		// register
//		MetawatchApi.registerMetawatch("token", userId);
//		// sign up && create profile
//		// create profile
//		System.err.println("-----Profile");
//		MetawatchProfileModel profile = MetawatchApi.DefaultProfile();
//		BaseResult result = MetawatchApi.createProfileMetawatch(email, password, profile,
//				userId);
//		// call get method
//		String response = result.response.getResponseString();
//		String url = response.substring(response.indexOf('h'));
//
//		result = MetawatchApi.getExchangeMetawatch(url);
		System.err.println("-----Sign in meta watch account");
		MetawatchApi.signInMetawatch(userId, email, password, true);
		System.err.println("-----Push data");
		// push data
		MetaWatchModel metaWatch = new MetaWatchModel();
		metaWatch.setData(generateByteDataForMetaWatch());
		metaWatch.setUserId(userId);
		metaWatch.setDeviceModel(MVPDCApi.generateDeviceModel());
		
		metaWatch.setSignature(metaWatch.calSignature(secretKey));
		MVPDCApi.pushMetaWatch(accessKeyId, metaWatch);
	}
}
