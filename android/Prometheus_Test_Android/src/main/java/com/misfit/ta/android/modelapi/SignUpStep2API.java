package com.misfit.ta.android.modelapi;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.gui.SignIn;
import com.misfit.ta.android.gui.SignUp;
import com.misfit.ta.android.gui.Helper.Helper;
import com.misfit.ta.modelAPI.ModelAPI;

public class SignUpStep2API extends ModelAPI {
   public SignUpStep2API(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
    super(automation, model, efsm, generator, weight);
  }

   int date = 16;
   int month = 9;
   int year = 1991;
   String height = "170 cm";
   String weight = "69.0 kg";
   boolean sex = true;

  /**
   * This method implements the Edge 'e_init'
   * 
   */
  public void e_init() {
   //TODO: 
  }




  /**
   * This method implements the Edge 'e_tapIDontHaveAnAccount'
   * 
   */
  public void e_tapIDontHaveAnAccount() {
   		SignUp.chooseSignUp();
   		Helper.wait1();
  }
  
  
  
  
  /**
   * This method implements the Edge 'e_inputBirthDay'
   * 
   */
  public void e_inputBirthDay() {
	  SignUp.inputBirthday(date, month, year);
  }




  /**
   * This method implements the Edge 'e_inputHeight'
   * 
   */
  public void e_inputHeight() {
   		SignUp.inputHeight(height);
  }




  /**
   * This method implements the Edge 'e_inputSex'
   * 
   */
  public void e_inputSex() {
	  	SignUp.inputSex(sex);
  }




  /**
   * This method implements the Edge 'e_inputWeight'
   * 
   */
  public void e_inputWeight() {
   		SignUp.inputWeight(weight);
  }




  /**
   * This method implements the Edge 'e_tapNext'
   * 
   */
  public void e_submit() {
	  SignUp.inputEmail(System.nanoTime() + "@qa.com");
	  SignUp.inputPassword("qwery1");
	  SignUp.pressNext();
	  Helper.wait(3);
  }




  /**
   * This method implements the Vertex 'v_InitialView'
   * 
   */
  public void v_InitialView() {
	  Assert.assertTrue(SignIn.isInitViewVisible(), "Current view is InitView");
  }




  /**
   * This method implements the Vertex 'v_SignUpStep1'
   * 
   */
  public void v_SignUpStep1() {
	  Assert.assertTrue(SignUp.isAtStep1(), "Current view is SignUpStep1");
  }




  /**
   * This method implements the Vertex 'v_SignUpStep2'
   * 
   */
  public void v_SignUpStep2() {
	  Assert.assertTrue(SignUp.isAtStep2(), "Current view is SignUpStep2");
  }



}

