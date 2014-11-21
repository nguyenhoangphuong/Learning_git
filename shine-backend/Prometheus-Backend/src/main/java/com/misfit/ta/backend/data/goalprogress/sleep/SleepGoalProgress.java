package com.misfit.ta.backend.data.goalprogress.sleep;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.data.goalprogress.GoalProgress;
import com.misfit.ta.common.MVPCommon;

public class SleepGoalProgress extends GoalProgress {

	// fields
	protected SleepGoalProgressOtherProperties others;
	protected SleepGoalSettings settings;

	
	// methods
	public JSONObject toJson() {
		try {
			
			JSONObject object = super.toJson();
			
			if(others != null)
				object.accumulate("others", others.toJson());
			
			if(settings != null)
				object.accumulate("settings", settings.toJson());
			
			return object;
			
		} catch (JSONException e) {
			e.printStackTrace();

			return null;
		}
	}

	public SleepGoalProgress fromJson(JSONObject objJson) {
		
		try {
			
			super.fromJson(objJson);
			
			if (!objJson.isNull("others")) {
				this.others = new SleepGoalProgressOtherProperties();
				this.others.fromJson(objJson.getJSONObject("others"));
			}
			
			if (!objJson.isNull("settings")) {
				this.settings = new SleepGoalSettings();
				this.settings.fromJson(objJson.getJSONObject("settings"));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	public static SleepGoalProgress getSleepGoalProgressFromResponse(ServiceResponse response) {
		
		try {

			JSONObject responseBody = new JSONObject(response.getResponseString());
			JSONObject json = responseBody.getJSONObject("goal_progress");
			
			SleepGoalProgress progress = new SleepGoalProgress();
			progress.fromJson(json);

			return progress;
		}
		catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<SleepGoalProgress> getSleepGoalProgressesFromResponse(ServiceResponse response) {
		
		try {
			List<SleepGoalProgress> items = new ArrayList<SleepGoalProgress>();

			JSONObject responseBody = new JSONObject(response.getResponseString());
			JSONArray jsonItems = responseBody.getJSONArray("goal_progresses");

			for (int i = 0; i < jsonItems.length(); i++) {

				SleepGoalProgress item = new SleepGoalProgress();
				item.fromJson(jsonItems.getJSONObject(i));
				items.add(item);
			}

			return items;
		}
		catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public static SleepGoalProgress getDefaultSleepGoalProgress() {
		
		return getDefaultSleepGoalProgress(System.currentTimeMillis() / 1000);
	}
	
	public static SleepGoalProgress getDefaultSleepGoalProgress(long timestamp) {
		
		SleepGoalProgress progress = new SleepGoalProgress();
		progress.setDuration(0);
		progress.setTimestamp(MVPCommon.getDayStartEpoch(timestamp));
		progress.setProgressValue(0);
		progress.setLocalId("sleepprogress-" + MVPApi.generateLocalId());
		progress.setOthers(new SleepGoalProgressOtherProperties());
		
		return progress;
	}
	
	
	// getters setters
	public SleepGoalProgressOtherProperties getOthers() {
		return others;
	}

	public void setOthers(SleepGoalProgressOtherProperties others) {
		this.others = others;
	}

	public SleepGoalSettings getSettings() {
		return settings;
	}

	public void setSettings(SleepGoalSettings settings) {
		this.settings = settings;
	}

}
