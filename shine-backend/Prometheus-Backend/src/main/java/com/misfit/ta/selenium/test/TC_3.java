package com.misfit.ta.selenium.test;

import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.misfit.ta.selenium.Has;
import com.misfit.ta.selenium.SeleniumUnifiedDriver;

public class TC_3
{
	SeleniumUnifiedDriver driver;
	
	@BeforeMethod
	public void beforeMethod()
	{
		driver = new SeleniumUnifiedDriver("chrome");
	}
	
	@AfterMethod
	public void afterMethod()
	{
		driver.driver.close();
		driver.driver.quit();
	}
	
	@Test
	public void test() throws InterruptedException, MalformedURLException
	{
		driver.goTo("http://filehippo.com/");

		print(driver.findElement(Has.attribute("title")) != null);
		print(driver.findElement(Has.attribute("title", "Popular software")) != null);
		print(driver.findElement(Has.attribute("placeholder")) != null);
		print(driver.findElement(Has.attribute("placeholder", "Popular software")) != null);
		print(driver.findElement(Has.attribute("placeholder", "Search for something...")) != null);
		print(driver.findElement(Has.attribute("placeholders")) != null);
		print(driver.findElement(Has.attribute("placeholders", "not thing")) != null);
//		print(driver.findElement(Has.text("Contact Us")) != null);
//		print(driver.findElement(Has.text("contact us", false)) != null);
//		print(driver.findElement(Has.text("well known media")) != null);
//		print(driver.findElement(Has.text("well known media", false)) != null);
//		print(driver.findElement(Has.partialText("Well Known Media")) != null);
//		print(driver.findElement(Has.partialText("well known media", false)) != null);
	}
	
	public void print(Object message) {
		
		System.out.println(message);
	}
	
}