package com.misfit.ta.selenium.driver;

import java.util.List;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByXPath;

public abstract class Has extends org.openqa.selenium.By {

	// helper
	private static String lowercase(String string) {

		return String.format("translate(%s, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')", string);
	}


	// by tag with text
	public static org.openqa.selenium.By tagWithText(final String tag, final String text) {
		return tagWithText(tag, text, true);
	}

	public static org.openqa.selenium.By tagWithText(final String tag, final String text, final boolean caseSensitive) {

		if (tag == null || text == null)
			throw new IllegalArgumentException("Cannot find elements when tag or text is null.");

		return new org.openqa.selenium.By() {
			@Override
			public List<WebElement> findElements(SearchContext context) {

				if(caseSensitive) {
					return ((FindsByXPath) context).findElementsByXPath(String.format("//%s[text()='%s']", tag, text));
				}
				else {
					return ((FindsByXPath) context).findElementsByXPath(String.format("//%s[%s='%s']", tag, lowercase("text()"), text.toLowerCase()));
				}
			}

			@Override
			public WebElement findElement(SearchContext context) {
				if(caseSensitive) {
					return ((FindsByXPath) context).findElementByXPath(String.format("//%s[text()='%s']", tag, text));
				}
				else {
					return ((FindsByXPath) context).findElementByXPath(String.format("//%s[%s='%s']", tag, lowercase("text()"), text.toLowerCase()));
				}
			}

			@Override
			public String toString() {
				return "By.tagWithText: " + tag + ", " + text + ", " + caseSensitive;
			}
		};
	}


	// by tag with partial text
	public static org.openqa.selenium.By tagWithPartialText(final String tag, final String text) {

		return tagWithPartialText(tag, text, true);
	}

	public static org.openqa.selenium.By tagWithPartialText(final String tag, final String text, final boolean caseSensitive) {

		if (tag == null || text == null)
			throw new IllegalArgumentException("Cannot find elements when tag or text is null.");

		return new org.openqa.selenium.By() {
			@Override
			public List<WebElement> findElements(SearchContext context) {
				if(caseSensitive) {
					return ((FindsByXPath) context).findElementsByXPath(String.format("//%s[contains(text(), '%s')]", tag, text));
				}
				else {
					return ((FindsByXPath) context).findElementsByXPath(String.format("//%s[contains(%s, '%s')]", tag, lowercase("text()"), text.toLowerCase()));
				}
			}

			@Override
			public WebElement findElement(SearchContext context) {
				if(caseSensitive) {
					return ((FindsByXPath) context).findElementByXPath(String.format("//%s[contains(text(), '%s')]", tag, text));
				}
				else {
					return ((FindsByXPath) context).findElementByXPath(String.format("//%s[contains(%s, '%s')]", tag, lowercase("text()"), text.toLowerCase()));
				}
			}

			@Override
			public String toString() {
				return "By.tagWithPartialText: " + tag + ", " + text + ", " + caseSensitive;
			}
		};
	}

	
	// by tag with attribute
	public static org.openqa.selenium.By tagWithAttribute(final String tag, final String attrName) {
		
		return tagWithAttribute(tag, attrName, null);
	}
	
	public static org.openqa.selenium.By tagWithAttribute(final String tag, final String attrName, final String attrValue) {
		
		return tagWithAttribute(tag, attrName, attrValue, true);
	}
	
	public static org.openqa.selenium.By tagWithAttribute(final String tag, final String attrName, final String attrValue, final boolean caseSensitive) {

		if (tag == null || attrName == null)
			throw new IllegalArgumentException("Cannot find elements when tag or attribute is null.");

		return new org.openqa.selenium.By() {
			@Override
			public List<WebElement> findElements(SearchContext context) {

				String attr = "@" + attrName;
				if(attrValue == null)
					return ((FindsByXPath) context).findElementsByXPath(String.format("//%s[%s]", tag, attr));

				if(caseSensitive)
					return ((FindsByXPath) context).findElementsByXPath(String.format("//%s[%s='%s']", tag, attr, attrValue));
				else
					return ((FindsByXPath) context).findElementsByXPath(String.format("//%s[%s='%s']", tag, lowercase(attr), attrValue.toLowerCase()));
			}

			@Override
			public WebElement findElement(SearchContext context) {

				String attr = "@" + attrName;
				if(attrValue == null)
					return ((FindsByXPath) context).findElementByXPath(String.format("//%s[%s]", tag, attr));

				if(caseSensitive)
					return ((FindsByXPath) context).findElementByXPath(String.format("//%s[%s='%s']", tag, attr, attrValue));
				else
					return ((FindsByXPath) context).findElementByXPath(String.format("//%s[%s='%s']", tag, lowercase(attr), attrValue.toLowerCase()));
			}

			@Override
			public String toString() {
				return "By.tagWithAttribute: " + tag + ", " + attrName + ", " + attrValue + ", " + caseSensitive;
			}
		};
	}

	
	// by tag with partial attribute
	public static org.openqa.selenium.By tagWithPartialAttribute(final String tag, final String attrName, final String attrValue) {
		
		return tagWithPartialAttribute(tag, attrName, attrValue, true);
	}
	
