package com.misfit.ta.base;

import java.util.ArrayList;
import java.util.List;

public class Clock {

    public static String DETEMITER = "\t";
	protected static List<String> eventNames = new ArrayList<String>();

	protected List<Long> startTimes;
	protected List<Long> endTimes;
	
	protected Long lastStartTime;
	protected Long lastEndTime;
	
	
	public Clock() {
		
		startTimes = new ArrayList<Long>();
		endTimes = new ArrayList<Long>();
		
		lastStartTime = 0l;
		lastEndTime = 0l;
		
		
	}
	
	public static void addEvents(String name) {
	    eventNames.add(name);
	}
	
	public void tick() {
	   
		startTimes.add(System.currentTimeMillis());
	}

	public void tock() {

		endTimes.add(System.currentTimeMillis());
	}
	
	public long lastInterval() {
		if(lastEndTime < lastEndTime)
			return 0;
		
		return lastEndTime - lastStartTime;
	}
	
	
	public static String getEventNamesAsString() {
		
		StringBuffer result = new StringBuffer();
		for(String eventName : eventNames)
			result.append(eventName + DETEMITER);
		return result.toString();
	}
	
	
	public String getTimeInteval() {
	    StringBuffer buf = new StringBuffer();
        for(int i=0; i< startTimes.size(); i++) {
            buf.append((endTimes.get(i) - startTimes.get(i))/1000 + DETEMITER);
        }
        return buf.toString();
	}
	
}
