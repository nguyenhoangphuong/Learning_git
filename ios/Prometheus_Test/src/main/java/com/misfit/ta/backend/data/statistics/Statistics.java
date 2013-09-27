package com.misfit.ta.backend.data.statistics;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.profile.PersonalRecord;

public class Statistics {

	// fields
	private String serverId;
	private String localId;
	private Long updatedAt;
	private PersonalRecord personalRecords;
	private Double lifetimeDistance;

	// constructors
	public Statistics() {

	}

	public Statistics(String serverId, String localId, Long updatedAt, PersonalRecord personalRecords) {
		this.serverId = serverId;
		this.localId = localId;
		this.updatedAt = updatedAt;
		this.personalRecords = personalRecords;
	}

	// methods
	public JSONObject toJson() {
		try {
			JSONObject obj = new JSONObject();
			if (serverId != null)
				obj.accumulate("serverId", serverId);
			if (localId != null)
				obj.accumulate("localId", localId);
			if (updatedAt != null)
				obj.accumulate("updatedAt", updatedAt);
			if (personalRecords != null)
				obj.accumulate("personalRecords", personalRecords.toJson());
			if (lifetimeDistance != null)
				obj.accumulate("lifetimeDistance", lifetimeDistance);

			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Statistics fromJson(JSONObject json) {
		Statistics obj = new Statistics();
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
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}

	public static Statistics fromResponse(ServiceResponse response) {
		try {
			JSONObject jsonResponse = new JSONObject(response.getResponseString());
			JSONObject jsonItem = jsonResponse.getJSONObject("statistics");

			return Statistics.fromJson(jsonItem);
		} catch (JSONException e) {
			return null;
		}
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
}
