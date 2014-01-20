package com.misfit.ta.backend.api;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;

public class CookieManager {

	private Map<String, Map<String, Map<String, String>>> store;

	private static final String SET_COOKIE = "Set-Cookie";
	private static final String COOKIE_VALUE_DELIMITER = ";";
	private static final String PATH = "path";
	private static final String EXPIRES = "expires";
	private static final String DATE_FORMAT = "EEE, dd-MMM-yyyy hh:mm:ss z";
	private static final String SET_COOKIE_SEPARATOR="; ";
	private static final String COOKIE = "Cookie";

	private static final char NAME_VALUE_SEPARATOR = '=';
	private static final char DOT = '.';

	private DateFormat dateFormat;

	public CookieManager() {

		store = new HashMap<String, Map<String, Map<String, String>>>();
		dateFormat = new SimpleDateFormat(DATE_FORMAT);
	}


	// public methods
	public void storeCookies(String host, BaseResult responseResult) throws IOException {

		String domain = getDomainFromHost(host);
		Map<String, Map<String, String>> domainStore; // this is where we will store cookies for this domain

		// now let's check the store to see if we have an entry for this domain
		if (store.containsKey(domain)) {
			// we do, so lets retrieve it from the store
			domainStore = (Map<String, Map<String, String>>)store.get(domain);
		} else {
			// we don't, so let's create it and put it in the store
			domainStore = new HashMap<String, Map<String, String>>();
			store.put(domain, domainStore);    
		}


		// parse cookie and store it under its domain store
		String cookieString = responseResult.getHeaderValue(SET_COOKIE);
		
		Map<String, String> cookie = new HashMap<String, String>();
		StringTokenizer st = new StringTokenizer(cookieString, COOKIE_VALUE_DELIMITER);

		// the specification dictates that the first name/value pair
		// in the string is the cookie name and value, so let's handle
		// them as a special case: 
		if (st.hasMoreTokens()) {
			String token  = st.nextToken();
			String name = token.substring(0, token.indexOf(NAME_VALUE_SEPARATOR));
			String value = token.substring(token.indexOf(NAME_VALUE_SEPARATOR) + 1, token.length());
			domainStore.put(name, cookie);
			cookie.put(name, value);
		}

		while (st.hasMoreTokens()) {
			String token  = st.nextToken();
			cookie.put(token.substring(0, token.indexOf(NAME_VALUE_SEPARATOR)).toLowerCase(),
					token.substring(token.indexOf(NAME_VALUE_SEPARATOR) + 1, token.length()));
		}

	}

	public void setCookies(String host, BaseParams requestParams) throws IOException {


		// let's determine the domain and path to retrieve the appropriate cookies
		String domain = getDomainFromHost(host);
		String path = host;

		Map<String, Map<String, String>> domainStore = (Map<String, Map<String, String>>)store.get(domain);
		if (domainStore == null) return;
		StringBuffer cookieStringBuffer = new StringBuffer();

		Iterator<String> cookieNames = domainStore.keySet().iterator();
		while(cookieNames.hasNext()) {
			String cookieName = (String)cookieNames.next();
			Map<String, String> cookie = (Map<String, String>)domainStore.get(cookieName);
			// check cookie to ensure path matches  and cookie is not expired
			// if all is cool, add cookie to header string 
			if (comparePaths((String)cookie.get(PATH), path) && isNotExpired((String)cookie.get(EXPIRES))) {
				cookieStringBuffer.append(cookieName);
				cookieStringBuffer.append("=");
				cookieStringBuffer.append((String)cookie.get(cookieName));
				if (cookieNames.hasNext()) cookieStringBuffer.append(SET_COOKIE_SEPARATOR);
			}
		}
		try {
//			conn.setRequestProperty(COOKIE, cookieStringBuffer.toString());
		} catch (java.lang.IllegalStateException ise) {
			IOException ioe = new IOException("Illegal State! Cookies cannot be set on a URLConnection that is already connected. " 
					+ "Only call setCookies(java.net.URLConnection) AFTER calling java.net.URLConnection.connect().");
			throw ioe;
		}
	}

	public String toString() {
		return store.toString();
	}
	
	
	// private methods
	private String getDomainFromHost(String host) {
		if (host.indexOf(DOT) != host.lastIndexOf(DOT)) {
			return host.substring(host.indexOf(DOT) + 1);
		} else {
			return host;
		}
	}

	private boolean isNotExpired(String cookieExpires) {
		if (cookieExpires == null) return true;
		Date now = new Date();
		try {
			return (now.compareTo(dateFormat.parse(cookieExpires))) <= 0;
		} catch (java.text.ParseException pe) {
			pe.printStackTrace();
			return false;
		}
	}

	private boolean comparePaths(String cookiePath, String targetPath) {
		if (cookiePath == null) {
			return true;
		} else if (cookiePath.equals("/")) {
			return true;
		} else if (targetPath.regionMatches(0, cookiePath, 0, cookiePath.length())) {
			return true;
		} else {
			return false;
		}

	}

}
