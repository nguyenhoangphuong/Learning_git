/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.misfit.ta.ios.aut;


import org.graphwalker.exceptions.StopConditionException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.gui.Backend;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.utils.TextTool;

/**
 * 
 * @author qa
 */
public class BackendTest extends AutomationTest {

  @Test(groups = {"ios", "Prometheus", "backend"})
  public void emailSubmitting() throws InterruptedException, StopConditionException {
      String email = TextTool.getRandomString(10) + "@" + "test.com";
      System.out.println("LOG [BackendTest.sampleTest]:email " + email);
      String response = Backend.submitEmail(email);
      Assert.assertTrue(Backend.isEmailExisting(email), "Email is not in DB");
  }
  
  @Test(groups = {"ios", "Prometheus", "backend"})
  public void emailSubmittingLong() throws InterruptedException, StopConditionException {
      String email = TextTool.getRandomString(1000) + "@" + "test.com";
      System.out.println("LOG [BackendTest.sampleTest]:email " + email);
      String response = Backend.submitEmail(email);
      Assert.assertTrue(!Backend.isEmailExisting(email), "Long email is in DB");
  }
}
