package com.misfit.ta.backend.data.timeline;

import java.util.List;
import java.util.Vector;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.timeline.timelineitemdata.ActivitySessionItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.FoodTrackingItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.LifetimeDistanceItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.MilestoneItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.SleepSessionItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimezoneChangeItem;

public class TimelineItem {

	// fields
	private Integer itemType;
	private Integer state;
	private Long timestamp;
	private TimelineItemDataBase data;
	private String attachedImageUrl;
	private String attachedImage;
	
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
			object.accumulate("attachedImageUrl", attachedImageUrl);
			object.accumulate("attachedImage", attachedImage);
			
			object.accumulate("localId", localId);
			object.accumulate("serverId", serverId);
			
			return object;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public TimelineItem fromJson(JSONObject jsonItem) {
		
		try {
						
			// timeline item
			TimelineItem item = this;
			if (!jsonItem.isNull("itemType"))
				item.setItemType(jsonItem.getInt("itemType"));
			
			if (!jsonItem.isNull("state"))
				item.setState(jsonItem.getInt("state"));
			
			if (!jsonItem.isNull("timestamp"))
				item.setTimestamp(jsonItem.getLong("timestamp"));
			
			if (!jsonItem.isNull("localId"))
				item.setLocalId(jsonItem.getString("localId"));
			
			if (!jsonItem.isNull("serverId"))
				item.setServerId(jsonItem.getString("serverId"));
			
			if (!jsonItem.isNull("updatedAt"))
				item.setUpdatedAt(jsonItem.getLong("updatedAt"));
			
			if (!jsonItem.isNull("attachedImageUrl"))
				item.setAttachedImageUrl(jsonItem.getString("attachedImageUrl"));
			
			if (!jsonItem.isNull("attachedImage"))
				item.setAttachedImage(jsonItem.getString("attachedImage"));
			
			// parse data depends on the itemType
			if (!jsonItem.isNull("data") && item.itemType != null) {
				
				TimelineItemDataBase data = null;
				JSONObject jsonData = jsonItem.getJSONObject("data");
				
				switch (item.itemType) {
				
					case TimelineItemDataBase.TYPE_GAP:
					case TimelineItemDataBase.TYPE_SESSION: {
						ActivitySessionItem sessionData = new ActivitySessionItem();
						data = sessionData.fromJson(jsonData);
						break;
					}
	
					case TimelineItemDataBase.TYPE_LIFETIME_DISTANCE: {
						LifetimeDistanceItem lifetimeData = new LifetimeDistanceItem();
						data = lifetimeData.fromJson(jsonData);
						break;
					}
	
					case TimelineItemDataBase.TYPE_MILESTONE: {
						MilestoneItem milestoneData = new MilestoneItem();
						data = milestoneData.fromJson(jsonData);
						break;
					}
					
					case TimelineItemDataBase.TYPE_TIMEZONE: {
						TimezoneChangeItem timezoneData = new TimezoneChangeItem();
						data = timezoneData.fromJson(jsonData);
						break;
					}
					
					case TimelineItemDataBase.TYPE_SLEEP: {
						SleepSessionItem sleepData = new SleepSessionItem();
						data = sleepData.fromJson(jsonData);
						break;
					}
					
					case TimelineItemDataBase.TYPE_FOOD: {
						FoodTrackingItem foodData = new FoodTrackingItem();
						data = foodData.fromJson(jsonData);
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
			TimelineItem item = new TimelineItem();
			
			return item.fromJson(jsonItem);

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
				TimelineItem item = new TimelineItem();
				item.fromJson(jsonItem);
				items.add(item);
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
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


	
	public String getAttachedImageUrl() {
		return attachedImageUrl;
	}
	


	public void setAttachedImageUrl(String attachedImageUrl) {
		this.attachedImageUrl = attachedImageUrl;
	}
	


	public String getAttachedImage() {
		return attachedImage;
	}
	


	public void setAttachedImage(String attachedImage) {
		this.attachedImage = attachedImage;
	}
	
}
