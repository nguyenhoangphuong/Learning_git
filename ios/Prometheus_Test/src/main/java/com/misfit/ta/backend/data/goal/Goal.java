package com.misfit.ta.backend.data.goal;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class Goal {

	// fields
	private Double value;
	private Long startTime;
	private Long endTime;
	private Integer timeZoneOffsetInSeconds;

	private ProgressData progressData;
	private List<TripleTapData> trippleTapTypeChanges;

	private String serverId;
	private String localId;
	private Long updatedAt;

	
	// constructor
	public Goal() {

	}

	
	// methods
	public JSONObject toJson() {
		try {
			JSONObject object = new JSONObject();

			object.accumulate("goalValue", value);
			object.accumulate("startTime", startTime);
			object.accumulate("endTime", endTime);
			object.accumulate("timeZoneOffsetInSeconds", timeZoneOffsetInSeconds);

			if (progressData != null)
				object.accumulate("progressData", progressData.toJson());
			else
				object.accumulate("progressData", null);

			if (trippleTapTypeChanges != null) {
				List<JSONObject> arr = new ArrayList<JSONObject>();
				for (int i = 0; i < trippleTapTypeChanges.size(); i++)
					arr.add(trippleTapTypeChanges.get(i).toJson());
				object.accumulate("tripleTapTypeChanges", arr);
			} else
				object.accumulate("tripleTapTypeChanges", null);
			
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

			if (!objJson.isNull("progressData")) {
				ProgressData data = ProgressData.fromJson(objJson.getJSONObject("progressData"));
				goal.setProgressData(data);
			}

			if (!objJson.isNull("tripleTapTypeChanges")) {
				JSONArray jarr = objJson.getJSONArray("tripleTapTypeChanges");
				List<TripleTapData> trippleTapDataList = new ArrayList<TripleTapData>();

				for (int i = 0; i < jarr.length(); i++)
					trippleTapDataList.add(TripleTapData.fromJson(jarr.getJSONObject(i)));

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
	
	
	// getters setters
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public ProgressData getProgressData() {
		return progressData;
	}

	public void setProgressData(ProgressData progressData) {
		this.progressData = progressData;
	}

	public List<TripleTapData> getTripleTapTypeChanges() {
		return trippleTapTypeChanges;
	}

	public void setTripleTapTypeChanges(List<TripleTapData> tripleTapTypeChanges) {
		this.trippleTapTypeChanges = tripleTapTypeChanges;
	}

	public Integer getTimeZoneOffsetInSeconds() {
		return timeZoneOffsetInSeconds;
	}

	public void setTimeZoneOffsetInSeconds(Integer timeZoneOffsetInSeconds) {
		this.timeZoneOffsetInSeconds = timeZoneOffsetInSeconds;
	}

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

}
