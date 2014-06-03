package com.misfit.ta.selenium.aut.test;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.misfit.ta.selenium.aut.SeleniumAutomationBase;
import com.misfit.ta.selenium.driver.Has;

public class SampleTC4 extends SeleniumAutomationBase
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
	public void singleAction() throws InterruptedException
	{
		driver.goTo("http://mootools.net/demos/?demo=Drag.Drop");
		driver.waitForElement(Has.partialText("Drag.Drop"));
		driver.delay(3);
		
		Actions builder = new Actions(driver.getDriver());
		WebElement src = driver.findElement(Has.cssSelector("#draggables > div"));
		WebElement des = driver.findElement(Has.cssSelector("#droppables > div"));

		builder.dragAndDrop(src, des);
		builder.build().perform();
		driver.delay(5);
	}
	
	@Test
	public void multiActions() throws InterruptedException
	{
		driver.goTo("https://tester.int.misfitwearables.com");
		driver.waitForElement(Has.partialText("Authentication"));
		driver.delay(3);
		
		Actions builder = new Actions(driver.getDriver());
		builder
		.sendKeys(driver.findElement(Has.name("username")), "admin")
		.sendKeys(driver.findElement(Has.name("password")), "misfit")	
		.click(driver.findElement(Has.name("submit")));
		
		builder.build().perform();
		driver.waitForPageTitle("App Installer");
		driver.delay(3);
	}	
}