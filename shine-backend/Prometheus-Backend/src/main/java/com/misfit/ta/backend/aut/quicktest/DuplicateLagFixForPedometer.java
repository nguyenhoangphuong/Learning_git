package com.misfit.ta.backend.aut.quicktest;

import org.testng.Assert;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.pedometer.Pedometer;

public class DuplicateLagFixForPedometer {

	public static void main(String[] args) {
		
		runTest();
	}

	public static void assertPedometers(Pedometer expected, Pedometer acutal) {

		Assert.assertEquals(expected.getBatteryLevel(), acutal.getBatteryLevel(), "Updated at");
		Assert.assertEquals(expected.getServerId(), acutal.getServerId(), "Server id");
		Assert.assertEquals(expected.getLocalId(), acutal.getLocalId(), "Local id");
		Assert.assertEquals(expected.getUpdatedAt(), acutal.getUpdatedAt(), "Updated at");
	}

	public static void runTest() {
		
		int numberOfCreate = 10;
		int numberOfUpdate = 10;
		int numberOfRetry = 1;


		// create 
		long timestamp = System.currentTimeMillis() / 1000;
		for(int i = 0; i < numberOfCreate; i++) {

			String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
			Pedometer pedo = DataGenerator.generateRandomPedometer(timestamp, null);
			BaseResult result = MVPApi.createPedometer(token, pedo);

			Pedometer tmp = Pedometer.getPedometer(result.response);
			pedo.setUpdatedAt(tmp.getUpdatedAt());
			pedo.setServerId(tmp.getServerId());

			// query
			for(int j = 0; j < numberOfRetry; j++) {

				// user info
				result = MVPApi.userInfo(token);
				Pedometer latestPedo = Pedometer.getPedometer(result.response);

				assertPedometers(latestPedo, pedo);
			}
		}


		// update 
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		Pedometer pedo = DataGenerator.generateRandomPedometer(timestamp, null);
		BaseResult result = MVPApi.createPedometer(token, pedo);
		
		Pedometer tmp = Pedometer.getPedometer(result.response);
		pedo.setUpdatedAt(tmp.getUpdatedAt());
		pedo.setServerId(tmp.getServerId());
		
		for(int i = 1; i <= numberOfUpdate; i++) {

			tmp = new Pedometer();
			tmp.setBatteryLevel(pedo.getBatteryLevel() + 1);
			
			pedo.setBatteryLevel(tmp.getBatteryLevel());
			result = MVPApi.updatePedometer(token, tmp);
			
			tmp = Pedometer.getPedometer(result.response);
			pedo.setUpdatedAt(tmp.getUpdatedAt());

			// query
			for(int j = 0; j < numberOfRetry; j++) {

				// user info
				result = MVPApi.userInfo(token);
				Pedometer latestPedo = Pedometer.getPedometer(result.response);

				assertPedometers(latestPedo, pedo);
			}
		}
	}
}
