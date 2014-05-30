package com.misfit.ta.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

public class SeleniumUnifiedDriver {
	
	// static
	public static String BROWSER_FIREFOX = "firefox";
	public static String BROWSER_CHROME = "chrome";
	public static String BROWSER_SAFARI = "safari";
	public static String BROWSER_IE = "internet explorer";
	
	public static String PLATFORM_MAC = "mac";
	public static String PLATFORM_WINDOWS = "windows";
	public static String PLATFORM_LINUX = "linux";
	
	
	// fields
	public WebDriver driver;
	

	// constructor
	private DesiredCapabilities createDesiredCapabilities(String browser, String version, String platform) {
		
		// parse params
		browser = browser == null ? null : browser.toLowerCase();
		version = version == null ? null : version.toLowerCase();
		platform = platform == null ? null : platform.toLowerCase();

		// setup browser
		DesiredCapabilities dc = null;

		if (browser == null || browser.contains(BROWSER_CHROME))
			dc = DesiredCapabilities.chrome();
		else if (browser.contains(BROWSER_FIREFOX) || browser.equals("ff"))
			dc = DesiredCapabilities.firefox();
		else if (browser.contains(BROWSER_SAFARI))
			dc = DesiredCapabilities.safari();
		else if (browser.contains(BROWSER_IE) || browser.equals("ie"))
			dc = DesiredCapabilities.internetExplorer();
		else
			dc = DesiredCapabilities.chrome();

		// setup version
		if (version != null && !version.equals("") && !version.equals("any"))
			dc.setVersion(version);

		// setup platform
		if (platform == null || platform.equals("any"))
			dc.setPlatform(Platform.ANY);
		else if (platform.contains(PLATFORM_MAC))
			dc.setPlatform(Platform.MAC);
		else if (platform.contains(PLATFORM_WINDOWS))
			dc.setPlatform(Platform.WINDOWS);
		else if (platform.contains(PLATFORM_LINUX))
			dc.setPlatform(Platform.LINUX);
		else
			dc.setPlatform(Platform.ANY);

		return dc;
	}
	
	public SeleniumUnifiedDriver(WebDriver driver) {
	
		this.driver = driver;
	}
	
	public SeleniumUnifiedDriver(String browser) {

        // setup driver
        if (browser == null || browser.contains(BROWSER_CHROME))
            this.driver = new ChromeDriver();
        else if (browser.contains(BROWSER_FIREFOX) || browser.equals("ff"))
            this.driver = new FirefoxDriver();
        else if (browser.contains(BROWSER_SAFARI))
        	this.driver = new SafariDriver();
        else if (browser.contains(BROWSER_IE) || browser.equals("ie"))
            this.driver = new InternetExplorerDriver();
        else
        	this.driver = new ChromeDriver();	
	}
	
	public SeleniumUnifiedDriver(String browser, String version, String platform, String nodeUrl)
            throws MalformedURLException {

		/* TODO:
		 * the code below can be written more simplier
		 * this.driver = new RemoteWebDriver(new URL(nodeUrl), dc);
		 * however due to some unknown problem, this cannot run with safari and ff
		 * using this way
		 * 
		 * The code below run well with chrome and safari but not ff
		 */
		
		DesiredCapabilities dc = createDesiredCapabilities(browser, version, platform);
		
		// setup driver
        if (browser == null || browser.contains(BROWSER_CHROME))
        	this.driver = new RemoteWebDriver(new URL(nodeUrl), new ChromeDriver(dc).getCapabilities());
        else if (browser.contains(BROWSER_FIREFOX) || browser.equals("ff"))
        	this.driver = new RemoteWebDriver(new URL(nodeUrl), new FirefoxDriver(dc).getCapabilities());
        else if (browser.contains(BROWSER_SAFARI))
        	this.driver = new RemoteWebDriver(new URL(nodeUrl), new SafariDriver().getCapabilities());
        else if (browser.contains(BROWSER_IE) || browser.equals("ie"))
        	this.driver = new RemoteWebDriver(new URL(nodeUrl), new InternetExplorerDriver(dc).getCapabilities());
        else
        	this.driver = new RemoteWebDriver(new URL(nodeUrl), new ChromeDriver(dc).getCapabilities());
    }

	
	// navigate
	public void goTo(String url) {
		
		driver.get(url);
	}
	
	public void goForward() {
		
		driver.navigate().forward();
	}
	
	public void goBack() {
		
		driver.navigate().back();
	}
	
	public void refresh() {
		
		driver.navigate().refresh();
	}
	
		
	
	// finding methods 
    public WebElement findElement(By by) {
    	
    	try {
    		return driver.findElement(by);
    	}
    	catch (Exception e) {
    		return null;
    	}
    }
    
    public List<WebElement> findElements(By by) {
    	
    	try {
    		return driver.findElements(by);
    	}
    	catch (Exception e) {
    		return null;
    	}
    }
    
}





