package com.misfit.ta.backend.aut.performance.openapi;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.openapi.OpenAPIThirdPartyApp;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.base.SeedThread;
import com.misfit.ta.base.SeedThreadParallelExecutor;
import com.misfit.ta.common.MVPCommon;

public class OpenAPIResourceServerPerformanceTest {

	protected static Logger logger = Util.setupLogger(MVPApi.class);
	public static ResultLogger resultLogger = ResultLogger
			.getLogger("openapi_rs_performance_" + System.nanoTime());

	protected static int[] resultCode = new int[600];
	protected static int numberOfRequest = 0;
	protected static int numberOfResponse = 0;

	private static synchronized void addRequest() {

		numberOfRequest++;
	}

	private static synchronized void addResult(BaseResult result) {

		if(result != null && result.response != null)
			numberOfResponse++;
		resultCode[result.statusCode]++;
	}

	public static void main(String[] args) {

		if (args.length == 0) {
			args = new String[] { "20", "200" };
		}

		int numberOfSeed = Integer.valueOf(args[0]);
		int numberOfThread = Integer.valueOf(args[1]);

		SeedThread seed = new OpenAPIResourceServerPerformanceSeed();
		SeedThreadParallelExecutor executor = new SeedThreadParallelExecutor(
				seed, numberOfSeed, numberOfThread);
		executor.execute();

		StringBuffer sb = new StringBuffer();
		sb.append("\nTest statistics: ");
		sb.append(executor.getSummary());

		sb.append("\nHTTP statistics: ");
		sb.append("\n------------------------------------------------------------------");
		sb.append("\nTotal requests: " + numberOfRequest);
		sb.append("\nTotal response: " + numberOfResponse);
		sb.append("\nRPS: " + (numberOfRequest * 1.0 / executor.totalTime));
		sb.append("\nStatus code distribution:");

		for (int i = 0; i < resultCode.length; i++) {
			if (resultCode[i] != 0)
				sb.append("\n- " + i + ": " + resultCode[i]);
		}
		sb.append("\n------------------------------------------------------------------\n");

		logger.error(sb.toString());
		resultLogger.log(sb.toString());
	}

	public static class OpenAPIResourceServerPerformanceSeed extends SeedThread {

		private static String myUid;
		private static OpenAPIThirdPartyApp app;
		private static String accessToken;

		private static String goalId;
		private static String sessionId;
		private static String sleepId;

		static {
			
			// set up temp account
			long timestamp = System.currentTimeMillis() / 1000;
			String email = MVPApi.generateUniqueEmail();
			String password = "qqqqqq";
			String token = MVPApi.signUp(email, password).token;
			myUid = MVPApi.getUserId(token);

			MVPApi.createProfile(token,DataGenerator.generateRandomProfile(timestamp, null));
			MVPApi.createPedometer(token,DataGenerator.generateRandomPedometer(timestamp, null));
			MVPApi.createGoal(token, Goal.getDefaultGoal(timestamp - 3600 * 24));

			BaseResult result1 = MVPApi.createGoal(token, Goal.getDefaultGoal());
			BaseResult result2 = MVPApi.createTimelineItem(token, DataGenerator.generateRandomActivitySessionTimelineItem(timestamp, null));
			BaseResult result3 = MVPApi.createTimelineItem(token, DataGenerator.generateRandomSleepTimelineItem(timestamp, null));

			goalId = Goal.getGoal(result1.response).getServerId();
			sessionId = TimelineItem.getTimelineItem(result2.response).getServerId();
			sleepId = TimelineItem.getTimelineItem(result3.response).getServerId();
			
			// get 3rd party app and access token
			String clientKey = Settings.getParameter("MVPOpenAPIClientID");
			String clientSecret = Settings
					.getParameter("MVPOpenAPIClientSecret");

			accessToken = OpenAPI.getAccessToken(email, password,
					OpenAPI.allScopesAsString(), clientKey,
					"http://misfit.com/");

			app = new OpenAPIThirdPartyApp();
			app.setClientKey(clientKey);
			app.setClientSecret(clientSecret);
		}

		// implements intefaces
		public void run() {

			String endDate = MVPCommon.getDateString(System
					.currentTimeMillis() / 1000);
			String startDate = MVPCommon.getDateString(System
					.currentTimeMillis() / 1000 - 3600 * 24 * 30);

			// device
			addRequest();
			addResult(OpenAPI.getDevice(accessToken, "me"));
			addRequest();
			addResult(OpenAPI.getDevice(app, myUid));

			// profile
			addRequest();
			addResult(OpenAPI.getProfile(accessToken, "me"));
			addRequest();
			addResult(OpenAPI.getProfile(app, myUid));

			// goals
			addRequest();
			addResult(OpenAPI.getGoals(accessToken, "me", startDate, endDate));
			addRequest();
			addResult(OpenAPI.getGoals(app, myUid, startDate, endDate));

			addRequest();
			addResult(OpenAPI.getGoal(accessToken, "me", goalId));
			addRequest();
			addResult(OpenAPI.getGoal(app, myUid, goalId));

			// sessions
			addRequest();
			addResult(OpenAPI
					.getSessions(accessToken, "me", startDate, endDate));
			addRequest();
			addResult(OpenAPI.getSessions(app, myUid, startDate, endDate));

			addRequest();
			addResult(OpenAPI.getSession(accessToken, "me", sessionId));
			addRequest();
			addResult(OpenAPI.getSession(app, myUid, sessionId));

			// sleeps
			addRequest();
			addResult(OpenAPI.getSleeps(accessToken, "me", startDate, endDate));
			addRequest();
			addResult(OpenAPI.getSleeps(app, myUid, startDate, endDate));

			addRequest();
			addResult(OpenAPI.getSleep(accessToken, "me", sleepId));
			addRequest();
			addResult(OpenAPI.getSleep(app, myUid, sleepId));

			// summary
			addRequest();
			addResult(OpenAPI.getSummary(accessToken, "me", startDate, endDate));
			addRequest();
			addResult(OpenAPI.getSummary(app, myUid, startDate, endDate));

			addRequest();
			addResult(OpenAPI.getSummary(accessToken, "me", startDate, endDate,
					true));
			addRequest();
			addResult(OpenAPI.getSummary(app, myUid, startDate, endDate, true));

		}

		public SeedThread duplicate() {

			return new OpenAPIResourceServerPerformanceSeed();
		}

	}

}
