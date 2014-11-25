package com.misfit.ta.backend.aut.correctness.backendapi.metawatch;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.api.metawatch.MetawatchApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.MetawatchModel.MetawatchProfileModel;

public class OpenAPIMetawatch extends BackendAutomation {

	String password = "qwerty";

	@Test(groups = { "api", "openapi_metawatch" })
	public void SignUpSuccessfully() {
		String userId = MVPApi.generateLocalId();
		String email = MVPApi.generateUniqueEmail();
		// register
		BaseResult result = MetawatchApi.registerMetawatch("token", userId);
		Assert.assertEquals(result.statusCode, 200, "Register!");
		// sign up && create profile
		// create profile
		MetawatchProfileModel profile = MetawatchApi.DefaultProfile();
		result = MetawatchApi.createProfileMetawatch(email, password, profile,
				userId);
		// call get method
		String response = result.response.getResponseString();
		String url = response.substring(response.indexOf('h'));

		result = MetawatchApi.getExchangeMetawatch(url);
	}

	@Test(groups = { "api", "openapi_metawatch" })
	public void SignUpInvalidData() {
		// userId is null
		String userId = null;
		BaseResult result = MetawatchApi.registerMetawatch("token", userId);
		Assert.assertEquals(result.statusCode, 400, "Register!");

		// userId is empty
		userId = " ";
		result = MetawatchApi.registerMetawatch("token", userId);
		Assert.assertEquals(result.statusCode, 400, "Register!");

		// sign up && create profile
		// create profile
		userId = MVPApi.generateLocalId();
		result = MetawatchApi.registerMetawatch("token", userId);
		Assert.assertEquals(result.statusCode, 200, "Register!");
		// email is null
		String email = null;
		MetawatchProfileModel profile = MetawatchApi.DefaultProfile();
		result = MetawatchApi.createProfileMetawatch(email, password, profile,
				userId);
		Assert.assertEquals(result.statusCode, 400, "Email is null");
		
		// email is empty
		email = " ";
		result = MetawatchApi.createProfileMetawatch(email, password, profile,
				userId);
		Assert.assertEquals(result.statusCode, 400, "Email is empty");
		// password is null
		email = MVPApi.generateUniqueEmail();
		String password = null;
		result = MetawatchApi.createProfileMetawatch(email, password, profile,
				userId);
		Assert.assertEquals(result.statusCode, 400, "Password is null");
		
		// pasword is empty
		password = " ";
		result = MetawatchApi.createProfileMetawatch(email, password, profile,
				userId);
		Assert.assertEquals(result.statusCode, 400, "Password is empty");
	}

	@Test(groups = { "api", "openapi_metawatch" })
	public void SignUpInvalidMetawatchProfile(){
		String userId = "tester1";
		String email = MVPApi.generateUniqueEmail();
		// register
		BaseResult result = MetawatchApi.registerMetawatch("token", userId);
		Assert.assertEquals(result.statusCode, 200, "Register!");
		// sign up && create profile
		// create profile
		// Gener is negative
		
		MetawatchProfileModel profile = MetawatchApi.DefaultProfile();
		profile.setGender(-1);
		result = MetawatchApi.createProfileMetawatch(email, password, profile,
				userId);
//		Assert.assertTrue(result.response.getResponseString().contains("/auth/profile/metawatch"), "Gender is negative");
		Assert.assertEquals(result.statusCode, 400, "Gender is negative");
		
		// Gener is greater than 1
		profile.setGender(2);
		result = MetawatchApi.createProfileMetawatch(email, password, profile,
				userId);
//		Assert.assertTrue(result.response.getResponseString().contains("/auth/profile/metawatch"), "Gender is negative");
		Assert.assertEquals(result.statusCode, 400, "Gender is greater than 1");
		// Date of birth is invalid
		profile = MetawatchApi.DefaultProfile();
		String[] wrongDates = new String[]{"2014-02-30", "2014-05-32", "2014-04-31", "25/12/2014"};
		for(int i = 0; i < wrongDates.length; i++){
			profile.setDateOfBirth(wrongDates[i]);
			result = MetawatchApi.createProfileMetawatch(email, password, profile,
					userId);
//			Assert.assertTrue(result.response.getResponseString().contains("/auth/profile/metawatch"), "Gender is negative");
			Assert.assertEquals(result.statusCode, 400, "Date of birth is invalid");
		}
		
		// DistanceUnit is negative
		profile = MetawatchApi.DefaultProfile();
		profile.setDistanceUnit(-1);
		result = MetawatchApi.createProfileMetawatch(email, password, profile,
				userId);
		Assert.assertEquals(result.statusCode, 400, "DistanceUnit is negative");
		
		// DistanceUnit is greater than 1
		profile.setDistanceUnit(2);
		result = MetawatchApi.createProfileMetawatch(email, password, profile,
				userId);
		Assert.assertEquals(result.statusCode, 400, "DistanceUnit is greater than 1");
		
		// WeightUnit is negative
		profile = MetawatchApi.DefaultProfile();
		profile.setWeightUnit(-1);
		result = MetawatchApi.createProfileMetawatch(email, password, profile,
				userId);
		Assert.assertEquals(result.statusCode, 400, "WeightUnit is negative");
		
		// WeightUnit is greater than 1
		profile.setWeightUnit(2);
		result = MetawatchApi.createProfileMetawatch(email, password, profile,
				userId);
		Assert.assertEquals(result.statusCode, 400, "WeightUnit is greater negative");
		
		// UserHeightInches is negative
		profile = MetawatchApi.DefaultProfile();
		profile.setUserHeightInches(-1);
		result = MetawatchApi.createProfileMetawatch(email, password, profile,
				userId);
		Assert.assertEquals(result.statusCode, 400, "UserHeightInches is negative");
		
		// call get method
	}
	
