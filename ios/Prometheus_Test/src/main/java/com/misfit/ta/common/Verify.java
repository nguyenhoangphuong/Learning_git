package com.misfit.ta.common;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

public class Verify {

	protected static Logger logger = Util.setupLogger(Verify.class);
	
	public static void verifyTrue(boolean actual, String message) {
		
		if(!actual) {
			logger.error(message + " expected true but found false");
		}
	}
	
	public static void verifyEquals(Object actual, Object expect, String message) {
		
		if(!expect.equals(actual)) {
			logger.error(message + " expected [" + expect + "] but found [" + actual + "]");
		}
	}
	
}
