package com.misfit.ta.backend.api;

import static com.google.resting.component.EncodingTypes.UTF8;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.Resting;
import com.google.resting.component.content.IContentData;
import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.method.delete.DeleteHelper;
import com.google.resting.method.post.PostHelper;
import com.google.resting.method.put.PutHelper;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.report.TRS;

public class RequestHelper {

	protected static Logger logger = Util.setupLogger(RequestHelper.class);
	
	public static final String HTTP_POST = "post";
	public static final String HTTP_GET = "get";
	public static final String HTTP_DELETE = "delete";
	public static final String HTTP_PUT = "put";
	
	// request helpers
	static public ServiceResponse request(String type, String url, int port, BaseParams requestInf) {
		
		// log address
		logger.info(type.toUpperCase() + ": " + url + " - port: " + port);
		logger.info("Request headers: " + requestInf.getHeadersAsJsonString());
		logger.info("Request params: " + requestInf.getParamsAsJsonString());

		// send to TRS
		TRS.instance().addStep(type.toUpperCase() + ": " + url + " - port: " + port, null);
		TRS.instance().addCode("Request headers: " + requestInf.getHeadersAsJsonString(), null);
		TRS.instance().addCode("Request params: " + requestInf.getParamsAsJsonString(), null);

		// wrapper send request
		ServiceResponse response = null;
		ResultLogger.registerRequest();
		if (type.equalsIgnoreCase(MVPApi.HTTP_POST))
			response = PostHelper.post(url, port, UTF8, requestInf.params, requestInf.headers);
		else if (type.equalsIgnoreCase(MVPApi.HTTP_GET))
			response = Resting.get(url, port, requestInf.params, UTF8, requestInf.headers);
		else if (type.equalsIgnoreCase(MVPApi.HTTP_PUT))
			response = PutHelper.put(url, UTF8, port, requestInf.params, requestInf.headers);
		else if (type.equalsIgnoreCase(MVPApi.HTTP_DELETE))
			response = DeleteHelper.delete(url, port, requestInf.params, UTF8, requestInf.headers);

		// log result
		IContentData rawData = response.getContentData();
		logger.info("Response raw Data: " + rawData);
		ResultLogger.registerResponse();
		int error = response.getStatusCode();
		ResultLogger.addErrorCode(error);
		logger.info("Response code: " + error + "\n\n");

		// send to TRS
		TRS.instance().addCode("Response raw Data: " + rawData, null);
		TRS.instance().addCode("Response code: " + error + "\n\n", null);

		return response;
	}

	static public ServiceResponse post(String url, int port, BaseParams requestInf) {
		return request("post", url, port, requestInf);
	}

	static public ServiceResponse get(String url, int port, BaseParams requestInf) {
		return request("get", url, port, requestInf);
	}

	static public ServiceResponse put(String url, int port, BaseParams requestInf) {
		return request("put", url, port, requestInf);
	}

	static public ServiceResponse delete(String url, int port, BaseParams requestInf) {
		return request("delete", url, port, requestInf);
	}

}
