package com.misfit.ta.ios.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.json.JSONException;
import com.misfit.ta.base.AWSHelper;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws JSONException, IOException {

//		File file = Files.getFile("rawdata/0101");
//		logger.info(SyncFileData.getFileTimestampFromRawData(FileUtils.readFileToString(file)));
		
		String bucket = "shine-binary-data";
		String username = "vyanh02@gmail.com";
		String serial = "science006";
		
		int start = 22;
		int end = 28;
		
		for(int i = start; i <= end; i++) {
			
			String prefix = "staging/2014/01/" + i + "/" + username + "/" + serial + "/";
			List<String> objects = AWSHelper.listFiles(bucket, prefix);
			
			for(String obj : objects) {
				
				if(obj.contains("debug_log") || obj.contains("hardware_log") || obj.contains("metadata"))
					continue;
				
				logger.info("Downloading " + obj);
				String[] parts = obj.split("/");
				String fileName = parts[parts.length - 1];
				String timestampString = parts[parts.length - 2];
				
				AWSHelper.downloadFile(bucket, obj, "rawdata/" + i + "/" + timestampString + "/" + fileName);
				
			}
		}
	}

}