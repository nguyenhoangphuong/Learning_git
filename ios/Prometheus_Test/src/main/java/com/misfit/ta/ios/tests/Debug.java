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

}