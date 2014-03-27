package com.misfit.ta.ios.tests;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.aut.correctness.servercalculation.ServerCalculationTestHelpers;
import com.misfit.ta.backend.data.sync.sdk.SDKSyncLog;


public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	// Shared queue for notifications from HTTP server
    static BlockingQueue<Map<String, String>> messageQueue = new LinkedBlockingQueue<Map<String, String>>();

	
	public static void main(String[] args) throws Exception {

		BackendHelper.link("haihonghao@qa.com", "qqqqqq", "HaiHongHao");
		BackendHelper.link("haidangyeu@qa.com", "qqqqqq", "HaiDangYeu");
//		Files.delete("rawdata");
//		Files.getFile("rawdata");
//		ServerCalculationTestHelpers.runTest("rawdata/test1", "dcsc047@a.a", "qqqqqq");

//		Gui.init("192.168.1.224");
//		ViewUtils.isExistedView("UILabel", "goal weight: 140.0");
//		ViewUtils.isExistedView("UILabel", "goal weight: _140.0_");
		
//		logger.info(ServerCalculationTestHelpers.createSDKSyncLogFromFilesInFolder(System.currentTimeMillis() / 1000,
//				"dc_performance@qa.com", "ABCDEFGHIJ", "rawdata/test1/1392170920").toJson().toString());
		
	}
}