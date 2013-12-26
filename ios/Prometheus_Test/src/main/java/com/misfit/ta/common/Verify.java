package com.misfit.ta.common;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.report.TRS;

public class Verify {

	protected static Logger logger = Util.setupLogger(Verify.class);
	
	public static boolean verifyTrue(boolean actual, String message) {
		
		if(!actual) {
			logger.error(message + " expected true but found false");
			TRS.instance().addCode(message + " expected true but found false", null);
			return false;
		}
		
		return true;
	}
	
	public static boolean verifyEquals(Object actual, Object expect, String message) {
		
		if(!expect.equals(actual)) {
			logger.error(message + " expected [" + expect + "] but found [" + actual + "]");
			TRS.instance().addCode(message + " expected [" + expect + "] but found [" + actual + "]", null);
			return false;
		}
		
		return true;
	}
	
	public static boolean verifyNearlyEquals(double actual, double expect, double delta, String message) {
		
		double diff = Math.abs(actual - expect); 
		if(diff > delta) {
			logger.error(message + " expect difference not greater than [" + delta + "] but found [" + diff + "] (" +
					actual + " - " + expect + ")");
			TRS.instance().addCode(message + " expect difference not greater than [" + delta + "] but found [" + diff + "] (" +
					actual + " - " + expect + ")", null);
			return false;
		}
		
		return true;
	}
	
}
