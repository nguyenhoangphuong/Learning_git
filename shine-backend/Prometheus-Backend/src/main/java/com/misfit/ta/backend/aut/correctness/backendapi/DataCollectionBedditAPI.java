package com.misfit.ta.backend.aut.correctness.backendapi;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPDCApi;
import com.misfit.ta.backend.api.internalapi.MultipartUtility;
import com.misfit.ta.backend.aut.BackendAutomation;

public class DataCollectionBedditAPI extends BackendAutomation{
	
	File uploadFile1 = new File("/Users/trunghoang/Downloads/beddit-example/sleep_1413745423_1413745440");
	String requestURL = "https://data.int.api.misfitwearables.com/beddit";
	String charset = "UTF-8";
	
	@Test(groups = { "DataCollection", "BedditAPI" })
	public void pushBedditDataSuccessfully(){
        
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
             
            multipart.addFormField("user_id", MVPDCApi.generateUserId());
            multipart.addFormField("device_model", MVPDCApi.generateDeviceModel());
             
            multipart.addFilePart("thumbnail", uploadFile1);
            
 
            List<String> response = multipart.finish();
             
            System.out.println("SERVER REPLIED:");
             
            for (String line : response) {
                System.out.println(line);
                Assert.assertEquals(line, "OK", "Test failed!");
            }
            
        } catch (IOException ex) {
            System.err.println(ex);
        }
	}
	
	@Test(groups = { "DataCollection", "BedditAPI" })
	public void pushBedditDataWithUserIdNull(){
		try {
			MultipartUtility multipart = new MultipartUtility(requestURL,
					charset);

			multipart.addFormField("user_id", " ");
			multipart.addFormField("device_model",
					MVPDCApi.generateDeviceModel());

			multipart.addFilePart("thumbnail", uploadFile1);

			List<String> response = multipart.finish();

			System.out.println("SERVER REPLIED:");

			for (String line : response) {
				System.out.println(line);
				Assert.assertNotEquals(line, "OK", "Test failed!");
			}
		} catch (IOException ex) {
			System.err.println(ex);
        }
	}
	
	@Test(groups = { "DataCollection", "BedditAPI" })
	public void pushBedditDataWithModelNull(){
		try {
			MultipartUtility multipart = new MultipartUtility(requestURL,
					charset);

			multipart.addFormField("user_id", MVPDCApi.generateUserId());
			multipart.addFormField("device_model",
					" ");

			multipart.addFilePart("thumbnail", uploadFile1);

			List<String> response = multipart.finish();

			System.out.println("SERVER REPLIED:");

			for (String line : response) {
				System.out.println(line);
				Assert.assertNotEquals(line, "OK", "Test failed!");
			}
		} catch (IOException ex) {
			System.err.println(ex);
        }
	}
}
