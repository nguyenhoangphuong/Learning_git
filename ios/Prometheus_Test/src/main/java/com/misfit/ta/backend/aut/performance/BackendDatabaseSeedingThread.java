package com.misfit.ta.backend.aut.performance;

import java.util.Random;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import com.google.resting.json.JSONArray;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.profile.ProfileResult;
import com.misfit.ta.backend.data.sync.SyncLog;
import com.misfit.ta.base.Clock;
import com.misfit.ta.utils.TextTool;

public class BackendDatabaseSeedingThread implements Runnable {

	private String password = "misfit1";

	private int userCount = 0;
	private JSONArray timelineItems;
	private JSONArray graphItems;
	private ResultLogger rlog;

	private String token;
	private Clock clock = new Clock();


	private boolean randomized = false;
	private long userRequestTime;
	private long countRequest;
	private String mySerial = TextTool.getRandomString(5, 6) + System.currentTimeMillis();


	Logger logger = Util.setupLogger(BackendDatabaseSeedingThread.class);


	public BackendDatabaseSeedingThread(int userCount, JSONArray timelineItems, JSONArray graphItems, ResultLogger rlog, boolean randomized) {
		this.userCount = userCount;
		this.timelineItems = timelineItems;
		this.graphItems = graphItems;
		this.rlog = rlog;
		this.randomized = randomized;
	}

	public void run() {

		clock = new Clock();

		logger.info(" ===============  User " + userCount + " =================");
		String email = MVPApi.generateUniqueEmail();

		// sign up first
		clock.tick("sign_up");
		AccountResult r = MVPApi.signUp(email, password);
		clock.tock();
		
		token = r.token;

		int operation = Settings.getInt("OPERATION");
		if (randomized) {
			Random rand = new Random(System.currentTimeMillis());
			operation = (randomized) ? rand.nextInt(6) : -1;
			System.out.println("LOG [BackendDatabaseSeedingThread.run]: random:  " + operation);
		}
		System.out.println("LOG [BackendDatabaseSeedingThread.run]: operation: " + operation + " : " + (operation == 0 || operation <= -1));

		countRequest = 1;
		if (operation == 0 || operation <= -1) {
			doAccountOperation(email);
		}
		if (operation == 1 || operation <= -1) {
			doProfileOperation();
		}
		if (operation == 2 || operation <= -1) {
			doPedometerOperations();
		}
		if (operation == 3 || operation <= -1) {
			doLinkinOperation();
		}
		if (operation == 4 || operation <= -1) {
			doGoalOperation();
		}
		if (operation == 5 || operation <= -1) {
			doTimelineOperation();
		}
		
		if (operation == 6 || operation <= -1) {
            doSyncOperation();
        }
		
		if (operation == 7 || operation <= -1) {
            doSocialOperation();
        }
		
		System.out.println("LOG [BackendStressTestThread.run]: ------------------------------------ DONE");
		userRequestTime = clock.getSumIntervals();
		
		ResultLogger.totalTime += clock.getSumIntervals();

        rlog.log((userCount + 1) +"\t"+ clock.getTimeInteval()
                + email + "\t" 
                + userRequestTime + "\t"
                + countRequest + "\t"
                + (String.valueOf(userRequestTime/countRequest))
                );
	}


	public void doAccountOperation(String email) {

		// sign out then
		clock.tick("signout");
		MVPApi.signOut(token);
		clock.tock();

		// sign in
		clock.tick("signin");
		AccountResult r = MVPApi.signIn(email, "misfit1");
		clock.tock();
		
		token = r.token;
		countRequest += 2;
	}

	public void doProfileOperation() {
		
		ProfileData profile = DefaultValues.DefaultProfile();
		
		// createProfile
		clock.tick("createProfile");
		ProfileResult result = MVPApi.createProfile(token, profile);
		clock.tock();

		// get Profile
		clock.tick("getProfile");
		result = MVPApi.getProfile(token);
		clock.tock();

		// update profile
		ProfileData newProfile = result.profile;
		newProfile.setWeight(profile.getWeight() + 1);
		
		clock.tick("update profile");
		result = MVPApi.updateProfile(token, newProfile, profile.getServerId());
		clock.tock();
		
		countRequest += 3;
	}

	public void doPedometerOperations() {
		
		long now = System.currentTimeMillis()/1000;
		
		// create pedometer
		clock.tick("createPedo");
		Pedometer pedo = MVPApi.createPedometer(token, mySerial, "hw1234", now, now, now, "localId", null, now);
		clock.tock();

		// get pedometer
		clock.tick("getPedo");
		pedo = MVPApi.showPedometer(token);
		clock.tock();

		// update pedometer
		pedo.setUpdatedAt(System.currentTimeMillis()/1000);
		pedo.setLastSuccessfulTime(System.currentTimeMillis() / 1000);
		clock.tick("updatePedo");
		pedo = MVPApi.updatePedometer(token, mySerial, "hw1234", now, now, now, "localId", null, now);
		clock.tock();

		countRequest += 3;
	}

	public void doLinkinOperation() {
		
		// get link status
	    clock.tick("getLinkingDevice");
		MVPApi.getDeviceLinkingStatus(token,mySerial);
		clock.tock();

		// unlink
		clock.tick("unlink");
		MVPApi.unlinkDevice(token);
		clock.tock();

		countRequest += 2;
	}

