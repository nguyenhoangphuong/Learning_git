package com.misfit.ta.selenium.element;

import org.openqa.selenium.WebElement;

public interface ExtendedWebElement extends WebElement {

	WebElement getWebElement();
	
	// props
	String getId();
	String getInnerHTML();
	String getOuterHTML();
	String getAbsoluteXPath();
	String getValue();
	String getHref();
	String getBackgroundImageSource();
	String getImageSource();
	String getBackgroundColor();
	String getForegroundColor();
	boolean isHidden();
	
	// axes
	ExtendedWebElement parent();
	ExtendedWebElement nextSibling();
	ExtendedWebElement previousSibling();
	ExtendedWebElement ancestor(int level);

}
