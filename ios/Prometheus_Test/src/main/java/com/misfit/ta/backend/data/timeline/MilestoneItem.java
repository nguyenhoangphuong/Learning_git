package com.misfit.ta.backend.data.timeline;

import java.util.Vector;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.JSONBuilder;

public class MilestoneItem extends TimelineItemBase {

    private Vector<String> headers = new Vector<String>();
    private Vector<String> messages = new Vector<String>();
    private float value = 0;

    public MilestoneItem(long timestamp, Vector<String> headers, Vector<String> messages, float value) {
        super(TYPE_MILESTONE, timestamp);
        
        if (headers != null) {
            this.headers = headers;
        }
        
        if (messages != null) { 
            this.messages = messages;
        }
        
        this.value = value;
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

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
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
            object.accumulate("value", value);
            return object;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

}
