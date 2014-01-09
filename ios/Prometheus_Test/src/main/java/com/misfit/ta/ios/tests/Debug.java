package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.utils.TextTool;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws IOException {
		
		
//		Gui.init("192.168.1.115");
//		HomeScreen.pullToRefresh();
		ResultLogger logger = ResultLogger.getLogger("serial.log");
		String serialnumber = TextTool.getRandomString(7, 7);
		
		for(int i = 0; i < 100; i++) {
			String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
			Pedometer pedo = DataGenerator.generateRandomPedometer(System.currentTimeMillis() / 1000, null);
			String fullSerial = serialnumber + String.format("%03d", i);
			pedo.setSerialNumberString(fullSerial);
		
			logger.log(fullSerial);
			MVPApi.createPedometer(token, pedo);
		}
		
	}
}
	