	public void doGoalOperation() {
		
		// create goal
		clock.tick("createGoal");
		Goal goal = DataGenerator.generateRandomGoal(System.currentTimeMillis() / 1000, null);
		GoalsResult goalResult = MVPApi.createGoal(token, goal);
		clock.tock();

		// get goal
		clock.tick("getGoal");
		goalResult = MVPApi.getGoal(token, goalResult.goals[0].getServerId());
		clock.tock();

		// search goal
		clock.tick("searchGoal");
		goalResult = MVPApi.searchGoal(token, 0, Integer.MAX_VALUE, 0);
		clock.tock();

		// update goal
		clock.tick("updateGoal");
		goal.getProgressData().setPoints(goal.getProgressData().getPoints() + 1);
		goalResult = MVPApi.updateGoal(token, goal);
		clock.tock();

		countRequest += 4;
	}

	public void doTimelineOperation() {

		// generate timeline items
	    clock.tick("createTimeline");
		MVPApi.createTimelineItems(token, timelineItems);
		clock.tock();

		// generate graph items
		clock.tick("createGraphItem");
		MVPApi.createGraphItems(token, graphItems);
		clock.tock();
		
		countRequest += 2;
	}

	public void doSyncOperation() {

		SyncLog log = DataGenerator.generateRandomSyncLog(System.currentTimeMillis() / 1000, 1, 60, null);
		
		clock.tick("push_sync_log");
		MVPApi.pushSyncLog(token, log);
		clock.tock();
		countRequest += 1;
	}

	public void doSocialOperation() {

		int index = (Settings.getParameter("MVPBackendEnviroment") == "Staging" ? 0 : 1);
		String[] mfwcqaAccessTokens = new String[] 
				{
					"CAAG661ngu9YBADeNMl7jiCAZChVFWvIyPId8ZBweDaqMudhr6Uke0Yjty13DW8hdqEzzz2r6EXoXjVqI06biVxZBZCfgtY6q7pJZAsGHNGQMR5fLmnxlkLBfYSZBbU9PGm7OXpjMroOD1UJ5h7op2sfLRuH58Fr12nd1mIAF4ACYUr8xwCJUZAR0Eco65s25DveAqpjaxS4n5ANInS2dwG5T153pS6JIhHv1YEylDBZCWwZDZD",
					"CAAG661ngu9YBAIamkuIVGygSZBufVPgbTSiVWYAZA9rfWqcpN7NrYmSvevyifoTUDiU6ZBdfdDfOGJnIJvHrvY4EuxW8h37CHzcrsTLqRC6HWAyXd7138dwb9bhG2rcMr1Rj4IU8rGRmhnPYq6PFnjTndboGbI77Qf3AQw4a0rkCFZCqyq1ZAsJqBHZAWrjCIkwYIttZBl6NhbKpB1cWyOH"
				};
		String[] mfwcqaUids = new String[]
				{
					"519ed8979f12e53fe40001c0",
					"528ca3d65c44ae996604abab"
				};
		String[] tungUids = new String[]
				{
					"5285e6f6513810db3e0002d7",
					"528d71805c44ae1e1e117c1c"
				};
		String[] tungHandles = new String[]
				{
					"tung.social.misfit",
					"tung_social_misfit"
				};
		
		String mfwcqaAccessToken = mfwcqaAccessTokens[index]; 
		String tungToken = MVPApi.signIn("tung.social.misfit@gmail.com", "qqqqqq").token;
		String mfwcqaUid = mfwcqaUids[index];
		String tungUid = tungUids[index];
		String tungHandle = tungHandles[index];

		// connect and get facebook token
		clock.tick("connect_facebook");
		String mfwcqaToken = SocialAPI.connectFacebook("mfwcqa@gmail.com", mfwcqaAccessToken,"").token;
		clock.tock();
				
		// send friend request
		clock.tick("send_friend_request");
		SocialAPI.sendFriendRequest(mfwcqaToken, tungUid);
		clock.tock();

		// ignore friend request
		clock.tick("ignore_friend_request");
		SocialAPI.ignoreFriendRequest(tungToken, mfwcqaUid);
		clock.tock();

		// get friend requests from me
		clock.tick("get_friend_request_to_me");
		SocialAPI.getFriendRequestsFromMe(mfwcqaToken);
		clock.tock();

		// get friend requests to me
		clock.tick("get_friend_request_from_me");
		SocialAPI.getFriendRequestsToMe(tungToken);
		clock.tock();

		// accept friend request
		clock.tick("accep_friend_request");
		SocialAPI.acceptFriendRequest(tungToken, mfwcqaUid);
		clock.tock();

		// get friend list
		clock.tick("get_friend_list");
		SocialAPI.getFriends(mfwcqaToken);
		clock.tock();

		// delete friend
		clock.tick("delete_friend");
		SocialAPI.deleteFriend(mfwcqaToken, tungUid);
		clock.tock();

		// search facebook friends
		clock.tick("search_facebook_friends");
		SocialAPI.getFacebookFriends(mfwcqaToken);
		clock.tock();

		// search social users
		clock.tick("search_social_users");
		SocialAPI.searchSocialUsers(mfwcqaToken, tungHandle);
		clock.tock();

		// get leaderboard info
		clock.tick("get_leaderboard_info");
		SocialAPI.getLeaderboardInfo(mfwcqaToken);
		clock.tock();

		// get world info
		clock.tick("get_world_info");
		SocialAPI.getWorldInfo(mfwcqaToken);
		clock.tock();
		countRequest += 12;
	}

}
