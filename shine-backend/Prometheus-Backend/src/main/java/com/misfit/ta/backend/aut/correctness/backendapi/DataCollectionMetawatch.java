package com.misfit.ta.backend.aut.correctness.backendapi;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPMetawatchApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.MetaWatch.MetaWatchModel;

public class DataCollectionMetawatch extends BackendAutomation{
	
	private String accessKeyId = "56915349444458734-yGW6kkeeWJsdlkfi23jifjkdflkfkjf8";
	private String secretKey = "eqFNHUAshCeQIZgSc48Yh80bTo9OVSYXkMfThspX";
	
	@Test(groups = { "DataCollection", "Metawatch" })
	public void createMetawatchSuccessfully(){
		MetaWatchModel metaWatch = new MetaWatchModel();
		metaWatch.setData(MVPMetawatchApi.generateDataForMetawatch());
		metaWatch.setUserId(MVPMetawatchApi.generateUserId());
		metaWatch.setDeviceModel(MVPMetawatchApi.generateDeviceModel());
		
		metaWatch.setSignature(metaWatch.calSignature(secretKey));
		BaseResult result = MVPMetawatchApi.pushMetaWatch(accessKeyId, metaWatch);
		
		Assert.assertEquals(result.statusCode, 200, "Push Metawatch");
	}

	@Test(groups = { "DataCollection", "Metawatch" })
	public void createMetawatchWrongSignature(){
		MetaWatchModel metaWatch = new MetaWatchModel();
		metaWatch.setData(MVPMetawatchApi.generateDataForMetawatch());
		metaWatch.setUserId(MVPMetawatchApi.generateUserId());
		metaWatch.setDeviceModel(MVPMetawatchApi.generateDeviceModel());
		
		metaWatch.setSignature(MVPMetawatchApi.generateDataForMetawatch());
		BaseResult result = MVPMetawatchApi.pushMetaWatch(accessKeyId, metaWatch);
		
		Assert.assertEquals(result.statusCode, 400, "Push Metawatch");
	}
	
	@Test(groups = { "DataCollection", "Metawatch" })
	public void createMetawatchWrongSecretKey(){
		MetaWatchModel metaWatch = new MetaWatchModel();
		metaWatch.setData(MVPMetawatchApi.generateDataForMetawatch());
		metaWatch.setUserId(MVPMetawatchApi.generateUserId());
		metaWatch.setDeviceModel(MVPMetawatchApi.generateDeviceModel());
		
		metaWatch.setSignature(metaWatch.calSignature("abc"));
		BaseResult result = MVPMetawatchApi.pushMetaWatch(accessKeyId, metaWatch);
		
		Assert.assertEquals(result.statusCode, 400, "Push Metawatch");
	}
	
	@Test(groups = { "DataCollection", "Metawatch" })
	public void createMetawatchNotEnoughData(){
		// user_id is null/ empty
		MetaWatchModel metaWatch = new MetaWatchModel();
		metaWatch.setData(MVPMetawatchApi.generateDataForMetawatch());
		metaWatch.setUserId(" ");
		metaWatch.setDeviceModel(MVPMetawatchApi.generateDeviceModel());
		
		metaWatch.setSignature(metaWatch.calSignature(secretKey));
		BaseResult result = MVPMetawatchApi.pushMetaWatch(accessKeyId, metaWatch);
		Assert.assertEquals(result.statusCode, 400, "user_id is null");
		
		// data is null/ empty
		metaWatch = new MetaWatchModel();
		metaWatch.setData(" ");
		metaWatch.setUserId(MVPMetawatchApi.generateUserId());
		metaWatch.setDeviceModel(MVPMetawatchApi.generateDeviceModel());
		
		metaWatch.setSignature(metaWatch.calSignature(secretKey));
		result = MVPMetawatchApi.pushMetaWatch(accessKeyId, metaWatch);
		Assert.assertEquals(result.statusCode, 400, "data is null");
		
		// device model is null/ empty
		metaWatch = new MetaWatchModel();
		metaWatch.setData(MVPMetawatchApi.generateDataForMetawatch());
		metaWatch.setUserId(MVPMetawatchApi.generateUserId());
		metaWatch.setDeviceModel(" ");
		
		metaWatch.setSignature(metaWatch.calSignature(secretKey));
		result = MVPMetawatchApi.pushMetaWatch(accessKeyId, metaWatch);
		Assert.assertEquals(result.statusCode, 400, "device model is null");
		
		// not hmac with all data
		metaWatch = new MetaWatchModel();
		metaWatch.setData(MVPMetawatchApi.generateDataForMetawatch());
		metaWatch.setUserId(MVPMetawatchApi.generateUserId());
		metaWatch.setDeviceModel(MVPMetawatchApi.generateDeviceModel());
		
		metaWatch.setSignature(metaWatch.calSignatureWithoutUserId(secretKey));
		result = MVPMetawatchApi.pushMetaWatch(accessKeyId, metaWatch);
		Assert.assertEquals(result.statusCode, 400, "Without User Id");
		
		metaWatch = new MetaWatchModel();
		metaWatch.setData(MVPMetawatchApi.generateDataForMetawatch());
		metaWatch.setUserId(MVPMetawatchApi.generateUserId());
		metaWatch.setDeviceModel(MVPMetawatchApi.generateDeviceModel());
		
		metaWatch.setSignature(metaWatch.calSignatureWithoutData(secretKey));
		result = MVPMetawatchApi.pushMetaWatch(accessKeyId, metaWatch);
		Assert.assertEquals(result.statusCode, 400, "Without Data");
		
		metaWatch = new MetaWatchModel();
		metaWatch.setData(MVPMetawatchApi.generateDataForMetawatch());
		metaWatch.setUserId(MVPMetawatchApi.generateUserId());
		metaWatch.setDeviceModel(MVPMetawatchApi.generateDeviceModel());
		
		metaWatch.setSignature(metaWatch.calSignatureWithoutDeviceModel(secretKey));
		result = MVPMetawatchApi.pushMetaWatch(accessKeyId, metaWatch);
		Assert.assertEquals(result.statusCode, 400, "Without device model");
	}
}
