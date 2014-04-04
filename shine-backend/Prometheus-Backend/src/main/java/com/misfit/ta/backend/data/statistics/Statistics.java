package com.misfit.ta.backend.data.statistics;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.api.internalapi.MVPApi;

public class Statistics {

	// fields
	protected String serverId;
	protected String localId;
	protected Long updatedAt;
	protected PersonalRecord personalRecords;
	protected Double lifetimeDistance;
	protected Integer bestStreak;
	protected Integer totalGoalHit;
	protected Integer todayApnsNumber;
	protected Long lastApnsTime;


	// methods
	public JSONObject toJson() {
		try {
			
			JSONObject obj = new JSONObject();
			
			obj.accumulate("serverId", serverId);
			obj.accumulate("localId", localId);
			obj.accumulate("updatedAt", updatedAt);
			
			if (personalRecords != null)
				obj.accumulate("personalRecords", personalRecords.toJson());
			
			obj.accumulate("lifetimeDistance", lifetimeDistance);
			obj.accumulate("bestStreak", bestStreak);
			obj.accumulate("totalGoalHit", totalGoalHit);
			obj.accumulate("todayApnsNumber", todayApnsNumber);
			obj.accumulate("lastApnsTime", lastApnsTime);

			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Statistics fromJson(JSONObject json) {
		Statistics obj = this;
		try {
			
			if (!json.isNull("serverId"))
				obj.setServerId(json.getString("serverId"));
			
			if (!json.isNull("localId"))
				obj.setLocalId(json.getString("localId"));
			
			if (!json.isNull("updatedAt"))
				obj.setUpdatedAt(json.getLong("updatedAt"));
			
			if (!json.isNull("personalRecords"))
				obj.setPersonalRecords(PersonalRecord.fromJson(json.getJSONObject("personalRecords")));
			
			if (!json.isNull("lifetimeDistance"))
				obj.setLifetimeDistance(json.getDouble("lifetimeDistance"));
			
			if (!json.isNull("bestStreak"))
				obj.setBestStreak(json.getInt("bestStreak"));
			
			if (!json.isNull("totalGoalsHit"))
				obj.setTotalGoalHit(json.getInt("totalGoalsHit"));
			
			if (!json.isNull("todayApnsNumber"))
				obj.setTodayApnsNumber(json.getInt("todayApnsNumber"));
			
			if (!json.isNull("lastApnsTime"))
				obj.setLastApnsTime(json.getLong("lastApnsTime"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}

	public static Statistics fromResponse(ServiceResponse response) {
		try {
			JSONObject jsonResponse = new JSONObject(response.getResponseString());
			JSONObject jsonItem = jsonResponse.getJSONObject("statistics");
			Statistics statistics = new Statistics();
			
			return statistics.fromJson(jsonItem);
		} catch (JSONException e) {
			return null;
		}
	}

	public static Statistics getDefaultStatistics() {
		
		Statistics statistics = new Statistics();
		statistics.setLifetimeDistance(0d);
		statistics.setBestStreak(0);
		statistics.setLocalId("statistics-" + MVPApi.generateLocalId());
		statistics.setTotalGoalHit(0);
		statistics.setPersonalRecords(new PersonalRecord());
		
		return statistics;
	}

	
	// getters setters
	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
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

	public PersonalRecord getPersonalRecords() {
		return personalRecords;
	}

	public void setPersonalRecords(PersonalRecord personalRecords) {
		this.personalRecords = personalRecords;
	}
	
	public Double getLifetimeDistance() {
		return lifetimeDistance;
	}

	public void setLifetimeDistance(Double lifetimeDistance) {
		this.lifetimeDistance = lifetimeDistance;
	}
	
	public Integer getBestStreak() {
		return bestStreak;
	}

	public void setBestStreak(Integer bestStreak) {
		this.bestStreak = bestStreak;
	}

	public Integer getTotalGoalHit() {
		return totalGoalHit;
	}

	public void setTotalGoalHit(Integer totalGoalHit) {
		this.totalGoalHit = totalGoalHit;
	}

	public Integer getTodayApnsNumber() {
		return todayApnsNumber;
	}

	public void setTodayApnsNumber(Integer todayApnsNumber) {
		this.todayApnsNumber = todayApnsNumber;
	}

	public Long getLastApnsTime() {
		return lastApnsTime;
	}

	public void setLastApnsTime(Long lastApnsTime) {
		this.lastApnsTime = lastApnsTime;
	}

}
