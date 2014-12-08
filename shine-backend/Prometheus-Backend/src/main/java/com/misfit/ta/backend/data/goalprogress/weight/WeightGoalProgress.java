package com.misfit.ta.backend.data.goalprogress.weight;

import java.util.ArrayList;
import java.util.List;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.goalprogress.GoalProgress;
import com.misfit.ta.backend.data.goalprogress.sleep.SleepGoalProgress;
import com.misfit.ta.backend.data.goalprogress.sleep.SleepGoalProgressOtherProperties;
import com.misfit.ta.common.MVPCommon;

public class WeightGoalProgress extends GoalProgress {

	// fields
	protected WeightGoalProgressOtherProperties others;
	protected WeightGoalSettings settings;

	
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

	public WeightGoalProgress fromJson(JSONObject objJson) {
		
		try {
			
			super.fromJson(objJson);
			
			if (!objJson.isNull("others")) {
				this.others = new WeightGoalProgressOtherProperties();
				this.others.fromJson(objJson.getJSONObject("others"));
			}
			
			if (!objJson.isNull("settings")) {
				this.settings = new WeightGoalSettings();
				this.settings.fromJson(objJson.getJSONObject("settings"));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}
	
	public static WeightGoalProgress getWeightGoalProgressFromResponse(ServiceResponse response) {
		
		try {

			JSONObject responseBody = new JSONObject(response.getResponseString());
			JSONObject json = responseBody.getJSONObject("goal_progress");
			
			WeightGoalProgress progress = new WeightGoalProgress();
			progress.fromJson(json);

			return progress;
		}
		catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<WeightGoalProgress> getWeightGoalProgressesFromResponse(ServiceResponse response) {
		
		try {
			List<WeightGoalProgress> items = new ArrayList<WeightGoalProgress>();

			JSONObject responseBody = new JSONObject(response.getResponseString());
			JSONArray jsonItems = responseBody.getJSONArray("goal_progresses");

			for (int i = 0; i < jsonItems.length(); i++) {

				WeightGoalProgress item = new WeightGoalProgress();
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
	
	public static WeightGoalProgress getDefaultWeightGoalProgress() {
		
		return getDefaultWeightGoalProgress(System.currentTimeMillis() / 1000);
	}
	
	public static WeightGoalProgress getDefaultWeightGoalProgress(long timestamp) {
		
		WeightGoalProgress progress = new WeightGoalProgress();
		progress.setLocalId("weightprogress-" + MVPApi.generateLocalId());
		progress.setDuration(0);
		progress.setTimestamp(MVPCommon.getDayStartEpoch(timestamp));
		progress.setProgressValue(0);
		progress.setOthers(new WeightGoalProgressOtherProperties());

		return progress;
	}
	
	
	// getters setters
	public WeightGoalProgressOtherProperties getOthers() {
		return others;
	}

	public void setOthers(WeightGoalProgressOtherProperties others) {
		this.others = others;
	}

	public WeightGoalSettings getSettings() {
		return settings;
	}

	public void setSettings(WeightGoalSettings settings) {
		this.settings = settings;
	}

}
