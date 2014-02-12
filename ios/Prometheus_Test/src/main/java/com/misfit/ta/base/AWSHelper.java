package com.misfit.ta.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AWSHelper {

	private static String ACCESS_KEY = "AKIAJVQX34LED7SQ6P5Q";
	private static String SECRET_KEY = "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";

	public static AmazonS3 getS3Connection() {

		AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
		AmazonS3 conn = new AmazonS3Client(credentials);

		return conn;
	}

	public static File downloadFile(String bucketName, String s3Path, String filepath) {

		File file = new File(filepath);
		AmazonS3 conn = getS3Connection();
		conn.getObject(new GetObjectRequest(bucketName, s3Path), file);
		return file;
	}

	public static String downloadFileAsString(String bucketName, String s3Path) {

		File s3folder = new File("s3download");
		if (!s3folder.isDirectory())
			s3folder.mkdirs();

		String filepath = "s3download/" + System.currentTimeMillis();
		File file = downloadFile(bucketName, s3Path, filepath);
		BufferedReader br = null;
		String content = "";

		try {

			String line;
			br = new BufferedReader(new FileReader(file.getAbsolutePath()));

			while ((line = br.readLine()) != null) {
				content += (line + "\n");
			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			file.delete();
			FileUtils.deleteDirectory(s3folder);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}

	public static List<String> listFiles(String bucketName, String prefix) {

		AmazonS3 conn = getS3Connection();
		ListObjectsRequest request = new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix);
		ObjectListing objectListing;
		List<String> files = new ArrayList<String>();

		do {
			objectListing = conn.listObjects(request);
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
				files.add(objectSummary.getKey());
			}

			request.setMarker(objectListing.getNextMarker());
		} while (objectListing.isTruncated());

		return files;
	}

}