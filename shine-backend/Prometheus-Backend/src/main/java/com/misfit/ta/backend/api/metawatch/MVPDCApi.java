package com.misfit.ta.backend.api.metawatch;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.RequestHelper;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.MetaWatch.MetaWatchModel;
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
}
