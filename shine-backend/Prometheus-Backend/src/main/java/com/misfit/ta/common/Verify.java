package com.misfit.ta.common;

import java.util.List;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.report.TRS;

public class Verify {

	protected static Logger logger = Util.setupLogger(Verify.class);
	
	public static String verifyTrue(boolean actual, String message) {
		
		if(!actual) {
			message = message + " expected true but found false";
			logger.error(message);
			TRS.instance().addCode(message, null);
			return message;
		}
		
		return null;
	}
	
	public static String verifyEquals(Object actual, Object expect, String message) {
		
		if(!expect.equals(actual)) {
			message = message + " expected [" + expect + "] but found [" + actual + "]";
			logger.error(message);
			TRS.instance().addCode(message, null);
			return message;
		}
		
		return null;
	}
	
	public static String verifyNotEquals(Object actual, Object expect, String message) {
		
		if(expect.equals(actual)) {
			message = message + " expected not [" + expect + "] but found [" + actual + "]";
			logger.error(message);
			TRS.instance().addCode(message, null);
			return message;
		}
		
		return null;
	}
	
	public static String verifyNearlyEquals(double actual, double expect, double delta, String message) {
		
		double diff = Math.abs(actual - expect); 
		if(diff > delta) {
			message = message + " expect difference not greater than [" + delta + "] but found [" + diff + "] (" +
					actual + " - " + expect + ")";
			logger.error(message);
			TRS.instance().addCode(message, null);
			return message;
		}
		
		return null;
	}
	
	public static String verifyContainsNoCase(String actualString, String expectContainString, String message) {
		
		if(!actualString.toLowerCase().contains(expectContainString.toLowerCase())) {
			message = message + " expected contains [" + expectContainString + "] but doesn't: [" + actualString + "]";
			logger.error(message);
			TRS.instance().addCode(message, null);
			return message;
		}
		
		return null;
	}
	
	public static boolean verifyAll(List<String> messages) {
		
		logger.error("\nErrors: ");
		TRS.instance().addStep("Errors:", "errors");
		
		boolean pass = true;
		for(String error : messages) {
			if(error != null) {
				logger.error(error);
				TRS.instance().addCode("- " + error, null);
				pass = false;
			}
		}
		
		return pass;
	}
	
}
