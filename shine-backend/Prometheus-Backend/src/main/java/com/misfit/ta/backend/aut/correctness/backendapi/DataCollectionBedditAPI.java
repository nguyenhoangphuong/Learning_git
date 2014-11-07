package com.misfit.ta.backend.aut.correctness.backendapi;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MultipartUtility;
import com.misfit.ta.backend.aut.BackendAutomation;

public class DataCollectionBedditAPI extends BackendAutomation{
	
	
	@Test(groups = { "DataCollection", "BedditAPI" })
	public void pushBedditDataSuccessfully(){
		String charset = "UTF-8";
        File uploadFile1 = new File("/Users/trunghoang/Downloads/beddit-example/sleep_1413745423_1413745440");

        String requestURL = "https://data.int.api.misfitwearables.com/beddit";
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
             
            multipart.addFormField("user_id", "trunghoang");
            multipart.addFormField("device_model", "Android");
             
            multipart.addFilePart("thumbnail", uploadFile1);
            
 
            List<String> response = multipart.finish();
             
            System.out.println("SERVER REPLIED:");
             
            for (String line : response) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
	}
	
}
