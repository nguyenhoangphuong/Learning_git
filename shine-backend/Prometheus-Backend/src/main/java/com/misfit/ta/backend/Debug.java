package com.misfit.ta.backend;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.correctness.servercalculation.ServerCalculationTestHelpers;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.beddit.BedditSleepSession;
import com.misfit.ta.utils.Files;

public class Debug {

	public static void main(String[] args) throws FileNotFoundException {
	
		List<BedditSleepSession> sleeps = new ArrayList<BedditSleepSession>();
		sleeps.add(DataGenerator.generateRandomBedditSleepSession(System.currentTimeMillis() / 1000, null));
		sleeps.add(DataGenerator.generateRandomBedditSleepSession(System.currentTimeMillis() / 1000, null));
		sleeps.add(DataGenerator.generateRandomBedditSleepSession(System.currentTimeMillis() / 1000, null));
		sleeps.add(DataGenerator.generateRandomBedditSleepSession(System.currentTimeMillis() / 1000, null));
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		MVPApi.createBedditSleepSession(token, sleeps.get(0));
		
		
//		OpenAPI.subscribeNotification("ANBwgUj0CtEpE06W", "Tp4cSIifwcO0YNAofWJ0KbuuWIVNk1nF", "https://tester.int.misfitwearables.com/handle_post.php", OpenAPI.RESOURCE_DEVICE);
//		OpenAPI.getAccessToken("thinh@misfitwearables.com", "misfit1", OpenAPI.allScopesAsString(), 
//				"ANBwgUj0CtEpE06W", "https://www.misfitwearables.com/");
//		MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq");
//		
//		Files.delete("rawdata");
//		Files.getFile("rawdata");
//		MVPApi.pushSDKSyncLog(ServerCalculationTestHelpers.createSDKSyncLogFromFilesInFolder(
//				System.currentTimeMillis() / 1000, 
//				"a@a.a", "adsasdasds", "rawdata/test1/1392170920"));
		
//		emails.add("test1396516200642vgbqkn@misfitqa.com");
//		SocialAPI.matchContacts(MVPApi.signIn("test1396516200642foiss0@misfitqa.com", "qqqqqq").token, emails);
	}
}
