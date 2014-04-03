package com.misfit.ta.backend.data.goal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.common.MVPEnums;

public class Goal {

	// fields
	private Double goalValue;
	private Long startTime;
	private Long endTime;
	private Integer timeZoneOffsetInSeconds;
	private Integer autosleepState;

	private ProgressData progressData;
	private List<TripleTapData> tripleTapTypeChanges;

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

			object.accumulate("goalValue", goalValue);
			object.accumulate("startTime", startTime);
			object.accumulate("endTime", endTime);
			object.accumulate("timeZoneOffsetInSeconds", timeZoneOffsetInSeconds);
			object.accumulate("autosleepState", autosleepState);

			if (progressData != null)
				object.accumulate("progressData", progressData.toJson());
			else
				object.accumulate("progressData", null);

			if (tripleTapTypeChanges != null) {
				List<JSONObject> arr = new ArrayList<JSONObject>();
				for (int i = 0; i < tripleTapTypeChanges.size(); i++)
					arr.add(tripleTapTypeChanges.get(i).toJson());
				object.accumulate("tripleTapTypeChanges", arr);
			}
			
			object.accumulate("localId", localId);
			object.accumulate("serverId", serverId);
			object.accumulate("updatedAt", updatedAt);
			return object;
		} catch (JSONException e) {
			e.printStackTrace();

			return null;
		}
	}

	public Goal fromJson(JSONObject objJson) {
		
		Goal goal = this;
		try {
			if (!objJson.isNull("goalValue"))
				goal.setGoalValue(objJson.getDouble("goalValue"));

			if (!objJson.isNull("startTime"))
				goal.setStartTime(objJson.getLong("startTime"));

			if (!objJson.isNull("endTime"))
				goal.setEndTime(objJson.getLong("endTime"));

			if (!objJson.isNull("timeZoneOffsetInSeconds"))
				goal.setTimeZoneOffsetInSeconds(objJson.getInt("timeZoneOffsetInSeconds"));
			
			if (!objJson.isNull("autosleepState"))
				goal.setAutosleepState(objJson.getInt("autosleepState"));

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
	
	public static Goal[] getGoals(ServiceResponse response) {

		try {
			
			Goal[] goals = null;
			JSONObject json = new JSONObject(response.getResponseString());
			
			if (!json.isNull("goals")) {
				
				JSONArray arrJson;
				arrJson = json.getJSONArray("goals");
				goals = new Goal[arrJson.length()];

				for (int i = 0; i < goals.length; i++) {
					JSONObject objJson;
					objJson = arrJson.getJSONObject(i);
					goals[i] = new Goal();
					goals[i].fromJson(objJson);
				}
			}
			
			return goals;

		} catch (JSONException e2) {
			return null;
		}	
	}
		
	public static Goal getGoal(ServiceResponse response) {

		try {
			
			JSONObject json = new JSONObject(response.getResponseString());
			Goal goal = new Goal();
			
			if (!json.isNull("goal"))
				return goal.fromJson(json.getJSONObject("goal"));
			else if(!json.isNull("latest_goal"))
				return goal.fromJson(json.getJSONObject("latest_goal"));
			
			return null;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static Goal getDefaultGoal() {
		
		return getDefaultGoal(System.currentTimeMillis() / 1000);
	}
	
	public static Goal getDefaultGoal(long timestamp) {
		
		TimeZone tz = TimeZone.getDefault();
		Date now = new Date();
		int offsetFromUtc = tz.getOffset(now.getTime()) / 1000;
		
		List<TripleTapData> tripleTapTypeChanges = new ArrayList<TripleTapData>();
		TripleTapData tripleTapDefault = new TripleTapData();
		tripleTapDefault.setActivityType(MVPEnums.ACTIVITY_SLEEPING);
		tripleTapDefault.setTimestamp(timestamp);
		tripleTapTypeChanges.add(tripleTapDefault);
		
		Goal goal = new Goal();
		goal.setLocalId("goal-" + MVPApi.generateLocalId());
		goal.setGoalValue(1000 * 2.5);
		goal.setStartTime(MVPCommon.getDayStartEpoch(timestamp));
		goal.setEndTime(MVPCommon.getDayEndEpoch(timestamp));
		goal.setTimeZoneOffsetInSeconds(offsetFromUtc);
		goal.setProgressData(ProgressData.getDefaultProgressData());
		goal.setTripleTapTypeChanges(tripleTapTypeChanges);	
		goal.setAutosleepState(1);
		
		return goal;
	}
	
	
	// getters setters
	public Double getGoalValue() {
		return goalValue;
	}

	public void setGoalValue(Double value) {
		this.goalValue = value;
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

	public Integer getAutosleepState() {
		return autosleepState;
	}

	public void setAutosleepState(Integer autosleepState) {
		this.autosleepState = autosleepState;
	}

	public ProgressData getProgressData() {
		return progressData;
	}

	public void setProgressData(ProgressData progressData) {
		this.progressData = progressData;
	}

	public List<TripleTapData> getTripleTapTypeChanges() {
		return tripleTapTypeChanges;
	}

	public void setTripleTapTypeChanges(List<TripleTapData> tripleTapTypeChanges) {
		this.tripleTapTypeChanges = tripleTapTypeChanges;
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
