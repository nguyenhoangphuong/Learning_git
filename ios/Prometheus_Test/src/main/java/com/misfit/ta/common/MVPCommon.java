package com.misfit.ta.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.utils.Files;

public class MVPCommon {

	// random
	public static int randInt(int includeFrom, int includeTo) {
		
		Random r = new Random();
		return r.nextInt(includeTo - includeFrom + 1) + includeFrom;
	}
	
	public static long randLong(long includeFrom, long includeTo) {
		
		Random r = new Random();
		return (long)r.nextInt((int)includeTo - (int)includeFrom + 1) + includeFrom;
	}
	
	public static boolean coin() {
		
		return System.nanoTime() % 2 == 0;
	}
	
	
	// conver number / bytes array to little endian string
	public static String toLittleEndianString(Integer number) {
		
		Integer reversed = Integer.reverseBytes(number);
		return String.format("%08X", reversed);
	}
	
	public static String toLittleEndianString(Long number) {
		
		Long reversed = Long.reverseBytes(number);
		return String.format("%016X", reversed).substring(0, 8);
	}
	
	public static String toLittleEndianString(List<Byte> bytes, boolean reverse) {
		
		if(reverse)
			Collections.reverse(bytes);
		
		String r = "";
		for(Byte b : bytes)
			r += String.format("%02X", b);
		
		return r;
	}
	
	
	// LE and BE string utilities
	public static List<Byte> hexStringToByteArray(String s) {
		
	    int len = s.length();
	    List<Byte> bytes = new ArrayList<Byte>();
	    
	    for (int i = 0; i < len; i += 2) {
	        Byte b = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	        bytes.add(b);
	    }
	    return bytes;
	}

	public static String convertLEtoBEString(String littleEndianString) {
		
		if(littleEndianString.length() % 2 != 0)
			return null;
		
		StringBuffer bigEndianStringBuffer = new StringBuffer();
		for(int i = 0; i < littleEndianString.length(); i += 2) {
			String byteString = littleEndianString.substring(i, i + 2);
			bigEndianStringBuffer.insert(0, byteString);
		}
		
		return bigEndianStringBuffer.toString();
	}

	
	// convert little endian string to number
	public static long litteEndianStringToLong(String LEString) {
		
		String BEString = convertLEtoBEString(LEString);
		return Long.parseLong(BEString, 16);
	}
	
	public static int litteEndianStringToInteger(String LEString) {
		
		String BEString = convertLEtoBEString(LEString);
		return Integer.parseInt(BEString, 16);
	}

	
	// number
	public static double round(double number, int decimalDigit) {
		
		double k = Math.pow(10, decimalDigit);
		return Math.round(number * k) / k;
	}
	
	
	// encoding
	public static String readFileAsBase64String(String filePath) {
		
		try {
			File file = Files.getFile(filePath);
			byte[] bytes = FileUtils.readFileToByteArray(file);
//			return Base64.encodeBase64String(bytes);
			return Base64.encodeBase64URLSafeString(bytes);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	// mvp related
	public static String getActivityName(int activityType) {
		
		switch(activityType) {
		
		case MVPEnums.ACTIVITY_WALKING:
			return DefaultStrings.WalkingLabel;
			
		case MVPEnums.ACTIVITY_RUNNING:
			return DefaultStrings.RunningLabel;
			
		case MVPEnums.ACTIVITY_CYCLING:
			return DefaultStrings.CyclingLabel;
			
		case MVPEnums.ACTIVITY_SOCCER:
			return DefaultStrings.SoccerLabel;
			
		case MVPEnums.ACTIVITY_SWIMMING:
			return DefaultStrings.SwimmingLabel;
			
		case MVPEnums.ACTIVITY_TENNIS:
			return DefaultStrings.TennisLabel;
			
		case MVPEnums.ACTIVITY_BASKETBALL:
			return DefaultStrings.BasketballLabel;
			
		case MVPEnums.ACTIVITY_SLEEPING:
			return DefaultStrings.SleepLabel;
		}
		
		return null;
	}
}
