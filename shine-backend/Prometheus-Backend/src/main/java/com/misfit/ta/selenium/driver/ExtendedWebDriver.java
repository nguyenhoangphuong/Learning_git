package com.misfit.ta.selenium.driver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.misfit.ta.selenium.element.ExtendedWebElement;
import com.misfit.ta.selenium.element.ExtendedWebElementImpl;

public class ExtendedWebDriver {

	// static
	public static final int DEFAULT_TIMEOUT = 5;
	
	public static final String BROWSER_FIREFOX = "firefox";
	public static final String BROWSER_CHROME = "chrome";
	public static final String BROWSER_SAFARI = "safari";
	public static final String BROWSER_IE = "internet explorer";

	public static final String PLATFORM_MAC = "mac";
	public static final String PLATFORM_WINDOWS = "windows";
	public static final String PLATFORM_LINUX = "linux";


	// fields
	private WebDriver driver;


	// constructor
	private DesiredCapabilities createDesiredCapabilities(String browser, String version, String platform) {

		// setup browser
		DesiredCapabilities dc = new DesiredCapabilities();

		if (browser == null || browser.isEmpty() || browser.contains(BROWSER_CHROME))
			dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
		else if (browser.contains(BROWSER_FIREFOX) || browser.equals("ff"))
			dc.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
		else if (browser.contains(BROWSER_SAFARI))
			dc.setBrowserName(DesiredCapabilities.safari().getBrowserName());
		else if (browser.contains(BROWSER_IE) || browser.equals("ie"))
			dc.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
		else
			dc.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());

		// setup version
		if (version != null && !version.isEmpty() && !version.equals("any"))
			dc.setVersion(version);

		// setup platform
		if (platform == null || platform.isEmpty() || platform.equals("any"))
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

	public ExtendedWebDriver(WebDriver driver) {

		this.driver = driver;
	}

	public ExtendedWebDriver(String browser) {

		// setup driver
		if (browser == null || browser.isEmpty() || browser.contains(BROWSER_CHROME))
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

	public ExtendedWebDriver(String browser, String version, String platform, String hubUrl)
			throws MalformedURLException {

		/* TODO:
		 * The code below can be written more simplier:
		 * this.driver = new RemoteWebDriver(new URL(nodeUrl), dc);
		 * 
		 * However due to some unknown problems / bugs, 
		 * this cannot run with safari using that way
		 * Issue link: https://code.google.com/p/selenium/issues/detail?id=3827
		 * 
		 * Firefox browser is launched but nothing is executed,
		 * known bugs, (version compatible)
		 * Issue link: https://code.google.com/p/selenium/issues/detail?id=6517
		 */

		// parse params
		browser = browser == null ? null : browser.toLowerCase();
		version = version == null ? null : version.toLowerCase();
		platform = platform == null ? null : platform.toLowerCase();

		DesiredCapabilities dc = createDesiredCapabilities(browser, version, platform);
		URL url = new URL(hubUrl);

		// setup driver
		if(browser.contains(BROWSER_SAFARI)) {
			
			SafariDriver wd = new SafariDriver();
			dc = (DesiredCapabilities)wd.getCapabilities();
			dc.setPlatform(Platform.ANY);
			dc.setVersion(null);
			this.driver = new RemoteWebDriver(url, dc);
		}
		else
			this.driver = new RemoteWebDriver(url, dc);
		
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

	public void close() {
		driver.close();
	}
	
	public void quit() {
		driver.quit();
	}
	
	
	// actions
	public void scroll(int deltaX, int deltaY) {
		
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript(String.format("window.scrollBy(%d,%d)", deltaX, deltaY));
	}
	
	public void scrollUp(int pixels) {
		
		scroll(0, -pixels);
	}
	
	public void scrollDown(int pixels) {
		
		scroll(0, pixels);
	}
	
	public void scrollLeft(int pixels) {
		
		scroll(-pixels, 0);
	}
	
	public void scrollRight(int pixels) {
		
		scroll(pixels, 0);
	}

	
	// manage
	public Options manage() {
		return driver.manage();
	}
	
	
	// multi tabs and windows
	public TargetLocator switchTo() {
		return driver.switchTo();
	}
	
	
	// misc
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}
	
	public String getPageSource() {
		return driver.getPageSource();
	}
	
	public String getTitle() {
		return driver.getTitle();
	}
	

	// finding methods 
	public ExtendedWebElement findElement(By by) {

		try {
			WebElement e = driver.findElement(by);
			return new ExtendedWebElementImpl(e);
		}
		catch (Exception e) {
			return null;
		}
	}

	public List<ExtendedWebElement> findElements(By by) {

		try {
			List<WebElement> le = driver.findElements(by);
			List<ExtendedWebElement> lee = new ArrayList<ExtendedWebElement>();
			for(WebElement e : le)
				lee.add(new ExtendedWebElementImpl(e));

			return lee;
		}
		catch (Exception e) {
			return null;
		}
	}

	public boolean isExisted(By by) {
		
		return findElement(by) != null;
	}

	
	// waiting methods
	public void delay() {
		
		delay(1);
	}
	
	public void delay(int seconds) {
		
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        ExpectedCondition<Boolean> cond = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                return false;
            }
        };

        try {
            wait.until(cond);
        } catch (Throwable e) {
        }
	}
	
	public ExtendedWebElement waitForElement(final By by) {
		return waitForElement(by, DEFAULT_TIMEOUT);
	}
	
	public ExtendedWebElement waitForElement(final By by, int seconds) {
		
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        ExpectedCondition<WebElement> elementAppears = new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver d) {
                try {
                    return d.findElement(by);
                } catch (Throwable e) {
                    return null;
                }
            }
        };

        try {
            WebElement we = wait.until(elementAppears);
            return we == null ? null : new ExtendedWebElementImpl(we);
        } catch (Exception e) {
            return null;
        }
	}
	
	public void waitForPageTitle(final String partialTitleText) {
		
		waitForPageTitle(partialTitleText, DEFAULT_TIMEOUT);
	}
	
	public void waitForPageTitle(final String partialTitleText, int seconds) {

        WebDriverWait wait = new WebDriverWait(driver, seconds);
        ExpectedCondition<Boolean> pageFullyLoaded = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
            	return driver.getTitle().toLowerCase().contains(partialTitleText.toLowerCase());
            }
        };
        
        wait.until(pageFullyLoaded);
	}
	
	
	// getters setters
	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

}





