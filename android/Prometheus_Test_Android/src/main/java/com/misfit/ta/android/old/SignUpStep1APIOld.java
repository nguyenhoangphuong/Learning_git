package com.misfit.ta.android.old;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.gui.SignIn;
import com.misfit.ta.android.gui.SignUp;
import com.misfit.ta.android.gui.Helper.Helper;
import com.misfit.ta.modelAPI.ModelAPI;

public class SignUpStep1APIOld extends ModelAPI {
   public SignUpStep1APIOld(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
    super(automation, model, efsm, generator, weight);
  }

   String existedEmail = "a@a.a";
   
   String name = "";
   String email = "";
   String password = "";
   int[] birthday = {16, 9, 1991};
   boolean isMale = true;
   boolean isUS = true;
   String height = "5'6\"";
   String weight = "120.0";

  /**
   * This method implements the Edge 'e_confirmAlert'
   * 
   */
  public void e_confirmAlert() {
   // blocked
  }




  /**
   * This method implements the Edge 'e_init'
   * 
   */
  public void e_init() {
	 
  }




  /**
   * This method implements the Edge 'e_inputBirthDay'
   * 
   */
  public void e_inputBirthDay() {
   // blocked
  }




  /**
   * This method implements the Edge 'e_inputDuplicatedEmail'
   * 
   */
  public void e_inputDuplicatedEmail() {
	  email = existedEmail;
	  password = "qwerty1";
	  
	  SignUp.inputEmail(email);
	  SignUp.inputPassword(password);
	  SignUp.pressNext();
  }




  /**
   * This method implements the Edge 'e_inputInvalidEmail'
   * 
   */
  public void e_inputInvalidEmail() {
	  email = System.currentTimeMillis() + ".com";
	  password = "qwerty1";
	  
	  SignUp.inputEmail(email);
	  SignUp.inputPassword(password);
	  SignUp.pressNext();
  }




  /**
   * This method implements the Edge 'e_inputInvalidPassword'
   * 
   */
  public void e_inputInvalidPassword() {
	  email = System.currentTimeMillis() + "@qa.com";
	  password = "qwerty";
	  
	  SignUp.inputEmail(email);
	  SignUp.inputPassword(password);
	  SignUp.pressNext();
  }




  /**
   * This method implements the Edge 'e_inputName'
   * 
   */
  public void e_inputName() {
	  name = Helper.createRandomString();
	  SignUp.inputName(name);
  }




  /**
   * This method implements the Edge 'e_inputSex'
   * 
   */
  public void e_inputSex() {
	  isMale = System.currentTimeMillis() % 2 == 0;
	  SignUp.inputSex(isMale);
  }




  /**
   * This method implements the Edge 'e_inputUnit'
   * 
   */
  public void e_inputUnit() {
	  isUS = System.currentTimeMillis() % 2 == 0;
	  SignUp.inputUnit(isUS);
  }




  /**
   * This method implements the Edge 'e_inputWeight'
   * 
   */
  public void e_inputWeight() {
   // blocked
  }




  /**
   * This method implements the Edge 'e_tapIDontHaveAnAccount'
   * 
   */
  public void e_tapIDontHaveAnAccount() {
	  SignUp.chooseSignUp();
  }




  /**
   * This method implements the Vertex 'v_InitialView'
   * 
   */
  public void v_InitialView() {
	  Assert.assertTrue(SignIn.isInitViewVisible(), "Current view is not InitView");
  }




  /**
   * This method implements the Vertex 'v_SignUpStep1'
   * 
   */
  public void v_SignUpStep1() {
	  Assert.assertTrue(SignUp.isAtStep1());
	  Assert.assertTrue(SignUp.getName().equals(name), "Different is not the same with input");
	  Assert.assertTrue(SignUp.getEmail().equals(email), "Email is not the same with input");
	  Assert.assertTrue(SignUp.getSex() == isMale, "Sex is not the same with input");
	  Assert.assertTrue(SignUp.getUnit() == isUS, "Unit is not the same with input");
  }




  /**
   * This method implements the Vertex 'v_SignUpStep1DuplicatedEmail'
   * 
   */
  public void v_SignUpStep1DuplicatedEmail() {
   // blocked by alert
  }




  /**
   * This method implements the Vertex 'v_SignUpStep1InvalidEmail'
   * 
   */
  public void v_SignUpStep1InvalidEmail() {
   // blocked by alert
  }




  /**
   * This method implements the Vertex 'v_SignUpStep1InvalidPassword'
   * 
   */
  public void v_SignUpStep1InvalidPassword() {
	// blocked by alert 
  }



}

