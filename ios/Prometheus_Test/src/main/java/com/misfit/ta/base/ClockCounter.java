package com.misfit.ta.base;

import java.util.ArrayList;
import java.util.List;

public class ClockCounter {

	protected List<String> eventNames;
	protected List<Long> startTimeList;
	protected List<Long> endTimeList;
	
	protected Long lastStartTime;
	protected Long lastEndTime;
	
	
	public ClockCounter() {
		
		startTimeList = new ArrayList<Long>();
		endTimeList = new ArrayList<Long>();
		
		lastStartTime = 0l;
		lastEndTime = 0l;
	}
	
	
	public void tick(String eventName) {

		eventNames.add(eventName);
		startTimeList.add(System.currentTimeMillis());
	}

	public void tock() {

		endTimeList.add(System.currentTimeMillis());
	}
	
	public long lastInterval() {
		
		if(lastEndTime < lastEndTime)
			return 0;
		
		return lastEndTime - lastStartTime;
	}
	
	
	public String getEventNamesAsString(String delemiter) {
		
		String result = "";
		for(String eventName : eventNames)
			result += (delemiter + eventName);
		
		return result.substring(1);
	}
	
	public String getStartTimesAsString(String delemiter) {
		
		String result = "";
		for(Long startTime : startTimeList)
			result += (delemiter + startTime);
		
		return result.substring(1);
	}
	
	public String getEndTimesAsString(String delemiter) {
		
		String result = "";
		for(Long endTime : endTimeList)
			result += (delemiter + endTime);
		
		return result.substring(1);
	}
	
}
