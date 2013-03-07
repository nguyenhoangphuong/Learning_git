package com.misfit.ta.android.modelapi;

import java.io.File;
import java.util.HashMap;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.gui.*;
import com.misfit.ta.android.gui.Helper.Helper;
import com.misfit.ta.modelAPI.ModelAPI;

public class SignUpStep3API extends ModelAPI {
   public SignUpStep3API(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
    super(automation, model, efsm, generator, weight);
  }

   int goalLevel = 0;

  /**
   * This method implements the Edge 'e_chooseActive'
   * 
   */
  public void e_chooseActive() {
	  goalLevel = 0;
   SignUp.setGoal(0);
  }




  /**
   * This method implements the Edge 'e_chooseHighlyActive'
   * 
   */
  public void e_chooseHighlyActive() {
	  goalLevel = 2;
   SignUp.setGoal(2);
  }




  /**
   * This method implements the Edge 'e_chooseVeryActive'
   * 
   */
  public void e_chooseVeryActive() {
	  goalLevel = 1;
   SignUp.setGoal(1);
  }




  /**
   * This method implements the Edge 'e_init'
   * 
   */
  public void e_init() {
   //TODO: 
  }




  /**
   * This method implements the Edge 'e_submit'
   * 
   */
  public void e_submit() {
   SignUp.inputEmail("qa_automation@qa.com");
   SignUp.inputPassword("qwerty1");
  }




  /**
   * This method implements the Edge 'e_tapIDontHaveAnAccount'
   * 
   */
  public void e_tapIDontHaveAnAccount() {
   SignUp.chooseSignUp();
  }




  /**
   * This method implements the Edge 'e_tapNext'
   * 
   */
  public void e_tapNext() {
   SignUp.pressNext();
  }




  /**
   * This method implements the Vertex 'v_InitialView'
   * 
   */
  public void v_InitialView() {
   Assert.assertTrue(SignIn.isInitViewVisible(), "At InitView");
  }




  /**
   * This method implements the Vertex 'v_SignUpStep1'
   * 
   */
  public void v_SignUpStep1() {
   Assert.assertTrue(SignUp.isAtStep1(), "At Step 1");
  }




  /**
   * This method implements the Vertex 'v_SignUpStep2'
   * 
   */
  public void v_SignUpStep2() {
   Assert.assertTrue(SignUp.isAtStep2(), "At Step 2");
  }




  /**
   * This method implements the Vertex 'v_SignUpStep3'
   * 
   */
  public void v_SignUpStep3() {
	  Assert.assertTrue(SignUp.isAtStep3(), "At Step 3");
  }



  /**
   * This method implements the Vertex 'v_SignUpStep3'
   * 
   */
  public void v_SignUpStep3Updated() {
	  // to do: create a static array that hold data for asserting
	  
//	  HashMap<String, Object> info = SignUp.getCurrentGoalInfo();
//	  
//	  Assert.assertTrue(SignUp.isAtStep3(), "At Step 3");
//	  Assert.assertTrue(info.get("title") == null, "Goal's title");
//	  Assert.assertTrue(info.get("activity") == null, "Goal's number of activities");
//	  Assert.assertTrue(info.get("point") == null, "Goal's total points");
//	  Assert.assertTrue(info.get("step") == null, "Goal's total steps");
  }
  
  
}

