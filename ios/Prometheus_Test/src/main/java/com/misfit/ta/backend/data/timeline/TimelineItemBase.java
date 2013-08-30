package com.misfit.ta.backend.data.timeline;

import com.google.resting.json.JSONObject;

public abstract class TimelineItemBase {
    
	public static final int TYPE_START = 0;
    public static final int TYPE_WEATHER = 1;
    public static final int TYPE_SESSION = 2;
    public static final int TYPE_MILESTONE = 3;
    public static final int TYPE_NOTABLE = 4;
    public static final int TYPE_END = 5;
    
    private int type;
    private long timestamp;
    
    public TimelineItemBase() {
    }
    
    public TimelineItemBase(int type, long timestamp) {
        this.type = type;
        this.timestamp = timestamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public abstract JSONObject toJson();
        
}
