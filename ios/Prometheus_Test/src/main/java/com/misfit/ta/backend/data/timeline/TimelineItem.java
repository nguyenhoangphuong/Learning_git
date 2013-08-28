package com.misfit.ta.backend.data.timeline;

import java.util.List;
import java.util.Vector;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class TimelineItem {

	private int itemType;
	private long timestamp;
	private TimelineItemBase data;
	private String localId;
	private String serverId;
	private long updatedAt;

	private Object requestChangedValues;

	public TimelineItem() {

	}

	public TimelineItem(int itemType, long updatedAt, long timestamp, TimelineItemBase data, String localId, String serverId, Object requestChangedValues) {
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
			e.printStackTrace();
			return null;
		}
	}

	public static TimelineItem fromJson(JSONObject jsonItem) {
		try {
			TimelineData data = new TimelineData();
			if (!jsonItem.isNull("itemType"))
				data.setType(jsonItem.getInt("itemType"));
			if (!jsonItem.isNull("timestamp"))
				data.setTimestamp(jsonItem.getLong("timestamp"));
			if (!jsonItem.isNull("data"))
				data.setData(jsonItem.getJSONObject("data"));

			TimelineItem item = new TimelineItem();
			if (!jsonItem.isNull("serverId"))
				item.setServerId(jsonItem.getString("serverId"));
			if (!jsonItem.isNull("localId"))
				item.setLocalId(jsonItem.getString("localId"));
			if (!jsonItem.isNull("updatedAt"))
				item.setUpdatedAt(jsonItem.getLong("updatedAt"));
			if (!jsonItem.isNull("timestamp"))
				item.setTimestamp(jsonItem.getLong("timestamp"));
			if (!jsonItem.isNull("itemType"))
				item.setItemType(jsonItem.getInt("itemType"));
			item.setData(data);

			return item;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static TimelineItem getTimelineItem(ServiceResponse response) {
		try {
			JSONObject responseBody = new JSONObject(response.getResponseString());
			JSONObject jsonItem = responseBody.getJSONObject("timeline_item");

			return TimelineItem.fromJson(jsonItem);

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<TimelineItem> getTimelineItems(ServiceResponse response) {

		try {
			JSONObject responseBody = new JSONObject(response.getResponseString());
			JSONArray jsonItems = responseBody.getJSONArray("timeline_items");

			List<TimelineItem> items = new Vector<TimelineItem>();
			for (int i = 0; i < jsonItems.length(); i++) {
				JSONObject jsonItem = jsonItems.getJSONObject(i);
				items.add(TimelineItem.fromJson(jsonItem));
			}

			return items;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
