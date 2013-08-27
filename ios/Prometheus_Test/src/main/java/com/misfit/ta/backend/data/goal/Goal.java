package com.misfit.ta.backend.data.goal;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class Goal {

	private Double value;
	private Long startTime;
	private Long endTime;
	private Integer timeZoneOffsetInSeconds;

	private ProgressData progressData;
	private List<TrippleTapData> trippleTapTypeChanges;

	private String serverId;
	private String localId;
	private Long updatedAt;

	public Goal() {

	}

	public Goal(double value, long startTime, long endTime, int timeZoneOffsetInSeconds, ProgressData progressData, List<TrippleTapData> trippleTapTypeChanges, String localId, long updatedAt) {
		super();
		this.value = value;
		this.startTime = startTime;
		this.endTime = endTime;
		this.timeZoneOffsetInSeconds = timeZoneOffsetInSeconds;

		this.progressData = progressData;
		this.trippleTapTypeChanges = trippleTapTypeChanges;

		this.localId = localId;
		this.updatedAt = updatedAt;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public ProgressData getProgressData() {
		return progressData;
	}

	public void setProgressData(ProgressData progressData) {
		this.progressData = progressData;
	}

	public List<TrippleTapData> getTripleTapTypeChanges() {
		return trippleTapTypeChanges;
	}

	public void setTripleTapTypeChanges(List<TrippleTapData> tripleTapTypeChanges) {
		this.trippleTapTypeChanges = tripleTapTypeChanges;
	}

	public int getTimeZoneOffsetInSeconds() {
		return timeZoneOffsetInSeconds;
	}

	public void setTimeZoneOffsetInSeconds(int timeZoneOffsetInSeconds) {
		this.timeZoneOffsetInSeconds = timeZoneOffsetInSeconds;
	}

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public JSONObject toJson() {
		try {
			JSONObject object = new JSONObject();

			object.accumulate("goalValue", value);
			object.accumulate("startTime", startTime);
			object.accumulate("endTime", endTime);
			object.accumulate("timeZoneOffsetInSeconds", timeZoneOffsetInSeconds);

			object.accumulate("progressData", progressData.toJson());

			List<JSONObject> arr = new ArrayList<JSONObject>();
			for (int i = 0; i < trippleTapTypeChanges.size(); i++)
				arr.add(trippleTapTypeChanges.get(i).toJson());
			object.accumulate("tripleTapTypeChanges", arr);

			object.accumulate("localId", localId);
			object.accumulate("serverId", serverId);
			object.accumulate("updatedAt", updatedAt);
			return object;
		} catch (JSONException e) {
			e.printStackTrace();

			return null;
		}
	}

	public static Goal fromJson(JSONObject objJson) {
		Goal goal = new Goal();
		try {
			if (!objJson.isNull("goalValue"))
				goal.setValue(objJson.getDouble("goalValue"));

			if (!objJson.isNull("startTime"))
				goal.setStartTime(objJson.getLong("startTime"));

			if (!objJson.isNull("endTime"))
				goal.setEndTime(objJson.getLong("endTime"));

			if (!objJson.isNull("timeZoneOffsetInSeconds"))
				goal.setTimeZoneOffsetInSeconds(objJson.getInt("timeZoneOffsetInSeconds"));

			
			if (!objJson.isNull("progressData"))
			{
				ProgressData data = ProgressData.fromJson(objJson.getJSONObject("progressData"));
				goal.setProgressData(data);
			}
			
			if (!objJson.isNull("tripleTapTypeChanges"))
			{
				JSONArray jarr = objJson.getJSONArray("tripleTapTypeChanges");
				List<TrippleTapData> trippleTapDataList = new ArrayList<TrippleTapData>();
				
				for(int i = 0; i < jarr.length(); i++)
					trippleTapDataList.add(TrippleTapData.fromJson(jarr.getJSONObject(i)));
				
				goal.setTripleTapTypeChanges(trippleTapDataList);
			}

			
			if (!objJson.isNull("serverId"))
				goal.setServerId(objJson.getString("serverId"));
			
			if (!objJson.isNull("localId"))
				goal.setLocalId(objJson.getString("localId"));
			
			if (!objJson.isNull("updatedAt"))
				goal.setUpdatedAt(objJson.getLong("updatedAt"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return goal;
	}
}
