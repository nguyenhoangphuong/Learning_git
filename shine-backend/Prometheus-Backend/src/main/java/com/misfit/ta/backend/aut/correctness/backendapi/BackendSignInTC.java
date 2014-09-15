package com.misfit.ta.backend.aut.correctness.backendapi;


import org.testng.Assert;
import org.testng.annotations.*;

import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.api.*;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;

public class BackendSignInTC extends BackendAutomation {
	
    String validEmail;
    String invalidEmail;
    String validPassword = "qwerty1";
    String invalidPassword = "wrong123";
    
	String[] wrongFormatEmails = { "", "wrong100", "wrong2@", "wrong3 @a.b" };
	String wellFormatEmail = "wellformat@qa.com";
	String[] wrongFormatPasswords = { "", "short" };
	String wellFormatPassword = "qwerty1";
    
    @BeforeClass(alwaysRun = true)
    public void setUp() {
        validEmail = MVPApi.generateUniqueEmail();
        invalidEmail = MVPApi.generateUniqueEmail() + ".invalid";
        MVPApi.signUp(validEmail, validPassword);

    }
    
    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signup" })
	public void SignInWrongFormatEmails() {
		for (int i = 0; i < wrongFormatEmails.length; i++) {
			AccountResult r = MVPApi.signIn(wrongFormatEmails[i], wellFormatPassword);
			r.printKeyPairsValue();
			
			Assert.assertEquals(r.statusCode, 400, "Status code");
			Assert.assertEquals(DefaultValues.InvalidEmail, r.errorMessage, "Error message content");
			Assert.assertTrue(r.token == null || r.token.isEmpty(), "Empty authenticate token");
		}
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signup" })
	public void SignInWrongFormatPasswords() {
		for (int i = 0; i < wrongFormatPasswords.length; i++) {
			AccountResult r = MVPApi.signIn(wellFormatEmail, wrongFormatPasswords[i]);
			r.printKeyPairsValue();

			Assert.assertEquals(r.statusCode, 400, "Status code");
			Assert.assertEquals(DefaultValues.InvalidPassword, r.errorMessage, "Error message content");
			Assert.assertTrue(r.token == null || r.token.isEmpty(), "Empty authenticate token");
		}
	}

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignInWrongEmail() {
        AccountResult r = MVPApi.signIn(invalidEmail, validPassword);
        r.printKeyPairsValue();

        Assert.assertEquals(r.statusCode, 400, "Status code");
        Assert.assertEquals(r.errorMessage, DefaultValues.WrongAccountMsg, "Error message content");
        Assert.assertTrue(r.token == null || r.token.isEmpty(), "Empty authenticate token");
    }

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignInWrongPassword() {
        AccountResult r = MVPApi.signIn(validEmail, invalidPassword);
        r.printKeyPairsValue();

        Assert.assertEquals(r.statusCode, 400, "Status code");
        Assert.assertEquals(r.errorMessage, DefaultValues.WrongAccountMsg, "Error message content");
        Assert.assertTrue(r.token == null || r.token.isEmpty(), "Empty authenticate token");
    }

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignInValidAccount() {
        AccountResult r = MVPApi.signIn(validEmail, validPassword);
        r.printKeyPairsValue();

        Assert.assertTrue(r.isOK(), "Status code is 200");
        Assert.assertTrue(r.isEmptyErrMsg(), "Empty error message");
        Assert.assertTrue(!r.token.isEmpty(), "Authenticate token is not empty");
    }

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignInWithTwoDevices() {
        AccountResult r1 = MVPApi.signIn(validEmail, validPassword);
        r1.printKeyPairsValue();

        AccountResult r2 = MVPApi.signIn(validEmail, validPassword);
        r2.printKeyPairsValue();

        Assert.assertNotEquals(r1.token, r2.token, "Different token between two devices");
    }

    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignBackInDifferentToken() {
    	
        // check if token changed after sign out and sign back in again
        AccountResult r1 = MVPApi.signIn(validEmail, validPassword);
        MVPApi.signOut(r1.token);
        AccountResult r2 = MVPApi.signIn(validEmail, validPassword);

        Assert.assertNotEquals(r1.token, r2.token, "Different token after sign out and sign in again");
    }
    
    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signin" })
    public void SignUpFlow() {
    	String email = MVPApi.generateUniqueEmail();
    	String pass = "qwerty";
    	
    	//Sign Up New Account
    	AccountResult accountResult = MVPApi.signUp(email, pass);
    	Assert.assertTrue(accountResult.isOK(), "Sign Up failed!");
    	//Sign In
    	accountResult = MVPApi.signIn(email, pass);
    	Assert.assertTrue(accountResult.isOK(), "Sign In failed!");
    	//Sign Out
    	MVPApi.signOut(accountResult.token);
    	
    	//Sign In with wrong pass
    	accountResult = MVPApi.signIn(email, "qwerty1");
    	Assert.assertTrue(!accountResult.isOK(), "Sign In is successfull with wrong password");
    	//Sign In with wrong email
    	accountResult = MVPApi.signIn("testqa14@misfitqa.com", pass);
    	Assert.assertTrue(!accountResult.isOK(), "Sign In is successfull with wrong email");
    	//Sign Up again with the same email
    	AccountResult r1 = MVPApi.signUp(email, "qwerty1");
    	Assert.assertTrue(!r1.isOK(), "Sign Up is successfull with the same email ");
    }
    
    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "reset" })
    public void ResetPassword_ReceiveEmail(){
    	String email = MVPApi.generateUniqueEmail();
    	String pass = "qwerty";
    	
    	//Sign Up
    	AccountResult accountResult = MVPApi.signUp(email, pass);
    	Assert.assertTrue(accountResult.isOK(), "Sign Up is failed");
    	
    	//Supplying true email to reset password
    	BaseResult baseResult = MVPApi.requestEmailToChangePassword(email);
    	Assert.assertTrue(baseResult.isOK(), "The email you entered is not associated with a Shine account");
    	
    	//Supplying wrong email to reset password
    	baseResult = MVPApi.requestEmailToChangePassword("trunghoangqa1@misfitqa.com");
    	Assert.assertTrue(baseResult.isNotShineAccount(), "Wrong email but associated with Shine account");
    }
    
    @Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "changepassword" })
    public void ChangePassword(){
    	String email = MVPApi.generateUniqueEmail();
    	String pass = "qwerty";
    	
    	//SignUp
    	MVPApi.signUp(email, pass);
    	String token = MVPApi.signIn(email, pass).token;
    	
    	//Change password with new password = confirm password
    	String newPassword = "qwerty1";
    	BaseResult baseResult = MVPApi.changePassword(token, newPassword, newPassword);
    	Assert.assertTrue(baseResult.isOK(), "Error message : " + baseResult.errorMessage);
    	
    	//SignIn after change password
    	AccountResult accountResult = MVPApi.signIn(email, newPassword);
    	Assert.assertTrue(accountResult.isOK(), "SignIn failed after changing password");
    	
    	//Change password with new password != confirm password
    	baseResult = MVPApi.changePassword(token, newPassword, pass);
    	Assert.assertTrue(!baseResult.isOK(), "Change password is failed!");
    }
}
