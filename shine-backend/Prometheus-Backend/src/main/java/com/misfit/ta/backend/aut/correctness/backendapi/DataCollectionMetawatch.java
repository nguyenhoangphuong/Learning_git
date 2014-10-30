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
		MVPMetawatchApi.pushMetaWatch(accessKeyId, metaWatch);
		
//		Assert.assertEquals(result.statusCode, 200, "Push Metawatch");
	}
}
