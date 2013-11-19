package com.misfit.ta.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MVPCommon {

	// helpers
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
	
}
