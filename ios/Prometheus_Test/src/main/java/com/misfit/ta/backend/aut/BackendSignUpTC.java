package com.misfit.ta.backend.aut;

import junit.framework.Assert;

import org.testng.annotations.*;

import com.misfit.ta.backend.data.*;
import com.misfit.ta.backend.api.*;
import com.misfit.ta.ios.AutomationTest;

public class BackendSignUpTC extends BackendAutomation {
    String[] wrongFormatEmails = { "", "wrong1", "wrong2@", "wrong3 @a.b", " wrong4@a.a" };
    String wellFormatEmail = "wellformat@qa.com";
    String duplicatedEmail = "";
    String notregisteredEmail;

    String[] wrongFormatPasswords = { "", "allletters", "11413312", "tiny1" };
    String wellFormatPassword = "qwerty1";

    String udid;

    @BeforeClass
    public void setup() {
        notregisteredEmail = MVPApi.generateUniqueEmail();
        udid = DefaultValues.UDID;
    }

    @Test(groups = { "ios", "MVPBackend", "api", "signup" })
    public void SignUpWrongFormatEmails() {
        for (int i = 0; i < wrongFormatEmails.length; i++) {
            AccountResult r = MVPApi.signUp(wrongFormatEmails[i], wellFormatPassword, udid);
            r.printKeyPairsValue();

            Assert.assertFalse("Status code is not 200", r.isOK());
            Assert.assertEquals("Error message content", r.errorMessage, DefaultValues.InvalidEmail);
            Assert.assertTrue("Empty authenticate token", r.token.isEmpty());
        }
    }

    @Test(groups = { "ios", "MVPBackend", "api", "signup" })
    public void SignUpWrongFormatPasswords() {
        for (int i = 0; i < wrongFormatPasswords.length; i++) {
            AccountResult r = MVPApi.signUp(wellFormatEmail, wrongFormatPasswords[i], udid);
            r.printKeyPairsValue();

            Assert.assertFalse("Status code is not 200", r.isOK());
            Assert.assertEquals("Error message content", r.errorMessage, DefaultValues.InvalidPassword);
            Assert.assertTrue("Empty authenticate token", r.token.isEmpty());
        }
    }

    @Test(groups = { "ios", "MVPBackend", "api", "signup" })
    public void SignUpDuplicateEmail() {
        duplicatedEmail = MVPApi.generateUniqueEmail();
        MVPApi.signUp(duplicatedEmail, wellFormatPassword, udid);

        AccountResult r = MVPApi.signUp(duplicatedEmail, wellFormatPassword, udid);
        r.printKeyPairsValue();

        Assert.assertFalse("Status code is not 200", r.isOK());
        Assert.assertEquals("Error message content", r.errorMessage, DefaultValues.DuplicateEmail);
        Assert.assertTrue("Empty authenticate token", r.token.isEmpty());
    }

    @Test(groups = { "ios", "MVPBackend", "api", "signup" })
    public void SignUpNotRegisteredEmail() {
        AccountResult r = MVPApi.signUp(notregisteredEmail, wellFormatPassword, udid);
        r.printKeyPairsValue();

        Assert.assertTrue("Status code is 200", r.isOK());
        Assert.assertTrue("Empty error message", r.isEmptyErrMsg());
        Assert.assertTrue("Authenticate token is not empty", !r.token.isEmpty());
    }

}
