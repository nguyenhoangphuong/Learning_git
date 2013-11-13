package com.misfit.ta.backend.data.timeline;

import java.util.List;
import java.util.Vector;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.timeline.timelineitemdata.ActivitySessionItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.LifetimeDistanceItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.MilestoneItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;

public class TimelineItem {

	// fields
	private Integer itemType;
	private Boolean state;
	private Long timestamp;
	private TimelineItemDataBase data;
	
	private String localId;
	private String serverId;
	private Long updatedAt;


	// constructors
	public TimelineItem() {
	}

	
	// methods
	public JSONObject toJson() {

		JSONObject object = new JSONObject();
		try {
			
			object.accumulate("itemType", itemType);
			object.accumulate("state", state);
			object.accumulate("timestamp", timestamp);
			object.accumulate("updatedAt", updatedAt);
			
			if(data != null)
				object.accumulate("data", data.toJson());
			
			object.accumulate("localId", localId);
			object.accumulate("serverId", serverId);
			
			return object;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static TimelineItem fromJson(JSONObject jsonItem) {
		
		try {
						
			// timeline item
			TimelineItem item = new TimelineItem();
			if (!jsonItem.isNull("itemType"))
				item.setItemType(jsonItem.getInt("itemType"));
			
			if (!jsonItem.isNull("state"))
				item.setState(jsonItem.getBoolean("state"));
			
			if (!jsonItem.isNull("timestamp"))
				item.setTimestamp(jsonItem.getLong("timestamp"));
			
			if (!jsonItem.isNull("localId"))
				item.setLocalId(jsonItem.getString("localId"));
			
			if (!jsonItem.isNull("serverId"))
				item.setServerId(jsonItem.getString("serverId"));
			
			if (!jsonItem.isNull("updatedAt"))
				item.setUpdatedAt(jsonItem.getLong("updatedAt"));
			
			
			// parse data depends on the itemType
			if (!jsonItem.isNull("data") && item.itemType != null) {
				
				TimelineItemDataBase data = null;
				JSONObject jsonData = jsonItem.getJSONObject("data");
				
				switch (item.itemType) {
				
					case TimelineItemDataBase.TYPE_SESSION: {
						data = ActivitySessionItem.fromJson(jsonData);
						break;
					}
	
					case TimelineItemDataBase.TYPE_LIFETIME_DISTANCE: {
						data = LifetimeDistanceItem.fromJson(jsonData);
						break;
					}
	
					case TimelineItemDataBase.TYPE_MILESTONE: {
						data = MilestoneItem.fromJson(jsonData);
						break;
					}
				}

				item.setData(data);
			}
			
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

	
	// getters setters
	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public TimelineItemDataBase getData() {
		return data;
	}

	public void setData(TimelineItemDataBase data) {
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

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

}
