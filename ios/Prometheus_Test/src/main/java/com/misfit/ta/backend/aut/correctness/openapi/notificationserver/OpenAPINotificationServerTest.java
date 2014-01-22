package com.misfit.ta.backend.aut.correctness.openapi.notificationserver;

import org.testng.annotations.Test;

import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.server.ServerHelper;
import com.misfit.ta.utils.ShortcutsTyper;

public class OpenAPINotificationServerTest extends BackendAutomation {

	private String clientKey = Settings.getParameter("MVPOpenAPIClientID");
	private String clientSecret = Settings.getParameter("MVPOpenAPIClientSecret");
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "notificationserver", "Excluded" })
	public void SubcribeAResourceToAnEndpoint() {
		
		// start notification endpoint server first
		ServerHelper.startNotificationEndpointServer("http://localhost:8999/");
		
		// bind to that server
		String endpoint = "http://jenkins.misfitwearables.com:8999/";
		OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, "profiles");
		ShortcutsTyper.delayTime(10000);
		
		// sign up 2 users and authorize one user
		String email1 = MVPApi.generateUniqueEmail();
		String email2 = MVPApi.generateUniqueEmail();
		String token1 = MVPApi.signUp(email1, "qqqqqq").token;
		String token2 = MVPApi.signUp(email2, "qqqqqq").token;
		OpenAPI.getAccessToken(email1, "qqqqqq", "public", OpenAPI.RESOURCE_PROFILE, clientKey);
		
		// create and update profile for both users'
		long timestamp = System.currentTimeMillis() / 1000;
		ProfileData profile1 = DataGenerator.generateRandomProfile(timestamp, null);
		ProfileData profile2 = DataGenerator.generateRandomProfile(timestamp, null);
		
		MVPApi.createProfile(token1, profile1);
		MVPApi.createProfile(token2, profile2);
		
		profile1.setName(profile1.getName() + "_updated");
		profile2.setName(profile2.getName() + "_updated");
		
		MVPApi.updateProfile(token1, profile1);
		MVPApi.updateProfile(token2, profile2);
		
		// expected:
		// - saw 2 messages from AWS notice the creation and update of profile1
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "notificationserver", "Excluded" })
	public void SubcribeAResourceToMultipleEndpoints() {
		
		// start notification endpoint server2 first
		ServerHelper.startNotificationEndpointServer("http://localhost:8999/");
		ServerHelper.startNotificationEndpointServer("http://localhost:8998/");

		// first subcribe to server on port 8999
		String endpoint = "http://jenkins.misfitwearables.com:8999/";
		OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, "profiles");
		
		// then subcribe to server on port 8998
		endpoint = "http://jenkins.misfitwearables.com:8998/";
		OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, "profiles");
		
		// sign up, create and update profile
		String email = MVPApi.generateUniqueEmail();
		String token = MVPApi.signUp(email, "qqqqqq").token;
		OpenAPI.getAccessToken(email, "qqqqqq", "public", OpenAPI.RESOURCE_PROFILE, clientKey);

		long timestamp = System.currentTimeMillis() / 1000;
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		MVPApi.createProfile(token, profile);

		profile.setName(profile.getName() + "_updated");
		MVPApi.updateProfile(token, profile);
		
		// create and update goal
		Goal goal = Goal.getDefaultGoal();
		MVPApi.createGoal(token, goal);
		
		goal.getProgressData().setPoints(1000d);
		MVPApi.updateGoal(token, goal);
		
		// expect:
		// - only notification to server on port 8998
		// - only notification for profile, not goal
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "notificationserver", "Excluded" })
	public void SubcribeMultipleResourcesToAnEndpoint() {
		
		// start notification endpoint server2 first
		ServerHelper.startNotificationEndpointServer("http://localhost:8999/");

		// first subcribe to profile
		String endpoint = "http://jenkins.misfitwearables.com:8999/";
		OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, "profiles");
		
		// then subcribe to goal
		endpoint = "http://jenkins.misfitwearables.com:8998/";
		OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, "goals");
		
		// sign up, create and update profile
		String email = MVPApi.generateUniqueEmail();
		String token = MVPApi.signUp(email, "qqqqqq").token;
		OpenAPI.getAccessToken(email, "qqqqqq", "public", OpenAPI.RESOURCE_PROFILE, clientKey);

		long timestamp = System.currentTimeMillis() / 1000;
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		MVPApi.createProfile(token, profile);

		profile.setName(profile.getName() + "_updated");
		MVPApi.updateProfile(token, profile);
		
		// create and update goal
		Goal goal = Goal.getDefaultGoal();
		MVPApi.createGoal(token, goal);
		
		goal.getProgressData().setPoints(1000d);
		MVPApi.updateGoal(token, goal);
		
		// expect:
		// - notification for create/update profile
		// - notification for create/update goal
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "notificationserver", "Excluded" })
	public void UnsubcribeAResourceFromAnEndpoint() {
		
		// start notification endpoint server first
		ServerHelper.startNotificationEndpointServer("http://localhost:8999/");

		// first subcribe to profile
		String endpoint = "http://jenkins.misfitwearables.com:8999/";
		OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, "profiles");
		
		// then subcribe to goal
		endpoint = "http://jenkins.misfitwearables.com:8998/";
		OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, "goals");
		
		// sign up, create and update profile
		String email = MVPApi.generateUniqueEmail();
		String token = MVPApi.signUp(email, "qqqqqq").token;
		OpenAPI.getAccessToken(email, "qqqqqq", "public", OpenAPI.RESOURCE_PROFILE, clientKey);

		long timestamp = System.currentTimeMillis() / 1000;
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		MVPApi.createProfile(token, profile);

		profile.setName(profile.getName() + "_updated");
		MVPApi.updateProfile(token, profile);
		
		// create and update goal
		Goal goal = Goal.getDefaultGoal();
		MVPApi.createGoal(token, goal);
		
		goal.getProgressData().setPoints(1000d);
		MVPApi.updateGoal(token, goal);
		
		// unsubcribe to profile
		OpenAPI.unsubscribeNotification(endpoint, clientSecret, "profiles");
		
		// update profile and goal again
		profile.setName(profile.getName() + "_updated2");
		MVPApi.updateProfile(token, profile);
		
		goal.getProgressData().setPoints(2000d);
		MVPApi.updateGoal(token, goal);
		
		// unsubcribe to goal
		OpenAPI.unsubscribeNotification(endpoint, clientSecret, "goals");
		
		// update goal again
		goal.getProgressData().setPoints(3000d);
		MVPApi.updateGoal(token, goal);
		
		// expect:
		// - first: notification for both profile and goal
		// - then: only notification for goal
		// - finnaly: no notification
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "notificationserver", "Excluded" })
	public void UnsubcribeAResourceFromAnEndpointTwice() {
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "notificationserver" })
	public void UnsubcribeAResourceAndThenSubscribeAgain() {
	}

}
