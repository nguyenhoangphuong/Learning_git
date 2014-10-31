package com.misfit.ta.backend.aut.correctness.backendapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MultipartUtility;
import com.misfit.ta.backend.aut.BackendAutomation;

public class DataCollectionBedditAPI extends BackendAutomation{
	
	@Test(groups = { "DataCollection", "BedditAPI" })
	public void demo(){
		String charset = "UTF-8";
		File uploadFile1 = new File("/Users/trunghoang/Downloads/sleep_1413745423_1413745440");
		String requestURL = "https://data.int.api.misfitwearables.com/beddit";

		try {
			MultipartUtility multipart = new MultipartUtility(requestURL, charset);
			
			multipart.addFormField("user_id", "qwerty@misfit.com");
			
			multipart.addFilePart("file", uploadFile1);

			List<String> response = multipart.finish();
			
			System.out.println("SERVER REPLIED:");
			
			for (String line : response) {
				System.out.println(line);
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
//		try{
//		 BufferedReader br = new BufferedReader(new FileReader("/Users/trunghoang/Downloads/sleep_1413745423_1413745440"));
//		 StringBuilder sb = new StringBuilder();
//		 String line = br.readLine();
//
//		 while (line != null) {
//		     sb.append(line).append("\r\n");
//		     line = br.readLine();
//		 }
//		 String everything = sb.toString();
//		 System.out.println("Content : " + everything);
//		}catch(Exception e){
//			System.out.println("Error!");
//		}
	}
}