	@Test(groups = { "api", "openapi_metawatch" })	
	public void SignInSuccessfully(){
		String email = MVPApi.generateUniqueEmail();
		String userId = MVPApi.generateLocalId();
		// register
		BaseResult result = MetawatchApi.registerMetawatch("token", userId);
		Assert.assertEquals(result.statusCode, 200, "Register!");
		// sign up && create profile
		// create profile
		MetawatchProfileModel profile = MetawatchApi.DefaultProfile();
		result = MetawatchApi.createProfileMetawatch(email, password, profile,
				userId);
		// call get method
		String response = result.response.getResponseString();
		String url = response.substring(response.indexOf('h'));

		result = MetawatchApi.getExchangeMetawatch(url);
		
		// Login
		result = MetawatchApi.signInMetawatch(userId, email, password, true);
		
		// Get access_token
		response = result.response.getResponseString();
		String access_token = response.substring(response.indexOf('=') + 1);
		System.err.println("access_token: " + access_token);
	}
	
	@Test(groups = { "api", "openapi_metawatch" })
	public void SignInWithInvalidData(){
		// Email is empty
		String email = " ";
		String userId = MVPApi.generateLocalId();
		BaseResult result = MetawatchApi.signInMetawatch(userId, email, password, false);
		Assert.assertEquals(result.statusCode, 400, "Email is empty");
		
		// Password is empty
		email = MVPApi.generateUniqueEmail();
		result = MetawatchApi.signInMetawatch(userId, email, password, false);
		Assert.assertEquals(result.statusCode, 400, "Password is empty");
	}
	
	@Test(groups = { "api", "openapi_metawatch" })
	public void SignInWithInvalidToken(){
		String userId = "tester1";
		String email = MVPApi.generateUniqueEmail();
		// register
		BaseResult result = MetawatchApi.registerMetawatch("token", userId);
		Assert.assertEquals(result.statusCode, 200, "Register!");
		// sign up && create profile
		// create profile
		MetawatchProfileModel profile = MetawatchApi.DefaultProfile();
		result = MetawatchApi.createProfileMetawatch(email, password, profile,
				userId);
		// call get method to get session
		
		// Sign in - we don't have token
		result = MetawatchApi.signInMetawatch(userId, email, password, true);
		// call get method to get session
		String response = result.response.getResponseString();
		String url = response.substring(response.indexOf('h'));

		result = MetawatchApi.getExchangeMetawatch(url);
	}

	@Test(groups = { "api", "openapi_metawatch" })
	public void SignUpExistAccount(){
		String userId = MVPApi.generateLocalId();
		String email = "test1416910162463sl5iog@misfitqa.com";
		// register
		BaseResult result = MetawatchApi.registerMetawatch("token", userId);
		Assert.assertEquals(result.statusCode, 200, "Register!");
		// sign up && create profile
		// create profile
		MetawatchProfileModel profile = MetawatchApi.DefaultProfile();
		result = MetawatchApi.createProfileMetawatch(email, password, profile,
				userId);
		
		Assert.assertEquals(result.statusCode, 400, "Someone else has used this before");
		
	}
}
