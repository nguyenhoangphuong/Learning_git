package com.misfit.ta.backend.data.profile;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.data.statistics.PersonalRecord;
import com.misfit.ta.backend.data.statistics.Statistics;

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
	
	protected String handle;
	protected String privacy;
	protected String avatar;
	protected String email;
	protected String authToken;
	
	
	// constructor
	public ProfileData() {

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
			
			if (displayedUnits != null)
				object.accumulate("displayedUnits", displayedUnits.toJson());
			
			object.accumulate("handle", handle);
			object.accumulate("privacy", privacy);
			object.accumulate("avatar", avatar);
			object.accumulate("email", email);
			object.accumulate("authToken", authToken);

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
			
			if (!json.isNull("handle"))
				obj.setHandle(json.getString("handle"));

			if (!json.isNull("description"))
				obj.setPrivacy(json.getString("privacy"));

			if (!json.isNull("avatar"))
				obj.setAvatar(json.getString("avatar"));

			if (!json.isNull("authToken"))
				obj.setAuthToken(json.getString("authToken"));

			if (!json.isNull("email"))
				obj.setEmail(json.getString("email"));
			
			if (!json.isNull("privacy"))
                obj.setPrivacy(json.getString("privacy"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}

	public static ProfileData fromResponse(ServiceResponse response) {
		try {
			JSONObject jsonResponse = new JSONObject(response.getResponseString());
			JSONObject jsonItem = jsonResponse.getJSONObject("profile");

			return ProfileData.fromJson(jsonItem);
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
	
	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

}
