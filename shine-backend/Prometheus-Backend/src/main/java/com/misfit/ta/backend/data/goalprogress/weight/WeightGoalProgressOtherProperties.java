package com.misfit.ta.backend.data.goalprogress.weight;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class WeightGoalProgressOtherProperties {

	// fields
	// TODO: add more field as needed in the future, 
	// when the model changes
	protected Integer testField;

	
	// methods
	public JSONObject toJson() {
		
		try {
			
			JSONObject object = new JSONObject();
			object.accumulate("testField", testField);
			
			return object;
			
		} catch (JSONException e) {
			e.printStackTrace();

			return null;
		}
	}

	public WeightGoalProgressOtherProperties fromJson(JSONObject objJson) {

		try {

			if (!objJson.isNull("testField"))
				this.setTestField(objJson.getInt("testField"));

			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	
	// getters setters
	public Integer getTestField() {
		return testField;
	}

	public void setTestField(Integer testField) {
		this.testField = testField;
	}

}