	public static org.openqa.selenium.By tagWithPartialAttribute(final String tag, final String attrName, final String attrValue, final boolean caseSensitive) {

		if (tag == null || attrName == null || attrValue == null)
			throw new IllegalArgumentException("Cannot find elements when tag or attribute name or attribute value is null.");

		return new org.openqa.selenium.By() {
			@Override
			public List<WebElement> findElements(SearchContext context) {

				String attr = "@" + attrName;
				if(caseSensitive)
					return ((FindsByXPath) context).findElementsByXPath(String.format("//%s[contains(%s,'%s')]", tag, attr, attrValue));
				else
					return ((FindsByXPath) context).findElementsByXPath(String.format("//%s[contains(%s,'%s')]", tag, lowercase(attr), attrValue.toLowerCase()));
			}

			@Override
			public WebElement findElement(SearchContext context) {

				String attr = "@" + attrName;

				if(caseSensitive)
					return ((FindsByXPath) context).findElementByXPath(String.format("//%s[contains(%s,'%s')]", tag, attr, attrValue));
				else
					return ((FindsByXPath) context).findElementByXPath(String.format("//%s[contains(%s,'%s')]", tag, lowercase(attr), attrValue.toLowerCase()));
			}

			@Override
			public String toString() {
				return "By.tagWithPartialAttribute: " + tag + ", " + attrName + ", " + attrValue + ", " + caseSensitive;
			}
		};
	}

	

	// by text
	public static org.openqa.selenium.By text(final String text) {

		return text(text, true);
	}

	public static org.openqa.selenium.By text(final String text, final boolean caseSensitive) {

		return tagWithText("*", text, caseSensitive);
	}


	// by partial text
	public static org.openqa.selenium.By partialText(final String text) {

		return partialText(text, true);
	}

	public static org.openqa.selenium.By partialText(final String text, final boolean caseSensitive) {

		return tagWithPartialText("*", text, caseSensitive);
	}


	// by attribute
	public static org.openqa.selenium.By attribute(final String attrName) {

		return attribute(attrName, null);
	}

	public static org.openqa.selenium.By attribute(final String attrName, final String attrValue) {

		return attribute(attrName, attrValue, true);
	}

	public static org.openqa.selenium.By attribute(final String attrName, final String attrValue, final boolean caseSensitive) {

		return tagWithAttribute("*", attrName, attrValue, caseSensitive);
	}


	// by partial attribute value
	public static org.openqa.selenium.By partialAttribute(final String attrName, final String attrValue) {

		return partialAttribute(attrName, attrValue, true);
	}

	public static org.openqa.selenium.By partialAttribute(final String attrName, final String attrValue, final boolean caseSensitive) {

		return tagWithPartialAttribute("*", attrName, attrValue, caseSensitive);
	}


	// by placeholder text
	public static org.openqa.selenium.By placeholderText(final String text) {

		return placeholderText(text, true);
	}

	public static org.openqa.selenium.By placeholderText(final String text, boolean caseSensitive) {

		return attribute("placeholder", text, caseSensitive);
	}


	// by partial placeholder text
	public static org.openqa.selenium.By partialPlaceholderText(final String text) {

		return partialPlaceholderText(text, true);
	}

	public static org.openqa.selenium.By partialPlaceholderText(final String text, boolean caseSensitive) {

		return partialAttribute("placeholder", text, caseSensitive);
	}

}
