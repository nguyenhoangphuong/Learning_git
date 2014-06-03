package com.misfit.ta.selenium.aut.test;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.*;

import com.misfit.ta.selenium.aut.SeleniumAutomationBase;
import com.misfit.ta.selenium.driver.Has;

public class SampleTC2 extends SeleniumAutomationBase
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
	public void googleNoResult() throws InterruptedException
	{
		String keyword = "asdaghfdkhasjasalska dlasj fhagsfksa dhjask";
		driver.goTo(baseUrl);
		driver.findElement(By.id("gbqfq")).clear();
		driver.findElement(By.id("gbqfq")).sendKeys(keyword);
		driver.findElement(By.id("gbqfq")).sendKeys(Keys.ENTER);
		driver.waitForPageTitle(keyword);
		driver.delay(5);
		
		Assert.assertTrue(driver.getTitle().startsWith(keyword));
		Assert.assertTrue(driver.findElement(Has.partialText("Không tìm thấy")) != null);
	}
	
}