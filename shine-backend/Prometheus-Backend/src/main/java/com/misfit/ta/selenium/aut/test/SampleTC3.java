package com.misfit.ta.selenium.aut.test;

import org.testng.Assert;
import org.testng.annotations.*;

import com.misfit.ta.selenium.aut.SeleniumAutomationBase;
import com.misfit.ta.selenium.driver.Has;

public class SampleTC3 extends SeleniumAutomationBase
{
	
	@BeforeMethod
	public void beforeMethod()
	{
	}
	
	@AfterMethod
	public void afterMethod()
	{
	}
	
	@Test
	public void login() throws InterruptedException
	{
		driver.goTo("https://tester.int.misfitwearables.com");
		driver.waitForElement(Has.partialText("Authentication"));
		
		driver.findElement(Has.name("username")).sendKeys("admin");
		driver.findElement(Has.name("password")).sendKeys("misfit");
		driver.findElement(Has.name("submit")).click();
		driver.waitForPageTitle("App Installer");
		
		Assert.assertTrue(driver.getTitle().contains("App Installer"), "Title is correct");
	}
	
}