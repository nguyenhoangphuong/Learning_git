package com.misfit.ta.backend.data.profile;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class ProfileData {

	// fields
	protected String serverId;
	protected Long updatedAt;
	protected String localId;

	protected Double weight;
	protected Double height;
	protected Integer gender;
	protected Long dateOfBirth;
	protected String name;

	protected Integer goalLevel;
	protected String latestVersion;
	protected String wearingPosition;
	protected PersonalRecord personalRecords;
	protected DisplayUnit displayedUnits;

	// constructor
	public ProfileData() {

	}
	
	public ProfileData(String serverId, Long updatedAt, String localId, Double weight, Double height, 
			Integer gender, Long dateOfBirth, String name, Integer goalLevel, String latestVersion, 
			String wearingPosition, PersonalRecord personalRecords, DisplayUnit displayedUnits) {
		super();
		this.serverId = serverId;
		this.updatedAt = updatedAt;
		this.localId = localId;
		this.weight = weight;
		this.height = height;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.name = name;
		this.goalLevel = goalLevel;
		this.latestVersion = latestVersion;
		this.wearingPosition = wearingPosition;
		this.personalRecords = personalRecords;
		this.displayedUnits = displayedUnits;
	}

	// methods
	public JSONObject toJson() {
		try {
			JSONObject object = new JSONObject();

			object.accumulate("localId", localId);
			object.accumulate("serverId", serverId);
			object.accumulate("updatedAt", updatedAt);

			object.accumulate("weight", weight);
			object.accumulate("height", height);
			object.accumulate("gender", gender);
			object.accumulate("dateOfBirth", dateOfBirth);
			object.accumulate("name", name);

			object.accumulate("goalLevel", goalLevel);
			object.accumulate("latestVersion", latestVersion);
			object.accumulate("wearingPosition", wearingPosition);

			if (personalRecords != null)
				object.accumulate("personalRecords", personalRecords.toJson());
			else
				object.accumulate("personalRecords", null);
			
			if (displayedUnits != null)
				object.accumulate("displayedUnits", displayedUnits.toJson());
			else
				object.accumulate("displayedUnits", null);

			return object;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ProfileData fromJson(JSONObject json) {
		ProfileData obj = new ProfileData();
		try {
			if (!json.isNull("localId"))
				obj.setLocalId(json.getString("localId"));

			if (!json.isNull("serverId"))
				obj.setServerId(json.getString("serverId"));

			if (!json.isNull("updatedAt"))
				obj.setUpdatedAt(json.getLong("updatedAt"));

			if (!json.isNull("weight"))
				obj.setWeight(json.getDouble("weight"));

			if (!json.isNull("height"))
				obj.setHeight(json.getDouble("height"));

			if (!json.isNull("gender"))
				obj.setGender(json.getInt("gender"));

			if (!json.isNull("dateOfBirth"))
				obj.setDateOfBirth(json.getLong("dateOfBirth"));

			if (!json.isNull("name"))
				obj.setName(json.getString("name"));

			if (!json.isNull("goalLevel"))
				obj.setGoalLevel(json.getInt("goalLevel"));

			if (!json.isNull("latestVersion"))
				obj.setLatestVersion(json.getString("latestVersion"));

			if (!json.isNull("wearingPosition"))
				obj.setWearingPosition(json.getString("wearingPosition"));

			if (!json.isNull("personalRecords"))
				obj.setPersonalRecords(PersonalRecord.fromJson(json.getJSONObject("personalRecords")));

			if (!json.isNull("displayedUnits"))
				obj.setDisplayedUnits(DisplayUnit.fromJson(json.getJSONObject("displayedUnits")));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}

	// getters setters
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

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Long getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Long dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGoalLevel() {
		return goalLevel;
	}

	public void setGoalLevel(Integer goalLevel) {
		this.goalLevel = goalLevel;
	}

	public String getLatestVersion() {
		return latestVersion;
	}

	public void setLatestVersion(String latestVersion) {
		this.latestVersion = latestVersion;
	}

	public String getWearingPosition() {
		return wearingPosition;
	}

	public void setWearingPosition(String wearingPosition) {
		this.wearingPosition = wearingPosition;
	}

	public PersonalRecord getPersonalRecords() {
		return personalRecords;
	}

	public void setPersonalRecords(PersonalRecord personalRecords) {
		this.personalRecords = personalRecords;
	}

	public DisplayUnit getDisplayedUnits() {
		return displayedUnits;
	}

	public void setDisplayedUnits(DisplayUnit displayedUnits) {
		this.displayedUnits = displayedUnits;
	}

}
