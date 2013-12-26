package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.Timeline;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws IOException {
		
		Gui.init("192.168.1.115");
		Timeline.openTile("5:59am ");

		// api: update statistics to set best point to 400 pts
//		BackendHelper.setPersonalBest(token, 400);
		
//		BaseParams.CurrentLocale = "ja";
//		String token = MVPApi.signIn("nhhai16991@gmail.com", "qqqqqq").token;
		
//		TimelineItem item = DataGenerator.generateRandomMilestoneItem(System.currentTimeMillis() / 1000 - MVPCommon.randInt(3000, 10000), TimelineItemDataBase.EVENT_TYPE_STREAK, null);
//		MilestoneItem milestone = (MilestoneItem) item.getData();
//		milestone.getInfo().setStreakNumber(10);
//		MVPApi.createTimelineItem(token, item);

//		Pedometer pedo = new Pedometer();
//		pedo.setBatteryLevel(10);
//		pedo.setLastSyncedTime(System.currentTimeMillis() / 1000);
//		pedo.setLastSuccessfulTime(System.currentTimeMillis() / 1000);
//		MVPApi.updatePedometer(token, pedo);
		
//		int numberOfGoals = 17;
//		
//		String[] emails = new String[] 
//				{
//					"thinh@misfitwearables.com",
//				};
//		
//		String[] passwords = new String[]
//				{
//					"misfit1",
//				};
//		
//		
//		for(int i = 0; i < emails.length; i++) {
//			
//			long startTime = MVPApi.getDayStartEpoch(System.currentTimeMillis() / 1000 - numberOfGoals * 3600 * 24);
//			String token = MVPApi.signIn(emails[i], passwords[i]).token;
//			
//			Goal[] goals = MVPApi.searchGoal(token, startTime, Integer.MAX_VALUE, 0).goals;
//			
//			logger.info("GOAL OF USER: " + emails[i]);
//			logger.info("====================================================================================");
//			for(Goal goal : goals)
//				logger.info("Points: " + goal.getProgressData().getPoints());
//			logger.info("====================================================================================\n\n");
//		}
		
//		logger.info(MVPApi.getUserId(MVPApi.signIn("nhhai16991@gmail.com", "qqqqqq").token));
//		BackendHelper.link(MVPApi.signIn("nhhai16991@gmail.com", "qqqqqq").token, "science019");
		
	}
}
	