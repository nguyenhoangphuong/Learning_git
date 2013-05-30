package com.misfit.ta.backend.data.timeline;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.JSONBuilder;

public class TimelineItem {

    private int itemType;
    private String clientId;
    private String userId;
    private long updatedAt;
    private long createdAt;

    public TimelineItem(int itemType, String clientId, String userId, long updatedAt, long createdAt,
            TimelineItemBase data) {
        super();
        this.itemType = itemType;
        this.clientId = clientId;
        this.userId = userId;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.data = data;
    }

    private TimelineItemBase data;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public TimelineItemBase getData() {
        return data;
    }

    public void setData(TimelineItemBase data) {
        this.data = data;
    }

    public String toJson() {
        JSONBuilder builder = new JSONBuilder();
        builder.addValue("data", data.toJson());
        builder.addValue("user_id", userId);
        builder.addValue("created_at", MVPApi.timestampToISODate(createdAt));
        builder.addValue("updated_at", MVPApi.timestampToISODate(updatedAt));

        return builder.toJSONString();
    }
}
