package com.misfit.ta.backend.data.timeline.timelineitemdata;

import com.google.resting.json.JSONObject;

public abstract class TimelineItemDataBase {
    
	// enum for item type
	public static final int TYPE_START = 0;
    public static final int TYPE_SESSION = 2;
    public static final int TYPE_LIFETIME_DISTANCE = 3;
    public static final int TYPE_MILESTONE = 4;   
    public static final int TYPE_SLEEP = 5;
    public static final int TYPE_END = 6;
    
    
    // enum for event type (in milestone item)
    public static final int EVENT_TYPE_100_GOAL = 2;
    public static final int EVENT_TYPE_150_GOAL = 4;
    public static final int EVENT_TYPE_200_GOAL = 6;
    public static final int EVENT_TYPE_PERSONAL_BEST = 5;
    public static final int EVENT_TYPE_STREAK = 7;
    
    
    public abstract JSONObject toJson();
        
}
