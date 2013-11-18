package com.misfit.ta.backend.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.api.MVPApi;


public class BaseResult {
	// logger
	protected static Logger logger = Util.setupLogger(MVPApi.class);

	// fields: these field normally transparent to users, just store in case of
	// someone want do complex things
	public ServiceResponse response;
	public String rawData;
	protected JSONObject json;

	// fileds: these fields too, should transparent to users, simplize at middle
	// level, inherit class should add result to pairResult
	public Map<String, Object> pairResult = new HashMap<String, Object>();
	public int statusCode;
	public String errorMessage;
	public int errorCode;

	// constructor
	public BaseResult(ServiceResponse response) {
		
		// raw data
		this.response = response;
		this.rawData = response.getContentData().toString();

		if (!this.rawData.trim().isEmpty()) {
		    try {
                json = new JSONObject(this.rawData.toString());
            } catch (JSONException e1) {
            }

			// middle level data
			if (!json.isNull("error_message")) {
				try {
                    this.errorMessage = json.getString("error_message");
                    this.pairResult.put("error_message", this.errorMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
			}
			
			if (!json.isNull("error_code")) {
				try {
                    this.errorCode = Integer.valueOf(json.getString("error_code"));
                    this.pairResult.put("error_code", this.errorCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
			}
		}

		this.statusCode = response.getStatusCode();
		this.pairResult.put("status_code", this.statusCode);
	}

	// utilities functions
	public void printKeyPairsValue() {
		logger.info("-------------------------------------------------------------------------------------------------------");
		
		for (String key : this.pairResult.keySet()) {
			if (this.pairResult.get(key) != null &&
					this.pairResult.get(key).getClass().isArray())
				logger.info(String.format("%30s", key) + ": "
						+ Arrays.toString((Object[]) this.pairResult.get(key)));
			else
				logger.info(String.format("%30s", key) + ": "
						+ this.pairResult.get(key));
		}
		
		logger.info("-------------------------------------------------------------------------------------------------------");
	}

	// helpers functions
	public boolean isOK() {
		return this.statusCode == 200;
	}

	public boolean isExisted() {
		return this.statusCode == 210;
	}

	public boolean isAuthInvalid() {
		return this.statusCode == 401;
	}

	public boolean isNotFound() {
		return this.statusCode == 404;
	}

	public boolean isNotSupported() {
		return this.statusCode == 403;
	}

	public boolean isServerErr() {
		return this.statusCode == 500;
	}

	public boolean isEmptyErrMsg() {
		return this.errorMessage == null ||
				this.errorMessage.equals("null") ||
				this.errorMessage.isEmpty();
	}

}
