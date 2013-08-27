package com.misfit.ta.backend.data.timeline;

import java.util.List;
import java.util.Vector;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.activity.ActivityResult;

public class TimelineItem {

    private int itemType;
    private long updatedAt;
    private long timestamp;
    private TimelineItemBase data;
    private String localId;
    private String serverId;
    private Object requestChangedValues;

    public TimelineItem(int itemType, long updatedAt, long timestamp, TimelineItemBase data, String localId,
            String serverId, Object requestChangedValues) {
        super();
        this.itemType = itemType;
        this.updatedAt = updatedAt;
        this.timestamp = timestamp;
        this.data = data;
        this.localId = localId;
        this.serverId = serverId;
        this.requestChangedValues = requestChangedValues;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public TimelineItemBase getData() {
        return data;
    }

    public void setData(TimelineItemBase data) {
        this.data = data;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Object getRequestChangedValues() {
        return requestChangedValues;
    }

    public void setRequestChangedValues(Object requestChangedValues) {
        this.requestChangedValues = requestChangedValues;
    }

    public JSONObject toJson() {
        
      JSONObject object = new JSONObject();
        try {
            object.accumulate("itemType", itemType);
            object.accumulate("timestamp", timestamp);
            object.accumulate("updatedAt", updatedAt);
            object.accumulate("data", data.toJson());
            object.accumulate("localId", localId);
            object.accumulate("serverId", serverId);
            object.accumulate("requestChangedValues", requestChangedValues);
            return object;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<TimelineItem> getTimelineItems(ServiceResponse response) throws JSONException {
        List<TimelineItem> items = new Vector<TimelineItem>();
        JSONObject responseBody = new JSONObject(response.getResponseString());
        JSONArray jsonItems = responseBody.getJSONArray("timeline_items");

        for (int i = 0; i < jsonItems.length(); i++) {
            JSONObject obj = jsonItems.getJSONObject(i);
            TimelineItem item = new TimelineItem(obj.getInt("itemType"),
                    obj.getLong("updatedAt"), 
                    obj.getLong("timestamp"), 
                    // TODO: for now we dont care about the data
                    //(TimelineItemBase) (obj.get("data")),
                    new TimelineItemBase(0, 0) {
                        @Override
                        public JSONObject toJson() {
                            // TODO Auto-generated method stub
                            return null;
                        }
                    },
                    obj.getString("localId"), 
                    obj.getString("serverId"),
                    null);
            
            items.add(item);
        }
        
        return items;
    }
}
