package com.misfit.ta.ios.tests;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.json.JSONException;
import com.misfit.ta.backend.aut.correctness.servercalculation.ServerCalculationTestHelpers;
import com.misfit.ta.utils.Files;


public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws JSONException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

		Files.delete("rawdata");
		Files.getFile("rawdata");
		ServerCalculationTestHelpers.runTest("rawdata/test1", "dcsc009@a.a", "qqqqqq");
	}

}