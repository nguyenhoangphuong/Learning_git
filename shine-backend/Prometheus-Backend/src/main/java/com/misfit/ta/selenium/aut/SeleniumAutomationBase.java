package com.misfit.ta.selenium.aut;

import java.net.MalformedURLException;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.misfit.ta.aut.AutomationTest;
import com.misfit.ta.selenium.driver.ExtendedWebDriver;

public class SeleniumAutomationBase extends AutomationTest
{
	// fields
	protected ExtendedWebDriver driver = null;
	
	
	// before class
	@BeforeClass(alwaysRun = true)
	public void beforeClass(ITestContext ctx) {	
		
		super.beforeTest(ctx);
		
		String hubUrl = ctx.getCurrentXmlTest().getParameter("hubUrl");
		String browser = ctx.getCurrentXmlTest().getParameter("browser");
		String version = ctx.getCurrentXmlTest().getParameter("version");
		String platform = ctx.getCurrentXmlTest().getParameter("platform");
		
		try {
			this.driver = (hubUrl == null || hubUrl.isEmpty() ?
					new ExtendedWebDriver(browser) :
					new ExtendedWebDriver(browser, version, platform, hubUrl));
		} catch (MalformedURLException e) {
			
			System.out.print("[ERROR] Error while launching browser");
			e.printStackTrace();
		}
	}
	
	
	// after class
	@AfterClass(alwaysRun = true)
	public void afterClass() {

		driver.quit();
	}

}
