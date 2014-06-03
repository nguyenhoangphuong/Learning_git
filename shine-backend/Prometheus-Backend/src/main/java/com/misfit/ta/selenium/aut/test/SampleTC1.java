package com.misfit.ta.selenium.aut.test;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.*;

import com.misfit.ta.selenium.aut.SeleniumAutomationBase;

public class SampleTC1 extends SeleniumAutomationBase
{
	private String baseUrl = "https://www.google.com.vn/";
	
	@BeforeMethod
	public void beforeMethod()
	{
	}
	
	@AfterMethod
	public void afterMethod()
	{
	}
	
	@Test
	public void googleBoys()
	{
		String keyword = "boys";
		driver.goTo(baseUrl);
		driver.findElement(By.id("gbqfq")).clear();
		driver.findElement(By.id("gbqfq")).sendKeys(keyword);
		driver.findElement(By.id("gbqfq")).sendKeys(Keys.ENTER);
		driver.waitForPageTitle(keyword);
		driver.delay(5);
		
		System.out.println(driver.getTitle());
		Assert.assertTrue(driver.getTitle().startsWith(keyword));
	}
	
}