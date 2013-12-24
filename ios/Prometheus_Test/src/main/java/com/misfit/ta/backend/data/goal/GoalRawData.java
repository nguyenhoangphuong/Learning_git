package com.misfit.ta.backend.data.goal;

import org.apache.commons.lang.ArrayUtils;

import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class GoalRawData {

	// fields
	protected int[] points;
	protected int[] steps;
	protected int[] variances;
	protected int[] triple_tap_minutes;
	
	
	// constructor
	public GoalRawData() {
		
		points = new int[0];
		steps = new int[0];
		variances = new int[0];
		triple_tap_minutes = new int[0];
	}
	
	
	// methods
	public JSONObject toJson() {
		
		try {
			
			JSONObject json = new JSONObject();
			if(points != null) {
				for(int point : points)
					json.accumulate("points", point);
			}
			
			if(steps != null) {
				for(int step : steps)
					json.accumulate("steps", step);
			}
			
			if(variances != null) {
				for(int variance : variances)
					json.accumulate("variances", variance);
			}
			
			if(triple_tap_minutes != null) {
				for(int triple_tap : triple_tap_minutes)
					json.accumulate("triple_tap_minutes", triple_tap);
			}
			
			return json;
			
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	public void fromJson(JSONObject json) {
		
		try {
				
			if(!json.isNull("points")) {

				JSONArray arr = json.getJSONArray("points");
				points = new int[arr.length()];
				for(int i = 0; i < arr.length(); i++)
					points[i] = arr.getInt(i);
			}

			if(!json.isNull("steps")) {

				JSONArray arr = json.getJSONArray("steps");
				steps = new int[arr.length()];
				for(int i = 0; i < arr.length(); i++)
					steps[i] = arr.getInt(i);
			}

			if(!json.isNull("variances")) {

				JSONArray arr = json.getJSONArray("variances");
				variances = new int[arr.length()];
				for(int i = 0; i < arr.length(); i++)
					variances[i] = arr.getInt(i);
			}

			if(!json.isNull("triple_tap_minutes")) {

				JSONArray arr = json.getJSONArray("triple_tap_minutes");
				triple_tap_minutes = new int[arr.length()];
				for(int i = 0; i < arr.length(); i++)
					triple_tap_minutes[i] = arr.getInt(i);
			}
			
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}
	
	public GoalRawData appendGoalRawData(GoalRawData data) {
		
		int[] steps1 = this.getSteps();
		int[] points1 = this.getPoints();
		int[] variances1 = this.getVariances();
		int[] tripleTapMinutes1 = this.getTriple_tap_minutes();
		
		int[] steps2 = data.getSteps();
		int[] points2 = data.getPoints();
		int[] variances2 = data.getVariances();
		int[] tripleTapMinutes2 = data.getTriple_tap_minutes();
		
		this.steps = ArrayUtils.addAll(steps1, steps2);
		this.points = ArrayUtils.addAll(points1, points2);
		this.variances = ArrayUtils.addAll(variances1, variances2);
		this.triple_tap_minutes = ArrayUtils.addAll(tripleTapMinutes1, tripleTapMinutes2);
				
		return this;
	}
	
	
	// getters setters	
	public int[] getPoints() {
		return points;
	}
	
	public void setPoints(int[] points) {
		this.points = points;
	}
	
	public int[] getSteps() {
		return steps;
	}
	
	public void setSteps(int[] steps) {
		this.steps = steps;
	}
	
	public int[] getVariances() {
		return variances;
	}
	
	public void setVariances(int[] variances) {
		this.variances = variances;
	}
	
	public int[] getTriple_tap_minutes() {
		return triple_tap_minutes;
	}
	
	public void setTriple_tap_minutes(int[] triple_tap_minutes) {
		this.triple_tap_minutes = triple_tap_minutes;
	}

}
