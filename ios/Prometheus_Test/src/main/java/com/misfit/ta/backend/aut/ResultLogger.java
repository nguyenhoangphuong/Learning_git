package com.misfit.ta.backend.aut;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ResultLogger {

	private static ResultLogger logger;
	private static String loggername;

	private static File file;
	private static FileWriter writer;
	private static Map<Integer, Integer> errors;
	private static int request = 0;
	private static int response = 0;

	// We track total time of request which doesn't have response missing
	public static long totalTime;
	public static long totalTestRunTime;

	public static ResultLogger getLogger(String testname) {
		if (loggername != null && !loggername.equalsIgnoreCase(testname))
			logger.close();

		if (logger == null) {
			logger = new ResultLogger(testname);
			if (errors != null) {
				errors.clear();
			}
			request = 0;
			response = 0;
		}

		totalTime = 0;
		return logger;
	}

	protected ResultLogger(String testname) {
		File logdir = new File("logs");
		if(!logdir.isDirectory()) {
			logdir.mkdirs();
		}
		
		file = new File("logs/result_" + testname + ".log");
		loggername = testname;
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			System.out.print("errr");
		}
	}

	public void log(String message) {
		
	    try {
		    
			writer.write(message + "\n");
			writer.flush();
		} catch (IOException e) {
		    System.out.println("LOG [ResultLogger.log]:  exception: " + e);
		}

	}

	public void close() {
		if (writer != null) {
			try {
				writer.close();
				writer = null;
				file = null;
				logger = null;
				loggername = null;
				errors = null;
			} catch (IOException e) {
			}
		}
	}

	public static void addErrorCode(int code) {
		if (logger == null) {
			return;
		} else if (errors == null) {
			errors = new HashMap<Integer, Integer>();
		}

		Integer count = errors.get(code);
		if (count == null || count.intValue() == 0) {
			errors.put(code, 1);
		} else {
			errors.put(code, count + 1);
		}

	}

	public static void registerRequest() {
		request++;
	}

	public static void registerResponse() {
		response++;
	}

	public static void logErrorSummary() {
		if (errors == null || errors.isEmpty()) {
			return;
		}
		StringBuffer buf = new StringBuffer();
		buf.append("\n\n======================================\n");
		buf.append("Request sent:\t" + request + "\n");
		buf.append("Response received:\t" + response + "\n");
		buf.append("Missing response:\t" + (request - response) + "\n");
		buf.append("HTTP error distribution:\n");
		int count = 0;

		Iterator<Integer> it = errors.keySet().iterator();
		while (it.hasNext()) {
			Integer code = it.next();
			Integer value = errors.get(code);
			count += value;
			buf.append("Error" + code + ": \t" + value + "\n");
		}
		buf.append("-------------------\n");
		buf.append("Total: \t" + count + "\n");
		buf.append("Total time of requests: " + totalTestRunTime + "ms\n");
		buf.append("Average time per request: " + (totalTime / request) + "ms\n");
		buf.append("Requests/second: " + request / (totalTestRunTime / 1000));
		System.out.println("LOG [ResultLogger.logErrorSummary]: " + buf.toString());
		logger.log(buf.toString());
	}

}
