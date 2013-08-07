package com.misfit.ta.backend.aut;

import junit.framework.Assert;

import org.testng.annotations.*;

import com.misfit.ta.backend.data.*;
import com.misfit.ta.backend.api.*;

public class BackendSignUpTC extends BackendAutomation {

	String[] wrongFormatEmails = { "", "wrong100", "wrong2@", "wrong3 @a.b" };
	String wellFormatEmail = "wellformat@qa.com";
	String duplicatedEmail = "";
	String notregisteredEmail;

	String[] wrongFormatPasswords = { "", "allletters", "11413312", "tiny1" };
	String wellFormatPassword = "qwerty1";

	String udid;

	@BeforeClass(alwaysRun = true)
	public void setup() {
		notregisteredEmail = MVPApi.generateUniqueEmail();
		udid = DefaultValues.UDID;
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signup" })
	public void SignUpWrongFormatEmails() {
		ResultLogger rlog = ResultLogger.getLogger("signup_wrong_format_emails");
		rlog.log("Number of try\ttime taken");
		for (int i = 0; i < wrongFormatEmails.length; i++) {
			long start = System.currentTimeMillis();
			AccountResult r = MVPApi.signUp(wrongFormatEmails[i], wellFormatPassword, udid);
			r.printKeyPairsValue();

			System.out.println("LOG [BackendSignUpTC.SignUpWrongFormatEmails]: " + wrongFormatEmails[i]);

			Assert.assertFalse("Status code is not 200", r.isOK());
			Assert.assertEquals("Error message content", DefaultValues.InvalidEmail, r.errorMessage);
			Assert.assertTrue("Empty authenticate token", r.token == null || r.token.isEmpty());

			long stop = System.currentTimeMillis();
			rlog.log((i + 1) + "\t" + (stop - start));
		}
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signup" })
	public void SignUpWrongFormatPasswords() {
		ResultLogger rlog = ResultLogger.getLogger("signup_wrong_format_password");
		rlog.log("Number of try\ttime taken");
		for (int i = 0; i < wrongFormatPasswords.length; i++) {
			long start = System.currentTimeMillis();
			AccountResult r = MVPApi.signUp(wellFormatEmail, wrongFormatPasswords[i], udid);
			r.printKeyPairsValue();

			Assert.assertFalse("Status code is not 200", r.isOK());
//			Assert.assertEquals("Error message content", DefaultValues.InvalidPassword, r.errorMessage);
			Assert.assertTrue("Empty authenticate token", r.token == null || r.token.isEmpty());
			long stop = System.currentTimeMillis();
			rlog.log((i + 1) + "\t" + (stop - start));
		}
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signup" })
	public void SignUpDuplicateEmail() {
		ResultLogger rlog = ResultLogger.getLogger("signup_duplicated");
		rlog.log("Number of try\ttime taken");
		long start = System.currentTimeMillis();
		duplicatedEmail = MVPApi.generateUniqueEmail();
		MVPApi.signUp(duplicatedEmail, wellFormatPassword, udid);

		for (int i = 0; i < 10; i++) {
			AccountResult r = MVPApi.signUp(duplicatedEmail, wellFormatPassword, udid);
			r.printKeyPairsValue();

			Assert.assertFalse("Status code is not 200", r.isOK());
			Assert.assertEquals("Error message content", r.errorMessage, DefaultValues.DuplicateEmail);
			Assert.assertTrue("Empty authenticate token", r.token == null || r.token.isEmpty());
			long stop = System.currentTimeMillis();
			rlog.log((i + 1) + "\t" + (stop - start));
		}
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signup" })
	public void SignUpNotRegisteredEmail() {
		ResultLogger rlog = ResultLogger.getLogger("sign_up_ok");
		rlog.log("Number of try\ttime taken");
		AccountResult r = MVPApi.signUp(notregisteredEmail, wellFormatPassword, udid);
		r.printKeyPairsValue();

		long start = System.currentTimeMillis();

		Assert.assertTrue("Status code is 200", r.isOK());
		Assert.assertTrue("Empty error message", r.isEmptyErrMsg());
		Assert.assertTrue("Authenticate token is not empty", !r.token.isEmpty());

		long stop = System.currentTimeMillis();
		rlog.log("0\t" + (stop - start));
	}

}
