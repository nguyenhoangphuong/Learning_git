package com.misfit.ta.backend.data.timeline;

import java.util.Vector;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.JSONBuilder;

public class NotableEventItem extends TimelineItemBase {

    public static final int EVENT_TYPE_ALMOST_HIT_GOAL = 1;
    public static final int EVENT_TYPE_HIT_GOAL = 2;
    public static final int EVENT_TYPE_KILL_GOAL = 3;
    public static final int EVENT_TYPE_CRUSH_GOAL = 4;

    private Vector<String> headers = new Vector<String>();
    private Vector<String> messages = new Vector<String>();
    private int value = 0;
    private int eventType = 1;

    public NotableEventItem(long timestamp, Vector<String> headers, Vector<String> messages, int value, int eventType) {
        super(TYPE_NOTABLE, timestamp);
        
        if (headers != null) {
            this.headers = headers;
        }
        
        if (messages != null) { 
            this.messages = messages;
        }
        
        this.value = value;
        this.eventType = EVENT_TYPE_ALMOST_HIT_GOAL;
    }

    public Vector<String> getHeaders() {
        return headers;
    }

    public void setHeaders(Vector<String> headers) {
        this.headers = headers;
    }

    public Vector<String> getMessages() {
        return messages;
    }

    public void setMessages(Vector<String> messages) {
        this.messages = messages;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public JSONObject toJson() {
        
        JSONObject object = new JSONObject();
        try {
            headers.add("Header 1");
            headers.add("Header 2");
            messages.add("Message 1");
            messages.add("Message 2");

            object.accumulate("headers", headers);
            object.accumulate("messages", messages);
            object.accumulate("eventType", eventType);
            return object;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

}
