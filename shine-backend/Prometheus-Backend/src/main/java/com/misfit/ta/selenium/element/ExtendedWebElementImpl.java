package com.misfit.ta.selenium.element;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsDriver;


public class ExtendedWebElementImpl implements ExtendedWebElement, Locatable, WrapsDriver {

	// fields
	private WebElement e;
	
	public WebElement getWebElement() {
	
		return e;
	}
	
	
	// constructor
	public ExtendedWebElementImpl(WebElement e) {
		
		this.e = e;
	}
	
	
	// implementation web driver
	@Override
	public void clear() {
		
		e.clear();
	}

	@Override
	public void click() {

		e.click();
	}

	@Override
	public WebElement findElement(By arg0) {
		
		return e.findElement(arg0);
	}

	@Override
	public List<WebElement> findElements(By arg0) {
		
		return e.findElements(arg0);
	}

	@Override
	public String getAttribute(String arg0) {
		
		return e.getAttribute(arg0);
	}

	@Override
	public String getCssValue(String arg0) {
		
		return e.getCssValue(arg0);
	}

	@Override
	public Point getLocation() {
		
		return e.getLocation();
	}

	@Override
	public Dimension getSize() {
		
		return e.getSize();
	}

	@Override
	public String getTagName() {
		
		return e.getTagName();
	}

	@Override
	public String getText() {
		
		return e.getText();
	}

	@Override
	public boolean isDisplayed() {
		
		return e.isDisplayed();
	}

	@Override
	public boolean isEnabled() {
		
		return e.isEnabled();
	}

	@Override
	public boolean isSelected() {
		
		return e.isSelected();
	}

	@Override
	public void sendKeys(CharSequence... arg0) {
		
		e.sendKeys(arg0);
	}

	@Override
	public void submit() {
		
		e.submit();
	}

	
	// implement Locatable
	@Override
	public Coordinates getCoordinates() {
		return ((Locatable) e).getCoordinates();
	}
	
	
	// implement WrapsDriver
	@Override
	public WebDriver getWrappedDriver() {
		
		return ((WrapsDriver) e).getWrappedDriver();
	}
	
	
	// implement additional properties methods
	@Override
	public String getId() {
		return e.getAttribute("id");
	}
	
	@Override
	public String getInnerHTML() {
		return e.getAttribute("innerHTML");
	}
	
	@Override
	public String getOuterHTML() {
		return e.getAttribute("outerHTML");
	}
	
	public String getAbsoluteXPath() {
	    
		ExtendedWebElement element = this;
			    
	    // travel back to html node
	    List<String> tagStack = new ArrayList<String>();
	    List<Integer> indexStack = new ArrayList<Integer>();
	    
	    while(element != null) {
	        
	    	// get index of current tag
	    	int index = 1;
			ExtendedWebElement curNode;

			for (curNode = element.previousSibling(); curNode != null; curNode = curNode.previousSibling()) {
				if (curNode.getTagName().equals(element.getTagName())) {
					++index;
				}
			}
			
			// store track
	    	tagStack.add(element.getTagName());
	        indexStack.add(index);
	        
	        // go up 1 level
	        element = element.parent();
	    }
	    
	    // build xpath expression
	    StringBuffer xpath = new StringBuffer();
	    for (int i = tagStack.size() - 1; i >= 0; i--) {
	        
	        xpath.append("/");
	        xpath.append(tagStack.get(i));
	        xpath.append("[" + indexStack.get(i) + "]");
	    }
	    
	    return xpath.toString();
	}
	
	@Override
	public String getValue() {
		
		return getAttribute("value");
	}
	
	@Override
	public String getHref() {
		return getAttribute("href");
	}

	@Override
	public String getBackgroundImageSource() {
		return getCssValue("background-image");
	}

	@Override
	public String getImageSource() {
		return getAttribute("src");
	}
	
	@Override
	public String getBackgroundColor() {
		return getCssValue("background-color");
	}

	@Override
	public String getForegroundColor() {
		return getCssValue("color");
	}
	
 	public boolean isHidden() {
		
		return  this.getCssValue("visibility").trim().equals("hidden") ||
				this.getCssValue("display").trim().equals("none");
	}
	

	// implement additional finding methods
	@Override
	public ExtendedWebElement parent() {
		
		return ancestor(1);
	}

	@Override
	public ExtendedWebElement nextSibling() {
		
		try {
			WebElement we = e.findElement(By.xpath("./following-sibling::*[position()=1]"));
			return new ExtendedWebElementImpl(we);
		}
		catch(Exception e) {
			return null;
		}
	} 
	
	@Override
	public ExtendedWebElement previousSibling() {
		
		try {
			WebElement we = e.findElement(By.xpath("./preceding-sibling::*[position()=1]"));
			return new ExtendedWebElementImpl(we);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public ExtendedWebElement ancestor(int level) {
		
		if(level < 1)
			return null;
		
		String xpath = "";
		for(int i = 0; i < level; i++)
			xpath += "/..";
		
		try {
			WebElement we = e.findElement(By.xpath(xpath.substring(1)));
			return new ExtendedWebElementImpl(we);
		}
		catch(Exception e) {
			return null;
		}
	}


}
