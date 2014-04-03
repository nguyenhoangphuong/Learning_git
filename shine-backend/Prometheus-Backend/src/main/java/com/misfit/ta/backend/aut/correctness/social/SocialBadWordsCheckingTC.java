package com.misfit.ta.backend.aut.correctness.social;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.report.TRS;

public class SocialBadWordsCheckingTC extends BackendAutomation {
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "SocialAPI", "BadWordsChecking", "Excluded" })
	public void CheckBadWordsInHandle() {

		// create account
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		MVPApi.createProfile(token, DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null));
		
		
		// check bad words checking
		List<String> badWordsFailed = new ArrayList<String>();
		List<String> notSoBadWordsFailed = new ArrayList<String>();
		
		String[] badWords = {
			"fuck_you", "asshole", "big_dick", "a_s_s_h_o_l_e", "daughter_of_a_bitch", 
		};
		
		String[] notSoBadWords = {
			"eyeglass", "afucker", "amadamna", "son_of_a_beach",
		};
		
		for(String word : badWords) {
			
			ProfileData profile = new ProfileData();
			profile.setHandle(word);
			
			BaseResult result = MVPApi.updateProfile(token, profile);
			if(result.statusCode != 400 && result.errorCode != 201)
				badWordsFailed.add(word);
		}
		
		for(String word : notSoBadWords) {
			
			ProfileData profile = new ProfileData();
			profile.setHandle(word);
			
			BaseResult result = MVPApi.updateProfile(token, profile);
			if(result.statusCode != 200 && result.statusCode != 210)
				notSoBadWordsFailed.add(word);
		}
		
		
		// update profile again
		ProfileData profile = new ProfileData();
		profile.setHandle("valid_" + System.nanoTime());
		MVPApi.updateProfile(token, profile);
		
		
		// check result
		TRS.instance().addStep("Result", null);
		if(badWordsFailed.size() != 0) {
			
			logger.info("Bad words that were allowed: ");
			logger.info("==============================================");
			for(String word : badWordsFailed) {
				logger.info(word);
				TRS.instance().addCode("Bad word that were allowed: " + word, null);
			}
			logger.info("==============================================");
		}
		
		if(notSoBadWordsFailed.size() != 0) {
			
			logger.info("Not so bad words that were not allowed: ");
			logger.info("==============================================");
			for(String word : notSoBadWordsFailed) {
				logger.info(word);
				TRS.instance().addCode("Not so bad word that were not allowed: " + word, null);
			}
			logger.info("==============================================");
		}	
		
		Assert.assertEquals(badWordsFailed.size(), 0, "Number of bad words that were allowed");
		Assert.assertEquals(notSoBadWordsFailed.size(), 0, "Number of not so bad words that were not allowed");
		
	}

}
