package com.misfit.ta.selenium.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

public class TC_Base
{
	// ===============================================================================
	// Attributes
	// All tests have a driver which is use to do things like: click buttons, fill
	// text..., the nodeUrl which tell on which machine and which port the test will
	// run and the baseUrl - www.google.com.vn for example.
	// ===============================================================================
	protected WebDriver driver = null;
	protected String nodeUrl;
	protected String baseUrl;
	
	// ===============================================================================
	// Global setup and teardown	
	// All tests have to setup its node first and quit the browser after all the test
	// methods had been run. The capabilities of the driver is specified through the
	// config file.
	//===============================================================================
	@Parameters({"browser", "version", "platform", "nodeUrl", "baseUrl"})
	@BeforeClass
	public void beforeClass(String browser, String version, String platform, String nodeUrl, String baseUrl)
			throws MalformedURLException
	{	
//		System.setProperty("webdriver.firefox.bin", "/Applications/Firefox.app/Contents/MacOS/firefox-bin");
		DesiredCapabilities dc = null;
		
		// setup url
		this.nodeUrl = nodeUrl;
		this.baseUrl = baseUrl;
		
		// setup browser
		if(browser.equalsIgnoreCase("Default") || browser.equalsIgnoreCase("Firefox") || browser.equalsIgnoreCase(""))
			dc = DesiredCapabilities.firefox();
		else if(browser.equalsIgnoreCase("Chrome"))
			dc = DesiredCapabilities.chrome();
		else if(browser.equalsIgnoreCase("IE"))
			dc = DesiredCapabilities.internetExplorer();
		else if(browser.equalsIgnoreCase("Safari"))
			dc = DesiredCapabilities.safari();
		
		// setup version
		if(!version.equalsIgnoreCase("") && version.equalsIgnoreCase("Any"))
			dc.setVersion(version);
		
		// setup platform
		if(platform.equalsIgnoreCase("Any"))
			dc.setPlatform(Platform.ANY);
		else if(platform.equalsIgnoreCase("Default") || platform.equalsIgnoreCase("Mac") || platform.equalsIgnoreCase(""))
			dc.setPlatform(Platform.MAC);
		else if(platform.equalsIgnoreCase("Windows"))
			dc.setPlatform(Platform.WINDOWS);
		else if(platform.equalsIgnoreCase("Linux"))
			dc.setPlatform(Platform.LINUX);
		
		// setup driver
		this.driver = new RemoteWebDriver(new URL(nodeUrl), dc);
	}

	@AfterClass
	public void afterClass() 
	{
		driver.quit();
	}

	// ===============================================================================
	// Common API
	// This is the place for common API that share among tests
	// ===============================================================================
	public void print(String text)
	{
		System.out.print(text);
	}
	
	public boolean isTextPresent(String text)
	{
		return (driver != null && driver.getPageSource().contains(text));
	}
	
}
