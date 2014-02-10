package com.misfit.ta.android.gui;

import java.util.Random;

import com.misfit.ta.utils.TextTool;

public class PrometheusHelper {
	public static String generateUniqueEmail() {
		return "test" + System.currentTimeMillis()
				+ TextTool.getRandomString(6, 6) + "@misfitqa.com";
	}
	
	public static int randInt(int includeFrom, int excludeTo) {
		Random r = new Random();
		return r.nextInt(excludeTo - includeFrom) + includeFrom;
	}
}
