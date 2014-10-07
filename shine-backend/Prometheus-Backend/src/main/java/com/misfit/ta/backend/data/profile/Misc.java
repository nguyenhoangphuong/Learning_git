package com.misfit.ta.backend.data.profile;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;

public class Misc {

	// fields
    private String hasBeddit;

	// constructors
	public Misc() {
		this.hasBeddit = "";
	}

	public Misc(String hasBeddit) {
		this.hasBeddit = hasBeddit;
	}

	// methods
	public JSONObject toJson() {
		try {
			JSONObject obj = new JSONObject();
			obj.accumulate("hasBeddit", hasBeddit);

			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Misc fromJson(JSONObject json) {
		Misc obj = new Misc();
		try {
			
			if(!json.isNull("hasBeddit"))
				obj.setHasBeddit(json.getString("hasBeddit"));
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}


	public String getHasBeddit() {
		return hasBeddit;
	}

	public void setHasBeddit(String hasBeddit) {
		this.hasBeddit = hasBeddit;
	}

}
