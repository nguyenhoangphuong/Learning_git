package com.misfit.ta.backend.aut.correctness.social;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.internalapi.social.SocialAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.social.SocialUserWithStatus;
import com.misfit.ta.report.TRS;
import com.misfit.ta.utils.TextTool;

public class SocialMatchContactsTC extends SocialTestAutomationBase {
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "MatchContactsAPI" })
	public void MatchContacts_WithoutEmailsParam() {
		
		// send without emails param
		BaseResult result = SocialAPI.matchContacts(misfitToken, null);

		// no result
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.InvalidParameterMessage, "Error message");
		Assert.assertEquals(result.errorCode, DefaultValues.InvalidParameterCode, "Error code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "MatchContactsAPI" })
	public void MatchContacts_ContainsInvalidEmails() {
		
		// emails list
		List<String> emails = new ArrayList<String>();
		emails.add(MVPApi.generateUniqueEmail());
		emails.add(TextTool.getRandomString(10, 10));
		emails.add((String)mapNameData.get("tung").get("email"));
		emails.add((String)mapNameData.get("thy").get("email"));
		
		// result
		BaseResult result = SocialAPI.matchContacts(misfitToken, emails);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "MatchContactsAPI" })
	public void MatchContacts_ContainsDuplicatedEmails() {
		
		// emails list
		List<String> emails = new ArrayList<String>();
		emails.add((String)mapNameData.get("thy").get("email"));
		emails.add((String)mapNameData.get("tung").get("email"));
		emails.add((String)mapNameData.get("thy").get("email"));
		
		// result
		BaseResult result = SocialAPI.matchContacts(misfitToken, emails);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "MatchContactsAPI" })
	public void MatchContacts_ContainsMyEmail() {
		
		// emails list
		List<String> emails = new ArrayList<String>();
		emails.add((String)mapNameData.get("thy").get("email"));
		emails.add((String)mapNameData.get("tung").get("email"));
		emails.add((String)mapNameData.get("misfit").get("email"));
		
		// result
		BaseResult result = SocialAPI.matchContacts(misfitToken, emails);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "MatchContactsAPI" })
	public void MatchContacts_DifferentStatuses() {
		
		// misfit -> tung
		SocialAPI.sendFriendRequest(misfitToken, tungUid);
		
		// emails list
		List<String> emails = new ArrayList<String>();
		emails.add((String)mapNameData.get("thy").get("email"));
		emails.add((String)mapNameData.get("tung").get("email"));
		emails.add((String)mapNameData.get("misfit").get("email"));
		
		// result
		BaseResult result = SocialAPI.matchContacts(misfitToken, emails);
	}
	
}
