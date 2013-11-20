package com.misfit.ta.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.aut.SocialAutomationBase;
import com.misfit.ta.backend.data.BaseResult;

public class ServerResultSummary {

	protected static Logger logger = Util.setupLogger(SocialAutomationBase.class);
	
	protected Map<Integer, Integer> statusCodeCountMap;
	protected List<Integer> statusCodes;
	
	
	// constructor
	public ServerResultSummary() {
		
		statusCodeCountMap = new HashMap<Integer, Integer>();
		statusCodes = new ArrayList<Integer>();
	}
	
	
	// methods
	public void addBaseResult(BaseResult result) {
		
		Integer count = statusCodeCountMap.get(result.statusCode);
		if(count == null) {
			count = 0;
			statusCodes.add(result.statusCode);
		}
		
		statusCodeCountMap.put(result.statusCode, ++count);
	}
	
	public void printSummary() {
		
		Collections.sort(statusCodes);
		
		logger.info("Number of status codes: " + statusCodes.size());
		logger.info("---------------------------------------------------------------------------");	
		for(Integer statusCode : statusCodes)
			logger.info(statusCode + ": " + statusCodeCountMap.get(statusCode));
		logger.info("---------------------------------------------------------------------------");
	}


	public String errorCodeCountAsString() {
		
		String result = "";
		for(Integer statusCode : statusCodes)
			result += (", " + statusCode + ": " + statusCodeCountMap.get(statusCode));
		return result.substring(2);
	}
	
}
