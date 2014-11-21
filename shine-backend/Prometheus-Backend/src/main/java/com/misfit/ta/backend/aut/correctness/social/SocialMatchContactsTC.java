package com.misfit.ta.backend.aut.correctness.social;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.social.SocialUserWithStatus;
import com.misfit.ta.utils.TextTool;

public class SocialMatchContactsTC extends SocialTestAutomationBase {

	protected String misfitEmail;
	protected String tungEmail;
	protected String thyEmail;
	
	@BeforeClass(alwaysRun = true)
    public void beforeClass() {
	
		super.beforeClass();
		misfitEmail = (String)mapNameData.get("misfit").get("email");
		tungEmail = (String)mapNameData.get("tung").get("email");
		thyEmail = (String)mapNameData.get("thy").get("email");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "MatchContacts" })
    public void MatchContacts_WithoutParamsOrEmptyList() {

		// empty list
		BaseResult result = SocialAPI.matchContacts(misfitToken, new ArrayList<String>());
		SocialUserWithStatus[] users = SocialUserWithStatus.usersFromResponse(result.response);
		
		Assert.assertEquals(users.length, 0, "Number of users found");

		
		// without emails param
		result = SocialAPI.matchContacts(misfitToken, null);
		Assert.assertEquals(result.statusCode, 400, "Number of users found");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "MatchContacts" })
    public void MatchContacts_InvalidContacts() {
	
		// sign up new user
		String nonSocialEmail = MVPApi.generateUniqueEmail();
		String token = MVPApi.signUp(nonSocialEmail, "qqqqqq").token;
		ProfileData profile = DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null);
		profile.setHandle(null);
		
		MVPApi.createProfile(token, profile);
		
		
		// match contacts
		// invalid emails
		List<String> emails = new  ArrayList<String>();
		emails.add(TextTool.getRandomString(10, 30));
		emails.add("a@.a");
		emails.add("as");
		
		// non existed / non-social user emails
		emails.add(MVPApi.generateUniqueEmail());
		emails.add(nonSocialEmail);
		
		// my email
		emails.add(misfitEmail);
		

		// check result
		BaseResult result = SocialAPI.matchContacts(misfitToken, emails);
		SocialUserWithStatus[] users = SocialUserWithStatus.usersFromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(users.length, 0, "Number of users found");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "MatchContacts" })
    public void MatchContacts_ValidContacts() {
		
		// misfit makes friend with tung
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		SocialAPI.acceptFriendRequest(tungToken, misfitUid);
		
		
		// match contacts
		// non-friend user / friend / duplicated emails
		List<String> emails = new  ArrayList<String>();
		emails.add(thyEmail);
		emails.add(tungEmail);
		emails.add(thyEmail);
		emails.add(tungEmail);
		
		
		// check result
		BaseResult result = SocialAPI.matchContacts(misfitToken, emails);
		SocialUserWithStatus[] users = SocialUserWithStatus.usersFromResponse(result.response);

		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(users.length, 2, "Number of users found");
		for(SocialUserWithStatus user : users) {
			
			if(user.getUid().equals(thyUid)) {
				Assert.assertEquals(user.getName(), thyName, "Name of thy");
				Assert.assertEquals(user.getStatus(), SocialAPI.STATUS_NOT_REQUESTED, "Status to thy");
			}
			
			if(user.getUid().equals(tungUid)) {
				Assert.assertEquals(user.getName(), tungName, "Name of tung");
				Assert.assertEquals(user.getStatus(), SocialAPI.STATUS_APPROVED, "Status to tung");
			}
		}
		
		
		// clean up: delete friends
		SocialAPI.deleteFriend(misfitToken, tungUid);
		
	}
	
}