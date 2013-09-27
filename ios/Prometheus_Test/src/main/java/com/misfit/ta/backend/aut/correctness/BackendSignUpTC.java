package com.misfit.ta.backend.aut.correctness;

import org.testng.Assert;
import org.testng.annotations.*;

import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.api.*;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;

public class BackendSignUpTC extends BackendAutomation {

	String[] wrongFormatEmails = { "", "wrong100", "wrong2@", "wrong3 @a.b" };
	String wellFormatEmail = "wellformat@qa.com";
	String duplicatedEmail = "";
	String notregisteredEmail;

	String[] wrongFormatPasswords = { "", "short" };
	String wellFormatPassword = "qwerty1";

	@BeforeClass(alwaysRun = true)
	public void setup() {
		notregisteredEmail = MVPApi.generateUniqueEmail();
		duplicatedEmail = MVPApi.generateUniqueEmail();
		MVPApi.signUp(duplicatedEmail, "qwerty1");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signup" })
	public void SignUpWrongFormatEmails() {
		for (int i = 0; i < wrongFormatEmails.length; i++) {
			AccountResult r = MVPApi.signUp(wrongFormatEmails[i], wellFormatPassword);
			r.printKeyPairsValue();

			Assert.assertEquals(r.statusCode, 400, "Status code");
			Assert.assertEquals(DefaultValues.InvalidEmail, r.errorMessage, "Error message content");
			Assert.assertTrue(r.token == null || r.token.isEmpty(), "Empty authenticate token");
		}
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signup" })
	public void SignUpWrongFormatPasswords() {
		for (int i = 0; i < wrongFormatPasswords.length; i++) {
			AccountResult r = MVPApi.signUp(wellFormatEmail, wrongFormatPasswords[i]);
			r.printKeyPairsValue();

			Assert.assertEquals(r.statusCode, 400, "Status code");
			Assert.assertEquals(DefaultValues.InvalidPassword, r.errorMessage, "Error message content");
			Assert.assertTrue(r.token == null || r.token.isEmpty(), "Empty authenticate token");
		}
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signup" })
	public void SignUpDuplicateEmail() {
		AccountResult r = MVPApi.signUp(duplicatedEmail, wellFormatPassword);

		Assert.assertEquals(r.statusCode, 400, "Status code");
		Assert.assertEquals(r.errorMessage, DefaultValues.DuplicateEmail, "Error message content");
		Assert.assertTrue(r.token == null || r.token.isEmpty(), "Empty authenticate token");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signup" })
	public void SignUpNotRegisteredEmail() {
		AccountResult r = MVPApi.signUp(notregisteredEmail, wellFormatPassword);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isOK(), "Status code is 200");
		Assert.assertTrue(r.isEmptyErrMsg(), "Empty error message");
		Assert.assertTrue(!r.token.isEmpty(), "Authenticate token is not empty");
	}

}
