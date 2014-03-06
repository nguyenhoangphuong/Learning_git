package com.misfit.ta.ios.tests;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.api.RequestHelper;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.internalapi.social.SocialAPI;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.CustomTimelineItemData;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws JSONException, IOException {

//		BackendHelper.unlink("nhhai16991@gmail.com", "qqqqqq");
		BackendHelper.link("nhhai16991@gmail.com", "qqqqqq", "9876543210");
	}

	 
	public static void deleteSessions() {
		
		String[] sessionsid = new String[] {
				"5200c060caedd01a1f58e32e",
				"5200cabecaedd01a1f58e87d",
				"52012c2fcaedd01a1f58e9a8",
				"5201ce34caedd01a1f58f608",
				"5201e768caedd01a1f590463",
				"5201ed58caedd01a1f5907ff",
				"5201f9cdcaedd01a1f590991",
				"5201fe70caedd01a1f5909c2",
				"520208f5caedd01a1f590b6a",
				"52027dbbcaedd01a1f590d61",
				"52030c34caedd01a1f591c23",
				"52030db6caedd01a1f591c9b",
				"52038795caedd03e466e2408",
				"5203cf37caedd03e466e31da",
				"5203cf40caedd03e466e31e2",
				"520494d3caedd031026e1717",
				"52049f9ccaedd031026e1987",
				"52052243caedd0656bd55000",
				"5206724ecaedd0656bd55eea",
				"5207c3d0caedd0656bd56d18",
				"5208a242caedd055ab35a3e9",
				"5209155ccaedd055ab35a8fd",
				"52099e55caedd055ab35b108",
				"520a66dccaedd055ab35bb04",
				"520c362ccaedd055ab35c7cb",
				"520d09dfcaedd055ab35d5f2",
				"520ddd9acaedd055ab35e324",
				"520dde50caedd055ab35e325",
				"520dded5caedd055ab35e327",
				"520de420caedd055ab35e436",
				"520deda3caedd055ab35e53b",
				"520df7b1caedd055ab35e644",
				"520e5b57caedd055ab35e74b",
				"520faca4caedd055ab35ec14",
				"5210fe1ecaedd055ab35ef1b",
				"521181afcaedd055ab35f220",
				"52118263caedd055ab35f256",
				"52118e34caedd055ab35f80c",
				"52118ef4caedd055ab35f826",
				"52118f76caedd055ab35f82b",
				"5211919acaedd055ab35f87c",
				"521198c6caedd055ab35f906",
				"52119b11caedd055ab35f93f",
				"52119cd7caedd055ab35f973",
				"52119d7ecaedd055ab35f992",
				"5211bafccaedd055ab35fc2e",
				"5211d6decaedd055ab360296",
				"52124fa2caedd055ab360895",
				"5212f32acaedd055ab361164",
				"52134178caedd055ab361cd3",
				"52142057caedd055ab362bc7",
				"52142b6ecaedd055ab362cd2",
				"521795adcaedd03a1cd0f2be",
				"5218e726caedd03a1cd100fe",
				"521a38a7caedd03a1cd10c3c",
				"521b1e30caedd03a1cd118b6",
				"521b8a30caedd03a1cd11c13",
				"521c5925caedd03a1cd12953",
				"521c91f8caedd03a1cd12b50",
				"521cdbb0caedd03a1cd1385c",
				"521d7d20caedd03a1cd147ac",
				"521d997ccaedd03a1cd14954",
				"521e2d08caedd03a1cd15c74",
				"5220d008caedd03a1cd1735a",
				"52216bfdcaedd03a1cd1845b",
				"52217123caedd03a1cd18658",
				"522176dacaedd03a1cd18855",
				"5213a11bcaedd055ab361d27",
				"5214f2bdcaedd03a1cd0e5a6",
				"52164426caedd03a1cd0e5fb",
		};
		
		BaseParams requestInf = new BaseParams();
		String url = "http://staging-api.misfitwearables.com/sessions/" + sessionsid[0];
		int port = 8101;
		RequestHelper.delete(url, port, requestInf);
		
//		String bucket = "shine-binary-data";
//		String username = "vyanh02@gmail.com";
//		String serial = "science006";
//		
//		int start = 22;
//		int end = 28;
//		
//		for(int i = start; i <= end; i++) {
//			
//			String prefix = "staging/2014/01/" + i + "/" + username + "/" + serial + "/";
//			List<String> objects = AWSHelper.listFiles(bucket, prefix);
//			
//			for(String obj : objects) {
//				
//				if(obj.contains("debug_log") || obj.contains("hardware_log") || obj.contains("metadata"))
//					continue;
//				
//				logger.info("Downloading " + obj);
//				String[] parts = obj.split("/");
//				String fileName = parts[parts.length - 1];
//				String timestampString = parts[parts.length - 2];
//				
//				AWSHelper.downloadFile(bucket, obj, "rawdata/" + i + "/" + timestampString + "/" + fileName);
//				
//			}
//		}

	}

}